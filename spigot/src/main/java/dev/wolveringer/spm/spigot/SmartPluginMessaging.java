package dev.wolveringer.spm.spigot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType.Play;

import dev.wolveringer.cache.CachedArrayList;
import dev.wolveringer.cache.ReadOnlyMapEntry;
import dev.wolveringer.spm.AbstractSmartPluginMessaging;
import dev.wolveringer.spm.MessageListener;
import dev.wolveringer.spm.buffer.DataBuffer;
import dev.wolveringer.spm.future.InstantProgressFuture;
import dev.wolveringer.spm.future.MessageStatusResponseFuture;
import dev.wolveringer.spm.future.ProgressFuture;
import dev.wolveringer.spm.message.MessageHandler;
import dev.wolveringer.spm.methods.BinaryMethode;
import dev.wolveringer.spm.methods.MethodeBinding;
import dev.wolveringer.spm.packets.BinaryMessagePacket;
import dev.wolveringer.spm.packets.MessagePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket;
import dev.wolveringer.spm.packets.MessageResponsePacket.MessageStatus;
import dev.wolveringer.spm.spigot.message.VanillaMessageHandler;
import lombok.Getter;
import lombok.Setter;

public class SmartPluginMessaging extends AbstractSmartPluginMessaging<Plugin, Player>{
	@Getter
	@Setter
	private static SmartPluginMessaging instance;
	
	private List<MessageBungee> avariableBungees = new ArrayList<>();
	
	private final Listener playerListener = new Listener() {
		@EventHandler
		public void a(PlayerJoinEvent e){
			callMethode(e.getPlayer(), "init#server", new BinaryMessagePacket(new DataBuffer().writeString(name))).get((response, ex, timeDiff) -> {
				if(ex != null){
					if(ex instanceof TimeoutException){
						System.err.println("Player "+e.getPlayer().getName()+" dosnt joined over a proxie!");
					} else {
						System.err.println("Having an error while inizalisize channel "+ex.getMessage()+" ("+ex.getClass().getName()+")");
					}
				} else {
					if(response.getStatus() != MessageStatus.SUCCESS){
						System.err.println("Error while inizalisize player channel.");
					} else {
						DataBuffer buffer = ((BinaryMessagePacket) response.getPacket()).asBuffer();
						String bungeeName = buffer.readString();
						System.out.println("Player "+e.getPlayer().getName()+" joined over "+bungeeName+" Speed: ("+timeDiff+"ms)");
					}
				}
			}, 2500, TimeUnit.MILLISECONDS);
		}
		
		@EventHandler
		public void a(PlayerQuitEvent e){
			
		}
	};
	
	public SmartPluginMessaging(Plugin instance,String serverName){
		super(instance, serverName);
		getMethodeBinding().bind("broadcastMessage", new BinaryMethode(buffer -> {
			Bukkit.broadcastMessage(buffer.readString());
			return null;
		}));
	}
	
	@Override
	public boolean load(){
		super.load();
		Bukkit.getPluginManager().registerEvents(this.playerListener, this.pinstance);
		return true;
	}
	
	@Override
	protected MessageHandler<Plugin, Player> createMessageHandler() {
		if(Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")){
			return new dev.wolveringer.spm.spigot.message.ProtocolLibMessageHandler(); //Dont load before via import
		} else {
			return new VanillaMessageHandler();
		}
	}
	
	@Override
	public void unload(){
		HandlerList.unregisterAll(this.playerListener);
	}
	public MessageBungee getBungee(Player player){
		for(MessageBungee bungee : this.avariableBungees)
			if(bungee.getConnections().contains(player)) return bungee;
		return null;
	}
	
	public MessageBungee getBungee(String name){
		for(MessageBungee bungee : this.avariableBungees)
			if(bungee.getName().equalsIgnoreCase(name)) return bungee;
		return null;
	}
}
