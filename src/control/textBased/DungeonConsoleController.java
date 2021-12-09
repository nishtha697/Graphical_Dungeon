package control.textBased;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.dungeon.Dungeon;
import model.location.Treasure;
import model.player.Player;

import static control.textBased.GetStringsHelper.getString;

/**
 * The controller implements the {@link Controller} controls the execution of the program by taking
 * inputs from users and calling the proper methods from {@link Dungeon}. Based on different action
 * it calls the respective {@link DungeonCommand} and executes the action.
 */
public class DungeonConsoleController implements Controller {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructs Dungeon console controller.
   *
   * @param in  the readable
   * @param out the appendable
   * @throws IllegalArgumentException if {@code in} or {@code out} is {@code null}.
   */
  public DungeonConsoleController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  @Override
  public void playGame(Dungeon dungeon) {
    if (dungeon == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    Map<String, DungeonCommand> knownCommands;
    knownCommands = new HashMap<>();
    knownCommands.put("M", new Move(scan, out));
    knownCommands.put("P", new Pick(scan, out));
    knownCommands.put("S", new Shoot(scan, out));

    Player player = dungeon.getPlayer();

    try {
      while (!dungeon.isDestinationReached() && !player.isDead()) {

        if (player.getLocation().getSmell() > 1) {
          out.append("You smell something terribly pungent here.\n");
        } else if (player.getLocation().getSmell() == 1) {
          out.append("You smell something lightly pungent here.\n");
        }
        if (player.getLocation().isTunnel()) {
          out.append("You are in a tunnel\n");
        } else {
          out.append("You are in a cave\n");
        }
        if (player.getLocation().getMonster() != null
                && player.getLocation().getMonster().getHealthPercentage() == 0) {
          out.append("You find a dead Otyugh here.\n");
        }
        if (player.getLocation().getMonster() != null
                && player.getLocation().getMonster().getHealthPercentage() == 50) {
          out.append("You find an injured Otyugh here and escape him. Hurry up and leave.\n");
        }

        Map<String, String> getTreasuresAndArrows = getString(dungeon.getPlayer().getLocation());
        String ruby = getTreasuresAndArrows.get("ruby");
        String diamond = getTreasuresAndArrows.get("diamond");
        String sapphire = getTreasuresAndArrows.get("sapphire");
        String arrow = getTreasuresAndArrows.get("arrow");

        if (dungeon.getPlayerLocation().getTreasures().size() > 0) {
          out.append("You find" + ruby + diamond + sapphire + " here.\n");
        }

        if (player.getLocation().getArrows().size() > 0) {
          out.append("You find" + arrow + " here.\n");
        }

        out.append("Doors lead to " + player.getLocation().getPossibleMoves() + "\n");
        out.append("Move, Pickup, or Shoot (M-P-S)?");

        if (!scan.hasNext()) {
          break;
        }
        String action = scan.next();
        DungeonCommand command = knownCommands.getOrDefault(action.toUpperCase(), null);
        if (command == null) {
          out.append("\nPlease enter a valid input.\n");
        } else {
          command.execute(dungeon);
        }
      }

      if (dungeon.isDestinationReached() && !player.isDead()) {
        out.append("Congratulations!! You won.\nDestination Reached.\n");
        out.append("Total treasure collected = "
                + "Rubies: " + player.getCollectedTreasures().get(Treasure.RUBY)
                + " Diamonds: " + player.getCollectedTreasures().get(Treasure.DIAMOND)
                + " Sapphires: " + player.getCollectedTreasures().get(Treasure.SAPPHIRE));
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }
}
