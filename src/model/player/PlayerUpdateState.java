package model.player;

import java.util.List;

import model.location.LocationUpdateState;
import model.location.Treasure;

/**
 * The PlayerUpdateState extends {@link Player} and represents a player that explores the
 * entire world of dungeon. This class is used of internal mutation of the player and is not client
 * facing.
 */
public interface PlayerUpdateState extends Player {

  /**
   * Collects the given type pf {@link Treasure}(s) from the current location of the player if
   * they exist.
   *
   * @param treasures the list of treasures to be collected.
   */
  void collectTreasures(List<Treasure> treasures);

  /**
   * Moves the player to the given location.
   *
   * @param newLocation the new location the player need to be moved to.
   */
  void move(LocationUpdateState newLocation);

  /**
   * Picks all the arrows from the current location if they exist.
   */
  void pickArrows();

  /**
   * Kills the player in the cases when the player is eaten by a monster.
   */
  void killPlayer();

  /**
   * Shoots an arrow and reduces the number of arrows the player has.
   */
  void shootArrow();
}
