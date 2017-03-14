package dev.wolveringer.spm;

import dev.wolveringer.spm.packets.MessagePacket;

public interface MessageListener<Player> {
	public <T extends MessagePacket> void handleMessage(Player player, T packet);
}
