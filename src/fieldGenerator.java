

public class fieldGenerator 
{
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	fieldContent Map[][];
	
	public fieldGenerator()
	{
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
	}
	
	public fieldContent[][] createSquareMap(int iXSize, int iYSize)
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
		for (int i = 0; i < iXSize; i++)
		{
			for (int j = 0; j < iYSize; j++)
			{
				if((i == 0) || (i == iXSize - 1) || (j == 0) || (j == iYSize -1))
				{
					Map[i][j].setContent(WALL);
				} else
				{
				
					if((i % 2 == 0) && (j % 2 == 0))
					{
						Map[i][j].setContent(WALL);
					} else
					{
						Map[i][j].setContent(FREE);
					}
				}
			}			
		}
		createRandomExit();
		return Map;
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
	}
	
	private void createRandomExit()
	{
		int iRand = (int)(Math.random() * iCountFreeSpace());
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
		
	}
}
