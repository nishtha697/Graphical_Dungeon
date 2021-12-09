import org.junit.Before;
import org.junit.Test;

import control.gui.DungeonControllerImpl;
import control.gui.DungeonGUIController;
import control.gui.PreLaunchView;
import model.dungeon.Dungeon;
import model.dungeon.DungeonImpl;
import model.random.RandomFactory;
import model.random.RandomGenerator;

import static org.junit.Assert.assertEquals;

public class DungeonGUIControllerTest {

  RandomGenerator rand;
  private Dungeon model;
  private Appendable log;
  private DungeonGUIController controller;
  private PreLaunchView mockPreLaunchView;

  @Before
  public void setUp() {
    mockPreLaunchView = new MockPreLaunchView();
    controller = new DungeonControllerImpl(mockPreLaunchView);
    rand = new RandomFactory().getRandomGenerator(false);
    model = new DungeonImpl(6, 4, 4, false,
            25, "Nishtha", 1, rand);
    log = new StringBuilder();
  }

  @Test
  public void testHandleCellClick() {
    controller.handleCellClick(1, 1);
    assertEquals(1, model.getPlayerLocation().getId());
  }

  @Test
  public void testRestartGame() {
  }

  @Test
  public void testNewGame() {
  }

  @Test
  public void testHandleKeyMove() {
  }

  @Test
  public void testHandlePickAll() {
  }

  @Test
  public void testHandlePickTreasures() {
  }

  @Test
  public void testHandlePickArrows() {
  }

  @Test
  public void testHandlePickRubies() {
  }

  @Test
  public void testHandlePickEmeralds() {
  }

  @Test
  public void testHandlePickDiamonds() {
  }

  @Test
  public void testHandleShootArrow() {
  }
}