package dev.wolveringer.spm.bungee.message;

import dev.wolveringer.spm.message.AbstractMessageHandler;
import lombok.NonNull;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class VanillaMessageHandler extends AbstractMessageHandler<Plugin, ProxiedPlayer> implements Listener{
	private Plugin plugin;
	private String channel;
	
	@Override
	public boolean inizalisize(Plugin plugin, String channel) {
		this.plugin = plugin;
		this.channel = channel;
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
		return true;
	}

	@Override
	public boolean uninizalisize() {
		BungeeCord.getInstance().getPluginManager().unregisterListener(this);
		return true;
	}

	@Override
	public boolean sendMessage(@NonNull ProxiedPlayer connection, byte[] data) {
		if(connection.getServer() == null) return false;
		((ServerConnection)connection.getServer()).sendData(channel, data);
		return true;
	}
	
	@EventHandler
	public void a(PluginMessageEvent e){
		if(e.getTag().equalsIgnoreCase(channel)) {
			if(e.getSender() instanceof ServerConnection){
				System.out.println("PL-Message ("+e.getTag()+") from "+e.getSender()+" to "+e.getReceiver());
				fireMessage((ProxiedPlayer) e.getReceiver(), e.getData());
			} else {
				e.setCancelled(true);
			}
		}
	}
}
