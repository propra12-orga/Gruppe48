package Field;

import Objects.Bomb;
import Objects.Player;

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
	private Bomb Bomb;
	private Player Player;

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
	 * @param iNewContent
	 *            : Zu setzender Wert
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
	 * @param bBomb
	 *            : Einzufuegende Bombe
	 */
	public void insertBomb(Bomb bBomb) {
		Bomb = bBomb;
		// fuegt Bombe hinzu
	}

	/**
	 * Fuegt Spieler in Feld ein
	 * 
	 * @param pPlayer
	 *            : Einzufuegender Spieler
	 */
	public void insertPlayer(Player pPlayer) {
		Player = pPlayer;
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
	 * @return Gibt Inhalt des Feldes zurueck
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
		return Bomb;
		// gibt Referenz auf Bombe zurueck falls vorhanden, gibt sonst null
		// zurueck
	}

	/**
	 * Gibt angelegten Spieler zurueck
	 * 
	 * @return Gibt Spieler zurueck, falls vorhanden. Gibt sonst null zurueck;
	 */
	public Player getPlayer() {
		return Player;
		// gibt Referenz auf Spieler zurueck falls vorhanden, gibt sonst null
		// zurueck
	}

}
