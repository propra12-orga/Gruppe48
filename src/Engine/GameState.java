package Engine;

import java.io.Serializable;
import java.util.List;

import Field.FieldContent;
import Objects.Bomb;
import Objects.Player;

/**
 * Klasse zum Speichern von Spielständen
 * 
 * @author Leonid Panich
 * 
 */
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	public FieldContent[][] map;
	public List<long[]> explosionList;
	public List<Bomb> bombList;
	public Player player;
	public Player player2;

	/**
	 * Konstruktor der Klasse GameState
	 * 
	 * @param map
	 *            Spielfeld
	 * @param explosionList
	 *            Liste der Explosionen
	 * @param bombList
	 *            Liste der Bombe
	 * @param p1
	 *            Player1
	 * @param p2
	 *            Player2
	 */
	public GameState(FieldContent[][] map, List<long[]> explosionList,
			List<Bomb> bombList, Player p1, Player p2) {
		this.map = map;
		this.explosionList = explosionList;
		this.bombList = bombList;
		this.player = p1;
		this.player2 = p2;
	}
}
