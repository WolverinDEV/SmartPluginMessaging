package dev.wolveringer.spm.methods;

import dev.wolveringer.spm.packets.MessagePacket;

public abstract class AbstractMethodeTransformer<P extends MessagePacket, R extends MessagePacket, NP, NR, TypePlayer> implements CallableMethode<P, R, TypePlayer>{
	public static interface LamdaCallable<P, R, TypePlayer> {
		R call(TypePlayer player, P parm);
	}
	public static interface NoPlayerLamdaCallable<P, R> {
		R call(P parm);
	}
	
	public AbstractMethodeTransformer() {
		this((LamdaCallable<NP, NR, TypePlayer>) null);
	}
	public AbstractMethodeTransformer(NoPlayerLamdaCallable<NP, NR> lamda){
		this((a, b) -> lamda.call(b));
	}
	
	private LamdaCallable<NP, NR, TypePlayer> lamda;
	public AbstractMethodeTransformer(LamdaCallable<NP, NR, TypePlayer> lamda){
		this.lamda = lamda;
	}
	
	@Override
	public R call(TypePlayer conn, P arguments) {
		return transform(call(conn, transform(arguments)));
	}
	
	public NR call(TypePlayer conn, NP args){
		if(lamda == null) throw new UnsupportedOperationException();
		return lamda.call(conn, args);
	}
	
	protected abstract R transform(NR obj);
	protected abstract NP transform(P obj);
	
}
