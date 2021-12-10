import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import control.textbased.Controller;
import control.textbased.DungeonConsoleController;
import model.dungeon.Dungeon;
import model.dungeon.DungeonImpl;
import model.random.RandomFactory;
import model.random.RandomFalseWithUpperBound;
import model.random.RandomGenerator;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DungeonConsoleController}.
 */
public class DungeonConsoleControllerTest {

  RandomGenerator rand;
  private Dungeon model;
  private Appendable log;

  @Before
  public void setUp() {
    rand = new RandomFactory().getRandomGenerator(false);
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 1, rand);
    log = new StringBuilder();
  }

  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    // Testing when something goes wrong with the Appendable
    // Here we are passing it a mock of an Appendable that always fails
    StringReader input = new StringReader("M E P arrow");
    Appendable gameLog = new FailingAppendable();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModelToController() {
    StringReader input = new StringReader("M E");
    Controller c = new DungeonConsoleController(input, log);
    c.playGame(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullReadableToController() {
    new DungeonConsoleController(null, log);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendableToController() {
    StringReader input = new StringReader("M E");
    new DungeonConsoleController(input, null);
  }

  @Test
  public void testInvalidAction() {
    Readable input = new StringReader("T");
    Controller c = new DungeonConsoleController(input, log);
    c.playGame(model);
    String[] splits = log.toString().split("\n");
    assertEquals("Please enter a valid input.", splits[5]);
  }

  @Test
  public void testMoveInvalidDirection() {
    Readable input = new StringReader("M N");
    Controller c = new DungeonConsoleController(input, log);
    c.playGame(model);
    String[] splits = log.toString().split("\n");
    assertEquals("Invalid direction!", splits[5]);
  }

  @Test
  public void testValidMove() {
    Readable input = new StringReader("M E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[5]);
    assertEquals("You find 1 ruby here.", gameLog.toString().split("\n")[6]);
    assertEquals("You find an arrow here.", gameLog.toString().split("\n")[7]);
  }

  @Test
  public void testPickInvalidItem() {
    Readable input = new StringReader("P abc");
    Controller c = new DungeonConsoleController(input, log);
    c.playGame(model);
    String[] splits = log.toString().split("\n");
    assertEquals("Invalid input!", splits[5]);
  }

  @Test
  public void testPickRuby() {
    Readable input = new StringReader("P ruby");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You pick up 1 ruby", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testPickDiamond() {
    Readable input = new StringReader("P diamond");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You pick up 0 diamond", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testPickSapphire() {
    Readable input = new StringReader("P sapphire");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You pick up 0 sapphire", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testPickTreasure() {
    Readable input = new StringReader("P treasure");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You pick up 1 ruby", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testPickArrow() {
    Readable input = new StringReader("P arrow");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You pick up an arrow", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testShootArrowHitMonster() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("S 1 E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?You hear a "
            + "great howl in the distance", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testShootArrowMissMonster() {
    Readable input = new StringReader("S 1 E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You shoot an arrow into the darkness", gameLog.toString().split("\n")[4]);
  }

  @Test
  public void testShootOutOfArrows() {
    Readable input = new StringReader("S 1 E S 1 E S 1 E S 1 E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You are out of arrows, explore to find more", gameLog.toString()
            .split("\n")[18]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You are out of arrows, explore to find more", gameLog.toString()
            .split("\n")[24]);
  }

  @Test
  public void testShootArrowWrongDirection() {
    Readable input = new StringReader("S 1 N");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
                    + "No door in that direction. You shoot an arrow into the same cave.",
            gameLog.toString().split("\n")[4]);
  }

  @Test
  public void testShootDistanceNaN() {
    Readable input = new StringReader("S s N");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Please enter a valid input", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testFindDeadMonster() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("S 1 E S 1 E M E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You find a dead Otyugh here.", gameLog.toString()
            .split("\n")[20]);
  }

  @Test
  public void testPlayerKilledByInjuredMonster() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("S 1 E M E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Chomp, chomp, chomp, you are eaten by an Otyugh!",
            gameLog.toString().split("\n")[13]);
    assertEquals("Better luck next time", gameLog.toString().split("\n")[14]);
  }

  @Test
  public void testPlayerSurvivesInjuredMonster() {
    RandomGenerator rand = new RandomFalseWithUpperBound();
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("S 1 W M W");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You find an injured Otyugh here and escape him. Hurry up and leave.",
            gameLog.toString().split("\n")[15]);
  }

  @Test
  public void testSmell() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("M W M S M E M E M S M S M S");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You smell something terribly pungent here.",
            gameLog.toString().split("\n")[0]);
    assertEquals("You smell something lightly pungent here.",
            gameLog.toString().split("\n")[6]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[11]);
    assertEquals("You smell something lightly pungent here.",
            gameLog.toString().split("\n")[16]);
    //terrible smell here because 2 monsters at 2 distance
    assertEquals("You smell something terribly pungent here.",
            gameLog.toString().split("\n")[21]);
    assertEquals("You smell something terribly pungent here.",
            gameLog.toString().split("\n")[25]);
    assertEquals("You smell something terribly pungent here.",
            gameLog.toString().split("\n")[29]);
    assertEquals("You smell something lightly pungent here.",
            gameLog.toString().split("\n")[33]);
  }

  @Test
  public void testSmellAfterKillingMonster() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("S 1 E S 1 E M W M S M E M E M S M S M S");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You smell something terribly pungent here.",
            gameLog.toString().split("\n")[0]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?You hear a "
            + "great howl in the distance", gameLog.toString().split("\n")[5]);
    assertEquals("You smell something terribly pungent here.",
            gameLog.toString().split("\n")[7]);

    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?You hear a "
            + "great howl in the distance", gameLog.toString().split("\n")[12]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[14]);
    assertEquals("You smell something lightly pungent here.",
            gameLog.toString().split("\n")[35]);
    assertEquals("You smell something terribly pungent here.",
            gameLog.toString().split("\n")[39]);
    assertEquals("You smell something lightly pungent here.",
            gameLog.toString().split("\n")[43]);
  }

  @Test
  public void testPlayerEatenByMonsterAndLoses() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("M E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Chomp, chomp, chomp, you are eaten by an Otyugh!",
            gameLog.toString().split("\n")[6]);
    assertEquals("Better luck next time", gameLog.toString().split("\n")[7]);
  }

  @Test
  public void testPlayerWins() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("P ruby M S M S M E M S S 1 E S 1 E M E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Congratulations!! You won.", gameLog.toString().split("\n")[37]);
    assertEquals("Destination Reached.", gameLog.toString().split("\n")[38]);
    assertEquals("Total treasure collected = Rubies: 1 Diamonds: 0 Sapphires: 0",
            gameLog.toString().split("\n")[39]);
  }

  @Test
  public void testPlayGameInWrappingDungeon() {
    model = new DungeonImpl(6, 4, 16, true,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("P arrow S 3 W S 3 W M W M W M W M S M S M S M S M N S 1 "
            + "E S 1 E M E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You pick up an arrow", gameLog.toString().split("\n")[6]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[11]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[17]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[19]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[23]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[28]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[33]);
    assertEquals("You find a dead Otyugh here.", gameLog.toString().split("\n")[34]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[44]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[48]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[52]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[56]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[58]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[61]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[63]);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[67]);

    assertEquals("Congratulations!! You won.", gameLog.toString().split("\n")[70]);
    assertEquals("Destination Reached.", gameLog.toString().split("\n")[71]);
    assertEquals("Total treasure collected = Rubies: 0 Diamonds: 0 Sapphires: 0",
            gameLog.toString().split("\n")[72]);
  }

  @Test
  public void testPlayerInATunnel() {
    Readable input = new StringReader("M W");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("You are in a cave", gameLog.toString().split("\n")[0]);
    assertEquals("You are in a tunnel", gameLog.toString().split("\n")[5]);
  }

  @Test
  public void testShootDistanceDoesNotIncludeTunnel() {
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("M S M W S 2 N S 2 N");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[15]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[21]);
  }

  @Test
  public void testShootThroughTunnel() {
    model = new DungeonImpl(6, 4, 12, true,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("M S M S S 3 E S 3 E");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[14]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[18]);
  }

  @Test
  public void testShootFromTunnel() {
    model = new DungeonImpl(6, 4, 13, true,
            25, "Nishtha", 2, rand);
    Readable input = new StringReader("M S M E M E M N S 6 W S 6 W");
    Appendable gameLog = new StringBuilder();
    Controller c = new DungeonConsoleController(input, gameLog);
    c.playGame(model);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[25]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?No. of caves (1-n)?Where to?"
            + "You hear a great howl in the distance", gameLog.toString().split("\n")[31]);
  }
}