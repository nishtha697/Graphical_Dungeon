package model.location;

import java.util.Comparator;

/**
 * Direction comparator to order them in the right order for image display.
 */
public class DirectionComparator implements Comparator<Direction> {

  @Override
  public int compare(Direction d1, Direction d2) {
    return Integer.compare(getAssignedValue(d1), getAssignedValue(d2));
  }

  int getAssignedValue(Direction direction) {
    switch (direction) {
      case N:
        return 0;
      case S:
        return 1;
      case E:
        return 2;
      case W:
        return 3;
      default:
        return Integer.MAX_VALUE;
    }
  }
}
