package Objects;

/**
 * Player.java
 * 
 * @author Martin Haase
 * 
 */
public class Player {
	public int x, y, id;
	// public static float a,b;
	public boolean stillAlive = true;

	/**
	 * Konstruktor der Klasse Player
	 * 
	 * @param iXPos
	 *            Gewuenschte X-Koordinate des Spielers
	 * @param iYPos
	 *            Gewuenschte Y-Koordinate des Spielers
	 * @param id
	 *            ID des Spielers(Mehrspielermodus)
	 */
	public Player(int iXPos, int iYPos, int id) {
		x = iXPos;
		y = iYPos;
		this.id = id;
	}

	/**
	 * Gibt die Position des Spielers zurueck
	 * 
	 * @return Gibt die Position des Spielers als Array zurueck
	 */
	public int[] getPosition() {
		int[] getPosition = new int[2];
		getPosition[0] = x;
		getPosition[1] = y;
		return getPosition;
	}

	/**
	 * Setzt den Spieler auf die gewuenschte Position
	 * 
	 * @param iXPos
	 *            X-Koordinate
	 * @param iYPos
	 *            Y-Koordinate
	 */
	public void setPosition(int iXPos, int iYPos) {
		x = iXPos;
		y = iYPos;
	}

	/**
	 * Bewegt den Spieler nach oben durch verschieben der X-Koordinate(bei
	 * Betaetigen des W-Knopfes)
	 */
	public void moveUp() {
		x -= 1;
	}

	/**
	 * Bewegt den Spieler nach unten durch verschieben der X-Koordinate(bei
	 * Betaetigen des S-Knopfes)
	 */
	public void moveDown() {
		x += 1;
	}

	/**
	 * Bewegt den Spieler nach links durch verschieben der Y-Koordinate(bei
	 * Betaetigen des A-Knopfes)
	 */
	public void moveLeft() {
		y -= 1;
	}

	/**
	 * Bewegt den Spieler nach rechts durch verschieben der Y-Koordinate(bei
	 * Betaetigen des D-Knopfes)
	 */
	public void moveRight() {
		y += 1;
	}

	/**
	 * Gibt die "Nummer" des Spielers zurueck
	 * 
	 * @return ID des Spielers(Multiplayermodus)
	 */
	public int getID() {
		return id;
	}
	/*
	 * public float[] getPositionfloat() { Für eine flüssige Bewegung anstatt
	 * int mit float bewegen float[] getPosition = new float[2]; getPosition[0]
	 * = x; getPosition[1] = y; return getPosition; }
	 * 
	 * public float moveUPf(){ float u; u = getPositionfloat()[1] + 0.1f; b = u;
	 * return b; } public float moveDOWNf(){ float d; d = getPositionfloat()[1]
	 * - 0.1f; b= d; return b; } public float moveLEFTf(){ float l; l =
	 * getPositionfloat()[0] - 0.1f; a= l; return a; } public float
	 * moveRIGHTf(){ float r; r = getPositionfloat()[0] + 0.1f; a= r; return a;
	 * }
	 */

}
