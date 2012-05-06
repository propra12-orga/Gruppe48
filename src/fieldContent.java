
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
		//setzt den Inhalt auf den gew�nschten Wert und l�scht etwaige Spieler- oder Bombenreferenzen.
	}
	
	public void insertBomb (bomb bBomb)
	{
		Bomb = bBomb;
		iContent = BOMB;	
		//f�gt Bombe hinzu
	}
	
	public void insertPlayer (player pPlayer)
	{
		Player = pPlayer;
		iContent = PLAYER;
		//f�gt Spieler hinzu und l�scht Referenz auf Bomben
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
		//gibt Inhalt als int zur�ck
	}
	
	public bomb getBomb()
	{
		return Bomb;
		//gibt Referenz auf Bombe zur�ck falls vorhanden, gibt sonst null zur�ck
	}
	
	public player getPlayer()
	{
		return Player;
		//gibt Referenz auf Spieler zur�ck falls vorhanden, gibt sonst null zur�ck
	}
	
	
}
