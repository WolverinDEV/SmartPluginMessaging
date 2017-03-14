package dev.wolveringer.spm.spigot.message;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;

import dev.wolveringer.spm.message.AbstractMessageHandler;
import dev.wolveringer.spm.spigot.ReflectionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;

public class ProtocolLibMessageHandler extends AbstractMessageHandler<Plugin, Player> implements PacketListener{

	private Plugin pinstance;
	private String channel;
	
	@Override
	public boolean inizalisize(Plugin plugin, String channel) {
		this.pinstance = plugin;
		this.channel = channel;
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
		return true;
	}

	@Override
	public boolean uninizalisize() {
		ProtocolLibrary.getProtocolManager().removePacketListener(this);
		return false;
	}
	
	@Override
	public boolean sendMessage(Player connection, byte[] data) {
		PacketContainer plMessage = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
		plMessage.getStrings().write(0, channel);
		
		try {
			ByteBuf buffer = Unpooled.copiedBuffer(data);
			ReflectionUtils.FIELD_PACKET_OUT_COSTUMEPAYLOAD_B.set(plMessage.getHandle(), ReflectionUtils.CLAZZ_PACKETDATASERELIZER.getConstructor(ByteBuf.class).newInstance(buffer));
			ProtocolLibrary.getProtocolManager().sendServerPacket(connection, plMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	@Override
	public Plugin getPlugin() {
		return pinstance;
	}

	@Override
	public ListeningWhitelist getReceivingWhitelist() {
		return ListeningWhitelist.newBuilder().highest().gamePhase(GamePhase.PLAYING).types(PacketType.Play.Client.CUSTOM_PAYLOAD).build();
	}

	@Override
	public ListeningWhitelist getSendingWhitelist() {
		return ListeningWhitelist.EMPTY_WHITELIST;
	}

	@Override
	public void onPacketReceiving(PacketEvent ev) {
		try {
			if(ev.getPacketType() == PacketType.Play.Client.CUSTOM_PAYLOAD){
				String ch = ev.getPacket().getStrings().read(0);
				if(ch.equalsIgnoreCase(this.channel)){
					ByteBuf buffer = (ByteBuf) ReflectionUtils.FIELD_PACKET_IN_COSTUMEPAYLOAD_B.get(ev.getPacket().getHandle());
					fireMessage(ev.getPlayer(), buffer.array());
					ev.setCancelled(true);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPacketSending(PacketEvent arg0) { }
}
