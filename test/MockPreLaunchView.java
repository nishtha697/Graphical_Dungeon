import java.awt.event.ActionListener;

import control.gui.PreLaunchView;

public class MockPreLaunchView implements PreLaunchView {

  @Override
  public void setCommandButtonListener(ActionListener actionEvent) {

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

  }

  @Override
  public void close() {

  }

}
