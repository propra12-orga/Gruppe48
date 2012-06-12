package Engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Field.Field;
import Field.FieldGenerator;
import GUI.GUI;
import Objects.Bomb;
import Objects.Player;

/**
 * Game.java
 * 
 * @author pallepilla
 * 
 */
public class Game implements Runnable {

	private Field gameField;
	public static GameStates gameState;

	private static GUI gui;
	public char key;
	long gameSpeed;
	Player player;
	Player player2;
	static int iPlayerCount = 1;
	int iNewPlayerCount = 1;
	int iDefeatedPlayer = 0;
	int iWinningPlayer = 0;
	static int iMaxPlayers = 0;
	List<Bomb> bombList;
	List<long[]> explosionList;
	Calendar calendar;
	long time;
	private Field testfield;

	/**
	 * Game Konstruktor
	 * 
	 * @param: field Erzeugtes Spielfeld
	 * @param: player Einzufuegender Spieler
	 */
	public Game(Field field) {
		testfield = field;
		int iPlayercounter = 1;
		for (int i = 0; i < testfield.getMap().length; i++) {
			for (int j = 0; j < testfield.getMap()[0].length; j++) {
				if (testfield.getMap()[i][j].getContent() == 5) {
					testfield.getMap()[i][j].setContent(1);
					if (iPlayercounter <= iPlayerCount) {
						switch (iPlayercounter) {
						case 1:
							player = new Player(i, j, 1);
							testfield.setPlayer(player);
							break;
						case 2:
							player2 = new Player(i, j, 2);
							testfield.setPlayer(player2);
							break;
						}
					}
					iPlayercounter++;
				}
			}
		}
		explosionList = new ArrayList<long[]>();
		time = Calendar.getInstance().getTimeInMillis();
		bombList = new ArrayList<Bomb>();
		gameSpeed = 100;
		// gui.insertField(testfield);
		gameState = GameStates.STOP;
	}

	/**
	 * Gibt ein neues Field
	 * 
	 */
	public Field getField() {
		return gameField;
	}

	/**
	 * Fuegt neues GUI ein
	 */
	public void insertGUI(GUI gui) {
		this.gui = gui;
	}

	/**
	 * Macht ein "restart"
	 * 
	 * @param field
	 *            Neues Spielfeld
	 * @param player
	 *            Einzufuegender Spieler
	 * @param restart
	 */
	public void restart(Field field) {
		if (field == null) {
			gameState = GameStates.STOP;
			return;
		}
		iPlayerCount = iNewPlayerCount;
		for (int i = 0; i <= bombList.size() + 1; i++) {
			gui.panel.removeExplosions();
		}
		testfield = field;
		int iPlayercounter = 1;
		for (int i = 0; i < testfield.getMap().length; i++) {
			for (int j = 0; j < testfield.getMap()[0].length; j++) {
				if (testfield.getMap()[i][j].getContent() == 5) {
					testfield.getMap()[i][j].setContent(1);
					if (iPlayercounter <= iPlayerCount) {
						switch (iPlayercounter) {
						case 1:
							player = new Player(j, i, 1);
							testfield.setPlayer(player);
							break;
						case 2:
							player2 = new Player(j, i, 2);
							testfield.setPlayer(player2);
							break;
						}
					}
					iPlayercounter++;
				}
			}
		}
		explosionList = new ArrayList<long[]>();
		time = Calendar.getInstance().getTimeInMillis();
		bombList = new ArrayList<Bomb>();
		gameSpeed = 100;
		gameState = GameStates.INITIALIZED;
		gui.insertField(testfield);
		gui.repaint();
		gameState = GameStates.STARTED;
	}

	/**
	 * Die wichtigste Methode von Thread, die das Spiel verändert, wenn ein
	 * GameState verändert wird
	 */
	@Override
	public void run() {
		Boolean doRestart = false;
		while (true && !doRestart) {
			switch (gameState) {
			case STARTED:
				start();
				// System.out.println("Game started");
				break;
			case TWOPLAYER:
				iNewPlayerCount = 2;
				gameState = GameStates.STARTED;
				break;
			case ONEPLAYER:
				iNewPlayerCount = 1;
				gameState = GameStates.STARTED;
				break;
			case VICTORY:
				if (iPlayerCount > 1) {
					gui.showError("Spieler " + iWinningPlayer
							+ " hat gewonnen!");
				} else
					gui.showError("Gewonnen!");
				doRestart = true;
				break;
			case GAMEOVER:
				if (iPlayerCount > 1) {
					gui.showError("Spieler " + iDefeatedPlayer
							+ " hat verloren!");
				} else
					gui.showError("GAMEOVER");
				doRestart = true;
				break;
			case INITIALIZED:
				// System.out.println("INITIALIZED");
				start();
				break;
			case STOP:
				break;
			default:
				System.out.println("default");
				break;
			}
		}
		if (doRestart) {
			restart(Game.createNewField());
			run();
		}
	}

	/**
	 * Macht gameState PAUSED
	 */
	public void pauseGame() {
		gameState = GameStates.PAUSED;
	}

	/**
	 * Startet ein neues Spiel
	 */
	public void start() {
		time = Calendar.getInstance().getTimeInMillis();
		handleBombs();
		handleMovement();
		gui.insertField(testfield);
		gui.repaint();
		try {
			Thread.sleep(gameSpeed);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * Erzeugt ein neues Feld
	 */
	public static Field createNewField() {
		FieldGenerator testGenerator = new FieldGenerator();
		Field testfield = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		testfield.insertMap(testGenerator.createSquareMap(15));
		return testfield;
	}

	public static Field createNewField(String sMap) {
		int iMaxPlayersLoaded = 0;
		FieldGenerator testGenerator = new FieldGenerator();
		Field testfield = new Field();
		Field fileTester = new Field();
		fileTester.insertMap(testGenerator.readMap(sMap));
		if (fileTester.getMap() != null) {
			for (int i = 0; i < fileTester.getMap().length; i++) {
				for (int j = 0; j < fileTester.getMap()[0].length; j++) {
					if (fileTester.getMap()[i][j].getContent() == 5) {
						iMaxPlayersLoaded++;
					}
				}
			}
			if (iMaxPlayersLoaded > 0) {
				testfield.insertMap(testGenerator.readMap(sMap));
				iMaxPlayers = iMaxPlayersLoaded;
			} else {
				gui.showError("Die Map ist unspielbar, da kein Spieler vorhanden ist");
				return null;
			}
		} else {
			gui.showError("Die Map enthaelt ungueltige Zeichen! Vorgang wird abgebrochen.");
			return null;
		}

		if (iPlayerCount > iMaxPlayers) {
			gui.showError("Diese Map ist nicht mit so vielen Spielern spielbar");
			gameState = GameStates.STOP;
			return null;
		}
		return fileTester;
	}

	public static Field createNewField(int iSize) {
		FieldGenerator testGenerator = new FieldGenerator();
		Field testfield = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		testfield.insertMap(testGenerator.createSquareMap(iSize));
		return testfield;
	}

	/**
	 * Fragt Timer der Bomben ab. Falls eine Bombe explodiert, werden die
	 * betroffenen Felder an die GUI zur Darstellung uebergeben sowie weitere
	 * betroffene Bomben gezuendet. Steht der Spieler auf einem betroffenen
	 * Feld, so wird der Spielstatus auf GAMEOVER gesetzt
	 */
	private void handleBombs() {
		ArrayList<int[]> exList;
		for (int i = 0; i < bombList.size(); i++) {
			if (bombList.get(i).getTimer() <= time) {
				exList = new ArrayList<int[]>();
				exList.add(new int[2]);
				exList.get(exList.size() - 1)[0] = bombList.get(i)
						.getPosition()[1];
				exList.get(exList.size() - 1)[1] = bombList.get(i)
						.getPosition()[0];
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] - j).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						testfield
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] - j)
								.getPlayer().getID();
					}
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] - j).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0] - j;
						tmp[1] = bombList.get(i).getPosition()[1];
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					try {
						if (testfield.getField(
								bombList.get(i).getPosition()[1],
								bombList.get(i).getPosition()[0] - j)
								.getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1];
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0] - j;
						} else {
							if (testfield.getField(
									bombList.get(i).getPosition()[1],
									bombList.get(i).getPosition()[0] - j)
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1];
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0] - j;
								testfield.getField(
										bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] - j)
										.setContent(1);
							}
							break;
						}
					} catch (Exception e) {
						// System.out.println(e);
					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] + j).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						testfield
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] + j)
								.getPlayer().getID();
					}
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] + j).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0] + j;
						tmp[1] = bombList.get(i).getPosition()[1];
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					try {
						if (testfield.getField(
								bombList.get(i).getPosition()[1],
								bombList.get(i).getPosition()[0] + j)
								.getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1];
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0] + j;
						} else {
							if (testfield.getField(
									bombList.get(i).getPosition()[1],
									bombList.get(i).getPosition()[0] + j)
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1];
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0] + j;
								testfield.getField(
										bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] + j)
										.setContent(1);
							}
							break;
						}
					} catch (Exception e) {

					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(
							bombList.get(i).getPosition()[1] - j,
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						testfield
								.getField(bombList.get(i).getPosition()[1] - j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
					}
					if (testfield.getField(
							bombList.get(i).getPosition()[1] - j,
							bombList.get(i).getPosition()[0]).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0];
						tmp[1] = bombList.get(i).getPosition()[1] - j;
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					try {
						if (testfield.getField(
								bombList.get(i).getPosition()[1] - j,
								bombList.get(i).getPosition()[0]).getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1] - j;
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0];
						} else {
							if (testfield.getField(
									bombList.get(i).getPosition()[1] - j,
									bombList.get(i).getPosition()[0])
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1] - j;
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0];
								testfield.getField(
										bombList.get(i).getPosition()[1] - j,
										bombList.get(i).getPosition()[0])
										.setContent(1);
							}
							break;
						}
					} catch (Exception e) {

					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(
							bombList.get(i).getPosition()[1] + j,
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						iDefeatedPlayer = testfield
								.getField(bombList.get(i).getPosition()[1] + j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
					}
					if (testfield.getField(
							bombList.get(i).getPosition()[1] + j,
							bombList.get(i).getPosition()[0]).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0];
						tmp[1] = bombList.get(i).getPosition()[1] + j;
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					try {
						if (testfield.getField(
								bombList.get(i).getPosition()[1] + j,
								bombList.get(i).getPosition()[0]).getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1] + j;
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0];
						} else {
							if (testfield.getField(
									bombList.get(i).getPosition()[1] + j,
									bombList.get(i).getPosition()[0])
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1] + j;
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0];
								testfield.getField(
										bombList.get(i).getPosition()[1] + j,
										bombList.get(i).getPosition()[0])
										.setContent(1);
							}
							break;
						}
					} catch (Exception e) {

					}
				}

				explosionList.add(new long[1]);
				explosionList.get(0)[0] = Calendar.getInstance()
						.getTimeInMillis() + 500;
				gui.panel.addExplosions(exList);
				exList = null;
				testfield.removeBomb(bombList.get(i));
				bombList.remove(i);
			}
		}
		for (int i = 0; i < explosionList.size(); i++) {
			if (explosionList.get(i)[0] < (Calendar.getInstance()
					.getTimeInMillis())) {
				gui.panel.removeExplosions();
				explosionList.remove(0);
			}
		}
	}

	/**
	 * Fragt gedrueckte Tasten ab und bewegt den Spieler entsprechend, wenn das
	 * neue Feld begehbar ist. w = hoch, a = links, s = unten, d = rechts Wird
	 * Space gedrueckt, so wird eine Bombe gelegt.
	 */
	private void handleMovement() {
		if (iPlayerCount == 1) {
			switch (key) {
			case 'w':
				switch (testfield.getField(player.getPosition()[1],
						player.getPosition()[0] - 1).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveUp();
					testfield.setPlayer(player);
					break;
				}
				break;
			case 'a':
				switch (testfield.getField(player.getPosition()[1] - 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveLeft();
					testfield.setPlayer(player);
					break;
				}
				break;
			case 's':
				switch (testfield.getField(player.getPosition()[1],
						player.getPosition()[0] + 1).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveDown();
					testfield.setPlayer(player);
					break;
				}
				break;
			case 'd':
				switch (testfield.getField(player.getPosition()[1] + 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveRight();
					testfield.setPlayer(player);
					break;
				}
				break;
			case KeyEvent.VK_SPACE:
				bombList.add(new Bomb(player.getPosition()[0], player
						.getPosition()[1], time));
				testfield.setBomb(bombList.get(bombList.size() - 1));
				break;
			}
			key = 0;
		} else {
			switch (key) {
			case 'w':
				switch (testfield.getField(player.getPosition()[1],
						player.getPosition()[0] - 1).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveUp();
					testfield.setPlayer(player);
					break;
				}
				break;
			case 'a':
				switch (testfield.getField(player.getPosition()[1] - 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveLeft();
					testfield.setPlayer(player);
					break;
				}
				break;
			case 's':
				switch (testfield.getField(player.getPosition()[1],
						player.getPosition()[0] + 1).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveDown();
					testfield.setPlayer(player);
					break;
				}
				break;
			case 'd':
				switch (testfield.getField(player.getPosition()[1] + 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (testfield.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player);
					player.moveRight();
					testfield.setPlayer(player);
					break;
				}
				break;
			case KeyEvent.VK_SPACE:
				bombList.add(new Bomb(player.getPosition()[0], player
						.getPosition()[1], time));
				testfield.setBomb(bombList.get(bombList.size() - 1));
				break;
			case 'i':
				switch (testfield.getField(player2.getPosition()[1],
						player2.getPosition()[0] - 1).getContent()) {
				case 1:
					if (testfield.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player2);
					player2.moveUp();
					testfield.setPlayer(player2);
					break;
				case 3:
					gameState = GameStates.VICTORY;
				}
				break;
			case 'j':
				switch (testfield.getField(player2.getPosition()[1] - 1,
						player2.getPosition()[0]).getContent()) {
				case 1:
					if (testfield.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player2);
					player2.moveLeft();
					testfield.setPlayer(player2);
					break;
				case 3:
					gameState = GameStates.VICTORY;
				}
				break;
			case 'k':
				switch (testfield.getField(player2.getPosition()[1],
						player2.getPosition()[0] + 1).getContent()) {
				case 1:
					if (testfield.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player2);
					player2.moveDown();
					testfield.setPlayer(player2);
					break;
				case 3:
					gameState = GameStates.VICTORY;
				}
				break;
			case 'l':
				switch (testfield.getField(player2.getPosition()[1] + 1,
						player2.getPosition()[0]).getContent()) {
				case 1:
					if (testfield.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (testfield.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					testfield.removePlayer(player2);
					player2.moveRight();
					testfield.setPlayer(player2);
					break;
				case 3:
					gameState = GameStates.VICTORY;
				}
				break;
			case KeyEvent.VK_ENTER:
				bombList.add(new Bomb(player2.getPosition()[0], player2
						.getPosition()[1], time));
				testfield.setBomb(bombList.get(bombList.size() - 1));
				break;
			}
			key = 0;
		}
	}

	/**
	 * Programm-Eintrittspunkt
	 */
	public static void main(String args[]) {
		Field field = createNewField();
		Game game = new Game(field);
		GUI gui = new GUI(game);
		gui.setVisible(true);
		game.insertGUI(gui);
		Thread gameThread = new Thread(game);
		gameThread.start();

	}
}
