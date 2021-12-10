package control.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import model.dungeon.Dungeon;
import model.dungeon.DungeonImpl;
import model.location.Direction;
import model.location.Location;
import model.location.Treasure;
import model.random.RandomFactory;
import model.random.RandomGenerator;

/**
 * The DungeonControllerImpl class represents the controller of the dungeon game. It interacts
 * with the model and the graphical view of the game. It tells view when to update it's state and
 * tell model to mutate the state when an action is performed.
 */
public class DungeonControllerImpl implements DungeonGUIController, ActionListener {

  private final PreLaunchView prelaunchView;
  private Dungeon model;
  private Dungeon freshModel;
  private DungeonView view;
  private int rows;
  private int cols;

  /**
   * Constructs Dungeon controller.
   *
   * @throws IllegalArgumentException if {@code prelaunchView} is {@code null}.
   */
  public DungeonControllerImpl(PreLaunchView prelaunchView) {
    if (prelaunchView == null) {
      throw new IllegalArgumentException("Cannot be null");
    }
    this.prelaunchView = prelaunchView;
    this.prelaunchView.setCommandButtonListener(this);
    this.prelaunchView.makeVisible();
  }

  /**
   * Constructs Dungeon controller.
   *
   * @throws IllegalArgumentException if {@code model} or {@code view} or {@code prelaunchView} is
   *                                  {@code null}.
   */
  public DungeonControllerImpl(Dungeon model, DungeonView view, PreLaunchView prelaunchView,
                               int rows, int cols) {
    if (prelaunchView == null || model == null || view == null) {
      throw new IllegalArgumentException("Cannot be null");
    }
    this.model = model;
    this.view = view;
    this.prelaunchView = prelaunchView;
    this.rows = rows;
    this.cols = cols;
  }

  @Override
  public void playGame() {
    prelaunchView.close();
    view.makeVisible();
    view.addClickListener(this);
    view.setKeyBoardListeners(this);
  }

  @Override
  public boolean handleCellClick(int row, int col) {
    boolean result = false;
    try {
      Direction direction = null;
      for (Map.Entry locationWithDirection : model.getPlayerLocation().getNeighborLocations()
              .entrySet()) {
        if (((Location) locationWithDirection.getValue()).getCoordinates().getX() == row
                && ((Location) locationWithDirection.getValue()).getCoordinates().getY() == col) {
          direction = (Direction) locationWithDirection.getKey();
        }

      }
      result = model.movePlayer(direction);
      view.setLocationAsVisited(row, col);
      view.refresh();
    } catch (IllegalStateException | IllegalArgumentException ignoredException) {
      //exception ignored
    }
    return result;
  }

  @Override
  public void restartGame() {
    Dungeon restart = new DungeonImpl((DungeonImpl) freshModel);
    model = new DungeonImpl((DungeonImpl) freshModel);
    freshModel = new DungeonImpl((DungeonImpl) restart);
    this.view.close();
    this.view = new DungeonViewImpl(model, this, rows, cols);
    playGame();
  }

  @Override
  public void newGame() {
    view.close();
    prelaunchView.makeVisible();
  }

  @Override
  public boolean handleKeyMove(Direction direction) {
    boolean result = false;
    try {
      result = model.movePlayer(direction);
      view.refresh();
    } catch (IllegalStateException | IllegalArgumentException ignoredException) {
      //exception ignored
    }
    return result;
  }

  @Override
  public void handlePickAll() {
    model.pickArrows();
    model.collectAllTreasures();
    view.refresh();
  }

  @Override
  public void handlePickTreasures() {
    model.collectAllTreasures();
    view.refresh();
  }

  @Override
  public void handlePickArrows() {
    model.pickArrows();
    view.refresh();
  }

  @Override
  public void handlePickRubies() {
    model.collectTreasure(List.of(Treasure.RUBY));
    view.refresh();
  }

  @Override
  public void handlePickEmeralds() {
    model.collectTreasure(List.of(Treasure.SAPPHIRE));
    view.refresh();
  }

  @Override
  public void handlePickDiamonds() {
    model.collectTreasure(List.of(Treasure.DIAMOND));
    view.refresh();
  }

  @Override
  public boolean handleShootArrow(Direction direction, int distance) {
    boolean result = false;
    try {
      result = model.shootArrow(distance, direction);
      view.refresh();
    } catch (IllegalStateException | IllegalArgumentException ignoredException) {
      //exception ignored
    }
    return result;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    int rows = prelaunchView.getRows();
    int cols = prelaunchView.getCols();
    int interconnectivity = prelaunchView.getInterconnectivity();
    boolean isWrapping = prelaunchView.getIsWrapping();
    int numberOfMonsters = prelaunchView.getNumberOfMonsters();
    String player = prelaunchView.getNameOfPlayer();
    double percentageOfTreasuresAndArrowsL = prelaunchView.getPercentageOfTreasuresAndArrowsL();
    RandomFactory randomFactory = new RandomFactory();
    RandomGenerator rand = randomFactory.getRandomGenerator(true);
    try {
      model = new DungeonImpl(rows, cols, interconnectivity, isWrapping,
              percentageOfTreasuresAndArrowsL, player, numberOfMonsters, rand);
      freshModel = new DungeonImpl((DungeonImpl) model);
      this.view = new DungeonViewImpl(model, this, rows, cols);
      this.rows = rows;
      this.cols = cols;
      playGame();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }
}
