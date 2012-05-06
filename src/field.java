public class field 
{
	public static int x,y;
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private fieldContent Map[][];	
	private int iOldPos[];
	public field()
	{
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
		iOldPos = new int[2]; 
		//Bezeichnungen werden in allen Spielfelddateien beibehalten 
	}
	
	public void insertMap(fieldContent newMap[][])
	{
		Map = newMap;
		//zum einf�gen von mit "fieldGenerator" erzeugten Karten
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
		//gibt den Inhalt der entsprechenden Zelle zur�ck. Falls die Zelle nicht existiert wird -1 zur�ckgegeben
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
		//f�gt neue Bombe in entsprechender Zelle ein, falls die Zelle existiert
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
		//f�gt Spieler in entsprechender Zelle ein, falls die Zelle existiert
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
		//testet ob gew�hlte Zelle existiert
	}


public void updatePlayer (int[] iOldPos, player updPlayer)
{
	Map[iOldPos[0]][iOldPos[1]].removePlayer();
	Map[updPlayer.getPosition(0)][updPlayer.getPosition(1)].insertPlayer(updPlayer);
}

// die folgende Methode ist als �berlegung zur fl�ssigen Bewegung des Spielers entstanden

/*public void updatePlayer(float fOldPos[], player updPlayer)
{
	int iPlayerID = updPlayer.getID();
	iOldPos[0] = (int)fOldPos[0];
	iOldPos[1] = (int)fOldPos[1];
	if(Map[iOldPos[0]][iOldPos[1]].getPlayer() != null)
	{
		if(Map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID)
		{
			Map[iOldPos[0]][iOldPos[1]].removePlayer();
		}
	}
	
	if(Map[iOldPos[0] + 1][iOldPos[1]].getPlayer() != null)
	{
		if(Map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID)
		{
			Map[iOldPos[0]][iOldPos[1]].removePlayer();
		}
	}
	
	if(Map[iOldPos[0]][iOldPos[1] + 1].getPlayer() != null)
	{
		if(Map[iOldPos[0]][iOldPos[1]].getPlayer().getID() == iPlayerID)
		{
			Map[iOldPos[0]][iOldPos[1]].removePlayer();
		}
	}		
	
	Map[Math.round(updPlayer.getPosition()[0])][Math.round(updPlayer.getPosition()[1])].insertPlayer(updPlayer);
	
}*/


}

