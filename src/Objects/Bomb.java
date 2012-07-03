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
	public static boolean bombStatus = false;
	public static boolean bombStatusP2 = false;
	long delay = 7 * 1000;
	long explosionTime;
	int x;
	int y;
	int radius;
	static int bombMax;
	static int bombMaxP2;
	public static int currentPlaced = 0;
	public static int currentPlacedP2 = 0;

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
	 * wert false Ist er größer dann true
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

	public static boolean getBombStatus() {
		return bombStatus;
	}

	public static boolean getBombStatusP2() {
		return bombStatusP2;
	}

	public static void setCurrentPlacedBomb() {
		currentPlaced += 1;
	}

	public static int getCurrentPlaced() {
		return currentPlaced;
	}

	public static void setCurrentPlacedBombP2() {
		currentPlacedP2 += 1;
	}

	public static int getCurrentPlacedP2() {
		return currentPlacedP2;
	}

	public static void setBombMax() {
		bombMax += 1;
	}

	public static int getBombMax() {
		return bombMax;
	}

	public static void setBombMaxP2() {
		bombMaxP2 += 1;
	}

	public static int getBombMaxP2() {
		return bombMaxP2;
	}

	public static void setBombMaxOnStart(int newBombMax) {
		bombMax = newBombMax;
	}

	public static void setBombMaxOnStartP2(int newBombMax) {
		bombMax = newBombMax;
	}

}
