package Field;
import Objects.bomb;
import Objects.player;

/*
 * FieldContent.java
 * 
 * Version 1
 * 
 * © Alexander Hering
 */

public class FieldContent {
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private int iContent;
	private bomb Bomb;
	private player Player;

	public FieldContent()

	{
		EMPTY = 0;
		FREE = 1;
		WALL = 2;
		EXIT = 3;
		BOMB = 4;
		PLAYER = 5;
		iContent = EMPTY;
		Bomb = null;
		Player = null;
	}

	/**
	 * Setzt Wert des Feldes auf angegebenen Wert
	 * 
	 * @param int iNewContent: Zu setzender Wert
	 */
	public void setContent(int iNewContent) {
		iContent = iNewContent;
		Bomb = null;
		Player = null;
		// setzt den Inhalt auf den gewuenschten Wert und loescht etwaige
		// Spieler- oder Bombenreferenzen.
	}

	/**
	 * Fuegt Bombe in Feld ein
	 * 
	 * @param bomb
	 *            bBomb: einzufuegende Bombe
	 */
	public void insertBomb(bomb bBomb) {
		Bomb = bBomb;
		iContent = BOMB;
		// fuegt Bombe hinzu
	}

	/**
	 * Fuegt Spieler in Feld ein
	 * 
	 * @param player
	 *            pPlayer: einzufuegender Spieler
	 */
	public void insertPlayer(player pPlayer) {
		Player = pPlayer;
		iContent = PLAYER;
		// fuegt Spieler hinzu und loescht Referenz auf Bomben
	}

	/**
	 * Entfernt Bombe aus Feld
	 */
	public void removeBomb() {
		Bomb = null;
	}

	/**
	 * Entfernt Spieler aus Feld
	 */
	public void removePlayer() {
		Player = null;
	}

	/**
	 * Gibt Inhalt des Feldes zurueck
	 * 
	 * @return int: Gibt Inhalt des Feldes zurueck
	 */
	public int getContent() {
		return iContent;
		// gibt Inhalt als int zurueck
	}

	/**
	 * Gibt angelegte Bombe zurueck
	 * 
	 * @return bomb: Gibt Bombe zurueck, falls vorhanden. Gibt sonst null
	 *         zurueck;
	 */
	public bomb getBomb() {
		return Bomb;
		// gibt Referenz auf Bombe zurueck falls vorhanden, gibt sonst null
		// zurueck
	}

	/**
	 * Gibt angelegten Spieler zurueck
	 * 
	 * @return player: Gibt Spieler zurueck, falls vorhanden. Gibt sonst null
	 *         zurueck;
	 */
	public player getPlayer() {
		return Player;
		// gibt Referenz auf Spieler zurueck falls vorhanden, gibt sonst null
		// zurueck
	}

}
