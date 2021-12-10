package control.textbased;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.dungeon.Dungeon;
import model.location.Treasure;

import static control.textbased.GetStringsHelper.getString;

/**
 * This class represents the pick command and makes the player pick treasures or arrows from the
 * current location based oj the prompt input by the user. This class calls the appropriate model
 * method for the action.
 */
public class Pick implements DungeonCommand {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructs the pick command.
   *
   * @param scan the readable.
   * @param out  the appendable.
   */
  public Pick(Scanner scan, Appendable out) {
    this.out = out;
    this.scan = scan;
  }

  @Override
  public void execute(Dungeon dungeon) {
    Map<String, String> getTreasuresAndArrows = getString(dungeon.getPlayer().getLocation());
    String ruby = getTreasuresAndArrows.get("ruby");
    String diamond = getTreasuresAndArrows.get("diamond");
    String sapphire = getTreasuresAndArrows.get("sapphire");
    String arrow = getTreasuresAndArrows.get("arrow");

    try {
      out.append("What?");
      String pick = scan.next();
      out.append("\n");
      switch (pick.toLowerCase()) {
        case "arrow":
          out.append("You pick up" + arrow + "\n");
          dungeon.pickArrows();
          break;
        case "ruby":
          dungeon.collectTreasure(List.of(Treasure.RUBY));
          out.append("You pick up" + (ruby.isEmpty() ? " 0 ruby" : ruby) + "\n");
          break;
        case "diamond":
          dungeon.collectTreasure(List.of(Treasure.DIAMOND));
          out.append("You pick up" + (diamond.isEmpty() ? " 0 diamond" : diamond) + "\n");
          break;
        case "sapphire":
          dungeon.collectTreasure(List.of(Treasure.SAPPHIRE));
          out.append("You pick up" + (sapphire.isEmpty() ? " 0 sapphire" : sapphire) + "\n");
          break;
        case "treasure":
          dungeon.collectAllTreasures();
          out.append("You pick up" + ruby + diamond + sapphire + "\n");
          break;
        default:
          out.append("Invalid input!\n");
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }
}
