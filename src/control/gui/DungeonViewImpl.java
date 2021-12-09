package control.gui;

import java.awt.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.dungeon.ReadOnlyDungeon;
import model.location.Direction;

/**
 * This DungeonImpl class represents the view of the dungeon game. It interacts
 * with the read only model to get the board game status and provides a GUI for the game.
 */
public class DungeonViewImpl extends JFrame implements DungeonView {

  private final DungeonPanel board;
  private final DescriptionPanel description;
  private boolean shootMode;
  private Direction shootDirection;
  private final ShootDialog shootDialog;
  private final ReadOnlyDungeon model;
  private final static String PATH = "res/dungeon-images/";

  /**
   * Constructs Dungeon view.
   * @param model the model.
   * @throws IllegalArgumentException if {@code model} is {@code null}.
   */
  public DungeonViewImpl(ReadOnlyDungeon model, DungeonGUIController listener, int rows,
                         int cols) {
    super("Dungeon");
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
    this.getContentPane().setBackground(Color.BLACK);
    this.setSize(64*10, (64*rows + 140));
    this.setLocation(100, 100);
    this.board = new DungeonPanel(model, rows, cols);

    this.description = new DescriptionPanel(model, listener);
    this.description.setSize(new Dimension(64*10, 140));
    this.description.setAlignmentY(Component.TOP_ALIGNMENT);

    setBackground(Color.black);
    setSize(64*10, (64*rows + 140));

    JPanel container = new JPanel();
    container.setBackground(new Color(26, 26, 26));
    container.setSize(64*10, (64*rows + 140));
    container.setLayout(new FlowLayout());

    JPanel treasures = new JPanel();
    treasures.setLayout(new GridLayout(1,4));

    Icon diamond = getImageIcon("diamond.png");
    treasures.add(new JButton("Diamond", diamond));

    Icon emerald = getImageIcon("emerald.png");
    treasures.add(new JButton("Emerald", emerald));

    Icon ruby = getImageIcon("ruby.png");
    treasures.add(new JButton("Ruby", ruby));

    Icon arrow = getImageIcon("arrow-black.png");
    treasures.add(new JButton("Arrow", arrow));

    board.setSize(new Dimension(64*rows, 64*cols));
    description.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    container.add(description);
    container.add(board);

    JScrollPane scrollPane2 = new JScrollPane(container);
    add(scrollPane2);

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.shootMode = false;
    this.shootDialog = new ShootDialog(this);
    this.shootDialog.setLocation(300,300);
    pack();
  }

  @Override
  public void addClickListener(DungeonGUIController listener) {
    MouseAdapter clickAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        super.mouseClicked(event);
        int column = (event.getX()) / DungeonPanel.SIZE;
        int row = (event.getY()) / DungeonPanel.SIZE;
        boolean result = listener.handleCellClick(row, column);
        description.setMoveResult(result);
      }
    };
    board.addMouseListener(clickAdapter);
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void setKeyBoardListeners(DungeonGUIController listener) {
    KeyListener keyAdapter = new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            if (shootMode) {
              shootDirection = Direction.N;
              shootDialog.setVisible(true);
            } else {
              boolean result = listener.handleKeyMove(Direction.N);
              description.setMoveResult(result);
            }
            break;
          case KeyEvent.VK_DOWN:
            if (shootMode) {
              shootDirection = Direction.S;
              shootDialog.setVisible(true);
            } else {
              boolean result = listener.handleKeyMove(Direction.S);
              description.setMoveResult(result);

            }
            break;
          case KeyEvent.VK_RIGHT:
            if (shootMode) {
              shootDirection = Direction.E;
              shootDialog.setVisible(true);
            } else {
              boolean result = listener.handleKeyMove(Direction.E);
              description.setMoveResult(result);
            }
            break;
          case KeyEvent.VK_LEFT:
            if (shootMode) {
              shootDirection = Direction.W;
              shootDialog.setVisible(true);
            } else {
              boolean result = listener.handleKeyMove(Direction.W);
              description.setMoveResult(result);
            }
            break;
          case KeyEvent.VK_P:
            listener.handlePickAll();
            break;
          case KeyEvent.VK_A:
            listener.handlePickArrows();
            break;
          case KeyEvent.VK_T:
            listener.handlePickTreasures();
            break;
          case KeyEvent.VK_R:
            listener.handlePickRubies();
            break;
          case KeyEvent.VK_E:
            listener.handlePickEmeralds();
            break;
          case KeyEvent.VK_D:
            listener.handlePickDiamonds();
            break;
          case KeyEvent.VK_S:
            if (model.getPlayer().getNumberOfArrows() <= 0) {
              description.setOutOfArrows();
            } else {
              shootMode = true;
            }
            break;
        }
      }
    };

    this.shootDialog.addClickListener(listener);
    this.addKeyListener(keyAdapter);
  }

  @Override
  public void close() {
    this.setVisible(false);
  }

  @Override
  public void setLocationAsVisited(int row, int col) {
    this.board.setLocationAsVisited(row, col);
  }

  private ImageIcon getImageIcon(String path) {
    Image image = null;
    try {
      InputStream imageStream = getClass().getClassLoader().getResourceAsStream(path);
      // convert the input stream into an image
      image = ImageIO.read(imageStream);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
      e.printStackTrace();
    }
    return new ImageIcon(image);
  }

  private class ShootDialog extends JDialog {
    JLabel label;
    JTextField distance;
    JButton shoot;

    public ShootDialog(DungeonViewImpl frame) {
      super(frame, "Shoot arrow");
      setLayout(new FlowLayout());
      label = new JLabel();
      label.setText("Enter shoot distance: ");
      distance = new JTextField(15);
      shoot = new JButton("Shoot");
      add(label);
      add(distance);
      add(shoot);
      this.setSize(300, 150);
      pack();
    }

    void addClickListener(DungeonGUIController listener) {
      shoot.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          boolean result = listener.handleShootArrow(shootDirection,
                  Integer.parseInt(distance.getText()));
          description.setShootResult(result);
          shootDialog.setVisible(false);
          shootDirection = null;
          shootMode = false;
        }
      });
    }
  }
}
