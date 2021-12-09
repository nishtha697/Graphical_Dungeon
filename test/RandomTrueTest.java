import org.junit.Before;
import org.junit.Test;

import model.random.RandomGenerator;
import model.random.RandomTrue;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link RandomTrue}.
 */
public class RandomTrueTest {

  RandomGenerator rand;

  @Before
  public void setUp() {
    rand = new RandomTrue();
  }

  @Test
  public void getRandom() {
    int random = rand.getRandom(12, 6);
    assertTrue(random >= 6 && random < 12);
  }
}