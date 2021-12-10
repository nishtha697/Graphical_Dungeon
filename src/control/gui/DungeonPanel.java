package control.gui;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import model.dungeon.ReadOnlyDungeon;
import model.location.coordinate.Coordinate;
import model.location.coordinate.CoordinateImpl;

/**
 * Represents the dungeon game panel of the graphical view and provides the user a graphical
 * representation of the dungeon with treasures, monsters, arrows, etc.
 */
class DungeonPanel extends JPanel {
  public static final int SIZE = 64;
  private final ReadOnlyDungeon model;
  private final java.util.List<java.util.List<Boolean>> exploredDungeon;
  private final JLabel[][] panelHolder;
  private final Map<Coordinate, String> previousLocation;

  /**
   * Constructs the dungeon panel.
   *
   * @param model the read only model.
   * @param rows  the rows.
   * @param cols  the columns.
   */
  DungeonPanel(ReadOnlyDungeon model, int rows, int cols) {
    this.model = model;
    setLayout(new GridLayout(rows, cols, 0, 0));
    Border line = BorderFactory.createLineBorder(new Color(27, 28, 27));
    setBorder(line);
    setBorder(new MatteBorder(10, 10, 10, 10,
            new Color(0, 0, 0)));
    this.exploredDungeon = new ArrayList<>();
    setBackground(Color.black);
    this.previousLocation = new HashMap<>();
    panelHolder = new JLabel[rows][cols];
    setSize(new Dimension(64 * cols, 64 * rows));
    for (int i = 0; i < rows; i++) {
      List<Boolean> dungeonCol = new ArrayList<>();
      for (int j = 0; j < cols; j++) {
        dungeonCol.add(false);
      }
      this.exploredDungeon.add(dungeonCol);
    }

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        panelHolder[i][j] = new JLabel();
        add(createJLabel(panelHolder[i][j]));
      }
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.setFont(new Font("Courier New", Font.PLAIN, 20));

    this.exploredDungeon.get(model.getPlayerLocation().getCoordinates().getX())
            .set(model.getPlayerLocation().getCoordinates().getY(), true);

    if (!this.previousLocation.isEmpty()) {
      Map.Entry<Coordinate, String> entry = this.previousLocation.entrySet().iterator().next();

      ImageIcon previousIcon = new ImageIcon(getBufferedImage(entry.getValue() + ".png"));
      panelHolder[entry.getKey().getX()][entry.getKey().getY()].setIcon(previousIcon);
    }

    if (this.exploredDungeon.get(model.getPlayerLocation().getCoordinates().getX())
            .get(model.getPlayerLocation().getCoordinates().getY())) {
      String moveString = model.getPlayerLocation().getPossibleMoves().stream()
              .map(direction -> String.valueOf(direction))
              .collect(Collectors.joining());
      BufferedImage buffered;
      BufferedImage cell = null;

      try {
        cell = getBufferedImage(moveString + ".png");

        if (model.getPlayerLocation().getSmell() >= 2) {
          buffered = getBufferedImage(moveString + ".png");
          cell = overlay(buffered, getBufferedImage("stench02.png"), 0);
        } else if (model.getPlayerLocation().getSmell() == 1) {
          buffered = getBufferedImage(moveString + ".png");
          cell = overlay(buffered, getBufferedImage("stench01.png"), 0);
        }

        if (!model.getPlayerLocation().getTreasures().isEmpty()) {
          cell = overlay(cell, getBufferedImage("treasure.png"), 40, 10);
        }

        if (!model.getPlayerLocation().getArrows().isEmpty()) {
          cell = overlay(cell, getBufferedImage("small-arrow.png"), 40, 30);
        }

        if (model.getPlayerLocation().getMonster() != null) {
          if (model.getPlayerLocation().getMonster().getHealthPercentage() > 0) {
            cell = overlay(cell, getBufferedImage("small-otyugh.png"), 40);
          } else {
            cell = overlay(cell, getBufferedImage("small-dead-monster.png"), 40);
          }
        }

        cell = overlay(cell, getBufferedImage("player.png"), 5);
      } catch (IOException e) {
        e.printStackTrace();
      }

      ImageIcon icon = new ImageIcon(cell);
      panelHolder[model.getPlayerLocation().getCoordinates().getX()]
              [model.getPlayerLocation().getCoordinates().getY()].setIcon(icon);
      this.previousLocation.clear();
      this.previousLocation.put(new CoordinateImpl(model.getPlayerLocation().getCoordinates()
              .getX(), model.getPlayerLocation().getCoordinates().getY()), moveString);
    }
  }

  public void setLocationAsVisited(int row, int col) {
    this.exploredDungeon.get(row).set(col, true);
  }

  private BufferedImage overlay(BufferedImage starting, BufferedImage overlay, int offsetX,
                                int offsetY)
          throws IOException {
    int w = Math.max(starting.getWidth(), overlay.getWidth());
    int h = Math.max(starting.getHeight(), overlay.getHeight());
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(starting, 0, 0, null);
    g.drawImage(overlay, offsetX, offsetY, null);
    return combined;
  }

  private BufferedImage overlay(BufferedImage starting, BufferedImage overlay, int offset)
          throws IOException {
    return overlay(starting, overlay, offset, offset);
  }

  private BufferedImage getBufferedImage(String path) {
    BufferedImage image = null;
    try {
      InputStream imageStream = getClass().getClassLoader().getResourceAsStream(path);
      // convert the input stream into an image
      image = ImageIO.read(imageStream);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
      e.printStackTrace();
    }
    return image;
  }

  private JLabel createJLabel(JLabel panel) {
    panel.setBackground(Color.black);
    panel.setMinimumSize(new Dimension(64, 64));
    panel.setMaximumSize(new Dimension(64, 64));
    panel.setPreferredSize(new Dimension(64, 64));
    return panel;
  }
}
