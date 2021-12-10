import org.junit.Before;
import org.junit.Test;

import control.gui.DungeonControllerImpl;
import control.gui.DungeonGUIController;
import control.gui.PreLaunchView;
import model.dungeon.Dungeon;
import model.location.Direction;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DungeonGUIController}.
 */
public class DungeonGUIControllerTest {

  private Appendable log;
  private DungeonGUIController controller;


  @Before
  public void setUp() {
    log = new StringBuilder();
    PreLaunchView mockPreLaunchView = new MockPreLaunchView();
    Dungeon model = new MockModel(log);
    MockView view = new MockView(log);
    controller = new DungeonControllerImpl(model, view, mockPreLaunchView, 6, 4);
  }

  @Test
  public void testNewGame() {
    controller.newGame();
    assertEquals("close called", log.toString());
  }

  @Test
  public void testHandleKeyMove() {
    controller.handleKeyMove(Direction.E);
    assertEquals("movePlayer called\nrefresh called", log.toString());
  }

  @Test
  public void testHandlePickAll() {
    controller.handlePickAll();
    assertEquals("pickArrows called\ncollectAllTreasures called"
            + "\nrefresh called", log.toString());
  }

  @Test
  public void testHandlePickTreasures() {
    controller.handlePickTreasures();
    assertEquals("collectAllTreasures called\nrefresh called", log.toString());
  }

  @Test
  public void testHandlePickArrows() {
    controller.handlePickArrows();
    assertEquals("pickArrows called\nrefresh called", log.toString());

  }

  @Test
  public void testHandlePickRubies() {
    controller.handlePickRubies();
    assertEquals("collectTreasure called\nrefresh called", log.toString());
  }

  @Test
  public void testHandlePickEmeralds() {
    controller.handlePickEmeralds();
    assertEquals("collectTreasure called\nrefresh called", log.toString());
  }

  @Test
  public void testHandlePickDiamonds() {
    controller.handlePickDiamonds();
    assertEquals("collectTreasure called\nrefresh called", log.toString());
  }

  @Test
  public void testHandleShootArrow() {
    controller.handleShootArrow(Direction.E, 1);
    assertEquals("shootArrow called 1 E\nrefresh called", log.toString());
  }
}