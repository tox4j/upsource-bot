package im.tox.upsourcebot.client.tasks;

/**
 * Callable with a specific exception to recover from.
 *
 * @param <E> Exception type recoverable by this Callable.
 */
public interface RecoveringCallable<E extends Exception> {

  /**
   * Does any computation
   *
   * @throws E                              if the computation failed. This exception is
   *                                        recoverable, and this callable can be retried.
   * @throws java.lang.InterruptedException if the thread is interrupted.
   */
  void call() throws E, InterruptedException;

}
