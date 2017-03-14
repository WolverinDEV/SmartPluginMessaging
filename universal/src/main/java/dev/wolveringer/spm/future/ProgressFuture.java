package dev.wolveringer.spm.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface ProgressFuture<V> {
	public static interface AsyncCallback<T> {
		public void done(T obj, Exception e);
	}
	public static interface TimedAsyncCallback<T> {
		public void done(T obj, Exception e, long timeDiff);
	}
	
	public V get();
	public V getOr(V obj);
	public void get(AsyncCallback<V> task);
	public void get(TimedAsyncCallback<V> task);
	
	public V get(int timeout, TimeUnit unit) throws TimeoutException;
	public V getOr(int timeout, TimeUnit unit, V obj);
	public void get(AsyncCallback<V> task, int timeout, TimeUnit unit);
	public void get(TimedAsyncCallback<V> task, int timeout, TimeUnit unit);
	
	public boolean isDone();
}
