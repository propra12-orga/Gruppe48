import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class fieldGenerator {

	private int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private fieldContent Map[][];
	private float fRandomChance;
	private int iModus, iRandomAmount;

	public fieldGenerator() {

		EMPTY = 0;
		FREE = 1;
		WALL = 2;
		EXIT = 3;
		BOMB = 4;
		PLAYER = 5;
		iModus = 0;
		// Anzahl der Modi: 3
		// Modus 0: Jedes 2. Feld in jeder 2. Spalte wird mit einer Mauer belegt
		// Modus 1: Jedes 2. Feld in jeder 2. Spalte wird mit einer Chance von
		// fRandomChance % mit einer Mauer belegt
		// Modus 2: Von allen mit Mauern belegbaren Feldern, (jedes 2. Feld in
		// jeder 2. Spalte), werden so lange zufaellig Felder mit Mauern belegt
		// bis iRandomAmount Felder belegt sind
		fRandomChance = 0;
		iRandomAmount = 0;
		// Bezeichnungen werden in allen Spielfelddateien beibehalten
	}

	public fieldContent[][] createRectangleMap(int iXSize, int iYSize) {
		Map = null;
		Map = new fieldContent[iXSize][iYSize];
		for (int i = 0; i < iXSize; i++) {
			for (int j = 0; j < iYSize; j++) {
				Map[i][j] = new fieldContent();
			}
		}

		for (int i = 0; i < iXSize; i++) {
			for (int j = 0; j < iYSize; j++) {
				Map[i][j].setContent(FREE);
			}
		}
		// Initialisierung des Spielfeldes
		for (int i = 0; i < iXSize; i++) {
			for (int j = 0; j < iYSize; j++) {
				if ((i == 0) || (i == iXSize - 1) || (j == 0)
						|| (j == iYSize - 1)) {
					Map[i][j].setContent(WALL);
					// Raender der Karte werden mit Mauern belegt
				}
			}
		}
		setWalls();
		createRandomExit();
		// Ausgang wird an zufaelliger Stelle eingefuegt
		return Map;
	}

	public fieldContent[][] createSquareMap(int iSize) {
		return createRectangleMap(iSize, iSize);
		// gibt Quadratische Map zurueck
	}

	public void setRandomChance(float fChance) {
		fRandomChance = fChance / 100;
		// erwartet Prozentangabe. Setzt die Zufallschance fuer Modus 1 auf die
		// angegebene Groesse
	}

	public void setRandomAmount(int iAmount) {
		iRandomAmount = iAmount;
		// setzt die Anzahl der zu setzenden Bloecke fuer Modus 2 auf den
		// angegebenen Wert
	}

	public void setModus(int iStatus) {
		iModus = iStatus;
		// aendert den Modus der Spielfeldgenerierung
	}

	public fieldContent[][] readMap(String sInputFile) {
		int iCounter = 0;
		FileReader inputFile;
		BufferedReader reader;
		List<String> mapList = new ArrayList<String>();
		try {
			inputFile = new FileReader(sInputFile);
		} catch (FileNotFoundException e) {
			System.out.println(e);
			return null;
		}
		reader = new BufferedReader(inputFile);
		// erstellt neuen Stream zum Zugriff auf Input Datei
		try {
			reader.mark(1);
			if (reader.read() == 99) {
				return readCompressedMap(reader);
			} else {
				reader.reset();
			}
			while (reader.ready()) {
				mapList.add(reader.readLine());
				// schreibt die gesamte Eingabe in eine Liste. Jeder
				// Listeneintrag entspricht einer Zeile
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
			if (iCounter < mapList.get(i).length()) {
				iCounter = mapList.get(i).length();
			}
			// vergleicht die Breiten aller Zeilen und schreibt den Maximalwert
			// in iCounter. Wird benoetigt, falls nicht rechteckige Muster
			// eingelesen werden
		}
		Map = new fieldContent[mapList.size()][iCounter];
		for (int i = 0; i < mapList.size(); i++) {
			for (int j = 0; j < iCounter; j++) {
				Map[i][j] = new fieldContent();
				Map[i][j].setContent(EMPTY);
			}
			// initialliesiert die Karte und setzt alle Felder auf Leer
		}
		iCounter = 0;
		while (mapList.size() > 0) {
			for (int i = 0; i < mapList.get(0).length(); i++) {
				switch (mapList.get(0).charAt(i)) {
				case 32: // ' '
					Map[iCounter][i].setContent(EMPTY);
					break;
				case 35: // '#'
					Map[iCounter][i].setContent(WALL);
					break;
				case 46: // '.'
					Map[iCounter][i].setContent(FREE);
					break;
				case 69: // 'E'
					Map[iCounter][i].setContent(EXIT);
					break;
				default:
					Map[iCounter][i].setContent(EMPTY);
					break;
				}
			}
			iCounter++;
			mapList.remove(0);
			// schreibt den Inhalt der eingelesenen Datei angepasst in das
			// Outputarray
		}
		return Map;
	}

	public fieldContent[][] readFillEmptyMap(String sInputFile) {
		Map = readMap(sInputFile);
		setWalls();
		createRandomExit();
		return Map;
		// liest Map ein und fuellt sie danach mit Waenden und Ausgang
		// gedacht zum Einlesen von Maps, bei denen nur die Form der Map gegeben
		// wurde
	}

	private fieldContent[][] readCompressedMap(BufferedReader reader) {
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
		// schreibt den gesamten Input in einen StringTokenizer und trennt die
		// einzelnen Elemente bei jedem &
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
		// berechnet die Anzahl an Elementen in der 1. Zeile um die
		// Spielfeldgroeße berechnen zu können
		Map = null;
		Map = new fieldContent[tokenizer.countTokens() + 1][iCounterX];
		for (int i = 0; i < tokenizer.countTokens() + 1; i++) {
			for (int j = 0; j < iCounterX; j++) {
				Map[i][j] = new fieldContent();
				Map[i][j].setContent(EMPTY);
			}
			// initialliesiert die Karte und setzt alle Felder auf Leer
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
					// Wird eine Zahl eingelesen, so wird sie zu einem String
					// hinzugefuegt um die Anzahl der zu schreibenden Zeichen zu
					// erhalten
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
						// wurde nur eine Zeichen ohne vorherige Zahl
						// eingelesen, so wird dieses an die entsprechende
						// Stelle in die Map geschrieben
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
						// wurde ein Zeichen mit vorheriger Zahl eingelesen, so
						// werden sowohl das aktuelle Feld als auch die
						// naechsten sCount - 1 Felder mit dem Zeichen
						// beschrieben
					}
				}

			}
			iCounterX = 0;
			iCounterY++;
		}
		return Map;
	}

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
		// in allen Feldern, die sich in einem Abstand von maximal iCheckHowFar
		// von [iCheckWhereX][iCheckWhereY] befinden wird das Vorhandensein von
		// iCheckForWhat geprueft.
		// ist bCheckIfThere auf true zurueckgesetzt, so wird true
		// zurueckgegeben, wenn das gesuchte Element mindestens einmal gefunden
		// wurde.
		// ist bCheckIfThere auf false gesetzt, so wird true zurueckgegeben,
		// wenn das gesuchte Element mindestens einmal nicht gefunden wurde.
	}

	private int iCountFreeSpace() {
		int iCount = 0;
		for (int i = 0; i < Map.length; i++) {
			for (int j = 0; j < Map[0].length; j++) {
				if (Map[i][j].getContent() == FREE) {
					iCount++;
				}
			}
		}
		return iCount;
		// zaehlt Anzahl leerer Stellen in Map
	}

	private void createRandomExit() {
		int iRand = (int) (Math.random() * iCountFreeSpace());
		// waehlt das x-te freie Feld fuer den Ausgang aus
		for (int i = 0; i < Map.length; i++) {
			for (int j = 0; j < Map[0].length; j++) {
				if (Map[i][j].getContent() == FREE) {
					iRand--;
					if (iRand == 0) {
						Map[i][j].setContent(EXIT);
						return;
					}
				}
			}
		}
		// geht das Spielfeld von oben links nach unten rechts durch und fuegt
		// abhaengig von iRand an der x.ten Stelle einen Ausgang ein. Terminiert
		// nach hinzufuegen des Ausgangs
	}

	private void setWalls() {
		switch (iModus) {
		case 0:
			for (int i = 0; i < Map.length; i++) {
				for (int j = 0; j < Map[0].length; j++) {
					if ((i % 2 == 0) && (j % 2 == 0)
							&& (Map[i][j].getContent() != EMPTY)) {
						Map[i][j].setContent(WALL);
						// jedes 2. Feld wird Mauern belegt
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
								// Feld wird mit einer x-prozentigen Chance mit
								// einer Mauer belegt, wenn auf keinem
								// Umliegenden Feld eine Mauer ist
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
					// die Koordinaten aller moeglichen Mauerstuecke werden in
					// einer Liste gespeichert
				}
				if (iTmpRandomAmount > iRandomList.size()) {
					iTmpRandomAmount = iRandomList.size();
					// falls die Anzahl der zu setzenden Mauerstuecke > der
					// Anzahl der moeglichen Plaetze ist, wird die Anzahl
					// reduziert
				}
				while (iTmpRandomAmount > 0) {
					iRandomPosition = (int) (Math.random() * iRandomList.size());
					iRandom = iRandomList.get(iRandomPosition);
					Map[iRandom[0]][iRandom[1]].setContent(WALL);
					iRandomList.remove(iRandomPosition);
					iTmpRandomAmount--;
					// aus der Liste werden zufaellig Koordinaten ausgewaehlt an
					// deren Stellen Mauerstuecke platziert werden, bis die
					// gewuenschte Anzahl erreicht ist
				}
			}
		}
	}
}
