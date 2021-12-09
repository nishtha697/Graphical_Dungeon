import java.util.List;

import model.dungeon.Dungeon;
import model.location.Cave;
import model.location.Direction;
import model.location.Location;
import model.location.Treasure;
import model.player.Player;
import model.player.PlayerImpl;

/**
 * Mock dungeon test.
 */
public class MockDungeon implements Dungeon {
  @Override
  public Player getPlayer() {
    return new PlayerImpl("Player", new Cave(0, 0, 0));
  }

  @Override
  public Location getPlayerLocation() {
    return new Cave(0, 0, 0);
  }

  @Override
  public Location getStartingCave() {
    return new Cave(0, 0, 0);
  }

  @Override
  public Location getDestinationCave() {
    return new Cave(0, 0, 0);
  }

  @Override
  public boolean movePlayer(Direction direction) {
    return false;
  }

  @Override
  public void collectAllTreasures() {
    getPlayer().getCollectedTreasures();
  }

  @Override
  public void collectTreasure(List<Treasure> treasures) {
    getPlayer().getCollectedTreasures();
  }

  @Override
  public boolean isDestinationReached() {
    return false;
  }

  @Override
  public boolean shootArrow(int distance, Direction direction) {
    return false;
  }

  @Override
  public void pickArrows() {
    getPlayer().getNumberOfArrows();
  }
}
