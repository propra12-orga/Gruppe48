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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Die Klasse MapCreator stellt dem Benutzer eine Schnittstelle sowie einige
 * Werkzeuge zu Verfügung um mit geringem Aufwand selber Spielfelder zu erzeugen
 * Sie erzeugt ihr eigenes Fenster
 * 
 * @author Martin
 */
public class MapCreator extends JFrame implements WindowListener,
		ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Ob etwas an der Map geaendert wurde
	 */
	private boolean savedMap = true;
	/**
	 * Das Array in dem die Map gespeichert wird und anschließend in einem Text
	 * Dokument gespeichert wird
	 */
	public int[][] array = new int[GUI.GUI.zahl + 1][GUI.GUI.zahl + 1];

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
		private int HIDDENEXIT = 4;
		private int PLAYER = 5;
		private int STONE = 6;
		private int SELECTED = 1;
		private JButton currentlySelected;
		private JButton picStone;
		private JButton picWall;
		private JButton picFree;
		private JButton picExit;
		private JButton picHiddenExit;
		private JButton picPlayer;
		private JPanel buttons;
		private ImageIcon player = new ImageIcon(
				ClassLoader.getSystemResource("images/player.png"));
		private ImageIcon exit = new ImageIcon(
				ClassLoader.getSystemResource("images/exit.png"));
		private ImageIcon hiddenExit = new ImageIcon(
				ClassLoader.getSystemResource("images/stone.png"));
		private ImageIcon stone = new ImageIcon(
				ClassLoader.getSystemResource("images/stone.png"));
		private ImageIcon wall = new ImageIcon(
				ClassLoader.getSystemResource("images/wall.png"));
		private ImageIcon free = new ImageIcon(
				ClassLoader.getSystemResource("images/free.png"));
		private ImageIcon selected = free;

		/**
		 * Erzeugt ein Objekt der Klasse CreationPanel und definiert die Stelle
		 * sowie Groesse des Panels und alle Elemente die es beeinhalten soll
		 * 
		 * @param x
		 *            Die neue X-Koordinate an welcher das Panel gesetzt werden
		 *            soll
		 * @param y
		 *            Die neue Y-Koordinate an welcher das Panel gesetzt werden
		 *            soll
		 * @param w
		 *            Die Breite des Panels
		 * @param h
		 *            Die Hoehe des Panels
		 */
		public CreationPanel(int x, int y, int w, int h) {
			super(null);
			setBounds(x, y, w, h);
			init();
		}

		/**
		 * Hier werden alle Elemente des CreationPanels definiert, d.h. die Map
		 * bestehend aus vielen Buttons, die drei Buttons(save,help,cancel) am
		 * unteren Ende des Panels sowie die 6 verschiedenen Buttons die
		 * bestimmen welches Element auf die Karte gezeichnet wird
		 */
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
			// Die Buttons am unteren Ende des Panels
			buttons = new JPanel(new GridLayout(0, 3));
			buttons.setBounds(255, 640, 300, 40);
			buttons.add(saveButton);
			buttons.add(helpButton);
			buttons.add(cancelButton);
			buttons.setBorder(new BevelBorder(BevelBorder.RAISED));
			JLabel text = new JLabel("Currently Selected");
			text.setBounds(670, 5, 130, 15);
			// Der Button der anzeigt welches Element zum zeichnen grade
			// ausgewaehlt wurde
			currentlySelected = new JButton();
			currentlySelected.setBounds(710, 25, 32, 32);
			currentlySelected.setIcon(selected);
			currentlySelected.setContentAreaFilled(false);
			currentlySelected.setFocusable(false);
			// Der Button fuer die zerstoerbaren Bloecke
			picStone = new JButton(stone);
			picStone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = stone;
					SELECTED = STONE;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picStone.setBounds(700, 90, 53, 60);
			picStone.setContentAreaFilled(false);
			TitledBorder modusStone = null;
			modusStone = BorderFactory.createTitledBorder(modusStone, "Stone",
					TitledBorder.CENTER, TitledBorder.ABOVE_BOTTOM);
			modusStone.setTitleColor(Color.black);
			picStone.setBorder(modusStone);
			// Der Button fuer die unzerstoerbaren Waende
			picWall = new JButton(wall);
			picWall.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = wall;
					SELECTED = WALL;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picWall.setBounds(700, 180, 53, 60);
			picWall.setContentAreaFilled(false);
			TitledBorder modusWall = null;
			modusWall = BorderFactory.createTitledBorder(modusWall, "Wall",
					TitledBorder.CENTER, TitledBorder.ABOVE_BOTTOM);
			modusWall.setTitleColor(Color.black);
			picWall.setBorder(modusWall);
			// Der Button fuer den freien Platz
			picFree = new JButton(free);
			picFree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = free;
					SELECTED = FREE;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picFree.setBounds(700, 270, 53, 60);
			picFree.setContentAreaFilled(false);
			TitledBorder modusFree = null;
			modusFree = BorderFactory.createTitledBorder(modusFree, "Free",
					TitledBorder.CENTER, TitledBorder.ABOVE_BOTTOM);
			modusFree.setTitleColor(Color.black);
			picFree.setBorder(modusFree);
			// Der Button fuer den Ausgang
			picExit = new JButton(exit);
			picExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = exit;
					SELECTED = EXIT;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picExit.setBounds(700, 360, 53, 60);
			picExit.setContentAreaFilled(false);
			TitledBorder modusExit = null;
			modusExit = BorderFactory.createTitledBorder(modusExit, "Exit",
					TitledBorder.CENTER, TitledBorder.ABOVE_BOTTOM);
			modusExit.setTitleColor(Color.black);
			picExit.setBorder(modusExit);
			// Der Button fuer den versteckten Ausgang
			picHiddenExit = new JButton(hiddenExit);
			picHiddenExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = hiddenExit;
					SELECTED = HIDDENEXIT;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picHiddenExit.setBounds(700, 450, 55, 60);
			picHiddenExit.setContentAreaFilled(false);
			JLabel text1 = new JLabel("Hidden");
			text1.setBounds(708, 438, 130, 15);
			TitledBorder modusHiddenExit = null;
			modusHiddenExit = BorderFactory.createTitledBorder(modusHiddenExit,
					"Exit", TitledBorder.CENTER, TitledBorder.ABOVE_BOTTOM);
			modusHiddenExit.setTitleColor(Color.black);
			picHiddenExit.setBorder(modusHiddenExit);
			// Der Button fuer den Spieler
			picPlayer = new JButton(player);
			picPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = player;
					SELECTED = PLAYER;
					currentlySelected.setIcon(selected);
					currentlySelected.repaint();
				}
			});
			picPlayer.setBounds(700, 540, 53, 60);
			picPlayer.setContentAreaFilled(false);
			TitledBorder modusPlayer = null;
			modusPlayer = BorderFactory.createTitledBorder(modusPlayer,
					"Player", TitledBorder.CENTER, TitledBorder.ABOVE_BOTTOM);
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
			add(text1);
			add(picHiddenExit);
			add(picPlayer);
			saveButton.addActionListener(new ActionListener() {
				/**
				 * Beim Klick auf den Save Button wird gefragt unter welchen
				 * Namen und wo die Map gespeichert werden soll
				 */
				public void actionPerformed(ActionEvent e) {
					save();
				}

			});
			cancelButton.addActionListener(new ActionListener() {
				/**
				 * Beim Klick auf den Cancel Button wird das Fenster geschlossen
				 * falls nichts veraendert wurde, wurde etwas veraendert wird
				 * abgefragt ob der Benutzer wirklich abbrechen will
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
											+ " by clicking on the Save Map button."
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

	/**
	 * Hier wird die Breite der Map berechnet, basierend auf der Pixelgroesse
	 * der Bilder
	 * 
	 * @param x
	 *            Die Anzahl der Felder die das Array haben soll
	 * @return Die Breite die die Map in dem MapCreator haben wird
	 */
	public int setArraySize(int x) {
		x = x * 32;
		return x;

	}

	/**
	 * Hier wird die Map gespeichert, erst oeffnet sich ein Fenster das abfragt
	 * wo und unter welchen Namen die selbst erstellte Map gespeichert werden
	 * soll, danach wenn auf Save gedrueckt wurde betrachtet die Methode alle
	 * Felder des Arrays und fuer jede 1 wird ein . in das .txt Dokument
	 * geschrieben, 2->*,3->E,4->H,5->P,6->%, so dass die Map einfach vom
	 * eigenen Parser gelesen werden kann
	 */
	private void save() {
		try {
			int playerCount = 0;
			int exitCount = 0;
			String f = "savedMap.txt";
			File file1 = new File(f);
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					".txt", "txt");
			chooser.setFileFilter(filter);
			chooser.setSelectedFile(file1);
			// Show the dialog; wait until dialog is closed
			int result = chooser.showSaveDialog(null);
			// Determine which button was clicked to close the dialog
			switch (result) {
			case JFileChooser.CANCEL_OPTION:
				// Cancel or the close-dialog icon was clicked
				break;
			case JFileChooser.ERROR_OPTION:
				// The selection process did not complete successfully
				JOptionPane.showMessageDialog(rootPane, "ERROR", "Error", 1);
				break;
			case JFileChooser.APPROVE_OPTION:
				// Approve (Save) was clicked
				File file = chooser.getSelectedFile();
				if (file.exists()) {
					int option = JOptionPane.showConfirmDialog(null,
							"Do you want to overwrite " + file.getName() + "?",
							"Caution", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (option == JOptionPane.NO_OPTION)
						// abort
						return;
					if (option == JOptionPane.CANCEL_OPTION)
						// abort
						return;
					if (option == JOptionPane.YES_OPTION) {
						FileWriter writer = null;
						writer = new FileWriter(file.getPath(), false);
						PrintWriter printer = new PrintWriter(writer);
						for (int i = 0; i < GUI.GUI.zahl; i++) {
							for (int j = 0; j < GUI.GUI.zahl; j++) {
								if (array[i][j] == 1) { // schreibt die freien
														// Plaetze in das txt
														// Dokument
									printer.print(".");
								} else if (array[i][j] == 2) {// schreibt die
																// aueßeren
																// Waende in das
																// txt Dokument
									printer.print("*");
								} else if (array[i][j] == 3) {// schreibt den
																// Ausgang in
																// das txt
																// Dokument
									printer.print("E");
									exitCount++;
								} else if (array[i][j] == 4) {// schreibt den
																// Ausgang in
																// das txt
																// Dokument
									printer.print("H");
									exitCount++;
								} else if (array[i][j] == 5) {// schreibt die
																// Spielerposition
																// in das txt
																// Dokument
									printer.print("P");
									playerCount++;
								} else if (array[i][j] == 6) {// schreibt die
																// zerstoerbaren
																// Bloecke in
																// das txt
																// Dokument
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
							JOptionPane.showMessageDialog(rootPane,
									"Map saved successfully", "Success", 1);
							savedMap = true;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {

		}
	}

	/**
	 * Prueft ob etwas an der eigenen Map veraendert worden ist, wenn nicht wird
	 * das Fenster einfach geschlossen, wurde etwas veraendert wird erst
	 * abgefragt ob der Benutzer sicher ist
	 */
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
