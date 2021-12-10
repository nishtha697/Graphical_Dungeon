package control.textbased;

import java.io.IOException;
import java.util.Scanner;

import model.dungeon.Dungeon;
import model.location.Direction;

/**
 * This class represents the shoot command and makes the player shoot an arrow in the given
 * direction for the specified number of caves entered by user. This class calls the appropriate
 * model method for the action.
 */
public class Shoot implements DungeonCommand {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructs the shoot command.
   *
   * @param scan the readable
   * @param out  the appendable
   */
  public Shoot(Scanner scan, Appendable out) {
    this.out = out;
    this.scan = scan;
  }

  @Override
  public void execute(Dungeon dungeon) {
    try {
      out.append("No. of caves (1-n)?");
      try {
        int distance = Integer.parseInt(scan.next());
        out.append("Where to?");
        String arrowDirection = scan.next();
        boolean success;
        try {
          success = dungeon.shootArrow(distance, Direction.valueOf(arrowDirection
                  .toUpperCase()));
          if (success) {
            out.append("You hear a great howl in the distance\n\n");
          } else {
            out.append("You shoot an arrow into the darkness\n\n");
          }
          if (dungeon.getPlayer().getNumberOfArrows() == 0) {
            out.append("You are out of arrows, explore to find more\n\n");
          }
        } catch (IllegalArgumentException iae) {
          out.append("No door in that direction. You shoot an arrow into the same cave.\n\n");
        } catch (IllegalStateException ise) {
          out.append("You are out of arrows, explore to find more\n\n");
        }
      } catch (NumberFormatException nfe) {
        out.append("\nPlease enter a valid input\n\n");
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }
}
