package dev.wolveringer.spm.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class SmartPluginMessagingPlugin extends Plugin{
	@Override
	public void onEnable() {
		System.out.println("Enable SmartPluginMessaging");
		SmartPluginMessaging.setInstance(new SmartPluginMessaging(this, "Bungee01"));
		SmartPluginMessaging.getInstance().load();
		SmartPluginMessaging.getInstance().getTarget("server01").broadcast("Hello world!");
	}
}
