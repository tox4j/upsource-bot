package im.tox.upsourcebot.client.tasks;

import java.util.concurrent.Callable;

/**
 * Callable with a specific exception to recover from.
 *
 * @param <E> Exception type recoverable by this Callable. If this exception is thrown, the callable
 *            can be retried at a later time.
 */
public interface RecoveringCallable<E extends Exception> {

  void call() throws E, InterruptedException;

}
