import java.io.IOException;
import java.util.List;

import model.dungeon.Dungeon;
import model.location.Direction;
import model.location.Location;
import model.location.Treasure;
import model.player.Player;

/**
 * Mock dungeon model for testing th GUI controller.
 */
class MockModel implements Dungeon {

  Appendable out;

  /**
   * Constructs Mock model.
   * @param out appendable.
   */
  MockModel(Appendable out) {
    this.out = out;
  }

  @Override
  public boolean movePlayer(Direction direction) {
    try {
      out.append("movePlayer called\n");
    } catch (IOException e) {
        //do nothing
    }
    return true;
  }

  @Override
  public void collectAllTreasures() {
    try {
      out.append("collectAllTreasures called\n");
    } catch (IOException e) {
        //do nothing
    }

  }

  @Override
  public void collectTreasure(List<Treasure> treasures) {
    try {
      out.append("collectTreasure called\n");
    } catch (IOException e) {
       //do nothing

    }

  }

  @Override
  public boolean shootArrow(int distance, Direction direction) {
    try {
      out.append("shootArrow called " + distance + " " + direction + "\n");
    } catch (IOException e) {
        //do nothing

    }
    return false;
  }

  @Override
  public void pickArrows() {
    try {
      out.append("pickArrows called\n");
    } catch (IOException e) {
       //do nothing

    }

  }

  @Override
  public Player getPlayer() {
    try {
      out.append("getPlayer called\n");
    } catch (IOException e) {
       //do nothing

    }
    return null;
  }

  @Override
  public Location getPlayerLocation() {
    try {
      out.append("getPlayerLocation called\n");
    } catch (IOException e) {
        //do nothing

    }
    return null;
  }

  @Override
  public Location getStartingCave() {
    try {
      out.append("getStartingCave called\n");
    } catch (IOException e) {
        //do nothing

    }
    return null;
  }

  @Override
  public Location getDestinationCave() {
    try {
      out.append("getDestinationCave called\n");
    } catch (IOException e) {
        //do nothing

    }
    return null;
  }

  @Override
  public boolean isDestinationReached() {
    try {
      out.append("isDestinationReached called\n");
    } catch (IOException e) {
        //do nothing

    }

    return false;
  }
}
