package dev.wolveringer.spm.bungee;

import dev.wolveringer.spm.AbstractSmartPluginMessaging;
import dev.wolveringer.spm.KnotType;
import dev.wolveringer.spm.buffer.DataBuffer;
import dev.wolveringer.spm.bungee.message.VanillaMessageHandler;
import dev.wolveringer.spm.message.MessageHandler;
import dev.wolveringer.spm.methods.BinaryMethode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class SmartPluginMessaging extends AbstractSmartPluginMessaging<Plugin, ProxiedPlayer, SpigotServer> {
	@Getter
	@Setter
	private static SmartPluginMessaging instance;
	
	public SmartPluginMessaging(Plugin instance, String bungeeName) {
		super(instance, bungeeName, KnotType.PROXY);
		getMethodeBinding().bind("init#server", new BinaryMethode<ProxiedPlayer>((player, buffer) -> {
			String serverName = buffer.readString();
			System.out.println("Init from "+serverName);
			System.out.println("Not can comuicate between me and "+serverName+" over player "+player.getName());
			return new DataBuffer().writeString(bungeeName);
		}));
	}

	@Override
	protected MessageHandler<Plugin, ProxiedPlayer> createMessageHandler() {
		return new VanillaMessageHandler();
	}

	@Override
	protected boolean isConnectionAvariable(ProxiedPlayer player) {
		return player != null && player.isConnected();
	}
	
}
