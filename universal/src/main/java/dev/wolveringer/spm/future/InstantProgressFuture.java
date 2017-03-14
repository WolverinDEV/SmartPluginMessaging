package dev.wolveringer.spm.future;

public class InstantProgressFuture<V> extends ObjectProgressFuture<V>{
	public InstantProgressFuture(V obj){
		done(obj);
	}
}
