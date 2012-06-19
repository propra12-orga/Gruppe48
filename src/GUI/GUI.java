package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import Options.Options;

/**
 * GUI.java Diese Klasse erzeugt ein Frame und legt eine Menueleiste fest
 * 
 * @author Carsten Stegmann
 * 
 */
public class GUI extends JFrame implements ActionListener, KeyListener,
		WindowListener {

	Field gameField;
	BufferedImage imgExit;
	BufferedImage imgWall;
	BufferedImage imgFree;
	public BoardPanel panel;

	/**
	 * Menueelemente fuer die Menueleiste
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
	private JMenuItem mapCreatorItem;
	private final Game game1;

	public GUI(Game game) {

		/**
		 * neues Panel und Frameeigenschaften
		 */
		this.game1 = game;
		setFocusable(true);
		panel = new BoardPanel();
		mainGame = game;
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.setSize(480, 480);

		/**
		 * Menueleiste mit den Elementen:
		 * 
		 * Ein Game-Menue mit den Untermenues: New Game: ein neues Spiel wird
		 * gestartet Open Map: Es kann eine Karte aus einer Datei geladen werden
		 * 1 Player: Startet den Einzelspieler Modus 2 Player: Startet des 2
		 * Spieler Modus Quit: Schliesst das Fenster
		 */
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		gameMenu = new JMenu("Game");
		JMenu optionMenu = new JMenu("Options");
		JMenu mapCreator = new JMenu("Map Creator");
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
		gameMenu.addSeparator();
		gameMenu.add(quitItem);
		menubar.add(gameMenu);
		menubar.add(optionMenu);
		menubar.add(mapCreator);
		mapCreatorItem = new JMenuItem("Create your own Map!");
		mapCreatorItem.addActionListener(this);
		mapCreator.add(mapCreatorItem);
		optionItem = new JMenuItem("GameOptions");
		optionItem.addActionListener(this);
		optionMenu.add(optionItem);
		this.add(menubar, BorderLayout.NORTH);
		this.setJMenuBar(menubar);
		this.add(panel);
		this.addKeyListener(this);
		singleplayer.setSelected(true);
		this.addWindowListener(this);

	}

	/**
	 * setzt das Spielfeld auf das Panel
	 * 
	 * @param field
	 *            das einzufuegende Spielfeld
	 */
	public void insertField(Field field) {
		gameField = field;
		panel.insertField(field);
	}

	/**
	 * Erzeugt Ausgabefenster mit angegebener Nachricht
	 * 
	 * @param sError
	 *            Auszugebene Nachricht
	 */
	public void showError(String sError) {
		JOptionPane.showMessageDialog(null, sError);
	}

	/**
	 * Veraendert die Panelgroeﬂe abhaengig des geladenen Spielfelds
	 */
	public void resize() {
		this.setSize((gameField.getMap().length) * 32 + 5,
				(gameField.getMap()[0].length + 1) * 32 + 18);
	}

	@Override
	/**
	 * Es werden die Aktionen der jeweiligen Menueeintraege "Game" 
	 * (New Game, Open Map, 1 Player, 2Player, Quit) und "Options" (Game Options)
	 * festgelegt
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
			shutdown();
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
			new Options(game1);
		}
		if (object.getSource() == mapCreatorItem) {
			System.out.println("Hier entsteht der Map Creator");
		}
	}

	@Override
	/**
	 * Methode,die aufgerufen wird, wenn eine Taste gedrueckt wird
	 */
	public void keyPressed(KeyEvent e) {
	}

	@Override
	/**
	 * wird ausgefuehrt, wenn die Taste innerhalb eines kurzen
	 * Zeitraums gedrueckt und losgelassen wird
	 */
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	/**
	 * wird ausgefuehrt, wenn die Taste losgelassen wird
	 */
	public void keyTyped(KeyEvent e) {
		mainGame.key = e.getKeyChar();
	}

	/**
	 * ÷ffnet ein neues Fenster, indem eine Datei mit der Endung .txt zum laden
	 * der Map gewaehlt werden kann
	 * 
	 */
	public boolean open() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".txt") // Nur die
																	// Endung
																	// .txt
						|| f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Maps";
			}
		});
		int iOpened = fileChooser.showOpenDialog(null);
		if (iOpened == JFileChooser.APPROVE_OPTION) { // Legt fest, was
														// passiert, nachdem der
														// "÷ffnen"-Button
														// gedrueckt wurde
			File file = fileChooser.getSelectedFile();
			mainGame.setMapPath(file.getAbsolutePath());
			mainGame.setMapLoaded(true);
			return mainGame.restart();
		} else {
			showError("Nichts ausgewaehlt");
			return false;
		}

	}

	/**
	 * Methode wird aufgerufen, nachdem im Menue "Quit" angeklickt worden ist
	 * und oeffnet ein neues Dialogfenster, zum Bestaetigen der Aktion
	 */
	protected void shutdown() {
		int result = JOptionPane.showConfirmDialog(null, "Sure?", "Quit?",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			Runtime.getRuntime().exit(0);
		}

	}

	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		shutdown();
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
