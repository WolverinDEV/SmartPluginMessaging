package dev.wolveringer.spm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dev.wolveringer.spm.exceptions.NoRouteToTargetException;
import dev.wolveringer.spm.future.InstantProgressFuture;
import dev.wolveringer.spm.future.ProgressFuture;
import dev.wolveringer.spm.packets.MessagePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket;
import dev.wolveringer.spm.packets.MethodeCallPacket;
import dev.wolveringer.spm.packets.MessageResponsePacket.MessageStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TargetKnot<TypePlugin, TypePlayer> {
	private List<TypePlayer> connections = new ArrayList<>();
	
	private final AbstractSmartPluginMessaging<TypePlugin, TypePlayer> handle;
	@Getter
	private final String name;
	
	public List<TypePlayer> getConnections(){
		return Collections.unmodifiableList(connections);
	}
	
	public <T extends MessagePacket> ProgressFuture<MessageResponsePacket> callMethode(String name, T parm){
		return sendMessage(new MethodeCallPacket(name, parm));
	}
	
	public <T extends MessagePacket> ProgressFuture<MessageResponsePacket> sendMessage(T message){
		if(connections.isEmpty()) throw new NoRouteToTargetException("Cant send messages to target '"+name+"' (No avariable connections)");
		
		Iterator<TypePlayer> avConnections = new ArrayList<TypePlayer>(connections).iterator();
		while (avConnections.hasNext()) {
			ProgressFuture<MessageResponsePacket> future = handle.sendMessage(avConnections.next(), message);
			if(future.isDone() && future instanceof InstantProgressFuture) return future;
		}
		return new InstantProgressFuture<MessageResponsePacket>(new MessageResponsePacket(message.getPacketId(), MessageStatus.SEND_FAILED, null));
	}
}
