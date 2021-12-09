import org.junit.Before;
import org.junit.Test;

import model.random.RandomFalseWithUpperBound;
import model.random.RandomGenerator;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link RandomFalseWithUpperBound}.
 */
public class RandomFalseWithUpperBoundTest {

  RandomGenerator rand;

  @Before
  public void setUp() {
    rand = new RandomFalseWithUpperBound();
  }

  @Test
  public void getRandom() {
    int random = rand.getRandom(12, 6);
    assertEquals(11, random);
  }
}