

public class fieldGenerator 
{
	private int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	fieldContent Map[][];
	
	public fieldGenerator()
	{
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
		//Bezeichnungen werden in allen Spielfelddateien beibehalten 
	}
	
	public fieldContent[][] createRectangleMap(int iXSize, int iYSize)
	{
		Map = null;
		Map = new fieldContent[iXSize][iYSize];
		for (int i = 0; i < iXSize; i++)
		{
			for (int j = 0; j < iYSize; j++)
			{
				Map[i][j] = new fieldContent();
			}
		}
		//Initialisierung des Spielfeldes
		for (int i = 0; i < iXSize; i++)
		{
			for (int j = 0; j < iYSize; j++)
			{
				if((i == 0) || (i == iXSize - 1) || (j == 0) || (j == iYSize -1))
				{
					Map[i][j].setContent(WALL);
					//R�nder der Karte werden mit Mauern belegt
				} else
				{
				
					if((i % 2 == 0) && (j % 2 == 0))
					{
						Map[i][j].setContent(WALL);
						//jedes 2. Feld wird Mauern belegt
					} else
					{
						Map[i][j].setContent(FREE);
						//alle anderen Felder sind begehbar
					}
				}
			}			
		}
		createRandomExit();
		//Ausgang wird an zuf�lliger Stelle eingef�gt
		return Map;
	}
	
	public fieldContent[][] createSquareMap(int iSize)
	{
	return createRectangleMap(iSize,iSize);	
	//gibt Quadratische Map zur�ck
	}
	
	private int iCountFreeSpace()
	{
		int iCount = 0;
		for (int i = 0; i < Map.length; i++)
		{
			for (int j = 0; j < Map[0].length; j++)
			{
				if(Map[i][j].getContent() == FREE)
				{
					iCount++;
				}
			}
		}
		return iCount;
		//z�hlt Anzahl leerer Stellen in Map 
	}
	
	private void createRandomExit()
	{
		int iRand = (int)(Math.random() * iCountFreeSpace());
		//w�hlt das x-te freie Feld f�r den Ausgang aus
		for (int i = 0; i < Map.length; i++)
		{
			for (int j = 0; j < Map[0].length; j++)
			{
				if(Map[i][j].getContent() == FREE)
				{
					iRand--;
					if(iRand == 0){
						Map[i][j].setContent(EXIT);
						return;
					}
				}
			}
		}
		//geht das Spielfeld von oben links nach unten rechts durch und f�gt abh�ngig von iRand an der x.ten Stelle einen Ausgang ein. Terminiert nach hinzuf�gen des Ausgangs
	}
}
