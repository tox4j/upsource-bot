package im.tox.upsourcebot.client.tasks;


/**
 * Class for retrying actions with different retry strategies
 */
public class RetryUtil {

  /**
   * Try to execute a callable.
   *
   * @param callable the callable to execute
   * @param strategy the strategy used for retrying
   * @param <E>      the exception type recoverable by the callable
   * @param <V>      result type of the callable
   * @throws InterruptedException if the containing thread is interrupted
   * @throws E                    if the execution failed after the strategy depleted its retries
   */
  @SuppressWarnings("unchecked")
  public static <E extends Exception, V> V retryExecute(RecoveringCallable<E, V> callable,
      BackoffStrategy strategy)
      throws InterruptedException, E {
    Exception previousException = null;
    for (; strategy.retryAgain(); strategy.tryComplete()) {
      try {
        return callable.call();
      } catch (InterruptedException e) {
        throw e;
      } catch (Exception e) {
        previousException = e;
        Thread.sleep(strategy.waitTime());
      }
    }
    if (previousException instanceof RuntimeException) {
      throw (RuntimeException) previousException;
    } else {
      throw (E) previousException;
    }
  }

}
