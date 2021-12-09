package model.location.coordinate;

/**
 * The Coordinate represents the coordinates of the {@link model.location.Location} in the
 * {@link model.dungeon.Dungeon} grid. x represents the x-coordinate and y represents the y
 * coordinate.
 */
public interface Coordinate {

  /**
   * Returns the x coordinate of the model.location in the 2D model.dungeon grid.
   *
   * @return the x coordinate. This will never be negative.
   */
  int getX();

  /**
   * Returns the y coordinate of the model.location in the 2D model.dungeon grid.
   *
   * @return the y coordinate. This will never be negative.
   */
  int getY();
}
