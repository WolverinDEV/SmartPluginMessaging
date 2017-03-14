package dev.wolveringer.spm.methods;

import java.util.HashMap;

import dev.wolveringer.spm.packets.MessagePacket;

public class MethodeBinding {
	public static final CallableMethode<MessagePacket, MessagePacket> NO_BINDING = (parm) -> {
		throw new MethodeNotFoundException("Cant find target methode");
	};
	
	private HashMap<String, CallableMethode<? extends MessagePacket, ? extends MessagePacket>> bindings = new HashMap<>();
	
	public void bind(String name, CallableMethode<? extends MessagePacket, ? extends MessagePacket> methode){
		if(bindings.get(name.toUpperCase()) != null) throw new RuntimeException("Methode "+name+" alredy registered!");
		bindings.put(name.toUpperCase(), methode);
	}
	
	public void unregisterBinding(String name){
		bindings.remove(name.toUpperCase());
	}
	
	@SuppressWarnings("unchecked")
	public <P extends MessagePacket, R extends MessagePacket> CallableMethode<P, R> getMethode(String name){
		return (CallableMethode<P, R>) bindings.getOrDefault(name.toUpperCase(), NO_BINDING);
	}
}
