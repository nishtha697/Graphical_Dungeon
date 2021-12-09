package model.dungeon;

import model.location.Location;
import model.player.Player;

/**
 * The ReadOnlyDungeon represents a world of game that consists of a dungeon, a network of tunnels
 * and caves that are interconnected so that player can explore the entire world by traveling from
 * cave to cave through the tunnels that connect them.
 */
public interface ReadOnlyDungeon {

  /**
   * Returns the {@link Player} that travels the model.dungeon through tunnels and caves.
   *
   * @return the model.player.
   */
  Player getPlayer();

  /**
   * Returns the current model.location of the model.player.
   *
   * @return the current model.location of the model.player.
   */
  Location getPlayerLocation();

  /**
   * Returns the starting cave that is selected randomly as the source of the travel through the
   * model.dungeon.
   *
   * @return the start cave.
   */
  Location getStartingCave();

  /**
   * Returns the ending cave that is selected randomly as the destination of the travel through the
   * model.dungeon.
   *
   * @return the destination cave.
   */
  Location getDestinationCave();

  /**
   * Returns if the destination cave is reached or not.
   *
   * @return {@code true} if current model.location is destination cave otherwise {@code false}.
   */
  boolean isDestinationReached();
}
