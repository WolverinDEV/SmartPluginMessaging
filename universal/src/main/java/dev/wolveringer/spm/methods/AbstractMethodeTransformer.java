package dev.wolveringer.spm.methods;

import dev.wolveringer.spm.packets.MessagePacket;

public abstract class AbstractMethodeTransformer<P extends MessagePacket, R extends MessagePacket, NP, NR> implements CallableMethode<P, R>{
	public static interface LamdaCallable<P, R> {
		R call(P parm);
	}
	
	public AbstractMethodeTransformer() {
		this(null);
	}
	
	private LamdaCallable<NP, NR> lamda;
	public AbstractMethodeTransformer(LamdaCallable<NP, NR> lamda){
		this.lamda = lamda;
	}
	
	@Override
	public R call(P arguments) {
		return transform(call(transform(arguments)));
	}
	
	public NR call(NP args){
		if(lamda == null) throw new UnsupportedOperationException();
		return lamda.call(args);
	}
	
	protected abstract R transform(NR obj);
	protected abstract NP transform(P obj);
	
}
