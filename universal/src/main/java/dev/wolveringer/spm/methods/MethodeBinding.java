package dev.wolveringer.spm.methods;

import java.util.HashMap;

import dev.wolveringer.spm.packets.MessagePacket;

public class MethodeBinding<TypePlayer> {
	public static final CallableMethode<MessagePacket, MessagePacket, Object> NO_BINDING = (player, parm) -> {
		throw new MethodeNotFoundException("Cant find target methode");
	};
	
	private HashMap<String, CallableMethode<? extends MessagePacket, ? extends MessagePacket, TypePlayer>> bindings = new HashMap<>();
	
	public void bind(String name, CallableMethode<? extends MessagePacket, ? extends MessagePacket, TypePlayer> methode){
		if(bindings.get(name.toUpperCase()) != null) throw new RuntimeException("Methode "+name+" alredy registered!");
		bindings.put(name.toUpperCase(), methode);
	}
	
	public void unregisterBinding(String name){
		bindings.remove(name.toUpperCase());
	}
	
	@SuppressWarnings("unchecked")
	public <P extends MessagePacket, R extends MessagePacket> CallableMethode<P, R, TypePlayer> getMethode(String name){
		return (CallableMethode<P, R, TypePlayer>) bindings.getOrDefault(name.toUpperCase(), (CallableMethode<P, R, TypePlayer>) NO_BINDING);
	}
}
