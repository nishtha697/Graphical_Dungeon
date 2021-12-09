package control.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;

import model.dungeon.ReadOnlyDungeon;
import model.location.Treasure;

class DescriptionPanel extends JPanel {

  private final ReadOnlyDungeon model;
  private final DungeonGUIController controller;
  private final JPanel playerDescription;
  private final JPanel locationDescription;
  private final Icon diamond;
  private final Icon emerald;
  private final Icon ruby;
  private final Icon arrow;
  private final Icon strongSmell;
  private final Icon lightSmell;
  private final Icon noSmell;

  private final JLabel info;

  DescriptionPanel(ReadOnlyDungeon model, DungeonGUIController controller) {
    this.model = model;

    diamond = getImageIcon("diamond.png");
    emerald = getImageIcon("emerald.png");
    ruby = getImageIcon("ruby.png");
    arrow = getImageIcon("arrow-white.png");
    strongSmell = getImageIcon("stench02.png");
    lightSmell = getImageIcon("stench01.png");
    noSmell = getImageIcon("no-smell.png");

    setLayout(new GridLayout(3, 1));
    playerDescription = new PlayerDescription();
    locationDescription = new LocationDescription();
    playerDescription.setBorder(new MatteBorder(1, 0, 1, 0,
            new Color(27, 28, 27)));
    locationDescription.setBorder(new MatteBorder(0, 0, 1, 0,
            new Color(27, 28, 27)));
    this.info = new JLabel("Welcome to the world of dungeons!");
    this.info.setForeground(Color.PINK);
    Font font = new Font("Arial", Font.BOLD,18);
    this.info.setFont(font);
    this.info.setBackground(Color.black);
    this.playerDescription.setBackground(Color.black);
    this.locationDescription.setBackground(Color.black);
    this.playerDescription.setForeground(Color.WHITE);
    this.locationDescription.setForeground(Color.WHITE);
    this.controller = controller;
    setBorder(new MatteBorder(10, 10, 10, 10,
            new Color(0, 0, 0)));
    setMenuBar();
  }

  public void setMenuBar() {
    JPanel top = new JPanel();
    top.setBackground(Color.black);
    top.setLayout(new BorderLayout());
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenuItem newGame = new JMenuItem(new AbstractAction("New Game") {
      public void actionPerformed(ActionEvent e) {
        controller.newGame();
      }
    });
    JMenuItem restart = new JMenuItem(new AbstractAction("Restart") {
      public void actionPerformed(ActionEvent e) {
        controller.restartGame();
      }
    });
    JMenuItem quit = new JMenuItem(new AbstractAction("Quit") {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    menu.add(restart);
    menu.add(newGame);
    menu.add(quit);
    menuBar.add(menu);

    top.add(menuBar, BorderLayout.PAGE_START);
    top.add(info, BorderLayout.CENTER);
    add(top);
    add(playerDescription);
    add(locationDescription);
  }

  public void setMoveResult(boolean result) {
      if (result) {
        if (model.getPlayerLocation().getMonster() != null) {
          if (model.getPlayerLocation().getMonster().getHealthPercentage() <= 0) {
            info.setText("<html>Good riddance!<br>  There is a dead Otyugh in this cave.</html>");
          } else if (model.getPlayerLocation().getMonster().getHealthPercentage() == 50) {
            info.setText("<html>Hurry up!<br>You just escape an injured Otyugh here.</html>");
          }
        } else {
          info.setText("Going good!!");
        }
        Font font = new Font("Arial", Font.BOLD, 18);
        info.setFont(font);
        info.setForeground(new Color(30, 141, 18));

      } else if (model.getPlayer().isDead()){
        info.setText("<html>Chomp, chomp, chomp, you are eaten by an Otyugh!<br>Better luck next "
                + "time</html>");
        info.setForeground(Color.RED);
      }
      if (model.isDestinationReached() && !model.getPlayer().isDead()) {
        info.setText("<html>Congratulations!! You won.<br>Destination Reached.</html>");
      }
  }

  public void setShootResult(boolean result) {
    if(result) {
      info.setText("You hear a great howl in the distance\n");
      Font font = new Font("Arial", Font.BOLD,18);
      info.setFont(font);
      info.setForeground(new Color(30, 141, 18));
    } else {
      info.setText("You shoot an arrow into the darkness\n");
      Font font = new Font("Arial", Font.BOLD,18);
      info.setFont(font);
      info.setForeground(Color.RED);
    }
  }

  public void setOutOfArrows() {
    info.setText("You are out of arrows, explore to find more\n");
    Font font = new Font("Arial", Font.BOLD,18);
    info.setFont(font);
    info.setForeground(new Color(44, 152, 185));
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

  private class PlayerDescription extends JPanel {

    private final JLabel rubies;
    private final JLabel sapphires;
    private final JLabel diamonds;
    private final JLabel arrows;

    public PlayerDescription() {
      setLayout(new GridLayout(2, 1));
      JLabel heading = new JLabel(model.getPlayer().getName() + "'s collected items:");
      Font font = new Font("Arial", Font.BOLD,16);
      heading.setFont(font);
      heading.setForeground(Color.WHITE);
      JPanel treasures = new JPanel();
      treasures.setBackground(Color.black);

      treasures.setLayout(new GridLayout(1, 4));
      rubies = new JLabel(model.getPlayer().getCollectedTreasures().get(Treasure.RUBY).toString());
      rubies.setIcon(ruby);
      rubies.setForeground(Color.WHITE);
      rubies.setBackground(Color.BLACK);

      sapphires = new JLabel(model.getPlayer().getCollectedTreasures().get(Treasure.SAPPHIRE)
              .toString());
      sapphires.setIcon(emerald);
      sapphires.setForeground(Color.WHITE);
      sapphires.setBackground(Color.BLACK);


      diamonds = new JLabel(model.getPlayer().getCollectedTreasures().get(Treasure.DIAMOND)
              .toString());
      diamonds.setIcon(diamond);
      diamonds.setForeground(Color.WHITE);
      diamonds.setBackground(Color.BLACK);


      arrows = new JLabel(String.valueOf(model.getPlayer().getNumberOfArrows()));
      arrows.setIcon(arrow);
      arrows.setForeground(Color.WHITE);
      arrows.setBackground(Color.BLACK);

      add(heading);
      treasures.add(rubies);
      treasures.add(sapphires);
      treasures.add(diamonds);
      treasures.add(arrows);
      add(treasures);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      rubies.setText(model.getPlayer().getCollectedTreasures().get(Treasure.RUBY).toString());
      sapphires.setText(model.getPlayer().getCollectedTreasures().get(Treasure.SAPPHIRE)
              .toString());
      diamonds.setText(model.getPlayer().getCollectedTreasures().get(Treasure.DIAMOND).toString());
      arrows.setText(String.valueOf(model.getPlayer().getNumberOfArrows()));
    }
  }

  private class LocationDescription extends JPanel {

    private final JLabel rubies;
    private final JLabel sapphires;
    private final JLabel diamonds;
    private final JLabel arrows;
    private final JLabel smell;

    public LocationDescription() {
      setLayout(new GridLayout(3, 1));
      JLabel heading = new JLabel("Current location description: ");
      Font font = new Font("Arial", Font.BOLD,16);
      heading.setFont(font);
      heading.setForeground(Color.WHITE);

      JPanel treasures = new JPanel();
      treasures.setLayout(new GridLayout(1, 4));
      treasures.setBackground(Color.black);
      rubies = new JLabel(String.valueOf(model.getPlayerLocation().getTreasures()
              .stream().filter(treasure -> treasure.equals(Treasure.RUBY)).count()));
      rubies.setIcon(ruby);
      rubies.setForeground(Color.WHITE);
      rubies.setBackground(Color.BLACK);

      sapphires = new JLabel(String.valueOf(model.getPlayerLocation().getTreasures()
              .stream().filter(treasure -> treasure.equals(Treasure.SAPPHIRE)).count()));
      sapphires.setIcon(emerald);
      sapphires.setForeground(Color.WHITE);
      sapphires.setBackground(Color.BLACK);

      diamonds = new JLabel(String.valueOf(model.getPlayerLocation().getTreasures()
              .stream().filter(treasure -> treasure.equals(Treasure.DIAMOND)).count()));
      diamonds.setIcon(diamond);
      diamonds.setForeground(Color.WHITE);
      diamonds.setBackground(Color.BLACK);

      arrows = new JLabel(String.valueOf((long) model.getPlayerLocation().getArrows().size()));
      arrows.setIcon(arrow);
      arrows.setForeground(Color.WHITE);
      arrows.setBackground(Color.BLACK);

      if (model.getPlayerLocation().getSmell() > 0) {
        if(model.getPlayerLocation().getSmell() == 1) {
          smell = new JLabel("Slightly pungent smell");
          smell.setIcon(lightSmell);
        } else {
          smell = new JLabel("Strongly pungent smell");
          smell.setIcon(strongSmell);
        }
      } else {
        smell = new JLabel("No pungent smell");
        smell.setIcon(noSmell);
      }
      smell.setForeground(Color.WHITE);

      arrows.setIcon(arrow);
      add(heading);
      treasures.add(rubies);
      treasures.add(sapphires);
      treasures.add(diamonds);
      treasures.add(arrows);
      add(treasures);
      add(smell);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      rubies.setText(String.valueOf(model.getPlayerLocation().getTreasures()
              .stream().filter(treasure -> treasure.equals(Treasure.RUBY)).count()));
      sapphires.setText(String.valueOf(model.getPlayerLocation().getTreasures()
              .stream().filter(treasure -> treasure.equals(Treasure.SAPPHIRE)).count()));
      diamonds.setText(String.valueOf(model.getPlayerLocation().getTreasures()
              .stream().filter(treasure -> treasure.equals(Treasure.DIAMOND)).count()));
      arrows.setText(String.valueOf((long) model.getPlayerLocation().getArrows().size()));
      if (model.getPlayerLocation().getSmell() > 0) {
        if(model.getPlayerLocation().getSmell() == 1) {
          smell.setText("Slightly pungent smell");
          smell.setIcon(lightSmell);
        } else {
          smell.setText("Strongly pungent smell");
          smell.setIcon(strongSmell);
        }
      } else {
        smell.setText("No pungent smell");
        smell.setIcon(noSmell);
      }
    }
  }
}


