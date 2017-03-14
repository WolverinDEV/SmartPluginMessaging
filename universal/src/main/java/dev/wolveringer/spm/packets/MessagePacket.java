package dev.wolveringer.spm.packets;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import dev.wolveringer.spm.buffer.DataBuffer;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

public abstract class MessagePacket {
	protected static final Charset UTF_8 = Charset.forName("UTF-8");
	private static HashMap<Class<? extends MessagePacket>, Integer> packetMapping = new HashMap<>();
	private static int packetIds = 1;
	
	static {
		registerPacket(MessageResponsePacket.class);
		registerPacket(MethodeCallPacket.class);
		registerPacket(JSONMessagePacket.class);
		registerPacket(BinaryMessagePacket.class);
	}
	
	public static int getClassId(Class<? extends MessagePacket> clazz){
		return packetMapping.getOrDefault(clazz, -1);
	}
	
	public static Class<? extends MessagePacket> getPacket(int id){
		for(Entry<Class<? extends MessagePacket>, Integer> entry : packetMapping.entrySet())
			if(entry.getValue() == id)
				return entry.getKey();
		return null;
	}
	
	public static int registerPacket(Class<? extends MessagePacket> clazz){
		if(getClassId(clazz) >= 0)
			return getClassId(clazz);
		while (getPacket(packetIds) != null) { packetIds++; }
		int pid = packetIds++;
		packetMapping.put(clazz, pid);
		return pid;
	}
	
	public static boolean registerPacket(Class<? extends MessagePacket> clazz, int id){
		if(getClassId(clazz) >= 0 || getPacket(id) != null)
			return false;
		packetMapping.put(clazz, id);
		return true;
	}
	
	@Getter
	private UUID packetId = UUID.randomUUID();
	
	public abstract void writeTo(DataBuffer buffer);
	public abstract void readFrom(DataBuffer buffer);
	
	/*
	protected void writeString(String message, ByteBuf buffer){
		if(message == null)
			buffer.writeInt(-1);
		else {
			byte[] data = message.getBytes(UTF_8);
			buffer.writeInt(data.length);
			buffer.readBytes(data);
		}
	}
	
	protected String readString(ByteBuf buffer){
		int length = buffer.readInt();
		if(length < 0) return null;
		byte[] data = new byte[length];
		buffer.writeBytes(data);
		return new String(data, UTF_8);
	}
	
	protected void writeUUID(UUID uuid, ByteBuf buffer){
		buffer.writeLong(uuid.getMostSignificantBits());
		buffer.writeLong(uuid.getLeastSignificantBits());
	}
	
	protected UUID readUUID(ByteBuf buffer){
		return new UUID(buffer.readLong(), buffer.readLong());
	}
	*/
}
