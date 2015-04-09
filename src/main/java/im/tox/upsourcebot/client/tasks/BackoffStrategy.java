package im.tox.upsourcebot.client.tasks;

/**
 * Provides different strategies to RetryingCallable
 */
public interface BackoffStrategy {

  /**
   * Next amount of ms to wait
   */
  int waitTime();

  /**
   * Whether or not to try again after this run
   */
  boolean retryAgain();

  /**
   * Invoked when a try has been completed
   */
  void tryComplete();

}
