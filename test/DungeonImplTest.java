import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.dungeon.Dungeon;
import model.dungeon.DungeonImpl;
import model.location.Direction;
import model.location.Treasure;
import model.player.Player;
import model.random.RandomFactory;
import model.random.RandomFalseWithUpperBound;
import model.random.RandomGenerator;
import model.weapon.Weapon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link DungeonImpl}.
 */
public class DungeonImplTest {

  Dungeon dungeon;
  RandomGenerator rand;
  RandomGenerator randTrue;
  Player player;
  boolean isWrapping = false;

  @Before
  public void setUp() {
    rand = new RandomFactory().getRandomGenerator(false);
    randTrue = new RandomFactory().getRandomGenerator(true);
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            25, "Nishtha", 1, rand);
    player = dungeon.getPlayer();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNumberOfCellsForNonWrappingDungeon() {
    new DungeonImpl(4, 4, 4, false,
            25, "Nishtha", 1, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNumberOfCellsForWrappingDungeon() {
    new DungeonImpl(2, 4, 0, true,
            25, "Nishtha", 1, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityForWrappingDungeon() {
    new DungeonImpl(4, 4, 18, true,
            25, "Nishtha", 1, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityForNonWrappingDungeon() {
    new DungeonImpl(5, 4, 13, false,
            25, "Nishtha", 1, rand);
  }

  @Test
  public void testValidInterconnectivityForWrappingDungeon() {
    dungeon = new DungeonImpl(6, 4, 25, true,
            25, "Nishtha", 1, rand);
    StringBuilder dungeonString = new StringBuilder();
    dungeonString.append("     |          |          |          |     \n"
            + "--- [P] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [D] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n");
    assertEquals(dungeonString.toString(), dungeon.toString());
  }

  @Test
  public void testValidInterconnectivityForNonWrappingDungeon() {
    dungeon = new DungeonImpl(5, 4, 12, false,
            25, "Nishtha", 1, rand);
    StringBuilder dungeonString = new StringBuilder();
    dungeonString.append("                                            \n"
            + "    [T] ------ [P] ------ [C] ------ [T]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [C] ------ [C] ------ [C] ------ [D]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [T] ------ [C] ------ [C] ------ [T]    \n"
            + "                                            \n");
    assertEquals(dungeonString.toString(), dungeon.toString());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidInterconnectivityForNegativePercentageOfTreasures() {
    new DungeonImpl(5, 4, 12, false,
            -25, "Nishtha", 1, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidInterconnectivityForMoreThan100PercentageOfTreasures() {
    new DungeonImpl(5, 4, 12, false,
            101, "Nishtha", 1, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidInterconnectivityForNullRandomGenerator() {
    new DungeonImpl(5, 4, 12, false,
            100, "Nishtha", 1, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWith0Monsters() {
    new DungeonImpl(5, 4, 12, false,
            100, "Nishtha", 0, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithExtraMonsters() {
    new DungeonImpl(5, 4, 12, false,
            100, "Nishtha", 16, rand);
  }

  @Test
  public void testGetPlayer() {
    assertEquals("Nishtha", dungeon.getPlayer().getName());
    assertEquals(player, dungeon.getPlayer());

  }

  @Test
  public void testGetPlayerLocation() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    assertEquals(dungeon.getPlayerLocation(), dungeon.getPlayerLocation());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testDungeonImplNullPlayer() {
    new DungeonImpl(5, 4, 12, false,
            100, null, 1, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDungeonImplEmptyPlayer() {
    new DungeonImpl(5, 4, 12, false,
            100, "", 1, rand);
  }

  @Test
  public void testGetStartingCave() {
    assertEquals(1, dungeon.getStartingCave().getId());
  }

  @Test
  public void testGetDestinationCave() {
    assertEquals(15, dungeon.getDestinationCave().getId());
  }

  @Test
  public void testMovePlayer() {
    dungeon.movePlayer(Direction.S);
    assertEquals(5, dungeon.getPlayerLocation().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePlayerNullDirection() {
    dungeon.movePlayer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePlayerInvalidDirection() {
    dungeon.movePlayer(Direction.N);
  }

  @Test
  public void testCollectAllTreasures() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectAllTreasures();
    assertEquals(Collections.emptyList(), dungeon.getPlayerLocation().getTreasures());
    collectedTreasures.put(Treasure.RUBY, 1);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void testCollectTreasure() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectTreasure(List.of(Treasure.RUBY));
    assertEquals(Collections.emptyList(), dungeon.getPlayerLocation().getTreasures());
    collectedTreasures.put(Treasure.RUBY, 1);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void testCollectTreasureNotAvailableTreasure() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectTreasure(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollectTreasureNullTreasureList() {
    dungeon.collectTreasure(null);
  }

  @Test
  public void testCollectTreasureEmptyTreasureList() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectTreasure(Collections.emptyList());
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollectTreasureNullTreasureElement() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    List<Treasure> givenTreasure = new ArrayList<>();
    givenTreasure.add(null);
    dungeon.collectTreasure(givenTreasure);
  }

  @Test
  public void testIsDestinationReached() {
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.E);
    assertTrue(dungeon.isDestinationReached());
  }

  @Test
  public void testToString() {
    StringBuilder dungeonString = new StringBuilder();
    dungeonString.append("                                            \n"
            + "    [T] ------ [P] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [T]        [T]        [C] ------ [D]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [T]        [T]        [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C]        [C]        [T] ------ [C]    \n"
            + "                                            \n");
    assertEquals(dungeonString.toString(), dungeon.toString());

  }

  @Test
  public void testWrappingDungeon() {
    dungeon = new DungeonImpl(6, 4, 22, true,
            25, "Nishtha", 1, rand);
    player = dungeon.getPlayer();
    assertEquals(0, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.W);
    assertEquals(3, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.E);
    assertEquals(0, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.N);
    assertEquals(20, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.S);
    assertEquals(0, dungeon.getPlayerLocation().getId());
  }

  @Test
  public void testNonWrappingDungeon() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.E);
    assertEquals(2, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.S);
    assertEquals(6, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.W);
    assertEquals(5, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.N);
    assertEquals(1, dungeon.getPlayerLocation().getId());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testNonWrappingDungeonIllegalMove() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.N);
  }

  @Test
  public void testEveryNodeCanBeReached() {
    Set<Integer> visited = new HashSet<>();
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    do {
      visited.add(dungeon.getPlayerLocation().getId());
      List<Direction> possibleMoves = dungeon.getPlayerLocation().getPossibleMoves();
      if (dungeon.getPlayerLocation().getId() == 14) {
        //removing destination position move as then the game will be over.
        possibleMoves.remove(Direction.E);
      }
      Direction move = possibleMoves.get(randTrue.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
    }
    while (visited.size() != 23);
    Set<Integer> expectedVisitedNodes = new HashSet<>();
    expectedVisitedNodes.addAll(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16,
            17, 18, 19, 20, 21, 22, 23));
    assertEquals(expectedVisitedNodes, visited);
  }

  @Test
  public void testPercentageOfTreasures() {
    Set<Integer> visited = new HashSet<>();
    int numberOfCavesWithTreasures = 0;
    int numberOfCaves = 0;
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    do {
      if (dungeon.getPlayerLocation().getTreasures().size() > 0) {
        numberOfCavesWithTreasures++;
      }
      dungeon.collectAllTreasures();
      if (!dungeon.getPlayerLocation().isTunnel() && !visited
              .contains(dungeon.getPlayerLocation().getId())) {
        numberOfCaves++;
      }
      visited.add(dungeon.getPlayerLocation().getId());
      List<Direction> possibleMoves = dungeon.getPlayerLocation().getPossibleMoves();
      if (dungeon.getPlayerLocation().getId() == 14) {
        possibleMoves.remove(Direction.E);
      }
      Direction move = possibleMoves.get(randTrue.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
    }
    while (visited.size() != 23);
    assertEquals((int) (numberOfCaves * 0.25), numberOfCavesWithTreasures);
  }

  @Test
  public void testPercentageOfArrows() {
    Set<Integer> visited = new HashSet<>();
    int numberOfLocationsWithArrows = 0;
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);

    do {
      if (dungeon.getPlayerLocation().getArrows().size() > 0) {
        numberOfLocationsWithArrows++;
      }
      dungeon.pickArrows();
      visited.add(dungeon.getPlayerLocation().getId());
      List<Direction> possibleMoves = dungeon.getPlayerLocation().getPossibleMoves();
      if (dungeon.getPlayerLocation().getId() == 14) {
        possibleMoves.remove(Direction.E);
      }
      Direction move = possibleMoves.get(randTrue.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
    }
    while (visited.size() != 23);
    assertEquals((int) (24 * 0.25), numberOfLocationsWithArrows);
  }

  @Test
  public void testNumberOfMonsters() {
    Set<Integer> visited = new HashSet<>();
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            80, "Nishtha", 4, rand);
    int numberOfMonsters = 0;

    dungeon.pickArrows();
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    dungeon.movePlayer(Direction.E);
    dungeon.pickArrows();
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    dungeon.movePlayer(Direction.E);
    dungeon.pickArrows();
    dungeon.shootArrow(3, Direction.W);
    dungeon.shootArrow(3, Direction.W);
    dungeon.movePlayer(Direction.W);
    dungeon.movePlayer(Direction.S);
    dungeon.pickArrows();
    dungeon.movePlayer(Direction.S);
    dungeon.pickArrows();
    dungeon.movePlayer(Direction.S);
    dungeon.pickArrows();
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);

    do {
      if (dungeon.getPlayerLocation().getMonster() != null
              && !visited.contains(dungeon.getPlayerLocation().getId())) {
        numberOfMonsters++;
      }
      dungeon.pickArrows();
      visited.add(dungeon.getPlayerLocation().getId());
      List<Direction> possibleMoves = dungeon.getPlayerLocation().getPossibleMoves();
      if (dungeon.getPlayerLocation().getId() == 14) {
        possibleMoves.remove(Direction.E);
      }
      Direction move = possibleMoves.get(randTrue.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
    }
    while (visited.size() != 23);

    do {
      List<Direction> possibleMoves = dungeon.getPlayerLocation().getPossibleMoves();
      Direction move = possibleMoves.get(randTrue.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
    }
    while (!dungeon.isDestinationReached());

    assertTrue(dungeon.isDestinationReached());
    if (dungeon.getPlayerLocation().getMonster() != null) {
      numberOfMonsters++;
    }
    assertEquals(4, numberOfMonsters);
  }

  @Test
  public void testDestinationHasMonster() {
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    dungeon.movePlayer(Direction.E);
    assertTrue(dungeon.isDestinationReached());
    assertNotNull(dungeon.getPlayerLocation().getMonster());
    assertEquals(0, dungeon.getPlayerLocation().getMonster().getHealthPercentage());
  }

  @Test
  public void testShortestPath5() {
    int pathLength = 0;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    dungeon.movePlayer(Direction.E);
    pathLength++;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    dungeon.movePlayer(Direction.E);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertTrue(pathLength >= 5);
  }

  @Test
  public void testShortestPathSmallestAndMostInterconnectedWrappingDungeon() {
    dungeon = new DungeonImpl(6, 4, 25, true,
            25, "Nishtha", 1, rand);
    int pathLength = 0;
    dungeon.movePlayer(Direction.N);
    pathLength++;
    dungeon.movePlayer(Direction.N);
    pathLength++;
    dungeon.movePlayer(Direction.N);
    pathLength++;
    dungeon.movePlayer(Direction.E);
    pathLength++;
    dungeon.movePlayer(Direction.E);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertEquals(5, pathLength);
  }

  @Test
  public void testShortestPathSmallestAndMostInterconnectedNonWrappingDungeon() {
    dungeon = new DungeonImpl(5, 4, 12, false,
            25, "Nishtha", 1, rand);
    int pathLength = 0;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    dungeon.movePlayer(Direction.E);
    pathLength++;
    dungeon.movePlayer(Direction.E);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertEquals(5, pathLength);
  }

  @Test
  public void testShortestPathZeroInterconnectivity() {
    dungeon = new DungeonImpl(3, 4, 0, true,
            25, "Nishtha", 1, rand);
    int pathLength = 0;
    dungeon.movePlayer(Direction.W);
    pathLength++;
    dungeon.movePlayer(Direction.W);
    pathLength++;
    dungeon.movePlayer(Direction.W);
    pathLength++;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    dungeon.movePlayer(Direction.S);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertEquals(5, pathLength);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShortestPathMaxInterconnectivitySmallestWrappingDungeon() {
    dungeon = new DungeonImpl(3, 4, 13, true,
            25, "Nishtha", 1, rand);
  }

  @Test
  public void testPlayerStartsAtStartingLocation() {
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getStartingCave());
    assertEquals(dungeon.getPlayer().getLocation().getId(), dungeon.getStartingCave().getId());
  }

  @Test
  public void testPlayerTraversalFromStartToEnd() {
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getStartingCave());
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.S);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.S);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.E);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.S);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.E);
    assertTrue(dungeon.isDestinationReached());
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getDestinationCave());
  }

  @Test
  public void testPlayerTraversalFromStartToEndRandomCave() {
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            25, "Nishtha", 1, randTrue);
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getStartingCave());
    do {
      dungeon.movePlayer(dungeon.getPlayerLocation().getPossibleMoves().get(randTrue
              .getRandom(dungeon.getPlayerLocation().getPossibleMoves().size(), 0)));
    }
    while (!dungeon.isDestinationReached());
    assertTrue(dungeon.isDestinationReached());
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getDestinationCave());
  }

  @Test
  public void testPlayerDescription() {
    assertEquals("Nishtha", dungeon.getPlayer().getName());
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayer().getLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectAllTreasures();
    assertEquals(Collections.emptyList(), dungeon.getPlayer().getLocation().getTreasures());
    collectedTreasures.put(Treasure.RUBY, 1);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void getLocationDescription() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
  }

  @Test
  public void testMovingInFourDirections() {
    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(1, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.E);

    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(2, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(2, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.S);

    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(2, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(6, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.W);

    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(5, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.N);

    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(1, dungeon.getPlayerLocation().getId());
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveAfterDestinationReached() {
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    dungeon.movePlayer(Direction.E);
    try {
      dungeon.movePlayer(Direction.W);
    } catch (IllegalStateException iae) {
      String message = "Game is already over!";
      assertEquals(message, iae.getMessage());
      throw iae;
    }
    fail("Game over exception did not throw!");
  }

  @Test
  public void testMoveFailWhenPlayerIsKilledByOtyugh() {
    assertTrue(dungeon.movePlayer(Direction.E));
    assertTrue(dungeon.movePlayer(Direction.S));
    assertTrue(dungeon.movePlayer(Direction.S));
    assertTrue(dungeon.movePlayer(Direction.S));
    assertFalse(dungeon.movePlayer(Direction.E));
  }

  @Test
  public void testMoveFailWhenPlayerIsKilledByInjuredOtyugh() {
    assertTrue(dungeon.movePlayer(Direction.E));
    assertTrue(dungeon.movePlayer(Direction.S));
    assertTrue(dungeon.movePlayer(Direction.S));
    assertTrue(dungeon.movePlayer(Direction.S));
    dungeon.shootArrow(1, Direction.E);
    assertFalse(dungeon.movePlayer(Direction.E));
  }

  @Test
  public void testMoveSuccessWhenPlayerSurvivesInjuredOtyugh() {
    RandomGenerator rand = new RandomFalseWithUpperBound();
    dungeon = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    dungeon.shootArrow(1, Direction.W);
    assertTrue(dungeon.movePlayer(Direction.W));
  }

  @Test
  public void testMoveSuccessWhenOtyughIsKilled() {
    assertTrue(dungeon.movePlayer(Direction.E));
    assertTrue(dungeon.movePlayer(Direction.S));
    assertTrue(dungeon.movePlayer(Direction.S));
    assertTrue(dungeon.movePlayer(Direction.S));
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    assertTrue(dungeon.movePlayer(Direction.E));
  }

  @Test
  public void testSmell() {
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            25, "Nishtha", 2, rand);
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(1, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.E);
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.E);
    assertEquals(1, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.W);
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    //terrible smell because of 2 monsters at two positions.
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.E);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.W);
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(1, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
  }

  @Test
  public void testSmellAfterKillingTheMonster() {
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            25, "Nishtha", 2, rand);
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.E);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.E);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.W);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(1, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.E);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.W);
    assertEquals(1, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(2, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(1, dungeon.getPlayerLocation().getSmell());
    dungeon.movePlayer(Direction.S);
    assertEquals(0, dungeon.getPlayerLocation().getSmell());
  }

  @Test
  public void testShootMissMonster() {
    dungeon.movePlayer(Direction.E);
    assertFalse(dungeon.shootArrow(4, Direction.S));
  }

  @Test
  public void testShootHitMonster() {
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    assertTrue(dungeon.shootArrow(1, Direction.E));
  }

  @Test
  public void testPlayerKillsMonster() {
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    assertTrue(dungeon.shootArrow(1, Direction.E));
    assertTrue(dungeon.shootArrow(1, Direction.E));
    assertTrue(dungeon.movePlayer(Direction.E));
    assertEquals(0, dungeon.getPlayerLocation().getMonster().getHealthPercentage());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootInvalidDirection() {
    dungeon.shootArrow(1, Direction.N);
  }

  @Test
  public void testShootDistanceDoesNotIncludeTunnel() {
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            25, "Nishtha", 2, rand);
    assertEquals("                                            \n"
            + "    [T] ------ [P] ------ [O] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [T]        [T]        [C] ------ [D]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [T]        [T]        [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C]        [C]        [T] ------ [C]    \n"
            + "                                            \n", dungeon.toString());
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.W);
    assertTrue(dungeon.shootArrow(2, Direction.N));
  }

  @Test
  public void testPlayerLoses() {
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.E);
    assertTrue(dungeon.isDestinationReached());
    assertTrue(dungeon.getPlayer().isDead());
  }

  @Test
  public void testPlayerWins() {
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    dungeon.shootArrow(1, Direction.E);
    dungeon.shootArrow(1, Direction.E);
    dungeon.movePlayer(Direction.E);
    assertTrue(dungeon.isDestinationReached());
    assertFalse(dungeon.getPlayer().isDead());
  }

  @Test
  public void testPickArrows() {
    assertEquals(List.of(Weapon.ARROW), dungeon.getPlayerLocation().getArrows());
    assertEquals(3, dungeon.getPlayer().getNumberOfArrows());
    dungeon.pickArrows();
    assertEquals(4, dungeon.getPlayer().getNumberOfArrows());
    dungeon.movePlayer(Direction.W);
    assertEquals(List.of(Weapon.ARROW), dungeon.getPlayerLocation().getArrows());
    dungeon.pickArrows();
    assertEquals(5, dungeon.getPlayer().getNumberOfArrows());
  }

  @Test
  public void testShootArrowWrappingDungeon() {
    dungeon = new DungeonImpl(6, 4, 18, true,
            25, "Nishtha", 2, rand);
    assertEquals("                                            \n" +
            "--- [P] ------ [O] ------ [C] ------ [C] ---\n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "--- [C] ------ [C] ------ [C] ------ [C] ---\n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "--- [C] ------ [C] ------ [C] ------ [C] ---\n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [D] ------ [C]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [T] ------ [C] ------ [C] ------ [T]    \n" +
            "                                            \n", dungeon.toString());
    assertTrue(dungeon.shootArrow(3, Direction.W));
  }

  @Test
  public void testShootArrowThroughTunnel() {
    dungeon = new DungeonImpl(6, 4, 12, true,
            25, "Nishtha", 2, rand);
    assertEquals("                                            \n" +
            "    [T] ------ [P] ------ [O] ------ [T]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [T]    \n" +
            "     |          |          |                \n" +
            "     |          |          |                \n" +
            "    [C] ------ [C] ------ [C] ------ [D]    \n" +
            "     |          |          |                \n" +
            "     |          |          |                \n" +
            "    [C] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |                \n" +
            "     |          |          |                \n" +
            "    [T] ------ [C] ------ [C] ------ [C]    \n" +
            "                                            \n", dungeon.toString());
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.S);
    assertTrue(dungeon.shootArrow(3, Direction.E));
  }

  @Test
  public void testShootArrowFromTunnel() {
    dungeon = new DungeonImpl(6, 4, 13, true,
            25, "Nishtha", 2, rand);
    assertEquals("                                            \n" +
            "    [T] ------ [P] ------ [O] ------ [T]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [T]    \n" +
            "     |          |          |                \n" +
            "     |          |          |                \n" +
            "    [D] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |                \n" +
            "     |          |          |                \n" +
            "    [T] ------ [C] ------ [C] ------ [C]    \n" +
            "                                            \n", dungeon.toString());
    dungeon.movePlayer(Direction.S);
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.E);
    dungeon.movePlayer(Direction.N);
    assertTrue(dungeon.shootArrow(6, Direction.W));
  }

  @Test
  public void testShootArrowThroughTunnel2() {
    dungeon = new DungeonImpl(6, 4, 13, true,
            25, "Nishtha", 2, rand);
    assertEquals("                                            \n" +
            "    [T] ------ [P] ------ [O] ------ [T]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |          |     \n" +
            "     |          |          |          |     \n" +
            "    [C] ------ [C] ------ [C] ------ [T]    \n" +
            "     |          |          |                \n" +
            "     |          |          |                \n" +
            "    [D] ------ [C] ------ [C] ------ [C]    \n" +
            "     |          |          |                \n" +
            "     |          |          |                \n" +
            "    [T] ------ [C] ------ [C] ------ [C]    \n" +
            "                                            \n", dungeon.toString());
    dungeon.movePlayer(Direction.W);
    assertTrue(dungeon.shootArrow(4, Direction.S));
  }
}


