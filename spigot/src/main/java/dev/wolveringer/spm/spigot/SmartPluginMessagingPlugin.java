package dev.wolveringer.spm.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import dev.wolveringer.spm.methods.JSONMethode;
import dev.wolveringer.spm.spigot.message.ProtocolLibMessageHandler;

public class SmartPluginMessagingPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		System.out.println("Enable SmartPluginMessaging");
		if(!ReflectionUtils.isAvariable()){
			System.err.println("Cant enable SmartPluginMessaging!");
			return;
		}
		
		SmartPluginMessaging.setInstance(new SmartPluginMessaging(this, "server01"));
		SmartPluginMessaging.getInstance().load();
	}
}
