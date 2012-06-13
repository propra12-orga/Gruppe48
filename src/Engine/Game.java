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
	 * Startet das Spiel neu, genereiert, falls angegeben, neue Maps Setzt die
	 * Spieler auf ihre Startpositionen und entfernt sämtliche
	 * Explosionsueberreste
	 */
	public boolean restart() {
		Field field;
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
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		generatedField.insertMap(testGenerator.createSquareMap(15));
		cacheField.insertMap(generatedField.getMap());
		return generatedField;
	}

	/**
	 * Erzeugt eine neue Map aus einer Datei
	 * 
	 * @param sMap
	 *            Pfad zur Datei
	 * @return Erzeugtes Spielfeld. Gibt null zurueck, falls geladenes Spielfeld
	 *         nicht den Vorgaben entspricht
	 */
	public static Field createNewField(String sMap) {
		int iMaxPlayersLoaded = 0;
		FieldGenerator testGenerator = new FieldGenerator();
		Field generatedField = new Field();
		Field fileTester = new Field();
		cacheField = new Field();
		fileTester.insertMap(testGenerator.readMap(sMap));
		if (fileTester.getMap() != null) { // Die Anzahl der moeglichen
											// Spielerstartplätze wird
											// ueberprueft
			for (int i = 0; i < fileTester.getMap().length; i++) {
				for (int j = 0; j < fileTester.getMap()[0].length; j++) {
					if (fileTester.getMap()[i][j].getContent() == 5) {
						iMaxPlayersLoaded++;
					}
				}
			}
			if (iMaxPlayersLoaded > 0) { // Gibt es keine Startplätze fuer
											// Spieler, so wird die Map
											// abgelehnt
				generatedField.insertMap(testGenerator.readMap(sMap));
				iMaxPlayers = iMaxPlayersLoaded;
			} else {
				gui.showError("Die Map ist unspielbar, da kein Spieler vorhanden ist");
				setMapLoaded(false);
				return null;
			}
		} else { // Gibt es Fehler beim Einlesen der Datei, so wird diese
					// abgelehnt
			gui.showError("Die Map enthaelt ungueltige Zeichen oder ist nicht mehr vorhanden! Vorgang wird abgebrochen.");
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
						// Überprueft Felder links der Bombe
						gameState = GameStates.GAMEOVER;
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
		if (iPlayerCount == 1) { // Abfragt der Spieleranzahl. Gibt es nur einen
									// Spieler, so werden die Kontrollen fuer
									// den
									// 2. Spieler deaktiviert.
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
							player.getPosition()[0] - 1).getPlayer() != null) {
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
							player.getPosition()[0]).getPlayer() != null) {
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
							player.getPosition()[0] + 1).getPlayer() != null) {
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
							player.getPosition()[0]).getPlayer() != null) {
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
							player2.getPosition()[0] - 1).getPlayer() != null) {
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
							player2.getPosition()[0]).getPlayer() != null) {
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
							player2.getPosition()[0] + 1).getPlayer() != null) {
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
							player2.getPosition()[0]).getPlayer() != null) {
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
