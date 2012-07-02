package Field;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Martin Die Klasse MapCreator stellt dem Benutzer eine Schnittstelle
 *         sowie einige Werkzeuge zu Verfügung um mit geringem Aufwand selber
 *         Spielfelder zu erzeugen Sie erzeugt ihr eigenes Fenster
 */
public class MapCreator extends JFrame implements WindowListener,
		ActionListener {
	private static final long serialVersionUID = 1L;
	private boolean savedMap = true;
	public int[][] array = new int[GUI.GUI.zahl + 1][GUI.GUI.zahl + 1];
	public int height;
	public int length;

	/**
	 * Erzeugt ein Objekt der Klasse MapCreator Das benutzte Fenster wird mit
	 * einer Groesse von 800x720 Pixeln initialisiert, ist sichtbar und laesst
	 * seine Größe nicht verändern
	 */
	public MapCreator() {
		super("Create your OWN Map");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(800, 720);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width / 2 - (getWidth() / 2);
		int y = dim.height / 2 - (getHeight() / 2);
		setLocation(x, y);
		setResizable(false);
		addWindowListener(this);
		add(new CreationPanel(0, 0, 400, 150));
	}

	/**
	 * Erzeugt ein Panel im Fenster von MapCreator, über welches sämtliche
	 * Schaltflächen verfügbar gemacht werden
	 * 
	 * @author Martin
	 * 
	 */
	public class CreationPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		JButton saveButton = new JButton("Save Map");
		JButton cancelButton = new JButton("Cancel");
		JButton helpButton = new JButton("Help");
		private int FREE = 1;
		private int WALL = 2;
		private int EXIT = 3;
		private int PLAYER = 5;
		private int STONE = 6;
		private int SELECTED = 1;
		private JButton currentlySelected;
		private JButton picStone;
		private JButton picWall;
		private JButton picFree;
		private JButton picExit;
		private JButton picPlayer;
		private JPanel buttons;
		private ImageIcon player = new ImageIcon(
				ClassLoader.getSystemResource("images/player.png"));
		private ImageIcon exit = new ImageIcon(
				ClassLoader.getSystemResource("images/exit.png"));
		private ImageIcon stone = new ImageIcon(
				ClassLoader.getSystemResource("images/stone.png"));
		private ImageIcon wall = new ImageIcon(
				ClassLoader.getSystemResource("images/wall.png"));
		private ImageIcon free = new ImageIcon(
				ClassLoader.getSystemResource("images/free.png"));
		private ImageIcon selected = free;

		public CreationPanel(int x, int y, int w, int h) {
			super(null);
			setBounds(x, y, w, h);
			init();
		}

		private void init() {
			JPanel grid = new JPanel();
			int mapSize = GUI.GUI.zahl;
			grid.setLayout(new GridLayout(mapSize, mapSize));
			for (int i = 0; i < mapSize; i++) {
				grid.add(new JButton(wall));
				array[0][i] = WALL;
			}
			for (int i = 1; i < mapSize - 1; i++) {
				array[i][0] = WALL;
				grid.add(new JButton(wall));
				for (int j = 1; j < mapSize - 1; j++) {
					grid.add(new JButton(free)).addFocusListener(
							new FocusListener() {
								@Override
								public void focusGained(FocusEvent arg0) {
									((AbstractButton) arg0.getComponent())
											.setIcon(selected);
									array[arg0.getComponent().getY() / 32][arg0
											.getComponent().getX() / 32] = SELECTED;
									savedMap = false;
									return;
								}

								@Override
								public void focusLost(FocusEvent arg0) {
									arg0.getComponent().repaint();
								}
							});
					array[i][j] = FREE;
				}
				array[i][mapSize - 1] = WALL;
				grid.add(new JButton(wall));
			}
			for (int i = 0; i < mapSize; i++) {
				grid.add(new JButton(wall));
				array[mapSize - 1][i] = WALL;
			}
			grid.setBounds(10, 10, setArraySize(mapSize), setArraySize(mapSize));
			buttons = new JPanel(new GridLayout(0, 3));
			buttons.setBounds(255, 640, 300, 40);
			buttons.add(saveButton);
			buttons.add(helpButton);
			buttons.add(cancelButton);
			buttons.setBorder(new BevelBorder(BevelBorder.RAISED));
			JLabel text = new JLabel("Currently Selected");
			text.setBounds(670, 5, 130, 15);
			currentlySelected = new JButton();
			currentlySelected.setBounds(710, 25, 32, 32);
			currentlySelected.setIcon(selected);
			currentlySelected.setContentAreaFilled(false);
			currentlySelected.setFocusable(false);
			picStone = new JButton(stone);
			picStone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = stone;
					SELECTED = STONE;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picStone.setBounds(700, 100, 53, 50);
			picStone.setContentAreaFilled(false);
			TitledBorder modusStone = null;
			modusStone = BorderFactory.createTitledBorder(modusStone, "Stone",
					TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
			modusStone.setTitleColor(Color.black);
			picStone.setBorder(modusStone);
			picWall = new JButton(wall);
			picWall.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = wall;
					SELECTED = WALL;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picWall.setBounds(700, 200, 53, 50);
			picWall.setContentAreaFilled(false);
			TitledBorder modusWall = null;
			modusWall = BorderFactory.createTitledBorder(modusWall, "Wall",
					TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
			modusWall.setTitleColor(Color.black);
			picWall.setBorder(modusWall);
			picFree = new JButton(free);
			picFree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = free;
					SELECTED = FREE;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picFree.setBounds(700, 300, 53, 50);
			picFree.setContentAreaFilled(false);
			TitledBorder modusFree = null;
			modusFree = BorderFactory.createTitledBorder(modusFree, "Free",
					TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
			modusFree.setTitleColor(Color.black);
			picFree.setBorder(modusFree);
			picExit = new JButton(exit);
			picExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = exit;
					SELECTED = EXIT;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picExit.setBounds(700, 400, 53, 50);
			picExit.setContentAreaFilled(false);
			TitledBorder modusExit = null;
			modusExit = BorderFactory.createTitledBorder(modusExit, "Exit",
					TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
			modusExit.setTitleColor(Color.black);
			picExit.setBorder(modusExit);
			picPlayer = new JButton(player);
			picPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = player;
					SELECTED = PLAYER;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picPlayer.setBounds(700, 500, 53, 50);
			picPlayer.setContentAreaFilled(false);
			TitledBorder modusPlayer = null;
			modusPlayer = BorderFactory.createTitledBorder(modusPlayer,
					"Player", TitledBorder.CENTER,
					TitledBorder.DEFAULT_POSITION);
			modusPlayer.setTitleColor(Color.black);
			picPlayer.setBorder(modusPlayer);
			add(grid);
			add(buttons);
			add(text);
			add(currentlySelected);
			add(picStone);
			add(picWall);
			add(picFree);
			add(picExit);
			add(picPlayer);
			saveButton.addActionListener(new ActionListener() {
				/**
				 * Beim Klick auf den Save Button wird die Map gespeichert und
				 * als .txt in den maps Ordner hinterlegt
				 */
				public void actionPerformed(ActionEvent e) {
					save();
				}

			});
			cancelButton.addActionListener(new ActionListener() {
				/**
				 * Beim Klick auf den Cancel Button wird das Fenster geschlossen
				 * ohne zu speichern
				 */
				public void actionPerformed(ActionEvent e) {
					checkCancelWithoutSave();
				}

			});
			helpButton.addActionListener(new ActionListener() {
				/**
				 * Beim Klick auf den Help Button wird eine Erklärung des
				 * MapCreators angezeigt
				 */
				public void actionPerformed(ActionEvent e) {
					JOptionPane
							.showMessageDialog(
									rootPane,
									" You need to click on the element "
											+ "you want to place on the right side of your screen"
											+ " \n and then afterwards choose where the element "
											+ "shall be placed in the field on the left side \n by "
											+ "simply clicking on it."
											+ " \n When you are finished you need to save the Map"
											+ " by clicking on the Save Map button. \n It will "
											+ "be saved in the maps folder of your game. "
											+ "\n If you want to play your created Map you will"
											+ " have to open it by clicking on Game and then "
											+ "Open Map. \n \n"
											+ "CAUTION: Your own Map will run through our "
											+ "tests of playability, so be sure it can be played!",
									"Explanation for the MapCreator", 1);
				}

			});

		}
	}

	public int setArraySize(int x) {
		x = x * 32;
		return x;

	}

	private void save() {
		try {
			int playerCount = 0;
			int exitCount = 0;
			File file = new File("src/maps/savedMap.txt");
			FileWriter writer = null;
			writer = new FileWriter(file.getPath(), false);//fuer weitere optionen zum 
			//speichern von mehreren maps parallel auf true setzen, und dann den dateinamen aendern
			PrintWriter printer = new PrintWriter(writer);
			for (int i = 0; i < GUI.GUI.zahl; i++) {
				for (int j = 0; j < GUI.GUI.zahl; j++) {
					if (array[i][j] == 1) { //schreibt die freien Plaetze in das txt Dokument
						printer.print(".");
					} else if (array[i][j] == 2) {//schreibt die aueßeren Waende in das txt Dokument
						printer.print("*");
					} else if (array[i][j] == 3) {//schreibt den Ausgang in das txt Dokument
						printer.print("E");
						exitCount++;
					} else if (array[i][j] == 5) {//schreibt die Spielerposition in das txt Dokument
						printer.print("P");
						playerCount++;
					} else if (array[i][j] == 6) {//schreibt die zerstoerbaren Bloecke in das txt Dokument
						printer.print("%");
					}
				}
				printer.println();
			}
			if (playerCount == 0 | playerCount > 2 | exitCount == 0
					| exitCount > 1) {
				JOptionPane
						.showMessageDialog(
								rootPane,
								"Check the amount of Exit(s)/Player(s), a Problem was detected!",
								"Error", 1);
				return;
			}
			writer.flush();
			writer.close();
			printer.flush();
			printer.close();
			if (file.exists()) {
				System.out.println("Map saved successfully!");
				JOptionPane.showMessageDialog(rootPane,
						"Map saved successfully", "Success", 1);
				savedMap = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkCancelWithoutSave() {
		if (!savedMap) {
			String yesNoOptions[] = { "Yes", "No" };
			int n = JOptionPane.showOptionDialog(null,
					"Exit without saving Changes?", "Cancel",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, yesNoOptions, yesNoOptions[0]);
			if (n == JOptionPane.YES_OPTION) {
				dispose();
			}
		} else {
			dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
