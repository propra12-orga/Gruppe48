
public class fieldContent 
{
	enum content {EMPTY, FREE, WALL, EXIT, BOMB, PLAYER}
	private int iContent;
	private bomb Bomb;
	private player Player;
	public fieldContent () 
	
	{
		bIsEmpty = bIsFree = bIsExit = bIsWall = false;
		Bomb = Player = null;
	}
	
	public void setContent (int iNewContent)
	{
		iContent = iNewContent
		Bomb = null;
		Player = null;
	}
	
	public void insertBomb (bomb bBomb)
	{
		Bomb = bBomb;
		iContent = content.BOMB;	
	}
	
	public void insertPlayer (player pPlayer)
	{
		Player = pPlayer;
		iContent = content.PLAYER;
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
