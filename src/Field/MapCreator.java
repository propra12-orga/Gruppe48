package Field;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
 * @author Alexander Die Klasse MapCreator stellt dem Benutzer eine
 *         Schnittstelle sowie einige Werkzeuge zu Verfügung um mit geringem
 *         Aufwand selber Spielfelder zu erzeugen Sie erzeugt ihr eigenes
 *         Fenster
 */
public class MapCreator extends JFrame implements WindowListener,
		ActionListener {
	private static final long serialVersionUID = 1L;
	private boolean savedMap = true;
	public int[][] Grid;
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
	 * @author Alexander
	 * 
	 */
	public class CreationPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		JButton saveButton = new JButton("Save Map");
		JButton cancelButton = new JButton("Cancel");
		JButton helpButton = new JButton("Help");
		private JLabel picStone;
		private JLabel picWall;
		private JLabel picFree;
		private JLabel picExit;
		private JLabel picPlayer;
		private JPanel stonePic;
		private JPanel wallPic;
		private JPanel freePic;
		private JPanel exitPic;
		private JPanel playerPic;
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

		public CreationPanel(int x, int y, int w, int h) {
			super(null);
			setBounds(x, y, w, h);
			init();
		}

		private void init() {
			JPanel grid = new JPanel();
			grid.setLayout(new GridLayout(5, 5));
			//for (int i = 0; i < 25; i++) {
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.add(new JButton(wall));
			grid.setBounds(10, 10, setArraySize(5), setArraySize(5));
			buttons = new JPanel(new GridLayout(0, 3));
			buttons.setBounds(255, 640, 300, 40);
			buttons.add(saveButton);
			buttons.add(helpButton);
			buttons.add(cancelButton);
			buttons.setBorder(new BevelBorder(BevelBorder.RAISED));
			stonePic = new JPanel();
			stonePic.setBounds(700, 100, 53, 65);
			picStone = new JLabel(stone);
			stonePic.add(picStone);
			TitledBorder modusStone;
			modusStone = BorderFactory.createTitledBorder("Stone");
			modusStone.setTitleColor(Color.black);
			stonePic.setBorder(modusStone);
			wallPic = new JPanel();
			wallPic.setBounds(700, 200, 53, 65);
			picWall = new JLabel(wall);
			wallPic.add(picWall);
			TitledBorder modusWall;
			modusWall = BorderFactory.createTitledBorder("Wall");
			modusWall.setTitleColor(Color.black);
			wallPic.setBorder(modusWall);
			freePic = new JPanel();
			freePic.setBounds(700, 300, 53, 65);
			picFree = new JLabel(free);
			freePic.add(picFree);
			TitledBorder modusFree;
			modusFree = BorderFactory.createTitledBorder("Free");
			modusFree.setTitleColor(Color.black);
			freePic.setBorder(modusFree);
			exitPic = new JPanel();
			exitPic.setBounds(700, 400, 53, 65);
			picExit = new JLabel(exit);
			exitPic.add(picExit);
			TitledBorder modusExit;
			modusExit = BorderFactory.createTitledBorder("Exit");
			modusExit.setTitleColor(Color.black);
			exitPic.setBorder(modusExit);
			playerPic = new JPanel();
			playerPic.setBounds(700, 500, 53, 65);
			picPlayer = new JLabel(player);
			playerPic.add(picPlayer);
			TitledBorder modusPlayer;
			modusPlayer = BorderFactory.createTitledBorder("Player");
			modusPlayer.setTitleColor(Color.black);
			playerPic.setBorder(modusPlayer);
			add(grid);
			add(buttons);
			add(stonePic);
			add(wallPic);
			add(freePic);
			add(exitPic);
			add(playerPic);
			saveButton.addActionListener(new ActionListener() {
				/**
				 * Beim Klick auf den Save Button wird die Map gespeichert und
				 * als .txt in den maps Ordner hinterlegt
				 */
				public void actionPerformed(ActionEvent e) {
					save();
					JOptionPane.showMessageDialog(rootPane,
							"Map wurde erfolgreich gespeichert", "Success", 1);
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
									" You need to click on the position "
											+ "in the array on the left side of your screen"
											+ " \n and then afterwards choose which element "
											+ "you want to put in your chosen field \n by "
											+ "simply clicking on the symbol at the right."
											+ " \n You need to fill every field of the array to "
											+ "make it work correctly! \n"
											+ " When you are finished you need to save the Map"
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
			File file = new File("src/maps/savedMap.txt");
			FileWriter writer = null;
			writer = new FileWriter(file.getPath(), false);//fuer weitere optionen zum 
			//speichern von mehreren maps parallel auf true setzen, und dann den dateinamen aendern
			PrintWriter printer = new PrintWriter(writer);
			printer.println("Hier kommt dann die Map rein mit den festgelegten zeichen!");
			writer.flush();
			writer.close();
			printer.flush();
			printer.close();
			if (file.exists()) {
				System.out.println("Map wurde erfolgreich gespeichert!");
				savedMap = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkCancelWithoutSave() {
		if (!savedMap) {
			String yesNoOptions[] = { "Ja", "Nein" };
			int n = JOptionPane.showOptionDialog(null,
					"Abbrechen ohne zu speichern ?", "Abbrechen",
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
