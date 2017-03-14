package dev.wolveringer.spm.packets;

import java.util.UUID;

import dev.wolveringer.spm.buffer.DataBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageResponsePacket extends MessagePacket {
	public static enum MessageStatus {
		SUCCESS,
		SEND_FAILED,
		NO_RESPONSE,
		HANDLE_EXCEPTION,
		UNKNOWN
	}
	private UUID uuid;
	private MessageStatus status;
	private MessagePacket packet;
	
	@Override
	public void writeTo(DataBuffer buffer) {
		buffer.writeUUID(uuid);
		buffer.writeEnum(status);
		buffer.writeBoolean(packet != null);
		if(packet != null){
			buffer.writeInt(getClassId(packet.getClass()));
			packet.writeTo(buffer);
		}
	}

	@Override
	public void readFrom(DataBuffer buffer) {
		uuid = buffer.readUUID();
		status = buffer.readEnum(MessageStatus.class);
		if(buffer.readBoolean()){
			try {
				packet = getPacket(buffer.readInt()).newInstance();
				packet.readFrom(buffer);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
