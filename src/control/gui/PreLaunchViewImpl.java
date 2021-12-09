package control.gui;

import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.WindowConstants;

/**
 * This PreLaunchViewImpl class represents the view of the dungeon game constraints. It provides a
 * GUI for the dungeon model creation and takes input from user.
 */
public class PreLaunchViewImpl extends JFrame implements PreLaunchView {

  private final JTextField rows;
  private final JTextField cols;
  private final JTextField interconnectivity;
  private final JTextField isWrapping;
  private final JTextField nameOfPlayer;
  private final JTextField numberOfMonsters;
  private final JTextField percentageOfTreasuresAndArrows;
  private final JButton start;
  private final boolean inTesting;

  /**
   * Constructs the pre-launch view.
   */
  public PreLaunchViewImpl(boolean inTesting) {
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
    isWrappingL.setText("Enter 'yes' if dungeon is wrapping: ");
    isWrapping = new JTextField(15);
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

    this.inTesting = inTesting;

    this.setSize(600, 250);
    this.setLocation(300,200);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  @Override
  public void setCommandButtonListener(ActionListener actionEvent) {
    start.addActionListener(actionEvent);
  }

  @Override
  public int getRows() {
    String command = this.rows.getText();
    this.rows.setText("");
    return Integer.parseInt(command);
  }

  @Override
  public int getCols() {
    String command = this.cols.getText();
    this.cols.setText("");
    return Integer.parseInt(command);
  }

  @Override
  public int getInterconnectivity() {
    String command = this.interconnectivity.getText();
    this.interconnectivity.setText("");
    return Integer.parseInt(command);
  }

  @Override
  public boolean getIsWrapping() {
    String command = this.isWrapping.getText();
    this.isWrapping.setText("");
    return command.equalsIgnoreCase("YES");
  }

  @Override
  public String getNameOfPlayer() {
    String command = this.nameOfPlayer.getText();
    this.nameOfPlayer.setText("");
    return command;
  }

  @Override
  public int getNumberOfMonsters() {
    String command = this.numberOfMonsters.getText();
    this.numberOfMonsters.setText("");
    return Integer.parseInt(command);
  }

  @Override
  public double getPercentageOfTreasuresAndArrowsL() {
    String command = this.percentageOfTreasuresAndArrows.getText();
    this.percentageOfTreasuresAndArrows.setText("");
    return Double.parseDouble(command);
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
