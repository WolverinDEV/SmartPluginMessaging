package dev.wolveringer.spm.methods;

import dev.wolveringer.spm.packets.MessagePacket;

public interface CallableMethode<A extends MessagePacket, R extends MessagePacket, TypePlayer> {
	public R call(TypePlayer player, A arguments);
}
