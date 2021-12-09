package model.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.location.coordinate.Coordinate;
import model.location.coordinate.CoordinateImpl;
import model.monster.Monster;
import model.monster.Otyugh;
import model.weapon.Weapon;

/**
 * The Cave implements {@link LocationUpdateState} and represents a location in the
 * {@link model.dungeon.Dungeon}. Each cave has either 1, 3, or 4 possible exits. A tunnel is a
 * special type of cave that has exactly 2 exits.
 */
public class Cave implements LocationUpdateState {

  private final int id;
  private final Coordinate coordinates;
  private Map<Direction, LocationUpdateState> validMovesAndLocations;
  private List<Treasure> treasures;
  private int smell;
  private List<Weapon> weapons;
  private Monster monster;

  /**
   * Constructs a cave.
   *
   * @param id  the id of the cave.
   * @param row the row (x) coordinate in the model.dungeon grid.
   * @param column the column (y) coordinate in the model.dungeon grid.
   * @throws IllegalArgumentException <ul><li>if {@code row} is negative</li>
   *                                  <li>if {@code column} is negative</li></ul>
   */
  public Cave(int id, int row, int column) {
    this.id = id;
    this.coordinates = new CoordinateImpl(row, column);
    this.validMovesAndLocations = new HashMap<>();
    this.treasures = new ArrayList<>();
    this.smell = 0;
    this.weapons = new ArrayList<>();
    this.monster = null;
  }

  public Cave(Cave cave) {
    this.id = cave.id;
    this.coordinates = cave.coordinates;
    this.validMovesAndLocations = new HashMap<>();
    this.treasures = new ArrayList<>(cave.treasures);
    this.smell = cave.smell;
    this.weapons = new ArrayList<>(cave.weapons);
    this.monster = cave.monster == null ? null : new Otyugh();
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public Coordinate getCoordinates() {
    return this.coordinates;
  }

  @Override
  public List<Treasure> getTreasures() {
    return new ArrayList<>(this.treasures);
  }

  @Override
  public List<Direction> getPossibleMoves() {
    List<Direction> possibleDirections = new ArrayList<>(this.validMovesAndLocations.keySet());
    possibleDirections.sort(new DirectionComparator());
    return possibleDirections;
  }

  @Override
  public boolean isTunnel() {
    return this.validMovesAndLocations.size() == 2;
  }

  @Override
  public int getSmell() {
    return this.smell;
  }

  @Override
  public List<Weapon> getArrows() {
    return this.weapons;
  }

  @Override
  public void setValidMoves(Map<Direction, LocationUpdateState> validMovesAndLocations) {
    this.validMovesAndLocations = new HashMap<>(validMovesAndLocations);
  }

  @Override
  public void addTreasures(List<Treasure> treasures) {
    this.treasures = new ArrayList<>(treasures);
  }

  @Override
  public void removeTreasures(List<Treasure> treasures) {
    List<Treasure> treasureList = new ArrayList<>(treasures);
    this.treasures.removeAll(treasureList);
  }

  @Override
  public void addSmell(int smell) {
    this.smell += smell;
  }

  @Override
  public void reduceSmell(int smell) {
    this.smell -= smell;
    if (this.smell < 0) {
      this.smell = 0;
    }
  }

  @Override
  public void addArrows(List<Weapon> arrows) {
    if (arrows == null) {
      throw new IllegalArgumentException("List of arrows cannot be null.");
    }
    arrows.forEach(arrow -> {
      if (arrow == null) {
        throw new IllegalArgumentException("List of arrows cannot be null elements");
      }
    });
    this.weapons.addAll(arrows);
  }

  @Override
  public void addMonster(Monster otyugh) {
    this.monster = otyugh;
  }

  @Override
  public Monster getMonster() {
    return this.monster;
  }

  @Override
  public List<LocationUpdateState> getNeighbors() {
    return new ArrayList<>(this.validMovesAndLocations.values());
  }

  @Override
  public void removeArrows() {
    this.weapons = new ArrayList<>();
  }

  @Override
  public Map<Direction, LocationUpdateState> getNeighborLocations() {
    return new HashMap<Direction, LocationUpdateState>(this.validMovesAndLocations);
  }

  /**
   * Returns the string representation of the model.location. "T" in case of a tunnel and "C" in
   * case of a cave.
   *
   * @return the string representation.
   */
  @Override
  public String toString() {
    if (isTunnel()) {
      return "T";
    } else if (this.monster != null) {
      return "O";
    } else {
      return "C";
    }
  }
}
