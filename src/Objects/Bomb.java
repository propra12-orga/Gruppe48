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
	long delay = 3 * 1000;
	long explosionTime;
	int x;
	int y;
	int radius;
	static int bombMax;
	static int bombMaxP2;
	static int m = 1;

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
		bombStatus = false;
		bombStatusP2 = false;
		System.out.println("bombstatus detonate():" + bombStatus);
		System.out.println("bombstatusP2 detonate():" + bombStatusP2);
		m = 1;

	}

	/**
	 * Gibt den Radius zurueck in welchem Ausmaﬂ die Bombe explodieren soll
	 * 
	 * @return Gibt den Radius zurueck
	 */
	public int getRadius() {
		return radius;
	}

	public static void setBombStatus() {
		if (getM() <= bombMax - 1) {
			bombStatus = false;
			System.out.println("getM() if: " + getM());
			System.out.println("bombstatus if:" + bombStatus);
			System.out.println("bombMax if:" + bombMax);

		} else {
			bombStatus = true;
			System.out.println("bombstatus else:" + bombStatus);
			System.out.println("bombMax else:" + bombMax);

		}
	}

	public static void setBombStatusP2() {
		if (getM() <= bombMaxP2 - 1) {
			bombStatusP2 = false;
			System.out.println("getM() if: " + getM());
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

	public static void setgelegteBomb() {
		m += 1;
	}

	public static int getM() {
		return m;
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

	public static void setbombMax(int newbombMax) {
		bombMax = newbombMax;
	}

	public static void setbombMaxP2(int newbombMax) {
		bombMaxP2 = newbombMax;
	}
}
