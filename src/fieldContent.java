
public class fieldContent 
{
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private int iContent;
	private bomb Bomb;
	private player Player;
	public fieldContent () 
	
	{
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
		//Bezeichnungen werden in allen Spielfelddateien beibehalten 
		iContent = EMPTY;
		Bomb = null;
		Player = null;
	}
	
	public void setContent (int iNewContent)
	{
		iContent = iNewContent;
		Bomb = null;
		Player = null;
		//setzt den Inhalt auf den gewünschten Wert und löscht etwaige Spieler- oder Bombenreferenzen.
	}
	
	public void insertBomb (bomb bBomb)
	{
		Bomb = bBomb;
		iContent = BOMB;	
		//fügt Bombe hinzu
	}
	
	public void insertPlayer (player pPlayer)
	{
		Player = pPlayer;
		iContent = PLAYER;
		//fügt Spieler hinzu und löscht Referenz auf Bomben
	}
	
	public void removeBomb ()
	{
		Bomb = null;
	}
	
	public void removePlayer ()
	{
		Player = null;
	}
	public int getContent ()
	{
		return iContent;
		//gibt Inhalt als int zurück
	}
	
	public bomb getBomb()
	{
		return Bomb;
		//gibt Referenz auf Bombe zurück falls vorhanden, gibt sonst null zurück
	}
	
	public player getPlayer()
	{
		return Player;
		//gibt Referenz auf Spieler zurück falls vorhanden, gibt sonst null zurück
	}
	
	
}
