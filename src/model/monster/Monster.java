package model.monster;

/**
 * Monsters are extremely smelly creatures that lead solitary lives in the deep, dark places of the
 * world like dungeon. They are adapted to eat whatever organic material that they can find, but
 * love it when fresh meat happens into the cave in which they dwell.
 */
public interface Monster {

  /**
   * Reduces the health of the monster by the given percentage. This cannot be less than 0 or
   * greater than 100.
   *
   * @param percentage the percentage of health to be reduced.
   * @throws IllegalArgumentException if {@code percentage} is less than 0 or greater than 100.
   */
  void reduceHealth(int percentage);

  /**
   * Returns the health percentage of the monster at the location. This will not be less than 0 or
   * greater than 100.
   *
   * @return the health Percentage of the monster.
   */
  int getHealthPercentage();
}
