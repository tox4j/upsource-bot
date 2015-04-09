package im.tox.upsourcebot.client.tasks;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Class for retrying actions with different retry strategies
 */
public class RetryingCallable<E extends Exception, V> implements RecoveringCallable<E, V> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RetryingCallable.class);
  private static final int SLOT_TIME = 5000;
  private static final int CEILING = 16;

  private RecoveringCallable<E, V> callable;

  private BackoffStrategy backoffStrategy;

  public RetryingCallable(RecoveringCallable<E, V> callable, BackoffStrategy backoffStrategy) {
    this.callable = callable;
    this.backoffStrategy = backoffStrategy;
  }

  /**
   * Static helper method to create a RetryingCallable without a lot of Generic parameters
   */
  public static <E extends Exception, V> RetryingCallable<E, V> of(
      RecoveringCallable<E, V> recoveringCallable) {
    return new RetryingCallable<>(recoveringCallable, getDefaultBackoffStrategy());
  }

  /**
   * Try to execute the callable.
   *
   * @throws InterruptedException if the containing thread is interrupted
   * @throws E                    if the execution failed after the strategy depleted its retries
   */
  @Nullable
  @Override
  public V call() throws E, InterruptedException {
    Exception previousException = null;
    for (; backoffStrategy.retryAgain(); backoffStrategy.tryComplete()) {
      try {
        return callable.call();
      } catch (InterruptedException e) {
        throw e;
      } catch (Exception e) {
        LOGGER
            .error("Execution of task failed, retrying in " + backoffStrategy.waitTime() + "ms", e);
        previousException = e;
        Thread.sleep(backoffStrategy.waitTime());
      }
    }
    if (previousException instanceof RuntimeException) {
      throw (RuntimeException) previousException;
    } else {
      throw (E) previousException;
    }
  }

  private static BackoffStrategy getDefaultBackoffStrategy() {
    return new ExponentialBackoffStrategy(SLOT_TIME, CEILING);
  }

}
