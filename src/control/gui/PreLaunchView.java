package control.gui;

import java.awt.event.ActionListener;

/**
 * This PreLaunchView class represents the pre launch view of the dungeon game. It provides a
 * GUI for the dungeon model creation and takes input from user.
 */
public interface PreLaunchView {

  /**
   * Set up the controller to handle click events in this view.
   *
   * @param actionEvent the actionEvent
   */
  void setCommandButtonListener(ActionListener actionEvent);

  /**
   * Get the number of rows from the user input.
   * @return the number of rows.
   */
  int getRows();

  /**
   * Get the number of columns from the user input.
   * @return the number of columns.
   */
  int getCols();

  /**
   * Get the interconnectivity from the user input.
   * @return the interconnectivity.
   */
  int getInterconnectivity();

  /**
   * Get if the dungeon is wrapping or not.
   * @return isWrapping.
   */
  boolean getIsWrapping();

  /**
   * Get the name of the player from the user input.
   * @return the name of the player.
   */
  String getNameOfPlayer();

  /**
   * Get the number of monsters from the user input.
   * @return the number of monsters.
   */
  int getNumberOfMonsters();

  /**
   * Get the percentage of arrows and treasures from the user input.
   * @return the percentage.
   */
  double getPercentageOfTreasuresAndArrowsL();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * Close the view.
   */
  void close();
}
