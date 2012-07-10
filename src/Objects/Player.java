package Objects;

import java.io.Serializable;

/**
 * Die Klasse Player stellt Methoden zur Verfügung um die aktuelle Position und
 * id eines Spielers zu speichern und abzufragen
 * 
 * @author Martin Haase
 * 
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	int x, y, id, bRadius, bCount;

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
		bRadius = 2;
		bCount = 1;
		this.id = id;
	}

	public Player clonePlayer() {
		Player outputPlayer = new Player(x, y, id);
		outputPlayer.setBombRadius(bRadius);
		outputPlayer.setBombCount(bCount);
		return outputPlayer;
	}

	/**
	 * Gibt die Position des Spielers zurueck
	 * 
	 * @return Gibt die Position des Spielers als int[2] Array zurueck
	 */
	public int[] getPosition() {
		int[] getPosition = new int[2];
		getPosition[0] = x;
		getPosition[1] = y;
		return getPosition;
	}

	private void setBombRadius(int radius) {
		bRadius = radius;
	}

	/**
	 * erhoeht den Bombenradius beim einsammeln eines items
	 */
	public void setBombRadius() {
		bRadius += 1;
	}

	/**
	 * gibt den Bombenradius zurück, damit die Bombe den Radius kennt
	 * 
	 * @return bRadius zurueckgegebener Bombenradius
	 */
	public int getBombRadius() {
		return bRadius;
	}

	public void setBombCount(int count) {
		bCount = count;
	}

	public int getBombCount() {
		return bCount;
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
}
