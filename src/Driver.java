import java.io.InputStreamReader;

import control.gui.DungeonControllerImpl;
import control.gui.PreLaunchView;
import control.gui.PreLaunchViewImpl;
import control.textbased.DungeonConsoleController;
import model.dungeon.Dungeon;
import model.dungeon.DungeonImpl;
import model.random.RandomFactory;
import model.random.RandomGenerator;

/**
 * The driver class that runs the {@link Dungeon} depicting a user.
 */
public class Driver {

  /**
   * Main method.
   *
   * @param args the arguments.
   */
  public static void main(String[] args) {

    if (args.length == 7) {
      System.out.println("Welcome to the World of Dungeon");
      int rows;
      rows = Integer.parseInt(args[0]);
      System.out.println("Number of rows: " + rows);

      int columns;
      columns = Integer.parseInt(args[1]);
      System.out.println("Number of columns: " + columns);

      int interconnectivity;
      interconnectivity = Integer.parseInt(args[2]);
      System.out.println("Interconnectivity: " + interconnectivity);

      boolean isWrapping;
      isWrapping = args[3].equalsIgnoreCase("y");
      System.out.println("Is model.dungeon wrapping: " + isWrapping);

      double percentageOfTreasuresAndArrows;
      percentageOfTreasuresAndArrows = Integer.parseInt(args[4]);
      System.out.println("Percentage of caves to have treasures: "
              + percentageOfTreasuresAndArrows);
      System.out.println("Percentage of locations to have arrows:: "
              + percentageOfTreasuresAndArrows);

      String playerName = args[5];
      System.out.println("Player name: " + playerName);

      int noOfMonsters = Integer.parseInt(args[6]);
      System.out.println("Number of monsters: " + noOfMonsters);
      System.out.println();

      RandomFactory randomFactory = new RandomFactory();
      RandomGenerator rand = randomFactory.getRandomGenerator(true);

      Dungeon dungeon = new DungeonImpl(10, 10, 4, true,
              57, "Captain", 2, rand);

      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      new DungeonConsoleController(input, output).playGame(dungeon);
    } else {
      PreLaunchView preLaunchView = new PreLaunchViewImpl();
      new DungeonControllerImpl(preLaunchView);
    }
  }
}
