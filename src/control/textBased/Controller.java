package control.textBased;

import model.dungeon.Dungeon;

/**
 * The Controller controls the execution of the program by taking inputs from users and calling
 * the proper methods from {@link Dungeon}. Based on different action it calls the respective
 * {@link DungeonCommand} and executes the action.
 */
public interface Controller {

  /**
   * Plays the game until the game is over.
   *
   * @param model the dungeon model.
   */
  void playGame(Dungeon model);
}
