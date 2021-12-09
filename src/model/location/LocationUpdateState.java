package model.location;

import java.util.List;
import java.util.Map;

import model.monster.Monster;
import model.weapon.Weapon;

/**
 * The location.LocationUpdateState extends {@link Location} and represents a location in the
 * dungeon grid a player can traverse through. This has functionalities to mutate the
 * state of the location. This class is used internally and is not client facing.
 */
public interface LocationUpdateState extends Location {

  /**
   * Sets the possible/valid moves for the model.location.
   *
   * @param validMoves the valid moves.
   */
  void setValidMoves(Map<Direction, LocationUpdateState> validMoves);

  /**
   * Adds {@link Treasure}(s) to the location.
   *
   * @param treasures the treasures to be added.
   */
  void addTreasures(List<Treasure> treasures);

  /**
   * Removes the given {@link Treasure}(s) from the location if they exist.
   *
   * @param treasures the list of treasures to be removed.
   */
  void removeTreasures(List<Treasure> treasures);

  /**
   * Adds the smell of the {@link Monster}s at the location.
   *
   * @param smell the smell.
   */
  void addSmell(int smell);

  /**
   * Reduces the smell of the {@link Monster}s at the location by given {@code smell}.
   * Smell cannot be reduced below 0.
   *
   * @param smell the smell.
   */
  void  reduceSmell(int smell);

  /**
   * Adds {@link Weapon}(s) to the location. {@code arrows} cannot be {@code null} or have
   * {@code null} elements.
   *
   * @param arrows the weapons to be added.
   * @throws IllegalArgumentException <ul><li>if {@code arrows} is {@code null}.</li>
   *                                  <li>if any element of {@code arrows} is {@code null}.</ul>
   */
  void addArrows(List<Weapon> arrows);

  /**
   * Adds the monster {@link model.monster.Otyugh} to the location.
   *
   * @param otyugh the model.monster.
   */
  void addMonster(Monster otyugh);

  /**
   * Returns the list of neighbor locations.
   *
   * @return the neighbor locations.
   */
  List<LocationUpdateState> getNeighbors();

  /**
   * Removes all the arrows from the location if they exist.
   */
  void removeArrows();

}
