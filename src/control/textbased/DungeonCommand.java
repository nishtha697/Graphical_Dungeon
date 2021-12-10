package control.textbased;

import model.dungeon.Dungeon;

/**
 * This interface represents a command from the user and executes the action of a
 * particular implementation based on that.
 */
public interface DungeonCommand {

  /**
   * Exceutes the command.
   *
   * @param dungeon the dungeon model.
   */
  void execute(Dungeon dungeon);
}
