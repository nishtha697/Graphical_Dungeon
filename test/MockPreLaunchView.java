import java.awt.event.ActionListener;

import control.gui.PreLaunchView;

/**
 * Mock pre-launch view for testing th GUI controller.
 */
class MockPreLaunchView implements PreLaunchView {

  @Override
  public void setCommandButtonListener(ActionListener actionEvent) {
    //do nothing
  }

  @Override
  public int getRows() {
    return 6;
  }

  @Override
  public int getCols() {
    return 4;
  }

  @Override
  public int getInterconnectivity() {
    return 4;
  }

  @Override
  public boolean getIsWrapping() {
    return false;
  }

  @Override
  public String getNameOfPlayer() {
    return "Nishtha";
  }

  @Override
  public int getNumberOfMonsters() {
    return 1;
  }

  @Override
  public double getPercentageOfTreasuresAndArrowsL() {
    return 25;
  }

  @Override
  public void makeVisible() {
    //do nothing
  }

  @Override
  public void close() {
    //do nothing
  }
}
