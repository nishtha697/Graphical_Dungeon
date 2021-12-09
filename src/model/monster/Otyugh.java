package model.monster;

/**
 * Otyughs are monsters that are extremely smelly creatures and lead solitary lives in the deep,
 * dark places of the world like dungeon. Otyugh only occupy caves and are never found in tunnels.
 * Their caves can also contain treasure or other items.
 */
public class Otyugh implements Monster {

  private int healthPercentage;

  /**
   * Constructs an Otyugh monster. The monster starts will full health.
   */
  public Otyugh() {
    this.healthPercentage = 100;
  }

  @Override
  public void reduceHealth(int percentage) {
    if (percentage < 0 || percentage > 100) {
      throw new IllegalArgumentException("Health percentage cannot be less than 0 or greater than"
              + " 100.");
    }
    this.healthPercentage -= percentage;
  }

  @Override
  public int getHealthPercentage() {
    return this.healthPercentage;
  }
}
