import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import model.location.Cave;
import model.location.Direction;
import model.location.LocationUpdateState;
import model.location.Treasure;
import model.monster.Monster;
import model.monster.Otyugh;
import model.weapon.Weapon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Cave}.
 */
public class CaveTest {

  LocationUpdateState cave;

  @Before
  public void setUp() {
    cave = new Cave(1, 0, 1);
  }

  @Test
  public void testGetId() {
    assertEquals(1, cave.getId());
  }

  @Test
  public void testGetCoordinates() {
    assertEquals(0, cave.getCoordinates().getX());
    assertEquals(1, cave.getCoordinates().getY());
  }

  @Test
  public void testGetTreasures() {
    assertEquals(Collections.emptyList(), cave.getTreasures());
    cave.addTreasures(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.DIAMOND), cave.getTreasures());
  }

  @Test
  public void testGetPossibleMoves() {
    assertEquals(Collections.emptyList(), cave.getPossibleMoves());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0)));
    assertTrue(cave.getPossibleMoves().containsAll(List.of(Direction.N, Direction.S)));
  }

  @Test
  public void testTunnel() {
    assertFalse(cave.isTunnel());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0)));
    assertFalse(cave.isTunnel());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0)));
    assertTrue(cave.isTunnel());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0), Direction.E, new Cave(1, 0, 0)));
    assertFalse(cave.isTunnel());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0), Direction.E, new Cave(1, 0, 0),
            Direction.W, new Cave(1, 0, 0)));
    assertFalse(cave.isTunnel());
  }

  @Test
  public void testSetValidMoves() {
    assertEquals(Collections.emptyList(), cave.getPossibleMoves());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0), Direction.E, new Cave(1, 0, 0),
            Direction.W, new Cave(1, 0, 0)));
    assertTrue(cave.getPossibleMoves().containsAll(List.of(Direction.S, Direction.E,
            Direction.W, Direction.N)));
  }

  @Test
  public void testAddTreasures() {
    assertEquals(Collections.emptyList(), cave.getTreasures());
    cave.addTreasures(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE));
    assertEquals(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE), cave.getTreasures());
  }

  @Test
  public void testRemoveTreasures() {
    assertEquals(Collections.emptyList(), cave.getTreasures());
    List<Treasure> addedTreasures = new ArrayList<>(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE));
    cave.addTreasures(addedTreasures);
    assertEquals(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE), cave.getTreasures());
    cave.removeTreasures(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.SAPPHIRE), cave.getTreasures());
    cave.removeTreasures(List.of(Treasure.SAPPHIRE));
    assertEquals(Collections.emptyList(), cave.getTreasures());
  }

  @Test
  public void testToString() {
    assertEquals("C", cave.toString());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0)));
    assertEquals("C", cave.toString());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0)));
    assertEquals("T", cave.toString());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0), Direction.E, new Cave(1, 0, 0)));
    assertEquals("C", cave.toString());
    cave.setValidMoves(Map.of(Direction.S, new Cave(1, 0, 0), Direction.N,
            new Cave(1, 0, 0), Direction.E, new Cave(1, 0, 0),
            Direction.W, new Cave(1, 0, 0)));
    assertEquals("C", cave.toString());
  }

  @Test
  public void testGetSmell() {
    assertEquals(0, cave.getSmell());
  }

  @Test
  public void testAddSmell() {
    assertEquals(0, cave.getSmell());
    cave.addSmell(1);
    assertEquals(1, cave.getSmell());
    cave.addSmell(2);
    assertEquals(3, cave.getSmell());
  }

  @Test
  public void testReduceSmell() {
    assertEquals(0, cave.getSmell());
    cave.addSmell(3);
    assertEquals(3, cave.getSmell());
    cave.reduceSmell(1);
    assertEquals(2, cave.getSmell());
    cave.reduceSmell(3);
    assertEquals(0, cave.getSmell());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddArrowsNullList() {
    cave.addArrows(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddArrowsNullListElement() {
    List<Weapon> weapons = new ArrayList();
    weapons.add(Weapon.ARROW);
    weapons.add(null);
    cave.addArrows(weapons);
  }

  @Test
  public void testAddArrows() {
    assertEquals(Collections.emptyList(), cave.getArrows());
    cave.addArrows(List.of(Weapon.ARROW, Weapon.ARROW));
    assertEquals(List.of(Weapon.ARROW, Weapon.ARROW), cave.getArrows());
  }

  @Test
  public void testRemoveArrows() {
    assertEquals(Collections.emptyList(), cave.getArrows());
    cave.addArrows(List.of(Weapon.ARROW, Weapon.ARROW));
    assertEquals(List.of(Weapon.ARROW, Weapon.ARROW), cave.getArrows());
    cave.removeArrows();
    assertEquals(Collections.emptyList(), cave.getArrows());
  }

  @Test
  public void testGetMonster() {
    assertEquals(null, cave.getMonster());
  }

  @Test
  public void testAddMonster() {
    Monster otyugh = new Otyugh();
    assertEquals(null, cave.getMonster());
    cave.addMonster(otyugh);
    assertEquals(otyugh, cave.getMonster());
  }

  @Test
  public void testGetNeighbors() {
    assertEquals(Collections.emptyList(), cave.getNeighbors());
    LocationUpdateState south = new Cave(1, 2, 2);
    LocationUpdateState north = new Cave(2, 0, 2);
    LocationUpdateState east = new Cave(3, 1, 3);

    List<LocationUpdateState> neighbors = new ArrayList<>();
    neighbors.add(south);
    neighbors.add(north);
    neighbors.add(east);

    cave.setValidMoves(Map.of(Direction.S, south, Direction.N, north, Direction.E, east));
    assertTrue(cave.getNeighbors().containsAll(neighbors));
    assertNotEquals(neighbors, cave.getNeighbors());
  }
}