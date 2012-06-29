package Objects;

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

public class Bomb {
	/**
	 * Ist true, falls Bombe explodiert, ist sonst false
	 */
	public static boolean isExploded = false;
	public static boolean bombStatus = false;
	long delay = 3 * 1000;
	long explosionTime;
	int x;
	int y;
	int radius = 3;
	static int zmax = 2;
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
		System.out.println(bombStatus + "bombstatus detonate()");
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
		if (getM() <= zmax) {
			bombStatus = false;
			System.out.println(getM() + "getM() if ");
			System.out.println(bombStatus + "bombstatus if");
			System.out.println(zmax + "zmax if");

		} else {
			bombStatus = true;
			System.out.println(bombStatus + "bombstatus else");
			System.out.println(zmax + "zmax else");

		}
	}

	public static boolean getBombStatus() {
		return bombStatus;
	}

	public static void setgelegteBomb() {
		m += 1;
	}

	public static int getM() {
		return m;
	}
}
