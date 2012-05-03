import java.math.*;

public class fieldGenerator 
{
	enum content {EMPTY, FREE, WALL, EXIT, BOMB, PLAYER}
	fieldContent Map[][];
	
	public fieldGenerator()
	{
		
	}
	
	public fieldContent[][] createSquareMap(int iXSize, iYSize)
	{
		Map = null;
		Map = new fieldContent[iXSize][iYSize];
		for (int i = 0; i < iXSize; i++)
		{
			for (int j = 0; j < iYSize; j++)
			{
				if((i == 0) || (i == iXSize - 1) || (j == 0) || (j == iYSize -1))
				{
					Map[i][j].setContent(content.WALL);
				} else
				{
				
					if((i % 2 == 0) && (j % 2 == 0))
					{
						Map[i][j].setContent(content.WALL);
					} else
					{
						Map[i][j].setContent(content.FREE);
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
		for (int i = 0; i < iXSize; i++)
		{
			for (int j = 0; j < iYSize; j++)
			{
				if(Map[i][j].getContent == content.FREE)
				{
					iCount++;
				}
			}
		}
	}
	
	private void createRandomExit()
	{
		int iRand = (int)(Math.rand() + iCountFreeSpace());
		for (int i = 0; i < iXSize; i++)
		{
			for (int j = 0; j < iYSize; j++)
			{
				if(Map[i][j].getContent == content.FREE)
				{
					iRand--;
					if(iRand == 0){
						Map[i][j].setContent = content.EXIT;
						return;
					}
				}
			}
		}
		
	}
}
