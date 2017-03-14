package dev.wolveringer.spm.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class BasicProgressFuture<V> implements ProgressFuture<V>{
	public static interface ThreadCreator {
		public void runAsync(Runnable run);
	}
	public static ThreadCreator ASYNC_RUNNER = new ThreadCreator() {
		@Override
		public void runAsync(Runnable run) {
			new Thread(run).start();
		}
	};
	
	@Override
	public void get(AsyncCallback<V> task) {
		get((a, b, c) -> task.done(a, b));
	}
	
	@Override
	public void get(TimedAsyncCallback<V> task) {
		long start = System.currentTimeMillis();
		ASYNC_RUNNER.runAsync(()-> {
			V obj = null;
			Exception ex = null;
			
			try {
				obj = get();
			} catch (Exception e) {
				ex = e;
			}
			task.done(obj, ex, System.currentTimeMillis() - start);
		});
	}
	
	@Override
	public void get(AsyncCallback<V> task, int timeout, TimeUnit unit) {
		get((a, b, c) -> task.done(a, b), timeout, unit);
	}
	
	@Override
	public void get(TimedAsyncCallback<V> task, int timeout, TimeUnit unit) {
		long start = System.currentTimeMillis();
		ASYNC_RUNNER.runAsync(()-> {
			V obj = null;
			Exception ex = null;
			
			try {
				obj = get(timeout, unit);
			} catch (Exception e) {
				ex = e;
			}
			task.done(obj, ex, System.currentTimeMillis() - start);
		});
	}
	
	@Override
	public V get(int timeout, TimeUnit unit) throws TimeoutException {
		long timeoutTime = System.currentTimeMillis() + unit.toMillis(timeout);
		while(timeoutTime > System.currentTimeMillis()){
			if(isDone()) return get();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) { }
		}
		throw new TimeoutException();
	}
	
	@Override
	public V getOr(V obj) {
		return isDone() ? get() : obj;
	}
	
	@Override
	public V getOr(int timeout, TimeUnit unit, V obj) {
		try {
			return get(timeout, unit);
		} catch(TimeoutException e){ 
			return obj;
		}
	}
}
