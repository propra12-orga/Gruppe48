package Field;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		final JButton saveButton = new JButton("Save Map");
		JButton cancelButton = new JButton("Cancel");
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
		private ImageIcon wallBig = new ImageIcon(
				ClassLoader.getSystemResource("images/wallBig.png"));

		public CreationPanel(int x, int y, int w, int h) {
			super(null);
			setBounds(x, y, w, h);
			init();
		}

		private void init() {
			JPanel grid = new JPanel();
			grid.setLayout(new GridLayout(5, 5));
			//for (int i = 0; i < 25; i++) {
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(""));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.add(new JButton(wallBig));
			grid.setBounds(10, 10, 600, 600);
			buttons = new JPanel(new GridLayout(0, 2));
			buttons.setBounds(255, 640, 300, 40);
			buttons.add(saveButton);
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
