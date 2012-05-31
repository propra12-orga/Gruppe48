package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Engine.Game;
import Engine.GameStates;
import Field.Field;
import Objects.Player;

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
	private JMenuItem multiplayer;
	private JMenuItem restartItem;
	private JMenuItem quitItem;
	private JMenuBar menu;
	private JMenu gameMenu;

	private Game mainGame;

	public GUI(Field field, Game game) {

		/**
		 * neues Panel
		 */

		setFocusable(true);
		panel = new BoardPanel(field);
		reinitialize(field, game);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/**
		 * Menüleiste mit den Elementen:
		 * 
		 * Game: Start Game 2 Player Reset Quit
		 */
		menu = new JMenuBar();
		gameMenu = new JMenu("Game");
		startItem = new JMenuItem("Start Game");
		startItem.addActionListener(this);
		multiplayer = new JMenuItem("2 Player");
		multiplayer.addActionListener(this);
		restartItem = new JMenuItem("Reset");
		restartItem.addActionListener(this);
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		gameMenu.add(startItem);
		gameMenu.add(multiplayer);
		gameMenu.add(restartItem);
		gameMenu.add(quitItem);
		menu.add(gameMenu);
		this.add(menu, BorderLayout.NORTH);
		this.setJMenuBar(menu);
		// this.invalidate();

		this.add(panel);

		this.addKeyListener(this);
	}

	public void reinitialize(Field field, Game game) {
		this.mainGame = game;
		gameField = field;
		this.setSize((gameField.getMap().length) * 32,
				(gameField.getMap()[0].length + 1) * 32 + 12);
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

	@Override
	/**
	 * Es werden die Aktionen der jeweiligen Menüeinträge(Game)
	 * festgesetzt(Starten, Neustarten, Beenden)
	 */
	public void actionPerformed(ActionEvent object) {
		if (object.getSource() == startItem) {
			System.out.println("Singleplayermodus");
			mainGame.key = 0;
			mainGame.gameState = GameStates.STARTED;
		}
		if (object.getSource() == quitItem) {
			System.exit(0);
		}
		if (object.getSource() == multiplayer) {
			System.out.println("2 Spielermodus");
			mainGame.key = 0;
			mainGame.gameState = GameStates.TWOPLAYER;
		}
		if (object.getSource() == restartItem) {
			mainGame.restart(Game.createNewField(), new Player(1, 1),
					new Player(13, 13), true);
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

}