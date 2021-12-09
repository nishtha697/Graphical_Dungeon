package model.location;

import java.util.List;
import java.util.Map;

import model.location.coordinate.Coordinate;
import model.monster.Monster;
import model.weapon.Weapon;

/**
 * The Location represents a location or cave in the {@link model.dungeon.Dungeon}. Each location
 * in the grid represents a location in the dungeon where a player can explore and can be connected
 * to at most four (4) other locations: one to the north, one to the east, one to the south, and one
 * to the west.
 */
public interface Location {

  /**
   * Gets the id of the location.
   *
   * @return the id.
   */
  int getId();

  /**
   * Returns the {@link Coordinate} containing x and y coordinates of the location in the 2D
   * model.dungeon grid.
   *
   * @return the coordinates.
   */
  Coordinate getCoordinates();

  /**
   * Returns the {@link List} of {@link Treasure}(s) located at the location.
   *
   * @return the list of treasures.
   */
  List<Treasure> getTreasures();

  /**
   * Returns the {@link List} of possible {@link Direction}(s) the player ca go from the location.
   *
   * @return the list of possible directions or moves.
   */
  List<Direction> getPossibleMoves();

  /**
   * Returns if the location is a tunnel. A location with exactly two paths is a tunnel.
   *
   * @return {@code true} if tunnel otherwise {@code false}.
   */
  boolean isTunnel();

  /**
   * Returns the smell of the {@link model.monster.Otyugh} at the location. This will be 0 if
   * the cave has no smell. 1 if the cave has a lightly pungent smell and more than 1 for a strong
   * pungent smell.
   *
   * @return the smell.
   */
  int getSmell();

  /**
   * Returns the {@link List} of {@link Weapon}(s) located at the location. Returns an empty list
   * if arrows are present at the location.
   */
  List<Weapon> getArrows();

  /**
   * Returns the monster present at the location. Returns {@code null} if no
   * {@link model.monster.Otyugh} exists at the location.
   *
   * @return the monster.
   */
  Monster getMonster();

  /**
   * Returns the list of neighbor locations.
   *
   * @return the neighbor locations.
   */
  Map<Direction, LocationUpdateState> getNeighborLocations();
}
