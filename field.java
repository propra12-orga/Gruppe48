
public class field 
{
	enum content {EMPTY, FREE, WALL, EXIT, BOMB, PLAYER}
	fieldContent Map[][];	
	public field()
	{
		
	}
	
	public void insertMap(fieldContent newMap[][])
	{
		Map = newMap;
	}
	
	public int iGetContent(int iXCoord, iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			return Map[iXCoord][iYCoord].getContent();
		}else
		{
			return null;		
		}
	}
	
	public void setBomb(bomb newBomb, int iXCoord, iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			Map[iXCoord][iYCoord].insertBomb(newBomb);
		}else
		{
			return null;		
		}
	}
	
	public void setPlayer(bomb newPlayer, int iXCoord, iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			Map[iXCoord][iYCoord].insertPlayer(newPlayer);
		}else
		{
			return null;		
		}
	}
	
	public boolean bInBounds (int iXCoord, iYCoord)
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
