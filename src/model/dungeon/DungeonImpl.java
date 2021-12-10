package model.dungeon;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import model.location.Cave;
import model.location.Direction;
import model.location.Location;
import model.location.LocationUpdateState;
import model.location.Treasure;
import model.monster.Monster;
import model.monster.Otyugh;
import model.player.Player;
import model.player.PlayerImpl;
import model.player.PlayerUpdateState;
import model.random.RandomGenerator;
import model.weapon.Weapon;

/**
 * The dungeon.DungeonImpl implements {@link Dungeon} and represents a world of game that consists
 * of a dungeon, a network of tunnels and caves that are interconnected so that player can explore
 * the entire world by traveling from cave to cave through the tunnels that connect them.
 */
public class DungeonImpl implements Dungeon {

  private final List<Edge> paths;
  private final List<Edge> potentialPaths;
  private final int rows;
  private final int columns;
  private final int interconnectivity;
  private final boolean isWrapping;
  private final RandomGenerator rand;
  private final PlayerUpdateState player;
  private final LocationUpdateState start;
  private final LocationUpdateState end;
  private final List<List<LocationUpdateState>> dungeon;

  /**
   * Constructs a dungeon.
   *
   * @param rows                           the number of rows.
   * @param columns                        the number of columns.
   * @param interconnectivity              the interconnectivity.
   * @param isWrapping                     {@code true} is dungeon is wrapping otherwise
   *                                       {@code false}.
   * @param percentageOfTreasuresAndArrows the percentage of caves to have treasures and the
   *                                       percentage of locations to have arrows.
   * @param rand                           the random generator.
   * @throws IllegalArgumentException <ul><li>if dungeon is wrapping and the sum of rows and columns
   *                                  is less than 7.</li>
   *                                  <li>if dungeon is non wrapping and the sum of rows and columns
   *                                  is less than 9.</li>
   *                                  <li>if there is no path between any two nodes of the dungeon
   *                                  with at lE length 5.</li>
   *                                  <li>if percentage of treasures is less than 0 or more than
   *                                  100.</li>
   *                                  <li>if {@code rand} is {@code null}.</li></ul>
   */
  public DungeonImpl(int rows, int columns, int interconnectivity, boolean isWrapping,
                     double percentageOfTreasuresAndArrows, String playerName, int numberOfMonsters,
                     RandomGenerator rand)
          throws IllegalArgumentException {

    if (rows + columns < 7 && isWrapping) {
      throw new IllegalArgumentException("Too small model.dungeon. Increase number of rows and/or "
              + "columns.");
    }
    if (rows + columns < 9 && !isWrapping) {
      throw new IllegalArgumentException("Too small model.dungeon. Increase number of rows and/or "
              + "columns.");
    }
    if (isWrapping) {
      if (interconnectivity > (2 * rows * columns - rows * columns + 1)) {
        throw new IllegalArgumentException("Invalid interconnectivity.");
      }
    } else {
      if (interconnectivity > ((2 * rows * columns - rows - columns) - rows * columns + 1)) {
        throw new IllegalArgumentException("Invalid interconnectivity.");
      }
    }
    if (percentageOfTreasuresAndArrows < 0 || percentageOfTreasuresAndArrows > 100) {
      throw new IllegalArgumentException("Percentage of caves with treasures cannot be negative.");
    }

    if (playerName == null || playerName.equals("")) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }

    if (rand == null) {
      throw new IllegalArgumentException("Random generator cannot be null.");
    }

    this.rows = rows;
    this.columns = columns;
    this.interconnectivity = interconnectivity;
    this.isWrapping = isWrapping;
    this.rand = rand;
    this.dungeon = createDungeon();
    this.potentialPaths = createPotentialPaths();
    this.paths = createPaths();
    getValidMovesForCaves();
    addTreasureToCaves(percentageOfTreasuresAndArrows);
    addArrowsToLocations(percentageOfTreasuresAndArrows);

    if (numberOfMonsters == 0) {
      throw new IllegalArgumentException("Invalid number of monsters. There need to be at lE 1 "
              + "monster");
    }
    if (numberOfMonsters >= getCavesOnly().size()) {
      throw new IllegalArgumentException("Invalid number of monsters. Too many monsters, reduce the"
              + " number of monsters.");
    }

    Map.Entry<LocationUpdateState, LocationUpdateState> sourceAndDestination = setStartAndEndCave();
    this.start = sourceAndDestination.getKey();
    this.end = sourceAndDestination.getValue();
    addMonstersToCaves(numberOfMonsters - 1);
    this.player = new PlayerImpl(playerName, this.start);
  }

  /**
   * This is a copy constructor.
   * @param dungeon the dungeon
   */
  public DungeonImpl(DungeonImpl dungeon) {
    if (dungeon == null) {
      throw new IllegalArgumentException();
    }
    this.rows = dungeon.rows;
    this.columns = dungeon.columns;
    this.interconnectivity = dungeon.interconnectivity;
    this.isWrapping = dungeon.isWrapping;
    this.rand = dungeon.rand;
    this.dungeon = new ArrayList<>();
    for (int i = 0; i < this.rows; i++) {
      List<LocationUpdateState> dungeonCol = new ArrayList<>();
      for (int j = 0; j < this.columns; j++) {
        dungeonCol.add(new Cave((this.columns * i) + j, i, j));
      }
      this.dungeon.add(dungeonCol);
    }
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        this.dungeon.get(i).set(j, new Cave((Cave) dungeon.dungeon.get(i).get(j)));
      }
    }
    this.potentialPaths = dungeon.potentialPaths;
    this.paths = dungeon.paths;
    this.start = dungeon.start;
    this.end = dungeon.end;
    this.player = new PlayerImpl(dungeon.player.getName(), start);
    getValidMovesForCaves();
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public Location getPlayerLocation() {
    return this.player.getLocation();
  }

  @Override
  public Location getStartingCave() {
    return this.start;
  }

  @Override
  public Location getDestinationCave() {
    return this.end;
  }

  @Override
  public boolean movePlayer(Direction direction)
          throws IllegalArgumentException {
    if (direction == null || !this.player.getLocation().getPossibleMoves().contains(direction)) {
      throw new IllegalArgumentException("Invalid direction!");
    }
    if (isDestinationReached() || this.player.isDead()) {
      throw new IllegalStateException("Game is already over!");
    }

    LocationUpdateState newLocation;

    int currentRow = 0;
    int currentColumn = 0;

    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        if (this.dungeon.get(i).get(j).getId() == this.player.getLocation().getId()) {
          currentRow = i;
          currentColumn = j;
        }
      }
    }

    int newRow = currentRow;
    int newCol = currentColumn;

    switch (direction) {
      case S:
        newRow = currentRow + 1;
        if (this.isWrapping) {
          if (newRow == this.rows) {
            newRow = 0;
          }
        }
        break;

      case N:
        newRow = currentRow - 1;
        if (this.isWrapping) {
          if (newRow == -1) {
            newRow = this.rows - 1;
          }
        }
        break;

      case W:
        newCol = currentColumn - 1;
        if (this.isWrapping) {
          if (newCol == -1) {
            newCol = this.columns - 1;
          }
        }
        break;

      case E:
        newCol = currentColumn + 1;
        if (this.isWrapping) {
          if (newCol == this.columns) {
            newCol = 0;
          }
        }
        break;

      default: //No action required.
    }
    newLocation = this.dungeon.get(newRow).get(newCol);
    this.player.move(newLocation);
    Monster locationMonster = ((LocationUpdateState)this.player.getLocation()).getMonster();
    if (locationMonster != null) {
      if (locationMonster.getHealthPercentage() == 0) {
        return true;
      }
      if (locationMonster.getHealthPercentage() < 100) {
        //chance a player can survive with the injured monster. 1 player is saved. 0 monster kills
        // player.
        int chance = this.rand.getRandom(2, 0);
        if (chance == 0) {
          this.player.killPlayer();
          return false;
        }
        return true;
      } else {
        this.player.killPlayer();
        return false;
      }
    }
    return true;
  }

  @Override
  public void collectAllTreasures() {
    this.player.collectTreasures(List.of(Treasure.values()));
  }

  @Override
  public void collectTreasure(List<Treasure> treasures) throws IllegalArgumentException {
    if (treasures == null) {
      throw new IllegalArgumentException("Treasures cannot be null.");
    }
    this.player.collectTreasures(new ArrayList<>(treasures));
  }

  @Override
  public boolean isDestinationReached() {
    return this.player.getLocation().getId() == this.end.getId();
  }

  @Override
  public boolean shootArrow(int distance, Direction direction) {
    if (!this.player.getLocation().getPossibleMoves().contains(direction)) {
      throw new IllegalArgumentException("Direction is wrong");
    }
    player.shootArrow();
    int x = this.player.getLocation().getCoordinates().getX();
    int y = this.player.getLocation().getCoordinates().getY();
    return moveArrow(distance, this.dungeon.get(x).get(y), direction);
  }

  @Override
  public void pickArrows() {
    this.player.pickArrows();
  }

  @Override
  public String toString() {
    StringBuilder dungeonBuilder = new StringBuilder();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.N)) {
          dungeonBuilder.append("     |     ");
        } else {
          dungeonBuilder.append("           ");
        }
      }
      dungeonBuilder.append("\n");
      for (int j = 0; j < columns; j++) {

        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.W)) {
          dungeonBuilder.append("--- ");
        } else {
          dungeonBuilder.append("    ");
        }
        Location location = this.dungeon.get(i).get(j);
        String locationString;
        if (this.player != null && location.getId() == this.player.getLocation().getId()) {
          locationString = "P";
        } else if (location.getId() == this.getStartingCave().getId()) {
          locationString = "S";
        } else if (location.getId() == this.getDestinationCave().getId()) {
          locationString = "D";
        } else {
          locationString = location.toString();
        }
        dungeonBuilder.append(String.format("[%s]", locationString));
        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.E)) {
          dungeonBuilder.append(" ---");
        } else {
          dungeonBuilder.append("    ");
        }
      }
      dungeonBuilder.append("\n");
      for (int j = 0; j < columns; j++) {
        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.S)) {
          dungeonBuilder.append("     |     ");
        } else {
          dungeonBuilder.append("           ");
        }
      }
      dungeonBuilder.append("\n");
    }

    return dungeonBuilder.toString();
  }

  private boolean moveArrow(int distance, LocationUpdateState location, Direction direction) {
    if (distance == 0) {
      if (location.getMonster() != null && location.getMonster().getHealthPercentage() > 0) {
        location.getMonster().reduceHealth(50);
        if (location.getMonster().getHealthPercentage() == 0) {
          removeMonsterSmellFromLocations(location);
        }
        return true;
      }
      return false;
    }
    List<Integer> nextPosition;
    if (location.getPossibleMoves().contains(direction) || location.isTunnel()) {
      Direction enteringDirection = null;

      if (location.isTunnel()) {
        switch (direction) {
          case E:
            enteringDirection = Direction.W;
            break;
          case W:
            enteringDirection = Direction.E;
            break;
          case N:
            enteringDirection = Direction.S;
            break;
          case S:
            enteringDirection = Direction.N;
            break;
          default: //no action
        }

        Direction finalEnteringDirection = enteringDirection;
        direction = location.getPossibleMoves().contains(direction)
                ? direction : location.getPossibleMoves().stream()
                .filter(l -> !l.equals(finalEnteringDirection)).findFirst().get();

      }
      nextPosition = getNextArrowCoordinate(direction, location);
      distance = this.dungeon.get(location.getCoordinates().getX() + nextPosition.get(0))
              .get(location.getCoordinates().getY() + nextPosition.get(1)).isTunnel() ? distance
              : --distance;

      return moveArrow(distance, this.dungeon.get(location.getCoordinates().getX()
              + nextPosition.get(0)).get(location.getCoordinates().getY()
              + nextPosition.get(1)), direction);
    }
    return false;
  }

  private List<Integer> getNextArrowCoordinate(Direction direction, LocationUpdateState location) {
    int i = 0;
    int j = 0;
    switch (direction) {
      case E:
        j = 1;
        if (isWrapping && location.getCoordinates().getY() == this.columns - 1) {
          j = -location.getCoordinates().getY();
        }
        break;
      case W:
        j = -1;
        if (isWrapping && location.getCoordinates().getY() == 0) {
          j = this.columns - 1;
        }
        break;
      case N:
        i = -1;
        if (isWrapping && location.getCoordinates().getX() == 0) {
          i = this.rows - 1;
        }
        break;
      case S:
        i = 1;
        if (isWrapping && location.getCoordinates().getX() == this.rows - 1) {
          i = -location.getCoordinates().getX();
        }
        break;
      default: //no action
    }
    return List.of(i, j);
  }

  private void addTreasureToCaves(double percentageOfTreasures) {
    List<LocationUpdateState> allLocations = getCavesOnly();
    int cavesWithTreasures = (int) (allLocations.size() * percentageOfTreasures / 100);
    final List<Treasure> allTreasures = List.of(Treasure.values());

    for (int i = 0; i < cavesWithTreasures; i++) {
      List<Treasure> treasuresToBeAdded = new ArrayList<>();
      int numberOfTreasures = rand.getRandom(4, 1);

      for (int j = 0; j < numberOfTreasures; j++) {
        treasuresToBeAdded.add(allTreasures.get(rand.getRandom(Treasure.values().length,
                0)));
      }
      int index = rand.getRandom(allLocations.size(), 0);
      allLocations.get(index).addTreasures(treasuresToBeAdded);
      allLocations.remove(index);
    }
  }

  private void addMonstersToCaves(double numberOfMonsters) {
    List<LocationUpdateState> potentialMonsterCaves = getCavesOnly().stream()
            .filter(c -> !c.equals(this.start) && !c.equals(this.end))
            .collect(Collectors.toList());

    while (numberOfMonsters > 0) {
      int index = rand.getRandom(potentialMonsterCaves.size(), 0);
      LocationUpdateState cave = potentialMonsterCaves.get(index);

      if (!cave.equals(this.end)) {
        potentialMonsterCaves.get(index).addMonster(new Otyugh());
        potentialMonsterCaves.remove(index);
        numberOfMonsters--;
      }
    }
    addMonsterSmellToLocations();
  }

  private void addMonsterSmellToLocations() {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        if (this.dungeon.get(i).get(j).getMonster() != null) {
          this.dungeon.get(i).get(j).addSmell(2);

          //add smell to direct neighbor locations
          for (LocationUpdateState neighbor : this.dungeon.get(i).get(j).getNeighbors()) {
            neighbor.addSmell(2);
          }

          List<Integer> neighborsWithSmell = new ArrayList<>();
          //adds smell to neighbor locations with distance 2.
          for (LocationUpdateState location : this.dungeon.get(i).get(j).getNeighbors()) {
            for (LocationUpdateState neighbor : location.getNeighbors()) {
              if (!neighborsWithSmell.contains(neighbor.getId())) {
                neighbor.addSmell(1);
                neighborsWithSmell.add(neighbor.getId());
              }
            }
          }
        }
      }
    }
  }

  private void removeMonsterSmellFromLocations(LocationUpdateState location) {

    location.reduceSmell(2);

    //reduces smell from direct neighbor locations
    for (LocationUpdateState neighbor : location.getNeighbors()) {
      neighbor.reduceSmell(2);
    }

    //reduces smell from neighbor locations with distance 2.
    for (LocationUpdateState loc : location.getNeighbors()) {
      for (LocationUpdateState neighbor : loc.getNeighbors()) {
        neighbor.reduceSmell(1);
      }
    }
  }

  private void addArrowsToLocations(double percentageOfArrows) {
    List<LocationUpdateState> allLocations = this.dungeon.stream().flatMap(Collection::stream)
            .collect(Collectors.toList());
    int locationsWithArrows = (int) (allLocations.size() * percentageOfArrows / 100);

    for (int i = 0; i < locationsWithArrows; i++) {
      List<Weapon> arrowsToBeAdded = new ArrayList<>();
      int numberOfArrows = rand.getRandom(3, 1);
      for (int j = 0; j < numberOfArrows; j++) {
        arrowsToBeAdded.add(Weapon.ARROW);
      }
      int index = rand.getRandom(allLocations.size(), 0);
      allLocations.get(index).addArrows(arrowsToBeAdded);
      allLocations.remove(index);
    }
  }

  private List<List<LocationUpdateState>> createDungeon() {
    List<List<LocationUpdateState>> dungeon = new ArrayList<>();
    for (int i = 0; i < this.rows; i++) {
      List<LocationUpdateState> dungeonCol = new ArrayList<>();
      for (int j = 0; j < this.columns; j++) {
        dungeonCol.add(new Cave((this.columns * i) + j, i, j));
      }
      dungeon.add(dungeonCol);
    }
    return dungeon;
  }

  private List<Edge> createPotentialPaths() {
    List<Edge> possibleEdges = new ArrayList<>();
    for (int i = 0; i < rows - 1; i++) {
      for (int j = 0; j < columns - 1; j++) {
        possibleEdges.add(new Edge(this.dungeon.get(i).get(j).getId(),
                this.dungeon.get(i).get(j + 1).getId()));
        possibleEdges.add(new Edge(this.dungeon.get(i).get(j).getId(),
                this.dungeon.get(i + 1).get(j).getId()));
      }
    }

    //border edges
    for (int j = 0; j < columns - 1; j++) {
      possibleEdges.add(new Edge(this.dungeon.get(rows - 1).get(j).getId(),
              this.dungeon.get(rows - 1).get(j + 1).getId()));
    }
    for (int i = 0; i < rows - 1; i++) {
      possibleEdges.add(new Edge(this.dungeon.get(i).get(columns - 1).getId(),
              this.dungeon.get(i + 1).get(columns - 1).getId()));
    }

    //edges for wrapping model.dungeon
    if (this.isWrapping) {
      for (int i = 0; i < rows; i++) {
        possibleEdges.add(new Edge(this.dungeon.get(i).get(0).getId(), this.dungeon.get(i)
                .get(columns - 1).getId()));
      }
      for (int j = 0; j < columns; j++) {
        possibleEdges.add(new Edge(this.dungeon.get(0).get(j).getId(), this.dungeon.get(rows - 1)
                .get(j).getId()));
      }
    }
    return possibleEdges;
  }

  private List<Edge> createPaths() {
    List<Edge> potentialPaths = new ArrayList<>(this.potentialPaths);
    List<Edge> paths = new ArrayList<>();
    List<Edge> leftOverPaths = new ArrayList<>();
    int nodes = this.rows * this.columns;
    Subset[] subsets = new Subset[nodes];
    for (int i = 0; i < nodes; i++) {
      subsets[i] = new Subset(i, 0);
    }
    int currentEdge = 0;

    while (currentEdge < nodes - 1 && potentialPaths.size() > 1) {
      int randomIndex = this.rand.getRandom(potentialPaths.size(), 0);
      Edge nextEdge = potentialPaths.get(randomIndex);

      int x = find(subsets, nextEdge.getFirstLocation());
      int y = find(subsets, nextEdge.getSecondLocation());

      if (x != y) {
        paths.add(nextEdge);
        union(subsets, x, y);
        currentEdge++;
      } else {
        leftOverPaths.add(nextEdge);
      }
      potentialPaths.remove(randomIndex);
    }

    for (int i = 0; i < this.interconnectivity; i++) {
      if (leftOverPaths.size() == 0) {
        int randomIndex = this.rand.getRandom(potentialPaths.size(), 0);
        paths.add(potentialPaths.get(randomIndex));
        potentialPaths.remove(randomIndex);
      } else {
        int randomIndex = this.rand.getRandom(leftOverPaths.size(), 0);
        paths.add(leftOverPaths.get(randomIndex));
        leftOverPaths.remove(randomIndex);
      }
      currentEdge++;
    }
    return paths;
  }

  private int find(Subset[] subsets, int i) {
    if (subsets[i].parent != i) {
      subsets[i].parent = find(subsets, subsets[i].parent);
    }
    return subsets[i].parent;
  }

  private void union(Subset[] subsets, int x, int y) {
    int rootOfX = find(subsets, x);
    int rootOfY = find(subsets, y);

    if (subsets[rootOfX].rank < subsets[rootOfY].rank) {
      subsets[rootOfX].parent = rootOfY;
    } else if (subsets[rootOfX].rank > subsets[rootOfY].rank) {
      subsets[rootOfY].parent = rootOfX;
    } else {
      subsets[rootOfY].parent = rootOfX;
      subsets[rootOfX].rank++;
    }
  }

  private void getValidMovesForCaves() {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        Map<Direction, LocationUpdateState> validDirections = new HashMap<>();
        LocationUpdateState cave = this.dungeon.get(i).get(j);

        //down
        if (i < this.rows - 1) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i + 1).get(j).getId()))) {
            validDirections.put(Direction.S, this.dungeon.get(i + 1).get(j));
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(0).get(j).getId()))) {
            validDirections.put(Direction.S, this.dungeon.get(0).get(j));
          }
        }
        //up
        if (i > 0) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i - 1).get(j).getId()))) {
            validDirections.put(Direction.N, this.dungeon.get(i - 1).get(j));
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(this.rows - 1).get(j)
                  .getId()))) {
            validDirections.put(Direction.N, this.dungeon.get(this.rows - 1).get(j));
          }
        }
        //right
        if (j < this.columns - 1) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(j + 1).getId()))) {
            validDirections.put(Direction.E, this.dungeon.get(i).get(j + 1));
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(0).getId()))) {
            validDirections.put(Direction.E, this.dungeon.get(i).get(0));
          }
        }
        //left
        if (j > 0) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(j - 1).getId()))) {
            validDirections.put(Direction.W, this.dungeon.get(i).get(j - 1));
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(this.columns - 1)
                  .getId()))) {
            validDirections.put(Direction.W, this.dungeon.get(i).get(this.columns - 1));
          }
        }
        cave.setValidMoves(validDirections);
      }
    }
  }

  private List<LocationUpdateState> getCavesOnly() {
    List<LocationUpdateState> allLocations = new ArrayList<>();
    for (List<LocationUpdateState> locations : this.dungeon) {
      allLocations.addAll(locations);
    }
    return allLocations.stream().filter(location -> !location.isTunnel())
            .collect(Collectors.toList());
  }

  private LocationUpdateState getRandomCave(List<LocationUpdateState> locations) {
    int index = this.rand.getRandom(locations.size(), 0);
    return locations.get(index);
  }

  private AbstractMap.SimpleImmutableEntry<LocationUpdateState, LocationUpdateState>
              setStartAndEndCave() throws IllegalArgumentException {
    List<LocationUpdateState> potentialSources = getCavesOnly();
    LocationUpdateState source;
    LocationUpdateState destination;

    do {
      source = getRandomCave(potentialSources);
      destination = setEndCave(source);
      potentialSources.remove(source);
    }
    while (destination == null && potentialSources.size() != 0);

    if (destination == null) {
      throw new IllegalArgumentException("Dungeon too small or interconnected! Cannot find any"
              + " path of at length 5 between two nodes.");
    }
    destination.addMonster(new Otyugh());
    return new AbstractMap.SimpleImmutableEntry<>(source, destination);
  }

  private LocationUpdateState setEndCave(LocationUpdateState source) {
    LocationUpdateState destination;
    int distance;
    List<LocationUpdateState> allLocations = getCavesOnly();

    do {
      destination = getRandomCave(allLocations);
      distance = getMinimumDistance(source, destination);
      allLocations.remove(destination);
    }
    while (distance < 5 && allLocations.size() > 0);

    if (distance < 5) {
      return null;
    }

    return destination;
  }

  private int getMinimumDistance(LocationUpdateState start, LocationUpdateState end) {
    NodeWithDistanceFromSource source = new NodeWithDistanceFromSource(0, 0, 0);
    Queue<NodeWithDistanceFromSource> nodes = new LinkedList<>();
    boolean[][] visited = new boolean[this.dungeon.size()][this.dungeon.get(0).size()];

    sourceLoop:
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        if (this.dungeon.get(i).get(j).getId() == start.getId()) {
          source.x = i;
          source.y = j;
          break sourceLoop;
        }
      }
    }

    nodes.add(new NodeWithDistanceFromSource(source.x, source.y, 0));
    visited[source.x][source.y] = true;

    while (!nodes.isEmpty()) {
      NodeWithDistanceFromSource node = nodes.remove();
      int x;
      int y;

      // Destination reached
      if (this.dungeon.get(node.x).get(node.y).getId() == end.getId()) {
        return node.distance;
      }

      // up
      x = node.x - 1;
      y = node.y;
      if (this.isWrapping && node.x == 0) {
        x = this.rows - 1;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves()
              .contains(Direction.N)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }

      // down
      x = node.x + 1;
      y = node.y;
      if (this.isWrapping && node.x == this.rows - 1) {
        x = 0;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves()
              .contains(Direction.S)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }

      // left
      x = node.x;
      y = node.y - 1;
      if (this.isWrapping && node.y == 0) {
        y = this.columns - 1;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves().contains(Direction.W)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }

      // right
      x = node.x;
      y = node.y + 1;
      if (this.isWrapping && node.y == this.columns - 1) {
        y = 0;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves().contains(Direction.E)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }
    }
    return -1;
  }

  private boolean isValid(int x, int y, boolean[][] visited) {
    return (x >= 0 && y >= 0 && x < this.dungeon.size() && y < this.dungeon.get(0).size()
            && !visited[x][y]);
  }

  /**
   * This represents a {@link Location} in the dungeon with co-ordinates of the location in the
   * dungeon grid and its distance from the source location.
   */
  private static class NodeWithDistanceFromSource {
    private final int distance;
    private int x;
    private int y;

    /**
     * Constructs a location node.
     *
     * @param x        the x co-ordinate of location in the dungeon grid.
     * @param y        the y co-ordinate of location in the dungeon grid.
     * @param distance the distance from the source location.
     */
    private NodeWithDistanceFromSource(int x, int y, int distance) {
      this.x = x;
      this.y = y;
      this.distance = distance;
    }
  }

  /**
   * This represents a subset of the dungeon grid with the root parent and rank. This class is used
   * for building the dungeon grid.
   */
  private static class Subset {
    private int parent;
    private int rank;

    private Subset(int parent, int rank) {
      this.parent = parent;
      this.rank = rank;
    }
  }

}
