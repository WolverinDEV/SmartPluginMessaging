package dev.wolveringer.spm.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.NonNull;

public abstract class AbstractMessageHandler<PluginClass, PlayerClass> extends MessageHandler<PluginClass, PlayerClass> {
	private List<PluginMessageListener<PlayerClass>> listener = new ArrayList<>();
	
	@Override
	public void addMessageListener(@NonNull PluginMessageListener<PlayerClass> listener) {
		synchronized (this.listener) {
			this.listener.add(listener);
		}
	}
	@Override
	public void removeMessageListener(@NonNull PluginMessageListener<PlayerClass> listener) {
		synchronized (this.listener) {
			this.listener.remove(listener);
		}
	}
	
	@Override
	public List<PluginMessageListener<PlayerClass>> getMessageListener() {
		synchronized (this.listener) {
			return Collections.unmodifiableList(listener);
		}
	}
	
	protected void fireMessage(PlayerClass player, byte[] data){
		List<PluginMessageListener<PlayerClass>> l;
		synchronized (this.listener) {
			l = new ArrayList<>(this.listener);
		}
		l.forEach(e -> e.onMessage(player, data));
	}
}
