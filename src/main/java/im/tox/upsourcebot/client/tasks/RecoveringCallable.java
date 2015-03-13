package im.tox.upsourcebot.client.tasks;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

/**
 * Callable with a specific exception to recover from.
 *
 * @param <E> Exception type recoverable by this Callable.
 * @param <V> result type of this Callable
 */
public interface RecoveringCallable<E extends Exception, V> extends Callable<V> {

  /**
   * Does any computation.
   *
   * @return the result of the computation
   * @throws E                              if the computation failed. This exception is
   *                                        recoverable, and this callable can be retried.
   * @throws java.lang.InterruptedException if the thread is interrupted.
   */
  @Nullable
  V call() throws E, InterruptedException;

}
