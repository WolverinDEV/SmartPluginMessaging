package dev.wolveringer.spm.methods;

import dev.wolveringer.spm.buffer.DataBuffer;
import dev.wolveringer.spm.packets.BinaryMessagePacket;

public class BinaryMethode<TypePlayer> extends AbstractMethodeTransformer<BinaryMessagePacket, BinaryMessagePacket, DataBuffer, DataBuffer, TypePlayer>{

	public BinaryMethode() {
		super();
	}

	public BinaryMethode(LamdaCallable<DataBuffer, DataBuffer, TypePlayer> lamda) {
		super(lamda);
	}
	
	public BinaryMethode(NoPlayerLamdaCallable<DataBuffer, DataBuffer> lamda) {
		super(lamda);
	}

	@Override
	protected BinaryMessagePacket transform(DataBuffer obj) {
		return obj == null ? BinaryMessagePacket.EMPTY_DATA : new BinaryMessagePacket(obj);
	}

	@Override
	protected DataBuffer transform(BinaryMessagePacket obj) {
		return obj == null ? new DataBuffer() : obj.asBuffer();
	}
}
