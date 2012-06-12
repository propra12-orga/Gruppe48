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
	private static Field cacheField;
	public static GameStates gameState;
	private static GUI gui;
	public char key;
	long gameSpeed;
	Player player;
	Player player2;
	static boolean bAutoRestart = false;
	static boolean bMapLoaded = false;
	static String sMapPath = "";
	static int iPlayerCount = 1;
	int iNewPlayerCount = 1;
	int iDefeatedPlayer = 0;
	int iWinningPlayer = 0;
	static int iMaxPlayers = 0;
	List<Bomb> bombList;
	List<long[]> explosionList;
	Calendar calendar;
	long time;

	/**
	 * Erzeugt ein neues Spielfeld mit 1-2 Spielern
	 * 
	 * @param field
	 *            Das zu erzeugende Spielfeld wird hier uebergeben
	 */
	public Game(Field field) {
		gameField = field;
		int iPlayercounter = 1;
		for (int i = 0; i < gameField.getMap().length; i++) {
			for (int j = 0; j < gameField.getMap()[0].length; j++) {
				if (gameField.getMap()[i][j].getContent() == 5) {
					gameField.getMap()[i][j].setContent(1);
					if (iPlayercounter <= iPlayerCount) {
						switch (iPlayercounter) {
						case 1:
							player = new Player(i, j, 1);
							gameField.setPlayer(player);
							break;
						case 2:
							player2 = new Player(i, j, 2);
							gameField.setPlayer(player2);
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
		// gui.insertField(gameField);
		gameState = GameStates.STOP;
	}

	/**
	 * Gibt ein Field zurueck
	 * 
	 */
	public Field getField() {
		return gameField;
	}

	/**
	 * Fuegt neue GUI ein
	 */
	public void insertGUI(GUI gui) {
		this.gui = gui;
	}

	public void setMapPath(String sPath) {
		sMapPath = sPath;
	}

	public static void setMapLoaded(boolean bLoad) {
		bMapLoaded = bLoad;
	}

	/**
	 * Startet das Spiel neu
	 * 
	 * @param field
	 *            Gegebenes Spielfeld
	 */
	public boolean restart() {
		Field field;
		if (bAutoRestart) {
			field = new Field();
			field.insertMap(cacheField.getMap());
		} else {
			if (bMapLoaded) {
				field = createNewField(sMapPath);
			} else {
				field = createNewField();
			}
		}
		if (field == null) {
			bMapLoaded = false;
			gameState = GameStates.STOP;
			return false;
		}
		iPlayerCount = iNewPlayerCount;
		for (int i = 0; i <= bombList.size() + 1; i++) {
			gui.panel.removeExplosions();
		}
		gameField = field;
		int iPlayercounter = 1;
		for (int i = 0; i < gameField.getMap().length; i++) {
			for (int j = 0; j < gameField.getMap()[0].length; j++) {
				if (gameField.getMap()[i][j].getContent() == 5) {
					gameField.getMap()[i][j].setContent(1);
					if (iPlayercounter <= iPlayerCount) {
						switch (iPlayercounter) {
						case 1:
							player = new Player(j, i, 1);
							gameField.setPlayer(player);
							break;
						case 2:
							player2 = new Player(j, i, 2);
							gameField.setPlayer(player2);
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
		gui.insertField(gameField);
		gui.resize();
		gui.repaint();
		gameState = GameStates.STARTED;
		bAutoRestart = false;
		return true;
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
				break;
			case VICTORY:
				if (iPlayerCount > 1) {
					gui.showError("Spieler " + iWinningPlayer
							+ " hat gewonnen!");
				} else
					gui.showError("Gewonnen!");
				doRestart = true;
				bAutoRestart = true;
				break;
			case GAMEOVER:
				if (iPlayerCount > 1) {
					gui.showError("Spieler " + iDefeatedPlayer
							+ " hat verloren!");
				} else
					gui.showError("GAMEOVER");
				doRestart = true;
				bAutoRestart = true;
				break;
			case INITIALIZED:
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
			restart();
			run();
		}
	}

	/**
	 * Pausiert das Spiel
	 */
	public void pauseGame() {
		gameState = GameStates.PAUSED;
	}

	public void setPlayerCount(int iCount) {
		iNewPlayerCount = iCount;
	}

	/**
	 * Startet ein neues Spiel
	 */
	public void start() {
		time = Calendar.getInstance().getTimeInMillis();
		handleBombs();
		handleMovement();
		gui.insertField(gameField);
		gui.repaint();
		try {
			Thread.sleep(gameSpeed);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * Erzeugt ein neues Feld mit zufaelligen Waenden und Mauern
	 */
	public static Field createNewField() {
		FieldGenerator testGenerator = new FieldGenerator();
		Field generatedField = new Field();
		cacheField = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		generatedField.insertMap(testGenerator.createSquareMap(15));
		cacheField.insertMap(generatedField.getMap());
		return generatedField;
	}

	public static Field createNewField(String sMap) {
		int iMaxPlayersLoaded = 0;
		FieldGenerator testGenerator = new FieldGenerator();
		Field generatedField = new Field();
		Field fileTester = new Field();
		cacheField = new Field();
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
				generatedField.insertMap(testGenerator.readMap(sMap));
				iMaxPlayers = iMaxPlayersLoaded;
			} else {
				gui.showError("Die Map ist unspielbar, da kein Spieler vorhanden ist");
				setMapLoaded(false);
				return null;
			}
		} else {
			gui.showError("Die Map enthaelt ungueltige Zeichen oder ist nicht mehr vorhanden! Vorgang wird abgebrochen.");
			setMapLoaded(false);
			return null;
		}

		if (iPlayerCount > iMaxPlayers) {
			gui.showError("Diese Map ist nicht mit so vielen Spielern spielbar");
			gameState = GameStates.STOP;
			setMapLoaded(false);
			return null;
		}
		sMapPath = sMap;
		cacheField.insertMap(fileTester.getMap());
		cacheField.insertMap(fileTester.getMap());
		return fileTester;
	}

	public static Field createNewField(int iSize) {
		FieldGenerator testGenerator = new FieldGenerator();
		Field generatedField = new Field();
		cacheField = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		generatedField.insertMap(testGenerator.createSquareMap(iSize));
		cacheField = generatedField;
		cacheField.insertMap(generatedField.getMap());
		return generatedField;
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
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
					}
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0]).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0];
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
						if (gameField.getField(
								bombList.get(i).getPosition()[1],
								bombList.get(i).getPosition()[0]).getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1];
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0];
						} else {
							if (gameField.getField(
									bombList.get(i).getPosition()[1],
									bombList.get(i).getPosition()[0])
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1];
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0];
								gameField.getField(
										bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0])
										.setContent(1);
							}
							break;
						}
					} catch (Exception e) {
						// System.out.println(e);
					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] - j).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] - j)
								.getPlayer().getID();
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] - j)
								.getPlayer().getID();
					}
					if (gameField.getField(bombList.get(i).getPosition()[1],
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
						if (gameField.getField(
								bombList.get(i).getPosition()[1],
								bombList.get(i).getPosition()[0] - j)
								.getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1];
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0] - j;
						} else {
							if (gameField.getField(
									bombList.get(i).getPosition()[1],
									bombList.get(i).getPosition()[0] - j)
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1];
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0] - j;
								gameField.getField(
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
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] + j).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] + j)
								.getPlayer().getID();
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] + j)
								.getPlayer().getID();
					}
					if (gameField.getField(bombList.get(i).getPosition()[1],
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
						if (gameField.getField(
								bombList.get(i).getPosition()[1],
								bombList.get(i).getPosition()[0] + j)
								.getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1];
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0] + j;
						} else {
							if (gameField.getField(
									bombList.get(i).getPosition()[1],
									bombList.get(i).getPosition()[0] + j)
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1];
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0] + j;
								gameField.getField(
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
					if (gameField.getField(
							bombList.get(i).getPosition()[1] - j,
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						gameField
								.getField(bombList.get(i).getPosition()[1] - j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1] - j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
					}
					if (gameField.getField(
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
						if (gameField.getField(
								bombList.get(i).getPosition()[1] - j,
								bombList.get(i).getPosition()[0]).getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1] - j;
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0];
						} else {
							if (gameField.getField(
									bombList.get(i).getPosition()[1] - j,
									bombList.get(i).getPosition()[0])
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1] - j;
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0];
								gameField.getField(
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
					if (gameField.getField(
							bombList.get(i).getPosition()[1] + j,
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
						gameField
								.getField(bombList.get(i).getPosition()[1] + j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1] + j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
					}
					if (gameField.getField(
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
						if (gameField.getField(
								bombList.get(i).getPosition()[1] + j,
								bombList.get(i).getPosition()[0]).getContent() == 1) {
							exList.add(new int[2]);
							exList.get(exList.size() - 1)[0] = bombList.get(i)
									.getPosition()[1] + j;
							exList.get(exList.size() - 1)[1] = bombList.get(i)
									.getPosition()[0];
						} else {
							if (gameField.getField(
									bombList.get(i).getPosition()[1] + j,
									bombList.get(i).getPosition()[0])
									.getContent() == 6) {
								exList.add(new int[2]);
								exList.get(exList.size() - 1)[0] = bombList
										.get(i).getPosition()[1] + j;
								exList.get(exList.size() - 1)[1] = bombList
										.get(i).getPosition()[0];
								gameField.getField(
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
				gameField.removeBomb(bombList.get(i));
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
	 * Space gedrueckt, so wird eine Bombe gelegt. Fuer einen zweiten Spieler i
	 * = hoch, j= links, k = unten, l = rechts, enter = Bombe legen
	 */
	private void handleMovement() {
		if (iPlayerCount == 1) {
			switch (key) {
			case 'w':
				switch (gameField.getField(player.getPosition()[1],
						player.getPosition()[0] - 1).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveUp();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'a':
				switch (gameField.getField(player.getPosition()[1] - 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveLeft();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 's':
				switch (gameField.getField(player.getPosition()[1],
						player.getPosition()[0] + 1).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveDown();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'd':
				switch (gameField.getField(player.getPosition()[1] + 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isExit() == true) {
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveRight();
					gameField.setPlayer(player);
					break;
				}
				break;
			case KeyEvent.VK_SPACE:
				bombList.add(new Bomb(player.getPosition()[0], player
						.getPosition()[1], time));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				break;
			}
			key = 0;
		} else {
			switch (key) {
			case 'w':
				switch (gameField.getField(player.getPosition()[1],
						player.getPosition()[0] - 1).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveUp();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'a':
				switch (gameField.getField(player.getPosition()[1] - 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveLeft();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 's':
				switch (gameField.getField(player.getPosition()[1],
						player.getPosition()[0] + 1).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveDown();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'd':
				switch (gameField.getField(player.getPosition()[1] + 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player);
					player.moveRight();
					gameField.setPlayer(player);
					break;
				}
				break;
			case KeyEvent.VK_SPACE:
				bombList.add(new Bomb(player.getPosition()[0], player
						.getPosition()[1], time));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				break;
			case 'i':
				switch (gameField.getField(player2.getPosition()[1],
						player2.getPosition()[0] - 1).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player2);
					player2.moveUp();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case 'j':
				switch (gameField.getField(player2.getPosition()[1] - 1,
						player2.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player2);
					player2.moveLeft();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case 'k':
				switch (gameField.getField(player2.getPosition()[1],
						player2.getPosition()[0] + 1).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player2);
					player2.moveDown();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case 'l':
				switch (gameField.getField(player2.getPosition()[1] + 1,
						player2.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					gameField.removePlayer(player2);
					player2.moveRight();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case KeyEvent.VK_ENTER:
				bombList.add(new Bomb(player2.getPosition()[0], player2
						.getPosition()[1], time));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				break;
			}
			key = 0;
		}
	}

	/**
	 * Hauptprogramm Methode, die alles initialisiert(field,gui,gamelogik)
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
