package dev.wolveringer.spm.packets;

import dev.wolveringer.spm.buffer.DataBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MethodeCallPacket extends MessagePacket {
	private String methodeName;
	private MessagePacket parameter;
	
	@Override
	public void writeTo(DataBuffer buffer) {
		buffer.writeString(methodeName);
		buffer.writeInt(getClassId(parameter.getClass()));
		parameter.writeTo(buffer);
	}

	@Override
	public void readFrom(DataBuffer buffer) {
		methodeName = buffer.readString();
		try {
			parameter = getPacket(buffer.readInt()).newInstance();
			parameter.readFrom(buffer);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
