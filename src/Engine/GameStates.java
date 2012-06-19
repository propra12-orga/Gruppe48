package Engine;

/**
 * GameStates.java
 * 
 * @author Leonid Panich Alle GameStates werden hier als enum-Variablen defeniert
 */
/**
 * 
 * Wird von Game aufgerufen um den aktuellen Status des Spiels anzugeben STARTED
 * = Spiel laeuft STOP = Spiel ist angehalten VICTORY = Ein Spieler hat gewonnen
 * GAMEOVER = Ein Spieler hat verloren
 * 
 */
public enum GameStates {
	STARTED, VICTORY, GAMEOVER, STOP
}