package dev.wolveringer.spm.spigot.message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import dev.wolveringer.spm.message.AbstractMessageHandler;
import dev.wolveringer.spm.spigot.ReflectionUtils;
import lombok.NonNull;

public class VanillaMessageHandler extends AbstractMessageHandler<Plugin, Player> implements PluginMessageListener{
	private Plugin pluginInstance = null;
	private String channel = "";
	
	@Override
	public boolean inizalisize(@NonNull Plugin plugin, @NonNull String channel) {
		this.pluginInstance = plugin;
		this.channel = channel;
		if(!Bukkit.getMessenger().isIncomingChannelRegistered(plugin, channel))
			Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, this);
		else
			System.err.println("Try to register channel (IN) "+channel+" for plugin "+pluginInstance.getName()+" twice!");
		if(!Bukkit.getMessenger().isOutgoingChannelRegistered(plugin, channel))
			Bukkit.getMessenger().isOutgoingChannelRegistered(plugin, channel);
		else
			System.err.println("Try to register channel (OUT) "+channel+" for plugin "+pluginInstance.getName()+" twice!");
		return true;
	}

	@Override
	public boolean uninizalisize() {
		if(Bukkit.getMessenger().isIncomingChannelRegistered(pluginInstance, channel))
			Bukkit.getMessenger().unregisterIncomingPluginChannel(pluginInstance, channel);
		if(Bukkit.getMessenger().isOutgoingChannelRegistered(pluginInstance, channel))
			Bukkit.getMessenger().unregisterOutgoingPluginChannel(pluginInstance, channel);
		return true;
	}

	@Override
	public boolean sendMessage(Player connection, byte[] data) {
		if(ReflectionUtils.getConnection(connection) == null){
			System.err.println("Tried to send a plugin message to and not inizalisized player!");
			return false;
		}
		connection.sendPluginMessage(pluginInstance, channel, data);
		return true;
	}

	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(channel.equalsIgnoreCase(this.channel)) fireMessage(player, message);
	}

}
