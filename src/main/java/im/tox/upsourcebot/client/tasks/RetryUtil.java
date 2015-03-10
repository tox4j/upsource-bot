package im.tox.upsourcebot.client.tasks;


/**
 * Class for retrying actions with different retry strategies
 */
public class RetryUtil {

  public static <E extends Exception> void retryExecute(RecoveringCallable<E> callable,
                                                        BackoffStrategy strategy)
      throws InterruptedException, E {
    Exception previousException = null;
    for (; strategy.retryAgain(); strategy.tryComplete()) {
      try {
        callable.call();
        return;
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
