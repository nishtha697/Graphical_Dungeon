package control.textbased;

import java.io.IOException;
import java.util.Scanner;

import model.dungeon.Dungeon;
import model.location.Direction;

/**
 * This class represents the move command and makes the player move from one position to next
 * based on the direction entered by user. This class calls the appropriate model method for the
 * action.
 */
public class Move implements DungeonCommand {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructs the move command.
   *
   * @param scan the readable
   * @param out  the appendable
   */
  public Move(Scanner scan, Appendable out) {
    this.out = out;
    this.scan = scan;
  }

  @Override
  public void execute(Dungeon dungeon) {
    try {
      out.append("Where to?");
      String direction = scan.next();
      out.append("\n");
      boolean moveSuccess;
      try {
        moveSuccess = dungeon.movePlayer(Direction.valueOf(direction.toUpperCase()));
        if (!moveSuccess) {
          out.append("Chomp, chomp, chomp, you are eaten by an Otyugh!\nBetter luck next "
                  + "time\n");
        }
      } catch (IllegalArgumentException iae) {
        out.append("Invalid direction!\n");
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }
}
