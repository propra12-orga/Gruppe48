package Objects;

/**
 * Bomb.java
 * 
 * @author Martin Haase
 * 
 */
public class Bomb {
	public boolean isExploded = false;
	long delay = 3 * 1000;
	long explosionTime;
	int x;
	int y;
	int radius = 3;

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
	public Bomb(int xPos, int yPos, long time) {
		x = xPos;
		y = yPos;
		explosionTime = time + delay;
		BOOM();
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
		isExploded = true;
	}

	/**
	 * Schreibt "Bombe wurde gesetzt" in die Konsole dient nur der
	 * Uebersichtlichkeit
	 */
	public void BOOM() {
		System.out.println("Bombe wurde gesetzt");

	}

	/**
	 * Gibt den Radius zurueck in welchem Ausma� die Bombe explodieren soll
	 * 
	 * @return Gibt den Radius zurueck
	 */
	public int getRadius() {
		return radius;
	}

}