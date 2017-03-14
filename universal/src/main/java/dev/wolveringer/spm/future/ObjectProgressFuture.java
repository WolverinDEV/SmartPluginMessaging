package dev.wolveringer.spm.future;

public class ObjectProgressFuture<V> extends BasicProgressFuture<V> {
	private V obj = null;
	private boolean done = false;
	
	@Override
	public boolean isDone() {
		return done;
	}
	
	@Override
	public V get() {
		while(!isDone()) try { Thread.sleep(5); } catch (Exception e) { }
		return obj;
	}
	
	protected void done(V obj){
		this.obj = obj;
		this.done = true;
	}
}
