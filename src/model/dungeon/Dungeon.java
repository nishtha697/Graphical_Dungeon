package model.dungeon;

import java.util.List;

import model.location.Direction;
import model.location.Location;
import model.location.Treasure;
import model.player.Player;

/**
 * The Dungeon represents a world of game that consists of a dungeon, a network of tunnels
 * and caves that are interconnected so that player can explore the entire world by traveling from
 * cave to cave through the tunnels that connect them.
 */
public interface Dungeon extends ReadOnlyDungeon {

  /**
   * Move the {@link Player} one step in the given {@link Direction}.
   *
   * @param direction the direction to be moved in to.
   * @return {@code true} if the move was successful. {@code false} if the monster at the new
   *         location eats the player so the player is dead.
   * @throws IllegalArgumentException <ul><li>if {@code direction} is {@code null}.</li>
   *                                  <li>if {@code direction} is not a valid move form current
   *                                  model.location.</li></ul>
   */
  boolean movePlayer(Direction direction);

  /**
   * Makes the {@link Player} collect all the {@link Treasure}(s) available at the current
   * {@link Location}.
   */
  void collectAllTreasures();

  /**
   * Makes the {@link Player} collect only the given type of {@link Treasure}(s) available at the
   * current {@link Location}.
   *
   * @throws IllegalArgumentException if {@code treasures} is {@code null}.
   */
  void collectTreasure(List<Treasure> treasures);

  /**
   * Makes the {@link Player} shoot at a given distance of caves in the given direction.
   *
   * @param distance  the number of caves.
   * @param direction the direction to shoot in.
   * @return {@code true} if the arrow hits the {@link model.monster.Otyugh} otherwise {code false}.
   * @throws IllegalArgumentException if {@code direction} is invalid.
   */
  boolean shootArrow(int distance, Direction direction);

  /**
   * Makes the {@link Player} pick all the arrows available at the current location.
   */
  void pickArrows();
}
