package dev.wolveringer.spm.spigot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

import dev.wolveringer.spm.exceptions.NoRouteToTargetException;
import dev.wolveringer.spm.future.InstantProgressFuture;
import dev.wolveringer.spm.future.ProgressFuture;
import dev.wolveringer.spm.packets.MessagePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MessageBungee {
	private final SmartPluginMessaging handle;
	@Getter
	private final String name;
	private List<Player> connections = new ArrayList<>();
	
	public List<Player> getConnections(){
		return Collections.unmodifiableList(connections);
	}
	
	public void callMethode(String name){
		
	}
	
	public <T extends MessagePacket> ProgressFuture<MessageResponsePacket> sendMessage(T message){
		if(connections.isEmpty()) throw new NoRouteToTargetException("Cant send messages to bungee "+name+" (No connected player)");
		
		Iterator<Player> avConnections = new ArrayList<Player>(connections).iterator();
		while (avConnections.hasNext()) {
			ProgressFuture<MessageResponsePacket> future = handle.sendMessage(avConnections.next(), message);
			if(future.isDone() && future instanceof InstantProgressFuture) return future;
		}
		return new InstantProgressFuture<MessageResponsePacket>(new MessageResponsePacket(message.getPacketId(), MessageStatus.SEND_FAILED, null));
	}
}
