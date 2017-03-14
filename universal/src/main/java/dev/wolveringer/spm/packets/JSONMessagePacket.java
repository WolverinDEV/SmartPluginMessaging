package dev.wolveringer.spm.packets;


import org.json.JSONObject;

import dev.wolveringer.spm.buffer.DataBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class JSONMessagePacket extends MessagePacket{
	public static final JSONMessagePacket EMPTY_JSON = new JSONMessagePacket(null);
	
	private JSONObject object;
	
	@Override
	public void writeTo(DataBuffer buffer) {
		buffer.writeString(object == null ? null : object.toString());
	}

	@Override
	public void readFrom(DataBuffer buffer) {
		String data = buffer.readString();
		if(data != null)
			object = new JSONObject(data);
	}

}
