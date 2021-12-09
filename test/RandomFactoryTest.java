import org.junit.Before;
import org.junit.Test;

import model.random.RandomFactory;
import model.random.RandomFalse;
import model.random.RandomGenerator;
import model.random.RandomTrue;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link RandomFactory}.
 */
public class RandomFactoryTest {

  RandomFactory randomFactory;
  RandomGenerator rand;

  @Before
  public void setUp() {
    randomFactory = new RandomFactory();
  }

  @Test
  public void testGetRandomGeneratorTrue() {
    rand = randomFactory.getRandomGenerator(true);
    assertTrue(rand instanceof RandomTrue);
  }

  @Test
  public void testGetRandomGeneratorFalse() {
    rand = randomFactory.getRandomGenerator(false);
    assertTrue(rand instanceof RandomFalse);
  }
}