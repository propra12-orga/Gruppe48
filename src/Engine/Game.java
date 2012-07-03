package Engine;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Field.Field;
import Field.FieldGenerator;
import GUI.GUI;
import Network.Server;
import Objects.Bomb;
import Objects.Player;
import Options.Options;

/**
 * Die Klasse Game ist die Main-Klasse des Bomberman Projekts. Sie enthaelt die Spiellogik, ruft die GUI auf und verwaltet Benutzereingaben.
 * 
 * @author Leonid Panich
 * 
 */
/**
 * Erzeugt ein neues Objekt der Klasse Game und initialisiert Diese. Standard
 * maessig wird von einem einzelnen Spieler ausgegangen
 */
public class Game implements Runnable {

	private Field gameField;
	private static Field cacheField;
	/**
	 * Enthaelt aktuellen Zustand des Spiels STARTED = Spiel laeuft STOP = Spiel
	 * ist angehalten VICTORY = Ein Spieler hat gewonnen GAMEOVER = Ein Spieler
	 * hat verloren
	 */
	public static GameStates gameState;
	private static GUI gui;
	/**
	 * Zuletzt gedrueckte Taste
	 */
	public char key;
	long gameSpeed;
	Player player;
	Player player2;
	static boolean bAutoRestart = false;
	static boolean bMapLoaded = false;
	static String sMapPath = "";
	static int iPlayerCount = 1;
	int iItemChance = 35;
	int iNewPlayerCount = 1;
	int iDefeatedPlayer = 0;
	int iWinningPlayer = 0;
	static int iMaxPlayers = 0;
	List<Bomb> bombList;
	List<long[]> explosionList;
	Server server = null;
	Calendar calendar;
	long time;
	/**
	 * Startgroesse der Map
	 */
	public static int startMapSize = 15;
	/**
	 * Startdichte der zerstoerbaren Bloecke
	 */
	public static double startDensity = 70;
	/**
	 * Breite der Map, falls eine rechteckige Map erzeugt wird
	 */
	public static int rectangleMapWidht = 20;
	/**
	 * Breite der Map, falls eine rechteckige Map erzeugt wird
	 */
	public static int rectangleMapHight = 15;
	/**
	 * Modus nach dem die Map mit Bloecken gefuellt werden soll
	 */
	public static int fillModus = 0;
	/**
	 * Startwarscheinlichkeit fuer das erstellen von Bloecken in Modus 1
	 */
	public static int startProbability = 50;
	/**
	 * Startwarscheinlichkeit fuer das erstellen von Bloecken in Modus 2
	 */
	public static int startRandomAmount = 5;
	/**
	 * Definiert ob eine rechteckige oder eine quadratische Map erstellt werden
	 * soll
	 */
	public static boolean mapModus = Options.mapModus;

	// public static boolean isExploded = Objects.Bomb.isExploded;

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
			// An den ersten 1 oder 2 Positionen mit einem Inhaltswert von 5,
			// werden Spieler1 und Spieler2 hinzugefuegt. Anschließend wird der
			// Wert aller dieser Felder auf 1 gesetzt.
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
							// player2.setBombRadius(3);
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
	 * gibt ein Field zurueck
	 * 
	 * @return das zurückzugebende Field
	 */
	public Field getField() {
		return gameField;
	}

	/**
	 * Fuegt ein neues GUI ein
	 */
	public void insertGUI(GUI gui) {
		this.gui = gui;
	}

	/**
	 * Aendert den Pfad an dem eine zu ladende Map zu finden ist
	 * 
	 * @param sPath
	 *            Pfad zur zu ladenden Map
	 */
	public void setMapPath(String sPath) {
		sMapPath = sPath;
	}

	/**
	 * Legt fest, ob eine Map bei Erzeugung eines neuen Spiels geladen oder
	 * generiert werden soll
	 * 
	 * @param bLoad
	 *            Bei true wird eine Map geladen, ansonsten eine generiert
	 */
	public static void setMapLoaded(boolean bLoad) {
		bMapLoaded = bLoad;
	}

	/**
	 * Setzt die Groesse der Map auf den angegebenen Wert
	 * 
	 * @param changedMapSize
	 *            neue Groesse der Map
	 */
	public void setGameMapOptions(int changedMapSize) {
		startMapSize = changedMapSize;
	}

	public int getStartMapSize() {
		return startMapSize;
	}

	/**
	 * setzt die Dichte der zerstoerbaren Bloecke auf den angegebenen Wert
	 * 
	 * @param changedMapDensity
	 *            neue Dichte der zerstoerbaren Bloecke
	 */
	public void setGameDensityOptions(double changedMapDensity) {
		startDensity = changedMapDensity;
	}

	public double getStartDensity() {
		return startDensity;
	}

	/**
	 * Laesst entweder quadratische oder rechteckige Maps erzeugen
	 * 
	 * @param Mapmode
	 *            Ist der Wert true, so werden quadratische Maps erzeugt. Sonst
	 *            werden rechteckige Maps erzeugt
	 */
	public void setMapModus(boolean Mapmode) {
		mapModus = Mapmode;
	}

	/**
	 * Setzt die Breite der Map aud den angegeben Wert
	 * 
	 * @param changedWidhtMapSize
	 *            neue Breite
	 */
	public void setGameMapWidht(int changedWidhtMapSize) {
		rectangleMapWidht = changedWidhtMapSize;
	}

	public int getGameMapWidht() {
		return rectangleMapWidht;
	}

	/**
	 * Setzt die Hoehe der Map auf den angegebenen Wert
	 * 
	 * @param changedHightMapSize
	 *            neue Hoehe
	 */
	public void setGameMapHight(int changedHightMapSize) {
		rectangleMapHight = changedHightMapSize;
	}

	public int getGameMapHight() {
		return rectangleMapHight;
	}

	/**
	 * Setzt den Modus der Maperstellung auf den angegeben Wert
	 * 
	 * @param fillMapModus
	 *            neuer Modus
	 */
	public void setFillModus(int fillMapModus) {

		fillModus = fillMapModus;
	}

	public int getFillModus() {
		return fillModus;
	}

	/**
	 * Setzt die Menge der zu setzenden Bloecke fuer Modus 2 auf den angebenen
	 * Wert
	 * 
	 * @param changedAmount
	 *            neue Anzahl an nicht zerstoerenden Bloecken
	 */
	public void setRAmount(int changedAmount) {

		startRandomAmount = changedAmount;
	}

	public int getRAmount() {
		return startRandomAmount;
	}

	/**
	 * Setzt die Warscheinlichkeit der zu setzenden Bloecke fuer Modus 1 auf den
	 * angebenen Wert
	 * 
	 * @param changedProbability
	 *            neue Warscheinlichkeit
	 */
	public void setProbability(int changedProbability) {

		startProbability = changedProbability;
	}

	public int getProbability() {
		return startProbability;
	}

	public void startServer() {
		if (bMapLoaded) {
			server = new Server(createNewField(sMapPath));
		} else {
			server = new Server(createNewField());
		}

	}

	public void connect(int port) {

	}

	public void saveGame(String path) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			GameState gs = new GameState(gameField.getMap(), explosionList,
					bombList, player, player2);
			out.writeObject(gs);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			// i.printStackTrace();
		}
	}

	public boolean loadGame(String savedGamePath) {
		GameState savedGS = null;
		try {
			FileInputStream fileIn = new FileInputStream(savedGamePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			savedGS = (GameState) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			bMapLoaded = false;
			gameState = GameStates.STOP;
			return false;
		} catch (ClassNotFoundException c) {
			System.out.println("File " + savedGamePath + " is invalid");
			c.printStackTrace();
			bMapLoaded = false;
			gameState = GameStates.STOP;
			return false;
		}
		Field field;
		Bomb.setBombMaxOnStart(0); // Setzt die Zahl maximal legbarer Bomben
									// (Player1)am Spielstart auf eins;
		Bomb.setBombMaxOnStartP2(0); // Setzt die Zahl maximal legbarer Bomben
										// (Player2)am Spielstart auf eins;
		Bomb.currentPlaced = 0;
		Bomb.currentPlacedP2 = 0;

		field = new Field();
		field.insertMap(savedGS.map);
		iPlayerCount = iNewPlayerCount;
		for (int i = 0; i <= bombList.size() + 1; i++) { // Alle Explosionen
															// werden entfernt
			gui.panel.removeExplosions();
		}
		gameField = field;
		if (savedGS.player != null)
			player = savedGS.player;
		if (savedGS.player2 != null)
			player2 = savedGS.player2;
		explosionList = savedGS.explosionList;
		time = Calendar.getInstance().getTimeInMillis();
		bombList = savedGS.bombList;
		gameSpeed = 100;
		gui.insertField(gameField);
		gui.resize();
		gui.repaint();
		gameState = GameStates.STARTED;
		bAutoRestart = false;
		return true;
	}

	/**
	 * Startet das Spiel neu, genereiert, falls angegeben, neue Maps Setzt die
	 * Spieler auf ihre Startpositionen und entfernt sämtliche
	 * Explosionsueberreste
	 */
	public boolean restart() {
		Field field;
		Bomb.setBombMaxOnStart(0); // Setzt die Zahl maximal legbarer Bomben
									// (Player1)am Spielstart auf eins;
		Bomb.setBombMaxOnStartP2(0); // Setzt die Zahl maximal legbarer Bomben
										// (Player2)am Spielstart auf eins;
		Bomb.currentPlaced = 0;
		Bomb.currentPlacedP2 = 0;

		if (bAutoRestart) {
			field = new Field();
			field.insertMap(cacheField.getMap()); // Wurde das Spiel automatisch
													// neu gestartet, so wird
													// wieder die vorherige Map
													// benutzt
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
		for (int i = 0; i <= bombList.size() + 1; i++) { // Alle Explosionen
															// werden entfernt
			gui.panel.removeExplosions();
		}
		gameField = field;
		int iPlayercounter = 1;
		for (int i = 0; i < gameField.getMap().length; i++) {
			// An den ersten 1 oder 2 Positionen mit einem Inhaltswert von 5,
			// werden Spieler1 und Spieler2 hinzugefuegt. Anschließend wird der
			// Wert aller dieser Felder auf 1 gesetzt.
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
							// player2.setBombRadius(3);
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
		gui.insertField(gameField);
		gui.resize();
		gui.repaint();
		gameState = GameStates.STARTED;
		bAutoRestart = false;
		return true;
	}

	/**
	 * Hauptschleife der Spiellogik. Überwacht Zustand des Spiels und nimmt
	 * entsprechende Änderungen vor.
	 */
	@Override
	public void run() {
		Boolean doRestart = false;
		while (true && !doRestart) {
			switch (gameState) {
			case STARTED: // Das Spiel laeuft und laesst sich bedienen
				start();
				break;
			case VICTORY: // Ein Spieler hat gewonnen. Es wird eine
							// Siegesmeldung ausgegeben und das Spiel neu
							// gestartet
				if (iPlayerCount > 1) {
					gui.showError("Spieler " + iWinningPlayer
							+ " hat gewonnen!");
				} else
					gui.showError("Gewonnen!");
				doRestart = true;
				bAutoRestart = true;
				break;
			case GAMEOVER: // Ein Spieler hat verloren. Es wird eine
							// Niederlagemeldung ausgegeben und das Spiel neu
							// gestartet
				if (iPlayerCount > 1) {
					gui.showError("Spieler " + iDefeatedPlayer
							+ " hat verloren!");
				} else
					gui.showError("GAMEOVER");
				doRestart = true;
				bAutoRestart = true;
				break;
			case STOP: // Das Spiel wird angehalten
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
	 * Legt fest ob ein oder zwei Spieler vorhanden sind.
	 * 
	 * @param iCount
	 *            Ein Wert > 1 setzt die Spieleranzahl auf 2, jeder andere auf 1
	 */
	public void setPlayerCount(int iCount) {
		if (iCount >= 2)
			iNewPlayerCount = 2;
		else
			iNewPlayerCount = 1;
	}

	/**
	 * Abarbeitung der Spiellogik. Bomben und eventuelle Explosionen werden
	 * abgearbeitet, Spieler bewegt und das Spielfeld aktualisiert
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
	 * 
	 * @return Erzeugtes Spielfeld
	 */
	public static Field createNewField() {

		FieldGenerator testGenerator = new FieldGenerator();
		Field generatedField = new Field();
		cacheField = new Field(); // Neues Spielfeld wird fuer einen etwaigen
									// Neustart auf der selben Map gespeichert.
		testGenerator.setRandomAmount(startRandomAmount);
		testGenerator.setRandomChance(startProbability);
		testGenerator.setModus(fillModus);
		System.out.println("Modus:" + fillModus + "     RandomAmount:"
				+ startRandomAmount + "     RandomChance:" + startProbability);

		if (mapModus == true) {

			generatedField.insertMap(testGenerator.createSquareMap(
					startMapSize, startDensity));
		} else {
			generatedField.insertMap(testGenerator.createRectangleMap(
					rectangleMapWidht, rectangleMapHight, startDensity));
		}
		cacheField.insertMap(generatedField.getMap());
		return generatedField;
	}

	/**
	 * Erzeugt eine neue quadratische Map aus einer Datei
	 * 
	 * @param sMap
	 *            Pfad zur Datei
	 * @return Erzeugtes Spielfeld. Gibt null zurueck, falls geladenes Spielfeld
	 *         nicht den Vorgaben entspricht
	 */
	public static Field createNewField(String sMap) {
		int iMaxPlayersLoaded = 0;
		boolean freeSpace = false;
		FieldGenerator testGenerator = new FieldGenerator();
		Field generatedField = new Field();
		Field fileTester = new Field();
		cacheField = new Field();
		fileTester.insertMap(testGenerator.readMap(sMap));
		if (fileTester.getMap() != null) {
			// Die Anzahl der moeglichen Spielerstartplätze wird ueberprueft
			for (int i = 0; i < fileTester.getMap().length; i++) {
				for (int j = 0; j < fileTester.getMap()[0].length; j++) {
					if (fileTester.getMap()[i][j].getContent() == 5) {
						iMaxPlayersLoaded++;
					}
				}
			}
			// Es wird ueberprueft ob sich die maximal 2 Spieler bewegen koennen
			for (int i = 0; i < fileTester.getMap().length; i++) {
				for (int j = 0; j < fileTester.getMap()[0].length; j++) {
					if (iMaxPlayersLoaded == 1) {
						if (fileTester.getMap()[1][2].getContent() == 1
								&& fileTester.getMap()[2][1].getContent() == 1) {
							freeSpace = true;
						}
					} else if (iMaxPlayersLoaded == 2) {
						if (fileTester.getMap()[1][2].getContent() == 1
								&& fileTester.getMap()[2][1].getContent() == 1
								&& fileTester.getMap()[fileTester.getMap().length - 2][fileTester
										.getMap().length - 3].getContent() == 1
								&& fileTester.getMap()[fileTester.getMap().length - 3][fileTester
										.getMap().length - 2].getContent() == 1) {
							freeSpace = true;
						}
					}
				}
			}
			if (iMaxPlayersLoaded > 0) { // Gibt es keine Startplätze fuer
											// Spieler, so wird die Map
											// abgelehnt
				generatedField.insertMap(testGenerator.readMap(sMap));
				iMaxPlayers = iMaxPlayersLoaded;
			} else {
				gui.showError("Diese Map ist unspielbar, da kein Spieler vorhanden ist!");
				setMapLoaded(false);
				return null;
			}
		} else { // Gibt es Fehler beim Einlesen der Datei, so wird diese
					// abgelehnt
			// gui.showError("Es gibt einen Fehler mit der Map, bitte ueberpruefen Sie die Eingabedatei!"
			// + " Vorgang wird abgebrochen.");
			setMapLoaded(false);
			return null;
		}
		if (!freeSpace) {
			gui.showError("Diese Map ist unspielbar, da die Spieler keine Bomben legen koennen "
					+ "ohne dabei zu sterben!");
			setMapLoaded(false);
			return null;
		}
		if (iPlayerCount > iMaxPlayers) { // Soll die Map mit mehr Spielern
											// gespielt werden koennen als
											// vorhanden sind, so wird die Map
											// abgelehnt
			gui.showError("Diese Map ist nicht mit so vielen Spielern spielbar");
			gameState = GameStates.STOP;
			setMapLoaded(false);
			return null;
		}
		sMapPath = sMap;
		cacheField.insertMap(fileTester.getMap());
		return fileTester;
	}

	/**
	 * Erzeugt ein quadratisches, zufällig generiertes Speilfeld
	 * 
	 * @param iSize
	 *            Hoehe und Breite des Spielfeldes
	 * @return Erzeugtes Spielfeld
	 */
	public static Field createNewField(int iSize, double dRandomchance) {
		FieldGenerator testGenerator = new FieldGenerator();
		Field generatedField = new Field();
		cacheField = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		generatedField.insertMap(testGenerator.createSquareMap(iSize,
				dRandomchance));
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
			if (bombList.get(i).getTimer() <= time) { // Eine Liste aller Bomben
														// deren Timer
														// abgelaufen ist wird
														// generiert
				exList = new ArrayList<int[]>();
				exList.add(new int[2]);
				exList.get(exList.size() - 1)[0] = bombList.get(i)
						.getPosition()[1];
				exList.get(exList.size() - 1)[1] = bombList.get(i)
						.getPosition()[0];
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					// Die folgenden 4 for-Schleifen machen efektiv dasselbe,
					// betracheten dabei allerdings verschiedene Felder. Alle
					// Felder im Radius der explodierenden Bombe werden
					// abgesucht, dabeiwerden folgende Aktionen unternommen:
					// Befindet sich ein Spieler im Radius, so hat dieser
					// verloren. befindet sich ein freies Feld im Radius, so
					// passiert nichts. Befindet sich ein fester Block im
					// Radius, so wird die Explosion vor ihm gestoppt. Befindet
					// sich ein zerstoerebarer Block im Radius, so wird dieser
					// Zerstoert und die Explosion danach gestoppt. Befindet
					// sich
					// ein Ausgang im Radius, so wird die Explosion vor ihm
					// gestoppt. Befindet sich eine andere Bombe im Radius, so
					// wird diese auch zur Explosion gebracht.
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						// Überprueft Feld der Bombe
						// Trifft die Explosion auf einen Spieler, so wird hier
						// der besiegte Spieler ermittelt
						gameState = GameStates.GAMEOVER;
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
						Sound.GAME_OVER.play();
					}
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0]).getBomb() != null) {
						// Trifft die Bombe auf eine andere Bombe, so wird diese
						// hier auch zur Detonation gebracht
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
							// Sämtliche explodierenden Felder werden zur
							// Darstellung gesammelt und anschließend an die gui
							// uebergeben
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
					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] - j).getPlayer() != null) {
						// Überprueft Felder rechts der Bombe
						gameState = GameStates.GAMEOVER;
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] - j)
								.getPlayer().getID();
						Sound.GAME_OVER.play();
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
								createRandomItem(
										bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] - j,
										iItemChance);
							}
							break;
						}
					} catch (Exception e) {
					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (gameField.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] + j).getPlayer() != null) {
						// Überprueft Felder unterhalb der Bombe
						gameState = GameStates.GAMEOVER;
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] + j)
								.getPlayer().getID();
						Sound.GAME_OVER.play();
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
								createRandomItem(
										bombList.get(i).getPosition()[1],
										bombList.get(i).getPosition()[0] + j,
										iItemChance);
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
						// Überprueft Felder links der Bombe
						gameState = GameStates.GAMEOVER;
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1] - j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
						Sound.GAME_OVER.play();
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
								createRandomItem(
										bombList.get(i).getPosition()[1] - j,
										bombList.get(i).getPosition()[0],
										iItemChance);
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
						// Überprueft Felder rechts der Bombe
						gameState = GameStates.GAMEOVER;
						gameField
								.getField(bombList.get(i).getPosition()[1] + j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
						iDefeatedPlayer = gameField
								.getField(bombList.get(i).getPosition()[1] + j,
										bombList.get(i).getPosition()[0])
								.getPlayer().getID();
						Sound.GAME_OVER.play();
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
								createRandomItem(
										bombList.get(i).getPosition()[1] + j,
										bombList.get(i).getPosition()[0],
										iItemChance);
							}
							break;
						}
					} catch (Exception e) {

					}
				}

				explosionList.add(new long[1]);
				explosionList.get(0)[0] = Calendar.getInstance()
						.getTimeInMillis() + 500;
				gui.panel.addExplosions(exList); // explosionen werden an Gui
													// uebergeben
				exList = null;
				gameField.removeBomb(bombList.get(i));
				bombList.remove(i);
			}
		}
		for (int i = 0; i < explosionList.size(); i++) {

			// abgelaufene explosionen werden entfernt
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
		if (iPlayerCount == 1) { // Abfrage der Spieleranzahl. Gibt es nur einen
									// Spieler, so werden die Kontrollen fuer
									// den 2. Spieler deaktiviert.
			switch (key) {
			case 'w': // nach oben
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
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] - 1).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] - 1).removeBombItem();
					}

					gameField.removePlayer(player);
					player.moveUp();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'a': // nach links
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
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1] - 1,
								player.getPosition()[0]).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1] - 1,
								player.getPosition()[0]).removeBombItem();
					}
					gameField.removePlayer(player);
					player.moveLeft();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 's': // nach unten
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
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] + 1).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] + 1).removeBombItem();
					}
					gameField.removePlayer(player);
					player.moveDown();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'd': // nach rechts
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
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1] + 1,
								player.getPosition()[0]).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1] + 1,
								player.getPosition()[0]).removeBombItem();
					}
					gameField.removePlayer(player);
					player.moveRight();
					gameField.setPlayer(player);
					break;
				}
				break;
			case KeyEvent.VK_SPACE:// Bombe legen
				if (Bomb.getBombStatus() == false)
					bombList.add(new Bomb(player.getPosition()[0], player
							.getPosition()[1], time, player.getBombRadius()));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				if (Bomb.getBombStatus() == false) {
					Bomb.setCurrentPlacedBomb();
				}
				Bomb.setBombStatus();

				System.out.println(Bomb.getCurrentPlacedP2());
				break;

			case 'q':
				player.setBombRadius();
				break;
			case 'e':
				Bomb.setBombMax();
				break;
			}
			key = 0;
		} else { // Im 2 Spielermodus wird ausserdem die Spielerkollision
					// abgefragt, da
					// ein Spieler nicht durch den anderen durchgehen sollte als
					// waere er Luft
			switch (key) {
			case 'w': // nach oben
				switch (gameField.getField(player.getPosition()[1],
						player.getPosition()[0] - 1).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] - 1).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] - 1).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] - 1).removeBombItem();
					}
					gameField.removePlayer(player);
					player.moveUp();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'a': // nach links
				switch (gameField.getField(player.getPosition()[1] - 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1] - 1,
								player.getPosition()[0]).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1] - 1,
							player.getPosition()[0]).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1] - 1,
								player.getPosition()[0]).removeBombItem();
					}

					gameField.removePlayer(player);
					player.moveLeft();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 's': // nach unten
				switch (gameField.getField(player.getPosition()[1],
						player.getPosition()[0] + 1).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] + 1).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1],
							player.getPosition()[0] + 1).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1],
								player.getPosition()[0] + 1).removeBombItem();
					}
					gameField.removePlayer(player);
					player.moveDown();
					gameField.setPlayer(player);
					break;
				}
				break;
			case 'd': // nach rechts
				switch (gameField.getField(player.getPosition()[1] + 1,
						player.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 1;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isFireItem() == true) {
						player.setBombRadius();
						gameField.getField(player.getPosition()[1] + 1,
								player.getPosition()[0]).removeFireItem();
					}
					if (gameField.getField(player.getPosition()[1] + 1,
							player.getPosition()[0]).isBombItem() == true) {
						Bomb.setBombMax();
						gameField.getField(player.getPosition()[1] + 1,
								player.getPosition()[0]).removeBombItem();
					}
					gameField.removePlayer(player);
					player.moveRight();
					gameField.setPlayer(player);
					break;
				}
				break;
			case KeyEvent.VK_SPACE: // Bombe legen
				if (Bomb.getBombStatus() == false)
					bombList.add(new Bomb(player.getPosition()[0], player
							.getPosition()[1], time, player.getBombRadius()));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				if (Bomb.getBombStatus() == false) {
					Bomb.setCurrentPlacedBomb();
				}
				Bomb.setBombStatus();

				System.out.println(Bomb.getCurrentPlacedP2());

				break;
			case 'q':
				player.setBombRadius();
				break;
			case 'e':
				Bomb.setBombMax();
				break;
			case 'u':
				player2.setBombRadius();
				break;
			case 'i': // nach oben (2ter Spieler)
				switch (gameField.getField(player2.getPosition()[1],
						player2.getPosition()[0] - 1).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).isFireItem() == true) {
						player2.setBombRadius();
						gameField.getField(player2.getPosition()[1],
								player2.getPosition()[0] - 1).removeFireItem();
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] - 1).isBombItem() == true) {
						Bomb.setBombMaxP2();
						gameField.getField(player2.getPosition()[1],
								player2.getPosition()[0] - 1).removeBombItem();
					}
					gameField.removePlayer(player2);
					player2.moveUp();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case 'j': // nach links (2ter Spieler)
				switch (gameField.getField(player2.getPosition()[1] - 1,
						player2.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).isFireItem() == true) {
						player2.setBombRadius();
						gameField.getField(player2.getPosition()[1] - 1,
								player2.getPosition()[0]).removeFireItem();
					}
					if (gameField.getField(player2.getPosition()[1] - 1,
							player2.getPosition()[0]).isBombItem() == true) {
						Bomb.setBombMaxP2();
						gameField.getField(player2.getPosition()[1] - 1,
								player2.getPosition()[0]).removeBombItem();
					}
					gameField.removePlayer(player2);
					player2.moveLeft();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case 'k': // nach unten (2ter Spieler)
				switch (gameField.getField(player2.getPosition()[1],
						player2.getPosition()[0] + 1).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).isFireItem() == true) {
						player2.setBombRadius();
						gameField.getField(player2.getPosition()[1],
								player2.getPosition()[0] + 1).removeFireItem();
					}
					if (gameField.getField(player2.getPosition()[1],
							player2.getPosition()[0] + 1).isBombItem() == true) {
						Bomb.setBombMaxP2();
						gameField.getField(player2.getPosition()[1],
								player2.getPosition()[0] + 1).removeBombItem();
					}
					gameField.removePlayer(player2);
					player2.moveDown();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case 'l': // nach rechts (2ter Spieler)
				switch (gameField.getField(player2.getPosition()[1] + 1,
						player2.getPosition()[0]).getContent()) {
				case 1:
					if (gameField.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).getBomb() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).getPlayer() != null) {
						break;
					}
					if (gameField.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).isExit() == true) {
						iWinningPlayer = 2;
						gameState = GameStates.VICTORY;
					}
					if (gameField.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).isFireItem() == true) {
						player2.setBombRadius();
						gameField.getField(player2.getPosition()[1] + 1,
								player2.getPosition()[0]).removeFireItem();
					}
					if (gameField.getField(player2.getPosition()[1] + 1,
							player2.getPosition()[0]).isBombItem() == true) {
						Bomb.setBombMaxP2();
						gameField.getField(player2.getPosition()[1] + 1,
								player2.getPosition()[0]).removeBombItem();
					}
					gameField.removePlayer(player2);
					player2.moveRight();
					gameField.setPlayer(player2);
					break;
				}
				break;
			case KeyEvent.VK_ENTER: // Bombe legen (2ter Spieler)
				if (Bomb.getBombStatusP2() == false)
					bombList.add(new Bomb(player2.getPosition()[0], player2
							.getPosition()[1], time, player2.getBombRadius()));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				if (Bomb.getBombStatusP2() == false) {
					Bomb.setCurrentPlacedBombP2();
				}
				Bomb.setBombStatusP2();

				System.out.println(Bomb.getCurrentPlacedP2());

				break;
			case 'o':
				Bomb.setBombMaxP2();
				break;
			}
			key = 0;
		}
	}

	private void createRandomItem(int pos1, int pos2, int chance) {
		if ((Math.random() * 100) > chance) {
			return;
		}
		if ((Math.random()) < 0.5) {
			gameField.getField(pos1, pos2).setFireItem();
		} else {
			gameField.getField(pos1, pos2).setBombItem();
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
		Sound.init();
		Sound.volume = Sound.Volume.LOW;

	}
}
