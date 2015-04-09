package im.tox.upsourcebot.client.tasks;

import java.util.Random;

/**
 * Backoff strategy with binary exponential backoff
 */
public class ExponentialBackoffStrategy implements BackoffStrategy {

  private int slotTime;
  private int ceiling;
  private int tries = 0;
  private Random random = new Random();

  /**
   * @param slotTime the slot time, must be >= 1
   * @param ceiling  the ceiling for the exponent. Must be >= 1 and <= 30
   */
  public ExponentialBackoffStrategy(int slotTime, int ceiling) {
    if (slotTime <= 0) {
      throw new IllegalArgumentException("Slot time must be at least 1 ms");
    }
    if (ceiling <= 0) {
      throw new IllegalArgumentException("Ceiling for backoff must be at least 1");
    }
    if (ceiling > 30) {
      throw new IllegalArgumentException(
          "Ceiling cannot be larger than 30, to prevent overflows");
    }
    this.slotTime = slotTime;
    this.ceiling = ceiling;
  }

  @Override
  public int waitTime() {
    int maxExponent = Math.min(tries, ceiling);
    int exponent = random.nextInt(maxExponent + 1);
    try {
      return Math.multiplyExact(slotTime, (int) Math.pow(2, exponent));
    } catch (ArithmeticException e) {
      return Integer.MAX_VALUE;
    }
  }

  @Override
  public boolean retryAgain() {
    return true;
  }

  @Override
  public void tryComplete() {
    tries++;
  }

}
