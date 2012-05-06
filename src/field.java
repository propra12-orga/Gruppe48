public class field 
{  
	public static int x,y;
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	static fieldContent Map[][];	
	public field()
	{
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
		//Bezeichnungen werden in allen Spielfelddateien beibehalten 
	}
	
	public void insertMap(fieldContent newMap[][])
	{
		Map = newMap;
		//zum einfügen von mit "fieldGenerator" erzeugten Karten
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
		//gibt den Inhalt der entsprechenden Zelle zurück. Falls die Zelle nicht existiert wird -1 zurückgegeben
	}
	
	public static void setBomb(bomb newBomb, int iXCoord, int iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			Map[iXCoord][iYCoord].insertBomb(newBomb);
		}else
		{
			return;		
		}
		//fügt neue Bombe in entsprechender Zelle ein, falls die Zelle existiert
	}
	
	public void setPlayer(player newPlayer, int iXCoord, int iYCoord)
	{
		if(bInBounds(iXCoord, iYCoord))
		{
			Map[iXCoord][iYCoord].insertPlayer(newPlayer);
			x = iXCoord;
			y = iYCoord;
		}else
		{
			return;		
		}
		//fügt Spieler in entsprechender Zelle ein, falls die Zelle existiert
	}
	
	public static boolean bInBounds (int iXCoord, int iYCoord)
	{
		if((iXCoord >= 0) && (iXCoord < Map.length) && (iYCoord >= 0) && (iYCoord < Map[0].length))
		{
			return true;
		}else
		{
			return false;		
		}
		//testet ob gewählte Zelle existiert
	}
}
