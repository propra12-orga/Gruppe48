
public class field 
{
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	fieldContent Map[][];	
	public field()
	{
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
	}
	
	public void insertMap(fieldContent newMap[][])
	{
		Map = newMap;
	}
	
	public int iGetContent(int iXCoord, int iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			return Map[iXCoord][iYCoord].getContent();
		}else
		{
			return -1;		
		}
	}
	
	public void setBomb(bomb newBomb, int iXCoord, int iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			Map[iXCoord][iYCoord].insertBomb(newBomb);
		}else
		{
			return;		
		}
	}
	
	public void setPlayer(player newPlayer, int iXCoord, int iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			Map[iXCoord][iYCoord].insertPlayer(newPlayer);
		}else
		{
			return;		
		}
	}
	
	public boolean bInBounds (int iXCoord, int iYCoord)
	{
		if((iXCoord >= 0) && (iXCoord < Map.length) && (iYCoord >= 0) && (iYCoord < Map[0].length))
		{
			return true;
		}else
		{
			return false;		
		}
		
	}
}
