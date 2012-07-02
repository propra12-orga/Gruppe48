package Field;

import java.io.Serializable;

import Objects.Bomb;
import Objects.Player;

/**
 * FieldContent.java
 * 
 * @author Alexander Hering
 */

/**
 * Die Klasse FieldContent erlaubt es ein Feld des Spielfeldes zu speichern und
 * zu verändern
 * 
 */
public class FieldContent implements Serializable {
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER, STONE, FIRE;
	private int iContent;
	private Bomb bomb;
	private Player player;
	private boolean bExit = false;
	private boolean bFireItem = false;
	private boolean bBombItem = false;

	/**
	 * Erzeugt ein Objekt der Klasse FieldContent Sein Inhalt wird mit null
	 * initialisiert
	 */
	public FieldContent()

	{
		EMPTY = 0;
		FREE = 1;
		WALL = 2;
		EXIT = 3;
		BOMB = 4;
		PLAYER = 5;
		STONE = 6;
		FIRE = 7;
		iContent = EMPTY;
		bomb = null;
		player = null;
	}

	/**
	 * Setzt Wert des Feldes auf angegebenen Wert
	 * 
	 * @param iNewContent
	 *            Zu setzender Wert
	 */
	public void setContent(int iNewContent) {
		iContent = iNewContent;
		bomb = null;
		player = null;
		// setzt den Inhalt auf den gewuenschten Wert und loescht etwaige
		// Spieler- oder Bombenreferenzen.
	}

	/**
	 * Fuegt Ausgang hinzu.
	 */
	public void setExit() {
		bExit = true;
	}

	public void setFireItem() {
		bFireItem = true;
	}

	/*
	 * public void setBombItem() { bBombItem = true; }
	 */
	/**
	 * Fuegt Bombe in Feld ein
	 * 
	 * @param bBomb
	 *            Einzufuegende Bombe
	 */
	public void insertBomb(Bomb bBomb) {
		bomb = bBomb;
		// fuegt Bombe hinzu
	}

	/**
	 * Fuegt Spieler in Feld ein
	 * 
	 * @param pPlayer
	 *            Einzufuegender Spieler
	 */
	public void insertPlayer(Player pPlayer) {
		player = pPlayer;
		// fuegt Spieler hinzu und loescht Referenz auf Bomben
	}

	/**
	 * Entfernt Bombe aus Feld
	 */
	public void removeBomb() {
		bomb = null;
	}

	/**
	 * Entfernt Spieler aus Feld
	 */
	public void removePlayer() {
		player = null;
	}

	/**
	 * Gibt Inhalt des Feldes zurueck
	 * 
	 * @return Gibt Inhalt des Feldes zurueck
	 */
	public boolean isExit() {
		return bExit;
	}

	public boolean isFireItem() {
		return bFireItem;
	}

	/*
	 * public boolean isBombItem() { return bBombItem; }
	 */
	/**
	 * Gibt Inhalt des Feldes als Integer zurueck
	 * 
	 * @return Inhalt des Feldes
	 */
	public int getContent() {
		return iContent;
		// gibt Inhalt als int zurueck
	}

	/**
	 * Gibt angelegte Bombe zurueck
	 * 
	 * @return Gibt Bombe zurueck, falls vorhanden. Gibt sonst null zurueck;
	 */
	public Bomb getBomb() {
		return bomb;
		// gibt Referenz auf Bombe zurueck falls vorhanden, gibt sonst null
		// zurueck
	}

	/**
	 * Gibt angelegten Spieler zurueck
	 * 
	 * @return Gibt Spieler zurueck, falls vorhanden. Gibt sonst null zurueck;
	 */
	public Player getPlayer() {
		return player;
		// gibt Referenz auf Spieler zurueck falls vorhanden, gibt sonst null
		// zurueck
	}

}
