package dev.wolveringer.spm.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TransformProgressFuture<I, O> implements ProgressFuture<O>{
	private final @NonNull ProgressFuture<I> handle;

	protected abstract O transform(I in);
	
	@Override
	public O get() {
		return transform(handle.get());
	}

	@Override
	public O getOr(O obj) {
		return isDone() ? transform(handle.get()) : obj;
	}

	@Override
	public void get(AsyncCallback<O> task) {
		handle.get((obj, ex) -> task.done(transform(obj), ex));
	}

	@Override
	public void get(TimedAsyncCallback<O> task) {
		handle.get((obj, ex, diff) -> task.done(transform(obj), ex, diff));
	}

	@Override
	public O get(int timeout, TimeUnit unit) throws TimeoutException {
		return transform(handle.get(timeout, unit));
	}

	@Override
	public O getOr(int timeout, TimeUnit unit, O obj) {
		try {
			return transform(handle.get(timeout, unit));
		} catch(TimeoutException e){ 
			return obj;
		}
	}

	@Override
	public void get(AsyncCallback<O> task, int timeout, TimeUnit unit) {
		handle.get((obj, ex) -> task.done(transform(obj), ex), timeout, unit);
	}

	@Override
	public void get(TimedAsyncCallback<O> task, int timeout, TimeUnit unit) {
		handle.get((obj, ex, diff) -> task.done(transform(obj), ex, diff), timeout, unit);
	}

	@Override
	public boolean isDone() {
		return handle.isDone();
	}
}
