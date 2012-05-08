import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class field {
	public static int x, y;
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private fieldContent Map[][];
	private int iOldPos[];

	public field() {
		EMPTY = 0;
		FREE = 1;
		WALL = 2;
		EXIT = 3;
		BOMB = 4;
		PLAYER = 5;
		iOldPos = new int[2];
		// Bezeichnungen werden in allen Spielfelddateien beibehalten
	}

	public void insertMap(fieldContent newMap[][]) {
		Map = newMap;
		// zum einfuegen von mit "fieldGenerator" erzeugten Karten
	}

	public int iGetContent(int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			return Map[iXCoord][iYCoord].getContent();
		} else {
			return -1;
		}
		// gibt den Inhalt der entsprechenden Zelle zurueck. Falls die Zelle
		// nicht existiert wird -1 zurueckgegeben
	}

	public fieldContent getField(int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			return Map[iXCoord][iYCoord];
		} else {
			return null;
		}
		// gibt angegebene Zelle zurueck. Gibt null zurueck, falls die Zelle
		// nicht existiert
	}

	public void setBomb(bomb newBomb, int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			Map[iXCoord][iYCoord].insertBomb(newBomb);
		} else {
			return;
		}
		// fuegt neue Bombe in entsprechender Zelle ein, falls die Zelle
		// existiert
	}

	public void setPlayer(player newPlayer, int iXCoord, int iYCoord) {
		if (bInBounds(iXCoord, iYCoord)) {
			Map[iXCoord][iYCoord].insertPlayer(newPlayer);
			x = iXCoord;
			y = iYCoord;
		} else {
			return;
		}
		// fuegt Spieler in entsprechender Zelle ein, falls die Zelle existiert
	}

	public boolean bInBounds(int iXCoord, int iYCoord) {
		if ((iXCoord >= 0) && (iXCoord < Map.length) && (iYCoord >= 0)
				&& (iYCoord < Map[0].length)) {
			return true;
		} else {
			return false;
		}
		// testet ob gewaehlte Zelle existiert
	}

	public fieldContent[][] getMap() {
		return Map;
	}

	public void saveMap(String sFileName, int iSaveModus) {
		// Anzahl der Modi: 2
		// Modus 0: Wandelt die Map in lesbareres Format um und speichert sie
		// als Datei
		// Modus 1: Komprimiert die Map und speichert sie als Datei
		FileWriter output;
		BufferedWriter writer;
		try {
			output = new FileWriter(sFileName);
			writer = new BufferedWriter(output);
		} catch (IOException e) {
			return;
		}
		// eine neue Datei wird erstellt und zum Schreiben bereit gehalten
		switch (iSaveModus) {
		case 0:
			try {
				for (int i = 0; i < Map.length; i++) {
					for (int j = 0; j < Map[0].length; j++) {
						switch (Map[i][j].getContent()) {
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
					// die Karte wir Feld fuer Feld ausgelesen, umgewandelt und
					// in die Datei geschrieben
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
		}
		try {
			writer.flush();
			writer.close();
			output.close();
			// die Aenderungen werden geschrieben und die Datei wird geschlossen
		} catch (IOException e) {
			System.out.println(e);
			return;
		}
	}

	public String compressMap() {
		String sOutput = "c";
		int iCounter = 1;
		for (int i = 0; i < Map.length; i++) {
			for (int j = 0; j < Map[0].length; j++) {
				if ((j + 1) < Map[0].length) {
					if (Map[i][j].getContent() == Map[i][j + 1].getContent()) {
						iCounter++;
						// ueberprueft, ob das aktuell betrachtete Zeichen und
						// das naechste Zeichen in der Reihe identisch sind
						// erhoeht einen counter, falls ja
					} else {
						if (iCounter > 1) {
							sOutput += String.valueOf(iCounter);
							iCounter = 1;
						}
						switch (Map[i][j].getContent()) {
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
					// wurde mehr als 1 gleiches Zeichen am Stueck gefunden, so
					// wird die gefundene Anzahl in den Outputstring geschrieben
					// unanhaengig davon wird anschliessend das letzte gefundene
					// Zeichen selbst geschrieben
				} else {
					if (iCounter > 1) {
						sOutput += String.valueOf(iCounter);
						iCounter = 1;
					}
					switch (Map[i][j].getContent()) {
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
					// gleiche Abfrage wie weiter oben, wird aber nur
					// aufgerufen, wenn das Ende einer Zeile erreicht ist.
					// wird benoetigt um einen Zugriffsfehler zu vermeiden
				}
			}
			sOutput += "&";
		}
		return sOutput;
	}
	/*
	 * public void updatePlayer (int[] iOldPos, player updPlayer) {
	 * Map[iOldPos[0]][iOldPos[1]].removePlayer(); <<<<<<< HEAD
	 * Map[updPlayer.getPosition
	 * (0)][updPlayer.getPosition(1)].insertPlayer(updPlayer); } =======
	 * Map[updPlayer
	 * .getPosition()[0]][updPlayer.getPosition()[1]].insertPlayer(updPlayer); }
	 */

	// die folgende Methode ist als ueberlegung zur fluessigen Bewegung des
	// Spielers entstanden

	/*
	 * public void updatePlayer(float fOldPos[], player updPlayer) { int
	 * iPlayerID = updPlayer.getID(); iOldPos[0] = (int)fOldPos[0]; iOldPos[1] =
	 * (int)fOldPos[1]; if(Map[iOldPos[0]][iOldPos[1]].getPlayer() != null) {
	 * if(Map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID) {
	 * Map[iOldPos[0]][iOldPos[1]].removePlayer(); } }
	 * 
	 * if(Map[iOldPos[0] + 1][iOldPos[1]].getPlayer() != null) {
	 * if(Map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID) {
	 * Map[iOldPos[0]][iOldPos[1]].removePlayer(); } }
	 * 
	 * if(Map[iOldPos[0]][iOldPos[1] + 1].getPlayer() != null) {
	 * if(Map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID) {
	 * Map[iOldPos[0]][iOldPos[1]].removePlayer(); } }
	 * 
	 * Map[Math.round(updPlayer.getPosition()[0])][Math.round(updPlayer.getPosition
	 * ()[1])].insertPlayer(updPlayer);
	 * 
	 * }
	 */

}
