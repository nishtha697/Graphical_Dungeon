import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.location.Cave;
import model.location.LocationUpdateState;
import model.location.Treasure;
import model.player.PlayerImpl;
import model.player.PlayerUpdateState;
import model.weapon.Weapon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PlayerImpl}.
 */
public class PlayerImplTest {

  PlayerUpdateState player;
  LocationUpdateState location;
  LocationUpdateState nextLocation;

  @Before
  public void setUp() {
    location = new Cave(1, 0, 0);
    nextLocation = new Cave(2, 0, 1);
    player = new PlayerImpl("Nishtha", location);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerImplNullName() {
    new PlayerImpl(null, new Cave(1, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerImplEmptyName() {
    new PlayerImpl("", new Cave(1, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerImplNullLocation() {
    new PlayerImpl("Nishtha", null);
  }

  @Test
  public void testGetName() {
    assertEquals("Nishtha", player.getName());
  }

  @Test
  public void getCollectedTreasures() {
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.RUBY, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    assertEquals(collectedTreasures, player.getCollectedTreasures());
  }

  @Test
  public void testGetLocation() {
    assertEquals(1, player.getLocation().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollectTreasuresNullTreasures() {
    player.collectTreasures(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollectTreasuresNullTreasureElement() {
    List<Treasure> treasures = new ArrayList<>();
    treasures.add(Treasure.SAPPHIRE);
    treasures.add(Treasure.RUBY);
    treasures.add(null);
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.RUBY, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    location.addTreasures(treasures);
    player.collectTreasures(treasures);
  }

  @Test
  public void testCollectTreasures() {
    List<Treasure> treasures = new ArrayList<>(List.of(Treasure.RUBY, Treasure.SAPPHIRE));
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.RUBY, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    location.addTreasures(treasures);
    player.collectTreasures(Collections.emptyList());
    assertEquals(List.of(Treasure.RUBY, Treasure.SAPPHIRE), location.getTreasures());
    assertEquals(collectedTreasures, player.getCollectedTreasures());
    player.collectTreasures(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.RUBY, Treasure.SAPPHIRE), location.getTreasures());
    assertEquals(collectedTreasures, player.getCollectedTreasures());
    player.collectTreasures(List.of(Treasure.RUBY));
    assertEquals(List.of(Treasure.SAPPHIRE), location.getTreasures());
    collectedTreasures.put(Treasure.RUBY, 1);
    assertEquals(collectedTreasures, player.getCollectedTreasures());
    player.collectTreasures(List.of(Treasure.SAPPHIRE));
    assertEquals(Collections.emptyList(), location.getTreasures());
    collectedTreasures.put(Treasure.SAPPHIRE, 1);
    assertEquals(collectedTreasures, player.getCollectedTreasures());
    location.addTreasures(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.DIAMOND), location.getTreasures());
    player.collectTreasures(List.of(Treasure.DIAMOND));
    assertEquals(Collections.emptyList(), location.getTreasures());
    collectedTreasures.put(Treasure.DIAMOND, 1);
    assertEquals(collectedTreasures, player.getCollectedTreasures());
  }

  @Test
  public void testMove() {
    assertEquals(1, player.getLocation().getId());
    player.move(nextLocation);
    assertEquals(2, player.getLocation().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNullLocation() {
    player.move(null);
  }

  //player starts with 3 arrows
  @Test
  public void testGetNumberOfArrows() {
    assertEquals(3, player.getNumberOfArrows());
  }

  @Test
  public void testPickArrows() {
    assertEquals(3, player.getNumberOfArrows());
    location.addArrows(List.of(Weapon.ARROW, Weapon.ARROW));
    player.pickArrows();
    assertEquals(5, player.getNumberOfArrows());
  }

  @Test
  public void testShootArrows() {
    assertEquals(3, player.getNumberOfArrows());
    player.shootArrow();
    assertEquals(2, player.getNumberOfArrows());
    player.shootArrow();
    assertEquals(1, player.getNumberOfArrows());
    player.shootArrow();
    assertEquals(0, player.getNumberOfArrows());
  }

  @Test(expected = IllegalStateException.class)
  public void testShootArrowsWhenNoMoreArrows() {
    assertEquals(3, player.getNumberOfArrows());
    player.shootArrow();
    assertEquals(2, player.getNumberOfArrows());
    player.shootArrow();
    assertEquals(1, player.getNumberOfArrows());
    player.shootArrow();
    assertEquals(0, player.getNumberOfArrows());
    player.shootArrow();
  }

  @Test
  public void testIsDead() {
    assertFalse(player.isDead());
  }

  @Test
  public void testKillPlayer() {
    assertFalse(player.isDead());
    player.killPlayer();
    assertTrue(player.isDead());
  }
}