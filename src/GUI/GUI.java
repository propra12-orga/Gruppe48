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
import javax.swing.filechooser.FileFilter;

import Engine.Game;
import Engine.GameStates;
import Field.Field;
import Field.FieldGenerator;

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
	private JMenuItem multiplayer;
	private JMenuItem singleplayer;
	private JMenuItem quitItem;
	private JMenuBar menu;
	private JMenu gameMenu;
	private FieldGenerator readMap;
	private Game mainGame;

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
		menu = new JMenuBar();
		gameMenu = new JMenu("Game");
		startItem = new JMenuItem("New Game");
		startItem.addActionListener(this);
		openItem = new JMenuItem("Open Map");
		openItem.addActionListener(this);
		multiplayer = new JMenuItem("2 Player");
		multiplayer.addActionListener(this);
		singleplayer = new JMenuItem("1 Player");
		singleplayer.addActionListener(this);
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		gameMenu.add(startItem);
		gameMenu.add(openItem);
		gameMenu.add(singleplayer);
		gameMenu.add(multiplayer);
		gameMenu.add(quitItem);
		menu.add(gameMenu);
		this.add(menu, BorderLayout.NORTH);
		this.setJMenuBar(menu);
		this.add(panel);
		this.addKeyListener(this);
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

	@Override
	/**
	 * Es werden die Aktionen der jeweiligen Menüeinträge(Game)
	 * festgesetzt(Starten, Neustarten, Beenden)
	 */
	public void actionPerformed(ActionEvent object) {
		if (object.getSource() == startItem) {
			mainGame.key = 0;
			mainGame.restart(Game.createNewField());
			this.setSize((gameField.getMap().length) * 32 + 5,
					(gameField.getMap()[0].length + 1) * 32 + 18);
		}
		if (object.getSource() == openItem) {
			System.out.println("öffnen wurde angeklickt");
			open();
			this.setSize((gameField.getMap().length) * 32 + 5,
					(gameField.getMap()[0].length + 1) * 32 + 18);
		}
		if (object.getSource() == quitItem) {
			System.exit(0);
		}
		if (object.getSource() == multiplayer) {
			mainGame.key = 0;
			mainGame.gameState = GameStates.TWOPLAYER;
		}
		if (object.getSource() == singleplayer) {
			mainGame.key = 0;
			mainGame.gameState = GameStates.ONEPLAYER;
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

	public void open() {
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
			String path = file.getAbsolutePath();
			System.out.println(path);
			mainGame.restart(Game.createNewField(file.getAbsolutePath()));
		} else {
			showError("Nichts ausgewählt");
		}
	}
}
