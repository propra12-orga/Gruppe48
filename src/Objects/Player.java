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
	int x, y, id, bRadius;

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
		this.id = id;
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

	public void setBombRadius() {
		bRadius += 1;
	}

	public int getBombRadius() {
		return bRadius;
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
