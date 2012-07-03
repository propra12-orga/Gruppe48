package Objects;

import java.io.Serializable;

import Engine.Sound;

/**
 * die Klasse Bomb stellt Methoden zu Verfuegung um die Position einer Bombe auf
 * dem Spielfeld sowie ihre Restzeit bis zur Explosion zu speichern und
 * auszulesen. Auch stellt sie die Moeglichkeit bereit eine Bombe vorzeitig
 * detonieren zu lassen
 * 
 * @author Martin Haase
 * 
 */

public class Bomb implements Serializable {
	/**
	 * Ist true, falls Bombe explodiert, ist sonst false
	 */
	public static boolean isExploded = false;
	public static boolean bombStatus = false; // wenn false darf Player1 neue
												// bomben legen
	public static boolean bombStatusP2 = false; // //wenn false darf player2
												// neue bomben legen
	long delay = 3 * 1000;
	long explosionTime;
	int x;
	int y;
	int radius;
	static int bombMax;// Anzahl maximal erlaubter Bomben Player1
	static int bombMaxP2;// Anzahl maximal erlaubter Bomben Player2
	public static int currentPlaced;// Anzahl aktuell gelegter Bomben Player1
	public static int currentPlacedP2;// Anzahl aktuell gelegter Bomben Player2

	/**
	 * Konstruktor der Klasse Bomb
	 * 
	 * @param xPos
	 *            Gewuenschte X-Koordinate auf die die Bombe gesetzt werden soll
	 * @param yPos
	 *            Gewuenschte Y-Koordinate auf die die Bombe gesetzt werden soll
	 * @param time
	 *            Zeit zu der die Bombe gesetzt worden ist
	 */
	public Bomb(int xPos, int yPos, long time, int radius) {
		x = xPos;
		y = yPos;
		this.radius = radius;
		explosionTime = time + delay;
	}

	/**
	 * Gibt die Position der Bombe zurueck
	 * 
	 * @return Gibt die Position der Bombe als Array zurueck
	 */
	public int[] getPosition() {
		int output[] = new int[2];
		output[0] = x;
		output[1] = y;
		return output;
	}

	/**
	 * Gibt die Zeit zurueck, wann die Bombe explodieren soll
	 * 
	 * @return Gibt Explosionszeit zurueck
	 */
	public long getTimer() {
		return explosionTime;
	}

	/**
	 * Setzt die Variable explosionTime auf den Zeitpunkt wann die Bombe gesetzt
	 * wurde und sagt dem System das diese Bombe explodiert ist
	 */
	public void detonate() {
		explosionTime -= delay;
		Sound.BOMB.play();
		isExploded = true;

	}

	/**
	 * Gibt den Radius zurueck in welchem Ausmaß die Bombe explodieren soll
	 * 
	 * @return Gibt den Radius zurueck
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Vergleicht den Wert aktuell gelegter Bomben(Player1) mit dem Wert maximal
	 * erlaubter legbarer Bomben. Ist der Wert kleiner, bekommt bombstatus den
	 * wert false Ist er größer dann true
	 */
	public static void setBombStatus() {
		if (getCurrentPlaced() <= bombMax) {
			bombStatus = false;
			System.out.println("getM() if: " + getCurrentPlaced());
			System.out.println("bombstatus if:" + bombStatus);
			System.out.println("bombMax if:" + bombMax);

		} else {
			bombStatus = true;
			System.out.println("bombstatus else:" + bombStatus);
			System.out.println("bombMax else:" + bombMax);

		}
	}

	/**
	 * Vergleicht den Wert aktuell gelegter Bomben(Player2) mit dem Wert maximal
	 * erlaubter legbarer Bomben. Ist der Wert kleiner, bekommt bombstatus den
	 * wert false Ist er größer dann true.
	 */
	public static void setBombStatusP2() {
		if (getCurrentPlacedP2() <= bombMaxP2) {
			bombStatusP2 = false;
			System.out.println("getM() if: " + getCurrentPlaced());
			System.out.println("bombstatusP2 if:" + bombStatusP2);
			System.out.println("bombMaxP2 if:" + bombMaxP2);

		} else {
			bombStatusP2 = true;
			System.out.println("bombstatusP2 else:" + bombStatusP2);
			System.out.println("bombMaxP2 else:" + bombMaxP2);

		}
	}

	/**
	 * wird zum Abfragen des aktuellen Bombenstatus verwendet
	 * 
	 * @return bombStatus gibt den Bombenstatus für Player1 zurueck, um zu
	 *         pruefen, ob weiter Bomben gelegt werden duerfen
	 */
	public static boolean getBombStatus() {
		return bombStatus;
	}

	/**
	 * wird zum Abfragen des aktuellen Bombenstatus verwendet
	 * 
	 * @return bombStatus gibt den Bombenstatus für Player2 zurueck, um zu
	 *         pruefen, ob weiter Bomben gelegt werden duerfen
	 */

	public static boolean getBombStatusP2() {
		return bombStatusP2;
	}

	/**
	 * setzt den Wert von currentPlaced fuer Player1 um eins hoeher, somit
	 * koennen die Bomben gezaehlt werden
	 */
	public static void setCurrentPlacedBomb() {
		currentPlaced += 1;
	}

	/**
	 * wird zum Abfragen der bereits gesetzten Bomben von Player1 verwendet
	 * 
	 * @return currentPlaced gibt die Anzahl bereits gesetzter Bomben zurück
	 */
	public static int getCurrentPlaced() {
		return currentPlaced;
	}

	/**
	 * setzt den Wert von currentPlaced fuer Player2 um eins hoeher, somit
	 * koennen die Bomben gezaehlt werden
	 */

	public static void setCurrentPlacedBombP2() {
		currentPlacedP2 += 1;
	}

	/**
	 * wird zum Abfragen der bereits gesetzten Bomben von Player2 verwendet
	 * 
	 * @return currentPlacedP2 gibt die Anzahl bereits gesetzter Bomben zurück
	 */
	public static int getCurrentPlacedP2() {
		return currentPlacedP2;
	}

	/**
	 * setzt den Wert der maximal legbaren Bomben fuer Player1 um eins hoeher.
	 * dies geschieht, wenn ein BombenItem eingesammelt wird
	 */
	public static void setBombMax() {
		bombMax += 1;
	}

	/**
	 * wird fuer Player1 abgefragt, damit die Zahl aktuell gelegter Bomben mit
	 * der Zahl der maximal erlaubten Bomben verglichen werden kann
	 * 
	 * @return bombMax gibt den Wert zurück der maximal erlaubten bomben
	 */
	public static int getBombMax() {
		return bombMax;
	}

	/**
	 * setzt den Wert der maximal legbaren Bomben fuer Player2 um eins hoeher.
	 * dies geschieht, wenn ein BombenItem eingesammelt wird
	 */
	public static void setBombMaxP2() {
		bombMaxP2 += 1;
	}

	/**
	 * wird fuer Player2 abgefragt, damit die Zahl aktuell gelegter Bomben mit
	 * der Zahl der maximal erlaubten Bomben verglichen werden kann
	 * 
	 * @return bombMax gibt den Wert zurück der maximal erlaubten bomben
	 */
	public static int getBombMaxP2() {
		return bombMaxP2;
	}

	/**
	 * beim Start des Spieles soll der Wert fuer Player1 der maximal erlaubten
	 * Bomben wieder 1 sein
	 * 
	 * @param newBombMax
	 *            legt den wert von bombMax am start des Spieles fest
	 */
	public static void setBombMaxOnStart(int newBombMax) {
		bombMax = newBombMax;
	}

	/**
	 * beim Start des Spieles soll der Wert fuer Player2 der maximal erlaubten
	 * Bomben wieder 1 sein
	 * 
	 * @param newBombMax
	 *            legt den wert von bombMax am start des Spieles fest
	 */
	public static void setBombMaxOnStartP2(int newBombMax) {
		bombMaxP2 = newBombMax;
	}

}
