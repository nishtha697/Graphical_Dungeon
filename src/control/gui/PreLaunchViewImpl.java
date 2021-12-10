package control.gui;

import java.awt.GridLayout;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * This PreLaunchViewImpl class represents the view of the dungeon game constraints. It provides a
 * GUI for the dungeon model creation and takes input from user.
 */
public class PreLaunchViewImpl extends JFrame implements PreLaunchView {

  private final JTextField rows;
  private final JTextField cols;
  private final JTextField interconnectivity;
  private final JCheckBox isWrapping;
  private final JTextField nameOfPlayer;
  private final JTextField numberOfMonsters;
  private final JTextField percentageOfTreasuresAndArrows;
  private final JButton start;

  /**
   * Constructs the pre-launch view.
   */
  public PreLaunchViewImpl() {
    super("Dungeon Constraints");

    JPanel container = new JPanel();
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new GridLayout(7, 2));

    JLabel rowsL = new JLabel();
    rowsL.setText("Enter number of rows: ");
    rows = new JTextField(15);
    inputPanel.add(rowsL);
    inputPanel.add(rows);

    JLabel colsL = new JLabel();
    colsL.setText("Enter number of Columns: ");
    cols = new JTextField(15);
    inputPanel.add(colsL);
    inputPanel.add(cols);

    JLabel interconnectivityL = new JLabel();
    interconnectivityL.setText("Enter interconnectivity: ");
    interconnectivity = new JTextField(15);
    inputPanel.add(interconnectivityL);
    inputPanel.add(interconnectivity);

    JLabel isWrappingL = new JLabel();
    isWrappingL.setText("Check if dungeon is wrapping: ");
    isWrapping = new JCheckBox("Is Wrapping?");
    inputPanel.add(isWrappingL);
    inputPanel.add(isWrapping);

    JLabel nameOfPlayerL = new JLabel();
    nameOfPlayerL.setText("Enter name of the player: ");
    nameOfPlayer = new JTextField(15);
    inputPanel.add(nameOfPlayerL);
    inputPanel.add(nameOfPlayer);

    JLabel numberOfMonstersL = new JLabel();
    numberOfMonstersL.setText("Enter number of monsters: ");
    numberOfMonsters = new JTextField(15);
    inputPanel.add(numberOfMonstersL);
    inputPanel.add(numberOfMonsters);

    JLabel percentageOfTreasuresAndArrowsL = new JLabel();
    percentageOfTreasuresAndArrowsL.setText("Enter percentage of treasures and arrows: ");
    percentageOfTreasuresAndArrows = new JTextField(15);
    inputPanel.add(percentageOfTreasuresAndArrowsL);
    inputPanel.add(percentageOfTreasuresAndArrows);

    start = new JButton("Start game");
    container.add(inputPanel);
    container.add(start);

    add(container);

    this.setSize(600, 250);
    this.setLocation(300, 200);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  @Override
  public void setCommandButtonListener(ActionListener actionEvent) {
    start.addActionListener(actionEvent);
  }

  @Override
  public int getRows() {
    int row = 0;
    try {
      String command = this.rows.getText();
      this.rows.setText("");
      row = Integer.parseInt(command);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Invalid number for rows");
      e.printStackTrace();
    }
    return row;
  }

  @Override
  public int getCols() {
    int col = 0;
    try {
      String command = this.cols.getText();
      this.cols.setText("");
      col = Integer.parseInt(command);

    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Invalid number for columns");
      e.printStackTrace();
    }
    return col;
  }

  @Override
  public int getInterconnectivity() {
    int inter = 0;
    try {
      String command = this.interconnectivity.getText();
      this.interconnectivity.setText("");
      inter = Integer.parseInt(command);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
              "Invalid number for interconnectivity");
      e.printStackTrace();
    }
    return inter;
  }

  @Override
  public boolean getIsWrapping() {
    boolean selected = this.isWrapping.isSelected();
    this.isWrapping.setSelected(false);
    return selected;
  }

  @Override
  public String getNameOfPlayer() {
    String command = this.nameOfPlayer.getText();
    this.nameOfPlayer.setText("");
    return command;
  }

  @Override
  public int getNumberOfMonsters() {
    int monster = 0;
    try {
      String command = this.numberOfMonsters.getText();
      this.numberOfMonsters.setText("");
      monster = Integer.parseInt(command);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Invalid number for monsters");
      e.printStackTrace();
    }
    return monster;
  }

  @Override
  public double getPercentageOfTreasuresAndArrowsL() {
    double percentage = 0.0;
    try {
      String command = this.percentageOfTreasuresAndArrows.getText();
      this.percentageOfTreasuresAndArrows.setText("");
      percentage = Double.parseDouble(command);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
              "Invalid percentage of treasures and arrows");
      e.printStackTrace();
    }
    return percentage;
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void close() {
    this.setVisible(false);
  }

}
