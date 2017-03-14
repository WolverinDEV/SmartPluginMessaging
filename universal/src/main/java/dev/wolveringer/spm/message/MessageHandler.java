package dev.wolveringer.spm.message;

import java.util.List;

import lombok.NonNull;

public abstract class MessageHandler<PluginClass, PlayerClass> {
	public static interface PluginMessageListener<PlayerClass> {
		public void onMessage(PlayerClass player, byte[] data);
	}
	public abstract boolean inizalisize(@NonNull PluginClass plugin, @NonNull String channel);
	public abstract boolean uninizalisize();
	
	public abstract boolean sendMessage(PlayerClass connection, byte[] data);
	public abstract void addMessageListener(PluginMessageListener<PlayerClass> listener);
	public abstract void removeMessageListener(PluginMessageListener<PlayerClass> listener);
	public abstract List<PluginMessageListener<PlayerClass>> getMessageListener();
}
