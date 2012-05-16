package Field;

/*
 * Field.java
 * 
 * Version 1
 * 
 * © Alexander Hering
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Objects.bomb;
import Objects.player;

public class Field {
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private FieldContent map[][];
	private final int iOldPos[];

	public Field() {
		EMPTY = 0;
		FREE = 1;
		WALL = 2;
		EXIT = 3;
		BOMB = 4;
		PLAYER = 5;
		iOldPos = new int[2];
	}

	/**
	 * Fuegt neue Map in field ein
	 * 
	 * @param newMap: einzufuegende Map
	 */
	public void insertMap(FieldContent newMap[][]) {
		map = newMap;
	}

	/**
	 * Gibt Wert des angegebenen Feldes als int zurueck
	 * 
	 * @param iXCoord: xKoordinate des Feldes
	 * @param iYCoord: yKoordinate des Feldes
	 * @return Wert des angegeben Feldes. Gibt -1 zurueck, falls das Feld
	 *         nicht existiert
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
	 * @param iXCoord: x-Koordinate des Feldes
	 * @param iYCoord: y-Koordinate des Feldes
	 * @return Inhalt des angegebenen Feldes. Gibt null zurueck,
	 *         falls das Feld nicht existiert
	 */
	public FieldContent getField(int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			return map[iXCoord][iYCoord];
		} else {
			return null;
		}
	}

	/**
	 * Fuegt Bombe an angegebener Stelle ein
	 * 
	 * @param newBomb: einzufuegende Bombe
	 * @param iXCoord: x-Koordinate des Feldes
	 * @param iYCoord: y-Koordinate des Feldes
	 */
	public void setBomb(bomb newBomb, int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			map[iXCoord][iYCoord].insertBomb(newBomb);
		} else {
			return;
		}
	}

	/**
	 * 
	 * Fuegt Spieler an angegebener Stelle ein
	 * 
	 * @param newPlayer: einzufuegender Spieler
	 * @param iXCoord: x-Koordinate des Feldes
	 * @param iYCoord: y-Koordinate des Feldes
	 */
	public void setPlayer(player newPlayer, int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			map[iXCoord][iYCoord].insertPlayer(newPlayer);
		} else {
			return;
		}
	}

	/**
	 * Ueberprueft ob angegebene Zelle existiert
	 * 
	 * @param iXCoord: x-Koordinate des Feldes
	 * @param iYCoord: y-Koordinate des Feldes
	 * @return gibt true zurueck, falls Zelle existiert. Gibt sonst false zurueck
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
	 * @return gibt komplette Map zurueck. Gibt null zurueck falls keine Map existiert.
	 */
	public FieldContent[][] getMap() {
		return map;
	}

	/**
	 * Speichert Map als Datei
	 * 
	 * @param sFileName: Name der Datei
	 * @param iSaveModus: Legt fest wie die Datei gespeichert werden soll.
	 *        0: normale Speicherung, 1: komprimierte Speicherung. Geht von 0
	 *        aus, wenn Angabe != 0 oder 1
	 */
	public void saveMap(String sFileName, int iSaveModus) {
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
	/*
	 * public void updatePlayer (int[] iOldPos, player updPlayer) {
	 * map[iOldPos[0]][iOldPos[1]].removePlayer();
	 * map[updPlayer.getPosition()[0]
	 * ][updPlayer.getPosition()[1]].insertPlayer(updPlayer); }
	 */

	// die folgende Methode ist als ueberlegung zur fluessigen Bewegung des
	// Spielers entstanden

	/*
	 * public void updatePlayer(float fOldPos[], player updPlayer) { int
	 * iPlayerID = updPlayer.getID(); iOldPos[0] = (int)fOldPos[0]; iOldPos[1] =
	 * (int)fOldPos[1]; if(map[iOldPos[0]][iOldPos[1]].getPlayer() != null) {
	 * if(map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID) {
	 * map[iOldPos[0]][iOldPos[1]].removePlayer(); } }
	 * 
	 * if(map[iOldPos[0] + 1][iOldPos[1]].getPlayer() != null) {
	 * if(map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID) {
	 * map[iOldPos[0]][iOldPos[1]].removePlayer(); } }
	 * 
	 * if(map[iOldPos[0]][iOldPos[1] + 1].getPlayer() != null) {
	 * if(map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID) {
	 * map[iOldPos[0]][iOldPos[1]].removePlayer(); } }
	 * 
	 * map[Math.round(updPlayer.getPosition()[0])][Math.round(updPlayer.getPosition
	 * ()[1])].insertPlayer(updPlayer);
	 * 
	 * }
	 */

}