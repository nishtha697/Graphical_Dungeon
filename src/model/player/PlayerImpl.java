package model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.location.LocationUpdateState;
import model.location.Treasure;
import model.weapon.Weapon;

/**
 * The PlayerImpl implements {@link PlayerUpdateState} and represents a player that explores
 * the entire world of dungeon by traveling from cave to cave through the tunnels that connect them
 * while collecting treasures on the way. Player starts with a bow and three crooked arrows.
 */
public class PlayerImpl implements PlayerUpdateState {

  private final String name;
  private LocationUpdateState currentLocation;
  private final Map<Treasure, Integer> treasures;
  private final List<Weapon> arrows;
  private boolean isDead;


  /**
   * Constructs a model.player.
   *
   * @param name            the name of the player.
   * @param currentLocation the current location of teh player.
   */
  public PlayerImpl(String name, LocationUpdateState currentLocation) {
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    if (currentLocation == null) {
      throw new IllegalArgumentException("Current model.location cannot be null");
    }
    this.name = name;
    this.currentLocation = currentLocation;
    this.treasures = new HashMap<>();
    this.treasures.put(Treasure.RUBY, 0);
    this.treasures.put(Treasure.DIAMOND, 0);
    this.treasures.put(Treasure.SAPPHIRE, 0);
    this.arrows = new ArrayList<>(List.of(Weapon.ARROW, Weapon.ARROW, Weapon.ARROW));
    this.isDead = false;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Map<Treasure, Integer> getCollectedTreasures() {
    return new HashMap<>(this.treasures);
  }

  @Override
  public int getNumberOfArrows() {
    return this.arrows.size();
  }

  @Override
  public boolean isDead() {
    return this.isDead;
  }

  @Override
  public LocationUpdateState getLocation() {
    return this.currentLocation;
  }

  @Override
  public void collectTreasures(List<Treasure> givenTreasures) {
    if (givenTreasures == null) {
      throw new IllegalArgumentException("List of treasures cannot be null.");
    }
    givenTreasures.forEach(treasure -> {
      if (treasure == null) {
        throw new IllegalArgumentException("List of treasures cannot have null elements.");
      }
    });
    List<Treasure> treasureList = new ArrayList<>(givenTreasures);
    List<Treasure> filteredTreasures = this.currentLocation.getTreasures().stream()
            .filter(treasureList::contains).collect(Collectors.toList());

    for (Treasure treasure : filteredTreasures) {
      switch (treasure) {
        case RUBY:
          this.treasures.put(Treasure.RUBY, this.treasures.get(Treasure.RUBY) + 1);
          break;
        case DIAMOND:
          this.treasures.put(Treasure.DIAMOND, this.treasures.get(Treasure.DIAMOND) + 1);
          break;
        case SAPPHIRE:
          this.treasures.put(Treasure.SAPPHIRE, this.treasures.get(Treasure.SAPPHIRE) + 1);
          break;
        default:
          throw new IllegalArgumentException("Invalid treasure.");
      }
    }
    this.currentLocation.removeTreasures(filteredTreasures);
  }

  @Override
  public void move(LocationUpdateState newLocation) throws IllegalArgumentException {
    if (newLocation == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    this.currentLocation = newLocation;
  }

  @Override
  public void pickArrows() {
    this.arrows.addAll(this.currentLocation.getArrows());
    this.currentLocation.removeArrows();
  }

  @Override
  public void killPlayer() {
    this.isDead = true;
  }

  @Override
  public void shootArrow() {
    if (this.getNumberOfArrows() <= 0) {
      throw new IllegalStateException("Can't shoot! You are out of arrows.");
    }
    this.arrows.remove(0);
  }
}
