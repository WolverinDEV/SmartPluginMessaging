package dev.wolveringer.spm.bungee;

import dev.wolveringer.spm.AbstractSmartPluginMessaging;
import dev.wolveringer.spm.buffer.DataBuffer;
import dev.wolveringer.spm.bungee.message.VanillaMessageHandler;
import dev.wolveringer.spm.message.MessageHandler;
import dev.wolveringer.spm.methods.BinaryMethode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class SmartPluginMessaging extends AbstractSmartPluginMessaging<Plugin, ProxiedPlayer> {
	@Getter
	@Setter
	private static SmartPluginMessaging instance;
	
	public SmartPluginMessaging(Plugin instance, String bungeeName) {
		super(instance, bungeeName);
		getMethodeBinding().bind("init#server", new BinaryMethode() {
			@Override
			public DataBuffer call(DataBuffer buffer) {
				String serverName = buffer.readString();
				System.out.println("Init from "+serverName);
				return new DataBuffer().writeString(bungeeName);
			}
		});
	}

	@Override
	protected MessageHandler<Plugin, ProxiedPlayer> createMessageHandler() {
		return new VanillaMessageHandler();
	}
	
}
