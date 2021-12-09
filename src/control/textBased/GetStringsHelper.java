package control.textBased;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.location.Location;
import model.location.Treasure;

/**
 * Get string helper for controller. Helps computing treasure and arrow strings.
 */
public class GetStringsHelper {

  static Map<String, String> getString(Location location) {
    Map<String, String> treasuresAndArrows = new HashMap<>();
    List<Treasure> treasures = location.getTreasures();
    int rubies = treasures.stream().filter(treasure -> treasure.equals(Treasure.RUBY))
            .collect(Collectors.toList()).size();
    int diamonds = treasures.stream().filter(treasure -> treasure.equals(Treasure.DIAMOND))
            .collect(Collectors.toList()).size();
    int sapphires = treasures.stream().filter(treasure -> treasure.equals(Treasure.SAPPHIRE))
            .collect(Collectors.toList()).size();
    int arrows = location.getArrows().size();

    String ruby = rubies > 0 ? (rubies == 1 ? " " + rubies + " ruby" : " " + rubies
            + " rubies") : "";
    String diamond = diamonds > 0 ? (diamonds == 1 ? " " + diamonds + " diamond" : " "
            + diamonds + " diamonds") : "";
    String sapphire = sapphires > 0 ? (sapphires == 1 ? " " + sapphires + " sapphire" : " "
            + sapphires + " sapphires") : "";
    String arrow = arrows == 1 ? " an arrow" : " " + arrows + " arrows";

    treasuresAndArrows.put("ruby", ruby);
    treasuresAndArrows.put("diamond", diamond);
    treasuresAndArrows.put("sapphire", sapphire);
    treasuresAndArrows.put("arrow", arrow);

    return treasuresAndArrows;
  }
}
