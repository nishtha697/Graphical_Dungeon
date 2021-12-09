import org.junit.Before;
import org.junit.Test;

import model.monster.Monster;
import model.monster.Otyugh;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Otyugh}.
 */
public class MonsterTest {

  Monster otyugh;

  @Before
  public void setUp() {
    otyugh = new Otyugh();
  }

  @Test(expected = IllegalArgumentException.class)
  public void reduceHealthLessThan0() {
    otyugh.reduceHealth(-10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void reduceHealthGreaterThan100() {
    otyugh.reduceHealth(110);
  }

  @Test
  public void reduceHealth() {
    assertEquals(100, otyugh.getHealthPercentage());
    otyugh.reduceHealth(50);
    assertEquals(50, otyugh.getHealthPercentage());
    otyugh.reduceHealth(50);
    assertEquals(0, otyugh.getHealthPercentage());
  }

  @Test
  public void getHealthPercentage() {
    assertEquals(100, otyugh.getHealthPercentage());
  }
}