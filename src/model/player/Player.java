package model.player;

import java.util.Map;

import model.location.Location;
import model.location.Treasure;

/**
 * The Player represents a player that explores the entire world of dungeon by traveling from
 * cave to cave through the tunnels that connect them while collecting treasures on the way.
 */
public interface Player {

  /**
   * Returns the name of the player.
   *
   * @return the name.
   */
  String getName();

  /**
   * Returns the current location the player is at.
   *
   * @return the current location of the player.
   */
  Location getLocation();

  /**
   * Returns a {@link Map} of all the {@link Treasure}(s) collected by the player with the total
   * number of each {@link Treasure} collected.
   *
   * @return the treasures collected by the player.
   */
  Map<Treasure, Integer> getCollectedTreasures();

  /**
   * Returns the number of arrows the player has.
   *
   * @return the number of arrows.
   */
  int getNumberOfArrows();

  /**
   * Returns if the player is dead or not.
   *
   * @return {@code true} if player is dead otherwise {@code false}.
   */
  boolean isDead();
}
