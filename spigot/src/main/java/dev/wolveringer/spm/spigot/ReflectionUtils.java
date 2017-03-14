package dev.wolveringer.spm.spigot;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;

public class ReflectionUtils {
	@Getter
	private static boolean avariable = true;
	
	private static Class<?> getClass(boolean depend, String name){
		if(!avariable) return null;
		try {
			return Class.forName(name);
		}catch (Exception e) {
			if(depend) avariable = false;
			e.printStackTrace();
		}
		return null;
	}
	
	private static Field getField(boolean depend, Class clazz, String name){
		if(!avariable) return null;
		try {
			Field ret = null;
			try {
				ret = clazz.getField(name);
			} catch (Exception e) { }
			try {
				ret = clazz.getDeclaredField(name);
			} catch (Exception e) { }
			if(ret == null) throw new Exception();
			ret.setAccessible(true);
			return ret;
		} catch (Exception e) {
			if(depend) avariable = false;
			e.printStackTrace();
		}
		return null;
	}
	
	private static Method getMethod(boolean depend, Class clazz,String name, Class... parms){
		if(!avariable) return null;
		try {
			Method ret = null;
			try {
				ret = clazz.getMethod(name, parms);
			} catch (Exception e) { }
			try {
				ret = clazz.getDeclaredMethod(name, parms);
			} catch (Exception e) { }
			if(ret == null) throw new Exception();
			ret.setAccessible(true);
			return ret;
		} catch (Exception e) {
			if(depend) avariable = false;
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getVersion(){
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
	
	public static final Class<? extends Player> CLAZZ_CPLAYER = (Class<? extends Player>) getClass(true, "org.bukkit.craftbukkit."+getVersion()+".entity.CraftPlayer");
	public static final Class<?> CLAZZ_ENTITYPLAYER = getClass(true, "net.minecraft.server."+getVersion()+".EntityPlayer");
	public static final Class<?> CLAZZ_PLAYERCONNECTION = getClass(true, "net.minecraft.server."+getVersion()+".PlayerConnection");
	public static final Class<?> CLAZZ_PACKET = getClass(true, "net.minecraft.server."+getVersion()+".Packet");
	public static final Class<?> CLAZZ_PACKETDATASERELIZER = getClass(true, "net.minecraft.server."+getVersion()+".PacketDataSerializer");
	//PacketDataSerializer
	
	public static final Class<?> CLAZZ_PACKET_IN_COSTUMEPAYLOAD = getClass(true, "net.minecraft.server."+getVersion()+".PacketPlayInCustomPayload");
	public static final Class<?> CLAZZ_PACKET_OUT_COSTUMEPAYLOAD = getClass(true, "net.minecraft.server."+getVersion()+".PacketPlayOutCustomPayload");
	public static final Field FIELD_PACKET_IN_COSTUMEPAYLOAD_B = getField(true, CLAZZ_PACKET_IN_COSTUMEPAYLOAD, "b");
	public static final Field FIELD_PACKET_OUT_COSTUMEPAYLOAD_B = getField(true, CLAZZ_PACKET_OUT_COSTUMEPAYLOAD, "b");
	
	public static final Method METHOD_CPLAYER_GETHANDLE = getMethod(true, CLAZZ_CPLAYER, "getHandle");
	public static final Method METHOD_PLAYERCONNECTION_SENDPACKET = getMethod(true, CLAZZ_PLAYERCONNECTION, "sendPacket", CLAZZ_PACKET);
	
	public static final Field FIELD_ENTITYPLAYER_PLAYERCONNECTION = getField(true, CLAZZ_ENTITYPLAYER, "playerConnection");
	
	public static Object getConnection(Player player) {
	   try {
		   Object nmsPlayer = METHOD_CPLAYER_GETHANDLE.invoke(player);
		    Object con = FIELD_ENTITYPLAYER_PLAYERCONNECTION.get(nmsPlayer);
		    return con;
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return null;
	}
}
