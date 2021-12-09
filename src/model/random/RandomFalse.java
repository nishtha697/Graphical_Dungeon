package model.random;

/**
 * This class implements  {@link RandomGenerator} and represents a predictable model.random number
 * generation class.
 */
public class RandomFalse implements RandomGenerator {

  @Override
  public int getRandom(int upperBound, int lowerBound) {
    return lowerBound;
  }
}
