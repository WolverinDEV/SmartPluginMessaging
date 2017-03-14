package dev.wolveringer.spm.bungee;

import dev.wolveringer.spm.TargetKnot;
import dev.wolveringer.spm.buffer.DataBuffer;
import dev.wolveringer.spm.future.ProgressFuture;
import dev.wolveringer.spm.future.TransformProgressFuture;
import dev.wolveringer.spm.packets.BinaryMessagePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket.MessageStatus;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class SpigotServer extends TargetKnot<Plugin, ProxiedPlayer> {

	public SpigotServer(SmartPluginMessaging handle, String name) {
		super(handle, name);
	}
	
	public ProgressFuture<Boolean> broadcast(String message){
		return new TransformProgressFuture<MessageResponsePacket, Boolean>(callMethode("broadcastMessage", new BinaryMessagePacket(new DataBuffer().writeString(message)))) {
			@Override
			protected Boolean transform(MessageResponsePacket in) {
				return in != null && in.getStatus() == MessageStatus.SUCCESS;
			}
		};
	}

}
