package control.gui;

import model.location.Direction;

/**
 * The DungeonGUIController class represents the controller of the dungeon game. It interacts
 * with the model and the graphical view of the game. It tells view when to update it's state and
 * tell model to mutate the state when an action is performed.
 */
public interface DungeonGUIController {

  /**
   * Execute a single game of Dungeon given a Dungeon Model. When the game is over,
   * the playGame method ends.
   */
  void playGame();

  /**
   * Handle an action in a single cell of the board, such as to make a move.
   *
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   * @return the move result when clicked on a cell.
   */
  boolean handleCellClick(int row, int col);

  /**
   * Restarts the dungeon game with the same dungeon, and it's settings.
   */
  void restartGame();

  /**
   * Quits the current game and starts a fresh game.
   */
  void newGame();

  /**
   * Handles the up, down, left and right key event actions on model for moving the player.
   *
   * @param direction the direction
   * @return the move result.
   */
  boolean handleKeyMove(Direction direction);

  /**
   * Makes the model pick all the treasures and arrows.
   */
  void handlePickAll();

  /**
   * Makes the model pick all the treasures.
   */
  void handlePickTreasures();

  /**
   * Makes the model pick all the arrows.
   */
  void handlePickArrows();

  /**
   * Makes the model pick all the rubies.
   */
  void handlePickRubies();

  /**
   * Makes the model pick all the emeralds.
   */
  void handlePickEmeralds();

  /**
   * Makes the model pick all the diamonds.
   */
  void handlePickDiamonds();

  /**
   * Makes the model shoot the arrow in the given direction and distance.
   *
   * @param direction the direction of shot
   * @param distance  the distance to be shot.
   */
  boolean handleShootArrow(Direction direction, int distance);
}
