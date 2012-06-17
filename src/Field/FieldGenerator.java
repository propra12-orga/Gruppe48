package Field;

/**
 * FieldContent.java
 *
 * @author Alexander Hering
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FieldGenerator {
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER, STONE;
	private FieldContent Map[][];
	private float fRandomChance;
	private int iModus, iRandomAmount;
	private boolean bCreateExit;

	// public int DENSITY;

	// private int densityRandomChance;
	private double densityStartChance;
	private double density;
	private double dRandomchance;

	public FieldGenerator() {

		EMPTY = 0;
		FREE = 1;
		WALL = 2;
		EXIT = 3;
		BOMB = 4;
		PLAYER = 5;
		STONE = 6;
		iModus = 0;
		fRandomChance = 0;
		iRandomAmount = 0;
		bCreateExit = true;
		densityStartChance = 75;

	}

	/**
	 * Erzeugt rechteckige Map abhaengig von iModus
	 * 
	 * @param iWidth
	 *            Breite der zu erstellenden Map
	 * @param iHeight
	 *            Hoehe der zu erstellenden Map
	 * @return Erzeugte Map
	 */

	public FieldContent[][] createRectangleMap(int iWidth, int iHeight,
			double dRandomchance) {
		Map = null;

		Map = new FieldContent[iWidth][iHeight];
		for (int i = 0; i < iWidth; i++) {
			for (int j = 0; j < iHeight; j++) {
				Map[i][j] = new FieldContent();
			}
		}

		for (int i = 0; i < iWidth; i++) {
			for (int j = 0; j < iHeight; j++) {
				Map[i][j].setContent(FREE);
			}
		}
		for (int i = 0; i < iWidth; i++) {
			for (int j = 0; j < iHeight; j++) {
				if ((i == 0) || (i == iWidth - 1) || (j == 0)
						|| (j == iHeight - 1)) {
					Map[i][j].setContent(WALL);
				}

				else if ((i >= 3 && i <= iWidth) || (j >= 3 && j <= iHeight)) {
					if (Math.random() <= dRandomchance / 100) // hier kann
						// man die
						// DICHTE
						// der
						// zerstoerbaren Bloecke
						// veraendern
						Map[i][j].setContent(STONE);

				}
			}
		}

		setWalls();
		if (bCreateExit)
			createRandomExit();
		Map[1][1].setContent(PLAYER);
		Map[iWidth - 2][iHeight - 2].setContent(PLAYER);
		Map[iWidth - 3][iHeight - 2].setContent(FREE);
		Map[iWidth - 2][iHeight - 3].setContent(FREE);
		return Map;
	}

	/**
	 * Erzeugt quadratische Map
	 * 
	 * @param iSize
	 *            Seitenlaenge der zu erzeugenden Map
	 * @return Erzeugte Map
	 */
	public FieldContent[][] createSquareMap(int iSize, double dRandomchance) {
		return createRectangleMap(iSize, iSize, dRandomchance);
	}

	/**
	 * Setzt Zufallschance fuer Modus 1 auf angegebenen Wert
	 * 
	 * @param fChance
	 *            Chance in Prozent
	 */
	public void setRandomChance(float fChance) {
		fRandomChance = fChance / 100;
	}

	/**
	 * Setzt Anzahl der Bloecke fuer Modus 2 auf angegebenen Wert
	 * 
	 * @param iAmount
	 *            Anzahl der zu setzenden Bloecke
	 */
	public void setRandomAmount(int iAmount) {
		iRandomAmount = iAmount;
	}

	/**
	 * Aendert Modus der Spielfeldgenerierung Modus 0: Jedes 2. Feld in jeder 2.
	 * Zeile wird mit einem festen Block versehen Modus 1: Jedes 2. Feld in
	 * jeder 2. Zeile wird mit einer Chance von fChance % mit einem festen Block
	 * versehen Modus 2: Es werden auf zufaelligen Feldern feste Bloecke
	 * platziert, bis das Spielfeld voll ist oder iAmount erreicht wurde
	 * 
	 * @param iStatus
	 *            Gewuenschter Modus. Wird zu 0, wenn iStatus < 0 oder iStatus >
	 *            2
	 */
	public void setModus(int iStatus) {
		if ((iStatus >= 0) && (iStatus <= 2)) {
			iModus = iStatus;
		} else {
			iModus = 0;
		}
	}

	/**
	 * Legt fest, ob ein Ausgang erzeugt werden soll
	 * 
	 * @param bSetExit
	 *            Falls true wird ein Ausgang erzeugt
	 */
	public void createExit(boolean bSetExit) {
		bCreateExit = bSetExit;
	}

	/**
	 * Liest Map aus Datei aus und gibt sie als FieldContent[][] zurueck.
	 * Erkennt automatisch ob die Map komprimiert oder unkomprimiert vorliegt.
	 * 
	 * @param sInputFile
	 *            Name der auszulesenden Datei
	 * @return Eingelesene Map
	 */
	public FieldContent[][] readMap(String sInputFile) {
		int iCounter = 0;
		FileReader inputFile;
		BufferedReader reader;
		List<String> mapList = new ArrayList<String>();
		try {
			inputFile = new FileReader(sInputFile);
		} catch (FileNotFoundException e) {
			return null;
		}
		reader = new BufferedReader(inputFile);

		try {
			while (reader.ready()) {
				mapList.add(reader.readLine());
			}
		} catch (IOException e) {
			return null;
		}

		try {
			reader.close();
			inputFile.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		Map = null;
		iCounter = mapList.get(0).length();
		for (int i = 1; i < mapList.size(); i++) {
			// laengste Zeile wird ausgesucht um die Groeße deszu erstellenden
			// Arrays zu ermitteln
			if (iCounter < mapList.get(i).length()) {
				iCounter = mapList.get(i).length();
			}
		}
		Map = new FieldContent[iCounter][mapList.size()];
		// Array wird erzeugt und initialisiert
		for (int i = 0; i < mapList.size(); i++) {
			for (int j = 0; j < iCounter; j++) {
				Map[j][i] = new FieldContent();
				Map[j][i].setContent(EMPTY);
			}
		}
		iCounter = 0;
		while (mapList.size() > 0) {
			for (int i = 0; i < mapList.get(0).length(); i++) {
				// Jedes Zeichen jeder Zeile wird ausgelesen udn der Inhalt der
				// entsprechenden ArrayZellen gesetzt
				switch (mapList.get(0).charAt(i)) {
				case 32: // ' '
					Map[i][iCounter].setContent(EMPTY);
					break;
				case 35: // '#'
					Map[i][iCounter].setContent(WALL);
					break;
				case 46: // '.'
					Map[i][iCounter].setContent(FREE);
					break;
				case 69: // 'E'
					Map[i][iCounter].setContent(FREE);// fuer sichtbaren Ausgang
					Map[i][iCounter].setExit();
					break;
				case 72: // 'H'
					Map[i][iCounter].setContent(STONE);// fuer versteckten
														// Ausgang
					Map[i][iCounter].setExit();
					break;
				case 80: // 'P'
					Map[i][iCounter].setContent(PLAYER);
					break;
				case 37: // '%'
					Map[i][iCounter].setContent(STONE);
					break;
				default: // Trifft der Algorithmus auf ein nicht bekanntes
							// Zeichen, so wird der Vorang abgebrochen
					return null;
				}
			}
			iCounter++;
			mapList.remove(0);
		}
		return Map;
	}

	/**
	 * Liest Map aus Datei aus, erzeugt Bloecke und Ausgang und gibt sie als
	 * FieldContent[][] zurueck. Erkennt automatisch ob die Map komprimiert oder
	 * unkomprimiert vorliegt. Zum einlesene von Maps gedacht, bei denen nur der
	 * Rand, nicht aber der Inhalt festgelegt ist.
	 * 
	 * @param sInputFile
	 *            Name der auszulesenden Datei
	 * @return Eingelesene Map
	 */
	public FieldContent[][] readFillEmptyMap(String sInputFile) {
		Map = readMap(sInputFile);
		setWalls();
		createRandomExit();
		return Map;
	}

	/**
	 * Interne Mothode zum einlesen komprimierter Maps.
	 * 
	 * @param reader
	 *            BufferedReader mit eingelesener Map als Inhalt
	 * @return Eingelesene Map
	 */
	private FieldContent[][] readCompressedMap(BufferedReader reader) {
		String sInputPart;
		String sCount = "";
		StringTokenizer tokenizer;
		int iCounterX = 0;
		int iCounterY = 0;
		int iInsert;
		try {
			tokenizer = new StringTokenizer(reader.readLine(), "&");
		} catch (IOException e) {
			return null;
		}
		sInputPart = tokenizer.nextToken();
		for (int i = 0; i < sInputPart.length(); i++) {
			if ((sInputPart.charAt(i) > 47) && (sInputPart.charAt(i) < 58)) {
				sCount += String.valueOf(sInputPart.charAt(i));
			} else {
				if (sCount != "") {
					iCounterX += Integer.parseInt(sCount);
				} else {
					iCounterX++;
					sCount = "";
				}
			}
		}
		Map = null;
		Map = new FieldContent[tokenizer.countTokens() + 1][iCounterX];
		for (int i = 0; i < tokenizer.countTokens() + 1; i++) {
			for (int j = 0; j < iCounterX; j++) {
				Map[i][j] = new FieldContent();
				Map[i][j].setContent(EMPTY);
			}
		}
		iCounterX = 0;
		sCount = "";
		while (tokenizer.hasMoreTokens()) {
			if (iCounterY > 0) {
				sInputPart = tokenizer.nextToken();
			}
			for (int i = 0; i < sInputPart.length(); i++) {
				if ((sInputPart.charAt(i) > 47) && (sInputPart.charAt(i) < 58)) {
					sCount += String.valueOf(sInputPart.charAt(i));
				} else {
					if (sCount == "") {
						switch (sInputPart.charAt(i)) {
						case 32: // ' '
							Map[iCounterY][iCounterX].setContent(EMPTY);
							break;
						case 35: // '#'
							Map[iCounterY][iCounterX].setContent(WALL);
							break;
						case 46: // '.'
							Map[iCounterY][iCounterX].setContent(FREE);
							break;
						case 69: // 'E'
							Map[iCounterY][iCounterX].setContent(EXIT);
							break;
						default:
							Map[iCounterY][iCounterX].setContent(EMPTY);
							break;
						}
						iCounterX++;
					} else {
						switch (sInputPart.charAt(i)) {
						case 32: // ' '
							iInsert = EMPTY;
							break;
						case 35: // '#'
							iInsert = WALL;
							break;
						case 46: // '.'
							iInsert = FREE;
							break;
						case 69: // 'E'
							iInsert = EXIT;
							break;
						default:
							iInsert = EMPTY;
							break;
						}
						for (int j = iCounterX; j < (iCounterX + Integer
								.parseInt(sCount)); j++) {
							Map[iCounterY][j].setContent(iInsert);
						}
						iCounterX += Integer.parseInt(sCount);
						sCount = "";
					}
				}

			}
			iCounterX = 0;
			iCounterY++;
		}
		return Map;
	}

	/**
	 * Ueberprueft alle Felder in einem angegebenen Radius auf Vorkommen oder
	 * nicht Vorkommen eines Blocks.
	 * 
	 * @param iCheckWhereX
	 *            X-Koordinate des Suchstarts
	 * @param iCheckWhereY
	 *            Y-Koordinate des Suchstarts
	 * @param iCheckHowFar
	 *            Radius der Suche
	 * @param iCheckForWhat
	 *            Typ des zu suchenden Blocks
	 * @param bCheckIfThere
	 *            Wird hier true angegeben, so wird ueberprueft, ob der
	 *            angegebene Block vorhanden ist. Wird false angegeben, so wird
	 *            ueberprueft ob der Block nicht vorhanden ist.
	 * @return Gibt true zurueck wenn der gesuchte Block gefunden wurde und
	 *         bCheckIfThere = true oder wenn der gesuchte Block nicht gefunden
	 *         wurde und bCheckIfThere = false. Gibt sonst false zurueck.
	 */
	private boolean checkSurroundings(int iCheckWhereX, int iCheckWhereY,
			int iCheckHowFar, int iCheckForWhat, boolean bCheckIfThere) {
		for (int i = iCheckWhereX - iCheckHowFar; i <= iCheckWhereX
				+ iCheckHowFar; i++) {
			for (int j = iCheckWhereY - iCheckHowFar; j <= iCheckWhereY
					+ iCheckHowFar; j++) {
				if ((i >= 0) && (j >= 0)) {
					if (Map[i][j].getContent() == iCheckForWhat == bCheckIfThere) {
						return true;
					}
				}
			}

		}
		return false;
	}

	/**
	 * Zaehlt die Anzahl der freien Felder
	 * 
	 * @return Anzahl der freien Felder
	 */
	private int iCountFreeSpace() {
		int iCount = 0;
		for (int i = 0; i < Map.length; i++) {
			for (int j = 0; j < Map[0].length; j++) {
				if ((Map[i][j].getContent() == STONE)
						|| (Map[i][j].getContent() == FREE)) {
					iCount++;
				}
			}
		}
		return iCount;
	}

	/**
	 * Erzeugt auf einem zufaelligen freien Feld einen Ausgang
	 */
	private void createRandomExit() {
		int iRand = (int) (Math.random() * iCountFreeSpace());
		for (int i = 0; i < Map.length; i++) {
			for (int j = 0; j < Map[0].length; j++) {
				if ((Map[i][j].getContent() == STONE)
						|| (Map[i][j].getContent() == FREE)) {
					iRand--;
					if (iRand == 0) {
						Map[i][j].setExit();
						return;
					}
				}
			}
		}

	}

	/**
	 * Erzeugt Abhaengig von iModus, Waende auf der Map. Fuer genauere
	 * Beschreibung der Modi siehe setModus()
	 */
	private void setWalls() {
		switch (iModus) {
		case 0:
			for (int i = 0; i < Map.length; i++) {
				for (int j = 0; j < Map[0].length; j++) {
					if ((i % 2 == 0) && (j % 2 == 0)
							&& (Map[i][j].getContent() != EMPTY)) {
						Map[i][j].setContent(WALL);
					}
				}
			}
			break;

		case 1:
			if (iModus == 1) {
				for (int i = 0; i < Map.length; i++) {
					for (int j = 0; j < Map[0].length; j++) {
						if (checkSurroundings(i, j, 1, WALL, true) == false) {
							if (Math.random() <= fRandomChance) {
								Map[i][j].setContent(WALL);
							}
						}
					}
				}
			}
			break;

		case 2:
			if (iModus == 2) {
				int iPosition = 0;
				int iRandomPosition;
				int iRandom[] = new int[2];
				int iTmpRandomAmount = iRandomAmount;
				List<int[]> iRandomList = new ArrayList<int[]>();
				for (int i = 2; i < Map.length - 2; i++) {
					for (int j = 2; j < Map[0].length - 2; j++) {
						if ((i % 2 == 0) && (j % 2 == 0)) {
							iRandomList.add(new int[2]);
							iRandomList.get(iPosition)[0] = i;
							iRandomList.get(iPosition)[1] = j;
							iPosition++;
						}
					}
				}
				if (iTmpRandomAmount > iRandomList.size()) {
					iTmpRandomAmount = iRandomList.size();
				}
				while (iTmpRandomAmount > 0) {
					iRandomPosition = (int) (Math.random() * iRandomList.size());
					iRandom = iRandomList.get(iRandomPosition);
					Map[iRandom[0]][iRandom[1]].setContent(WALL);
					iRandomList.remove(iRandomPosition);
					iTmpRandomAmount--;
				}
			}
		}
	}

}