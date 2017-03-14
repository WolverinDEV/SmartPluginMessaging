package dev.wolveringer.spm.methods;

import dev.wolveringer.spm.packets.MessagePacket;

public interface CallableMethode<A extends MessagePacket, R extends MessagePacket> {
	public R call(A arguments);
}
