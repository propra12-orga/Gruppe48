package Field;

/**
 * Field.java
 * 
 * @author Alexander Hering
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import Objects.Bomb;
import Objects.Player;

/**
 * 
 * @author Alexander Die Field Klass enthält Methoden um ein in ihr abgelegtes
 *         Feld auszulesen oder zu verändern.
 */
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER, STONE;
	private FieldContent map[][];

	/**
	 * Erzeugt ein Objekt der Klasse Field
	 */
	public Field() {
		EMPTY = 0;
		FREE = 1;
		WALL = 2;
		EXIT = 3;
		BOMB = 4;
		PLAYER = 5;
		STONE = 6;
	}

	/**
	 * Kopiert den Inhalt eines uebergebenen, 2-dimensionalen FieldContent
	 * Arrays in ein neu erzeugtes Array und speichert dies als neue Map.
	 * 
	 * @param newMap
	 *            Einzufuegende Map
	 */
	public void insertMap(FieldContent newMap[][]) {
		if (newMap == null) {
			map = null;
			return;
		}
		map = new FieldContent[newMap.length][newMap[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				map[i][j] = new FieldContent();
		}
		for (int i = 0; i < newMap.length; i++)
			for (int j = 0; j < newMap[0].length; j++) {
				map[i][j].setContent(newMap[i][j].getContent());
				map[i][j].insertPlayer(newMap[i][j].getPlayer());
				map[i][j].insertBomb(newMap[i][j].getBomb());
				if (newMap[i][j].isExit())
					map[i][j].setExit();
				if (newMap[i][j].isFireItem())
					map[i][j].setFireItem();

				if (newMap[i][j].isBombItem())
					map[i][j].setBombItem();
			}
	}

	/**
	 * Gibt Wert des angegebenen Feldes als int zurueck
	 * 
	 * @param iXCoord
	 *            X-Koordinate des Feldes
	 * @param iYCoord
	 *            Y-Koordinate des Feldes
	 * @return Wert des angegeben Feldes. Gibt -1 zurueck, falls das Feld nicht
	 *         existiert
	 */
	public int iGetContent(int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			return map[iXCoord][iYCoord].getContent();
		} else {
			return -1;
		}
	}

	/**
	 * Gibt Wert des angegebenen Feldes als FieldContent zurueck
	 * 
	 * @param iXCoord
	 *            X-Koordinate des Feldes
	 * @param iYCoord
	 *            Y-Koordinate des Feldes
	 * @return Inhalt des angegebenen Feldes. Gibt null zurueck, falls das Feld
	 *         nicht existiert
	 */
	public FieldContent getField(int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			return map[iXCoord][iYCoord];
		} else {
			return null;
		}
	}

	/**
	 * Fuegt Bombe an in Bombe gespeicherter Stelle ein
	 * 
	 * @param newBomb
	 *            Einzufuegende Bombe
	 */
	public void setBomb(Bomb newBomb) {
		if (bInBounds(newBomb.getPosition()[1], newBomb.getPosition()[0])) {
			map[newBomb.getPosition()[1]][newBomb.getPosition()[0]]
					.insertBomb(newBomb);

		} else {
			return;
		}
	}

	/**
	 * 
	 * Fuegt Spieler an in Spieler gespeicherter Stelle ein
	 * 
	 * @param newPlayer
	 *            Einzufuegender Spieler
	 */
	public void setPlayer(Player newPlayer) {
		if (bInBounds(newPlayer.getPosition()[1], newPlayer.getPosition()[0])) {
			map[newPlayer.getPosition()[1]][newPlayer.getPosition()[0]]
					.insertPlayer(newPlayer);
		} else {
			return;
		}
	}

	/**
	 * Entfernt Spieler
	 * 
	 * @param player
	 *            Zu entfernender Spieler
	 */
	public void removePlayer(Player player) {
		map[player.getPosition()[1]][player.getPosition()[0]].removePlayer();
	}

	/**
	 * Entfernt Bombe
	 * 
	 * @param bomb
	 *            Zu entfernende Bombe
	 */
	public void removeBomb(Bomb bomb) {
		map[bomb.getPosition()[1]][bomb.getPosition()[0]].removeBomb();
		Bomb.bombStatus = false; // gibt an, ob aktuell eine Bombe auf dem
									// Spielfeld
		// liegt (Spieler1)
		Bomb.bombStatusP2 = false;// gibt an, ob aktuell eine Bombe auf dem
									// Spielfeld
		// liegt (Spieler2)
		Bomb.currentPlaced = 0;
		Bomb.currentPlacedP2 = 0;

	}

	/**
	 * Fuegt angegebenen Inhalt in gewaehltes Feld ein
	 * 
	 * @param iX
	 *            X-Koordinate des Feldes
	 * @param iY
	 *            Y-Koordinate des Feldes
	 * @param iContent
	 *            Einzufuegender Inhalt. Erwartet Angabe zwischen 0 und 3
	 */
	public void setField(int iX, int iY, int iContent) {
		map[iX][iY].setContent(iContent);
	}

	/**
	 * Ueberprueft ob angegebene Zelle existiert
	 * 
	 * @param iXCoord
	 *            X-Koordinate des Feldes
	 * @param iYCoord
	 *            Y-Koordinate des Feldes
	 * @return Gibt true zurueck, falls Zelle existiert. Gibt sonst false
	 *         zurueck
	 */
	public boolean bInBounds(int iXCoord, int iYCoord) {
		if ((iXCoord >= 0) && (iXCoord < map.length) && (iYCoord >= 0)
				&& (iYCoord < map[0].length)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gibt komplette Map zurueck
	 * 
	 * @return Gibt komplette Map zurueck. Gibt null zurueck falls keine Map
	 *         existiert.
	 */
	public FieldContent[][] getMap() {
		return map;
	}

	/**
	 * Speichert Map als Datei
	 * 
	 * @param sOutputFileName
	 *            Name der Datei
	 * @param iSaveModus
	 *            Legt fest wie die Datei gespeichert werden soll. 0: normale
	 *            Speicherung, 1: komprimierte Speicherung. Geht von 0 aus, wenn
	 *            Angabe != 0 oder 1
	 */
	public void saveMap(String sOutputFileName, int iSaveModus) {
		String sFileName = "./src/maps/" + sOutputFileName;
		FileWriter output;
		BufferedWriter writer;
		try {
			output = new FileWriter(sFileName);
			writer = new BufferedWriter(output);
		} catch (IOException e) {
			return;
		}
		switch (iSaveModus) {
		case 0:
			try {
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						switch (map[i][j].getContent()) {
						case 0:
							writer.write(32); // ' '
							break;
						case 1:
							writer.write(46); // '.'
							break;
						case 2:
							writer.write(35); // '#'
							break;
						case 3:
							writer.write(69); // 'E'
							break;
						case 5:
							writer.write(80); // 'P'
							break;
						case 6:
							writer.write(37); // '%'
							break;
						}
					}
					writer.newLine();
				}
			} catch (IOException e) {
				System.out.println(e);
				return;
			}
			break;
		case 1:
			try {
				writer.write(compressMap());
			} catch (IOException e) {
				return;
			}
		default:
			try {
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						switch (map[i][j].getContent()) {
						case 0:
							writer.write(32); // ' '
							break;
						case 1:
							writer.write(46); // '.'
							break;
						case 2:
							writer.write(35); // '#'
							break;
						case 3:
							writer.write(69); // 'E'
							break;
						}
					}
					writer.newLine();
				}
			} catch (IOException e) {
				System.out.println(e);
				return;
			}
		}
		try {
			writer.flush();
			writer.close();
			output.close();
		} catch (IOException e) {
			System.out.println(e);
			return;
		}
	}

	/**
	 * Gibt die Map komprimiert zurueck
	 * 
	 * @return Gibt Map im komprimierten Format zurueck
	 */
	private String compressMap() {
		String sOutput = "c";
		int iCounter = 1;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if ((j + 1) < map[0].length) {
					if (map[i][j].getContent() == map[i][j + 1].getContent()) {
						iCounter++;
					} else {
						if (iCounter > 1) {
							sOutput += String.valueOf(iCounter);
							iCounter = 1;
						}
						switch (map[i][j].getContent()) {
						case 0:
							sOutput += " ";
							break;
						case 1:
							sOutput += ".";
							break;
						case 2:
							sOutput += "#";
							break;
						case 3:
							sOutput += "E";
							break;
						}
					}
				} else {
					if (iCounter > 1) {
						sOutput += String.valueOf(iCounter);
						iCounter = 1;
					}
					switch (map[i][j].getContent()) {
					case 0:
						sOutput += " ";
						break;
					case 1:
						sOutput += ".";
						break;
					case 2:
						sOutput += "#";
						break;
					case 3:
						sOutput += "E";
						break;
					}
				}
			}
			sOutput += "&";
		}
		return sOutput;
	}
}