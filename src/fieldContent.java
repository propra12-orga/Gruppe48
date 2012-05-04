
public class fieldContent 
{
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private int iContent;
	private bomb Bomb;
	private player Player;
	public fieldContent () 
	
	{
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
		iContent = EMPTY;
		Bomb = null;
		Player = null;
	}
	
	public void setContent (int iNewContent)
	{
		iContent = iNewContent;
		Bomb = null;
		Player = null;
	}
	
	public void insertBomb (bomb bBomb)
	{
		Bomb = bBomb;
		iContent = BOMB;	
	}
	
	public void insertPlayer (player pPlayer)
	{
		Player = pPlayer;
		iContent = PLAYER;
		Bomb = null;
	}
	
	public int getContent ()
	{
		return iContent;
	}
	
	public bomb getBomb()
	{
		return Bomb;
	}
	
	public player getPlayer()
	{
		return Player;
	}
	
	
}
