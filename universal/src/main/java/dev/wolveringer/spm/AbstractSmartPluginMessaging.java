package dev.wolveringer.spm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.UUID;

import dev.wolveringer.cache.CachedArrayList;
import dev.wolveringer.cache.CachedArrayList.CachedArrayListBuilder;
import dev.wolveringer.cache.ReadOnlyMapEntry;
import dev.wolveringer.spm.MessageListener;
import dev.wolveringer.spm.buffer.DataBuffer;
import dev.wolveringer.spm.future.InstantProgressFuture;
import dev.wolveringer.spm.future.MessageStatusResponseFuture;
import dev.wolveringer.spm.future.ProgressFuture;
import dev.wolveringer.spm.message.MessageHandler;
import dev.wolveringer.spm.methods.BinaryMethode;
import dev.wolveringer.spm.methods.MethodeBinding;
import dev.wolveringer.spm.packets.MessagePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket.MessageStatus;
import dev.wolveringer.spm.packets.MethodeCallPacket;
import lombok.Getter;

public abstract class AbstractSmartPluginMessaging<TypePlugin, TypePlayer, KnotImpl extends TargetKnot<TypePlugin, TypePlayer, ?>> {
	private static final String CHANNEL = "SPM";
	
	protected final String name;
	protected final KnotType type;
	protected final TypePlugin pinstance;
	private MessageHandler<TypePlugin, TypePlayer> messageHandler;
	
	@Getter
	private MethodeBinding<TypePlayer> methodeBinding = new MethodeBinding<TypePlayer>();
	private List<MessageListener<TypePlayer>> listener = new ArrayList<>();
	private CachedArrayList<Entry<UUID, MessageStatusResponseFuture>> waitingFutures = ((CachedArrayListBuilder<Entry<UUID, MessageStatusResponseFuture>>) (CachedArrayListBuilder)CachedArrayList.builder()).timeout(1, TimeUnit.MINUTES).listener(e -> {
		e.getValue().done(new MessageResponsePacket(e.getKey(), MessageStatus.NO_RESPONSE, null));
		return true;
	}).build();
	
	protected CachedArrayList<KnotImpl> knots = ((CachedArrayListBuilder<KnotImpl>) (CachedArrayListBuilder) CachedArrayList.builder()).timeout(2, TimeUnit.MINUTES).listener(e -> {
		for(TypePlayer conn : new ArrayList<TypePlayer>(e.getConnections()))
			if(!isConnectionAvariable(conn))
				synchronized (e.connections) {
					e.connections.remove(conn);
				}
		synchronized (e.connections) {
			return e.connections.isEmpty();
		}
	}).build();
	protected abstract boolean isConnectionAvariable(TypePlayer player);
	
	public AbstractSmartPluginMessaging(TypePlugin instance,String bungeeName, KnotType type){
		this.pinstance = instance;
		this.name = bungeeName;
		this.type = type;
		
		getMethodeBinding().bind("whoAmYou", new BinaryMethode<TypePlayer>((player, buffer) -> {
			return new DataBuffer().writeString(name).writeEnum(type);
		}));
	}
	
	public boolean load(){
		messageHandler = createMessageHandler();
		messageHandler.inizalisize(pinstance, CHANNEL);
		messageHandler.addMessageListener((a, b) -> handleMessage(a, b));
		return true;
	}
	
	protected abstract MessageHandler<TypePlugin, TypePlayer> createMessageHandler();
	
	public void unload(){
		messageHandler.uninizalisize();
	}
	
	private void handleMessage(TypePlayer player, byte[] data){
		try {
			DataBuffer buffer = new DataBuffer(data);
			int typeId = buffer.readInt();
			UUID packetId = buffer.readUUID();
			MessagePacket packet = MessagePacket.getPacket(typeId).newInstance();
			
			packet.readFrom(buffer);
			
			if(packet instanceof MethodeCallPacket){
				MethodeCallPacket pkt = (MethodeCallPacket) packet;
				try { 
					MessagePacket response = methodeBinding.getMethode(pkt.getMethodeName()).call(player, pkt.getParameter()); 
					writeMessage(player, new MessageResponsePacket(packetId, MessageStatus.SUCCESS, response));
				} catch(Exception e){
					e.printStackTrace();
					writeMessage(player, new MessageResponsePacket(packetId, MessageStatus.HANDLE_EXCEPTION, null)); //TODO transfare exception
				}
				return;
			} else if(packet instanceof MessageResponsePacket) {
				MessageResponsePacket pkt = (MessageResponsePacket) packet;
				MessageStatusResponseFuture future = null;
				synchronized (waitingFutures) {
					for(Entry<UUID,MessageStatusResponseFuture> f : waitingFutures)
						if(f.getKey().equals(pkt.getUuid()))
							future = f.getValue();
				}
				if(future != null){
					future.done(pkt);
					waitingFutures.remove(future);
				} else System.err.println("Got MessageResponsePacket for not existing request. UUID: "+pkt.getUuid());
			} else {
				List<MessageListener<TypePlayer>> listener = new ArrayList<>(this.listener);
				listener.forEach(e -> e.handleMessage(player, packet)); //TODO handle exception etc
				writeMessage(player, new MessageResponsePacket(packetId, MessageStatus.SUCCESS, null));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addMessageListener(MessageListener<TypePlayer> listener){
		this.listener.add(listener);
	}
	
	public void removeMessageListener(MessageListener<TypePlayer> listener){
		this.listener.remove(listener);
	}
	
	public <T extends MessagePacket> boolean writeMessage(TypePlayer player, T message){
		DataBuffer buffer = new DataBuffer();
		buffer.writeInt(MessagePacket.getClassId(message.getClass()));
		buffer.writeUUID(message.getPacketId());
		message.writeTo(buffer);
		return messageHandler.sendMessage(player, buffer.array());
	}
	
	/**
	 * Send message and request a response
	 */
	public <T extends MessagePacket> ProgressFuture<MessageResponsePacket> sendMessage(TypePlayer player, T message){
		if(!writeMessage(player, message))
			return new InstantProgressFuture<MessageResponsePacket>(new MessageResponsePacket(message.getPacketId(), MessageStatus.SEND_FAILED, null));
		MessageStatusResponseFuture future = new MessageStatusResponseFuture();
		synchronized (this.waitingFutures) {
			this.waitingFutures.add(new ReadOnlyMapEntry(message.getPacketId(), future));
		}
		return future;
	}
	
	public <T extends MessagePacket> ProgressFuture<MessageResponsePacket> callMethode(TypePlayer player, String name, MessagePacket parm){
		return sendMessage(player, new MethodeCallPacket(name, parm));
	}
	
	public KnotImpl getTarget(TypePlayer player){
		for(KnotImpl conn : this.knots)
			if(conn.getConnections().contains(player)) return conn;
		return null;
	}
	
	public KnotImpl getTarget(String name){
		for(KnotImpl conn : this.knots)
			if(conn.getName().equalsIgnoreCase(name)) return conn;
		return null;
	}
	
	public List<KnotImpl> getTargets(){
		return Collections.unmodifiableList(this.knots);
	}
}