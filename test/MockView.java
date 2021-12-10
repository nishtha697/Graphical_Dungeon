import java.io.IOException;

import control.gui.DungeonGUIController;
import control.gui.DungeonView;

/**
 * Mock dungeon view for testing th GUI controller.
 */
class MockView implements DungeonView {

  Appendable out;

  /**
   * Constructs Mock view.
   * @param out appendable
   */
  MockView(Appendable out) {
    this.out = out;
  }

  @Override
  public void addClickListener(DungeonGUIController listener) {
    try {
      out.append("addClickListener called");
    } catch (IOException e) {
      //do nothing
    }
  }

  @Override
  public void refresh() {
    try {
      out.append("refresh called");
    } catch (IOException e) {
      //do nothing
    }
  }

  @Override
  public void makeVisible() {
    try {
      out.append("makeVisible called");
    } catch (IOException e) {
      //do nothing
    }
  }

  @Override
  public void setLocationAsVisited(int row, int col) {
    try {
      out.append("setLocationAsVisited called:" + row + ", " + col);
    } catch (IOException e) {
      //do nothing
    }
  }

  @Override
  public void setKeyBoardListeners(DungeonGUIController controller) {
    try {
      out.append("setKeyBoardListeners called");
    } catch (IOException e) {
      //do nothing
    }
  }

  @Override
  public void close() {
    try {
      out.append("close called");
    } catch (IOException e) {
      //do nothing
    }
  }
}
