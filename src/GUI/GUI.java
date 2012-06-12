package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileFilter;

import Engine.Game;
import Field.Field;
import Field.FieldGenerator;
import Options.OptionFrame;

/**
 * GUI.java
 * 
 * @author cst
 * 
 */
public class GUI extends JFrame implements ActionListener, KeyListener {

	private int w = 0;
	private int h = 0;
	Field gameField;
	BufferedImage imgExit;
	BufferedImage imgWall;
	BufferedImage imgFree;
	public BoardPanel panel;

	/**
	 * Menüelemente
	 */

	private JMenuItem startItem;
	private JMenuItem openItem;
	private JRadioButtonMenuItem multiplayer;
	private JRadioButtonMenuItem singleplayer;
	private JMenuItem quitItem;
	private JMenuBar menubar;
	private JMenu gameMenu;
	private FieldGenerator readMap;
	private Game mainGame;
	private JMenuItem optionItem;

	public GUI(Game game) {

		/**
		 * neues Panel
		 */

		setFocusable(true);
		panel = new BoardPanel();
		mainGame = game;
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(300, 200));
		/**
		 * Menüleiste mit den Elementen:
		 * 
		 * Game: Start Game 2 Player Reset Quit
		 */
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		gameMenu = new JMenu("Game");
		JMenu optionMenu = new JMenu("Options");
		startItem = new JMenuItem("New Game");
		startItem.addActionListener(this);
		openItem = new JMenuItem("Open Map");
		openItem.addActionListener(this);
		multiplayer = new JRadioButtonMenuItem("2 Player");
		multiplayer.addActionListener(this);
		singleplayer = new JRadioButtonMenuItem("1 Player");
		singleplayer.addActionListener(this);
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		gameMenu.add(startItem);
		gameMenu.add(openItem);
		gameMenu.add(singleplayer);
		gameMenu.add(multiplayer);
		gameMenu.add(quitItem);
		menubar.add(gameMenu);
		menubar.add(optionMenu);
		optionItem = new JMenuItem("GameOptions");
		optionItem.addActionListener(this);
		optionMenu.add(optionItem);
		this.add(menubar, BorderLayout.NORTH);
		this.setJMenuBar(menubar);
		this.add(panel);
		this.addKeyListener(this);
		singleplayer.setSelected(true);
		/*
		 * AbstractAction optionAction = new AbstractAction("GameOptions") {
		 * 
		 * /**
		 * 
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { // TODO
		 * Auto-generated method stub System.exit(0); } };
		 * 
		 * optionMenu.add(optionAction);
		 */
	}

	public void insertField(Field field) {
		gameField = field;
		panel.insertField(field);
	}

	public int getBoardWidth() {
		return gameField.getMap().length;
	}

	public int getBoardHeight() {
		return gameField.getMap()[0].length;
	}

	public void showError(String sError) {
		JOptionPane.showMessageDialog(null, sError);
	}

	public void resize() {
		this.setSize((gameField.getMap().length) * 32 + 5,
				(gameField.getMap()[0].length + 1) * 32 + 18);
	}

	@Override
	/**
	 * Es werden die Aktionen der jeweiligen Menüeinträge(Game)
	 * festgesetzt(Starten, Neustarten, Beenden)
	 */
	public void actionPerformed(ActionEvent object) {
		if (object.getSource() == startItem) {
			mainGame.key = 0;
			mainGame.setMapLoaded(false);
			mainGame.restart();
			resize();
		}
		if (object.getSource() == openItem) {
			if (open()) {
				resize();
			}
		}
		if (object.getSource() == quitItem) {
			System.exit(0);
		}
		if (object.getSource() == multiplayer) {
			mainGame.key = 0;
			mainGame.setPlayerCount(2);
			singleplayer.setSelected(false);
		}
		if (object.getSource() == singleplayer) {
			mainGame.key = 0;
			mainGame.setPlayerCount(1);
			multiplayer.setSelected(false);
		}
		if (object.getSource() == optionItem) {
			new OptionFrame();
		}
	}

	@Override
	/**
	 * Methode,die aufgerufen wird, wenn die Taste gedrückt wird
	 */
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// mainGame.key = e.getKeyChar();
	}

	@Override
	/**
	 * wird ausgeführt, wenn die Taste innerhalb eines kurzen
	 * Zeitraums gedrückt und losgelassen wird
	 */
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * wird ausgeführt, wenn die Taste losgelassen wird
	 */
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		mainGame.key = e.getKeyChar();
	}

	public boolean open() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".txt")
						|| f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Maps";
			}
		});
		int iOpened = fileChooser.showOpenDialog(null);
		if (iOpened == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			mainGame.setMapPath(file.getAbsolutePath());
			mainGame.setMapLoaded(true);
			return mainGame.restart();
		} else {
			showError("Nichts ausgewählt");
			return false;
		}
	}
}
