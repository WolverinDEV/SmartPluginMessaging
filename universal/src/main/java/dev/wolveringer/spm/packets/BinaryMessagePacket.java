package dev.wolveringer.spm.packets;

import dev.wolveringer.spm.buffer.DataBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BinaryMessagePacket extends MessagePacket{
	public static final BinaryMessagePacket EMPTY_DATA = new BinaryMessagePacket((byte[]) null);
	
	private byte[] object;
	
	public BinaryMessagePacket(DataBuffer buffer){
		if(buffer == null) return;
		object = new byte[buffer.readableBytes()];
		buffer.readBytes(object);
	}
	
	@Override
	public void writeTo(DataBuffer buffer) {
		if(object == null){
			buffer.writeInt(-1);
		} else {
			buffer.writeInt(object.length);
			buffer.writeBytes(object);
		}
	}

	@Override
	public void readFrom(DataBuffer buffer) {
		int length = buffer.readInt();
		if(length >= 0){
			object = new byte[length];
			buffer.readBytes(object);
		}
	}
	
	public DataBuffer asBuffer(){
		return new DataBuffer(object == null ? new byte[0] : object);
	}
}
