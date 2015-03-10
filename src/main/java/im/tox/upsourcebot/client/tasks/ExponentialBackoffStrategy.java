package im.tox.upsourcebot.client.tasks;

import java.util.Random;

public class ExponentialBackoffStrategy implements BackoffStrategy {

  private int maxRetries;
  private int slotTime;
  private int ceiling;
  private int tries = 0;
  private Random random = new Random();

  public ExponentialBackoffStrategy(int maxRetries, int slotTime, int ceiling) {
    if (maxRetries <= 0) {
      throw new IllegalArgumentException("Must try at least 1 time");
    }
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
    this.maxRetries = maxRetries;
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
    return maxRetries > tries;
  }

  @Override
  public void tryComplete() {
    tries++;
  }

}
