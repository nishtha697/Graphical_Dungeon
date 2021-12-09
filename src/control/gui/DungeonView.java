package control.gui;

/**
 * A graphical view for Dungeon, display the dungeon board and provide visual interface
 * for users for explore the dungeon collecting treasures along the way.
 */
public interface DungeonView {

  /**
   * Set up the controller to handle click events in this view.
   *
   * @param listener the controller
   */
  void addClickListener(DungeonGUIController listener);

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * Sets the location with the given coordinated as visited true.
   *
   * @param row the row
   * @param col the col
   */
  void setLocationAsVisited(int row, int col);

  /**
   * Set up the controller to handle key events in this view.
   *
   * @param controller the controller
   */
  void setKeyBoardListeners(DungeonGUIController controller);

  /**
   * Sets the visibility of this view as false.
   */
  void close();
}
