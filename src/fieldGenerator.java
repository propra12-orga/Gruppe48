import java.util.*;
import java.io.*;

public class fieldGenerator 
{
	private static int EMPTY, FREE, WALL, EXIT, BOMB, PLAYER;
	private fieldContent Map[][];
	private float fRandomChance;
	private int iModus, iRandomAmount;
	public fieldGenerator()
	{
		
		EMPTY = 0; FREE = 1; WALL = 2; EXIT = 3; BOMB = 4; PLAYER = 5;
		iModus = 0;
		//Anzahl der Modi: 3
		//Modus 0: Jedes 2. Feld in jeder 2. Spalte wird mit einer Mauer belegt
		//Modus 1: Jedes 2. Feld in jeder 2. Spalte wird mit einer Chance von fRandomChance % mit einer Mauer belegt
		//Modus 2: Von allen mit Mauern belegbaren Feldern, (jedes 2. Feld in jeder 2. Spalte), werden so lange zufaellig Felder mit Mauern belegt bis iRandomAmount Felder belegt sind
		fRandomChance = 0;
		iRandomAmount = 0;
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
		
		for (int i = 0; i < iXSize; i++)
		{
			for (int j = 0; j < iYSize; j++)
			{
				Map[i][j].setContent(FREE);
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
					//Raender der Karte werden mit Mauern belegt
				} 
			}
		}
		setWalls();
		createRandomExit();
		//Ausgang wird an zufaelliger Stelle eingefuegt
		return Map;
	}
		
	
	
	public fieldContent[][] createSquareMap(int iSize)
	{
	return createRectangleMap(iSize,iSize);	
	//gibt Quadratische Map zurueck
	}
	
	public void setRandomChance (float fChance)
	{
		fRandomChance = fChance / 100;
		//erwartet Prozentangabe. Setzt die Zufallschance fuer Modus 1 auf die angegebene Groesse
	}
	
	public void setRandomAmount (int iAmount)
	{
		iRandomAmount = iAmount;
		//setzt die Anzahl der zu setzenden Bloecke fuer Modus 2 auf den angegebenen Wert
	}
	
	public void setModus(int iStatus)
	{
		iModus = iStatus;
		//aendert den Modus der Spielfeldgenerierung
	}
	
	public fieldContent[][] readMap(String sInputFile)
	{
		int iCounter = 0;
		FileReader inputFile;
		BufferedReader reader;
		List<String> mapList = new ArrayList<String>();
		try{
			inputFile = new FileReader(sInputFile);
		}catch(FileNotFoundException e)
		{
			System.out.println(e);
			return null;
		}		
		reader = new BufferedReader(inputFile);		
		try
		{
			while(reader.ready())
			{
				mapList.add(reader.readLine());
				
			}			
		}catch(IOException e)
		{
			return null;
		}		
		Map = null;
		Map = new fieldContent[mapList.size()][mapList.get(0).length()];
		for (int i = 0; i < mapList.size();  i++)
		{
			for (int j = 0; j < mapList.get(0).length(); j++)
			{
				Map[i][j] = new fieldContent();
			}
		}		
		System.out.println(mapList.size());
		System.out.println(mapList.get(0).length());
		while(mapList.size() > 0)
		{
			for(int i = 0; i < mapList.get(0).length(); i++)
			{
				switch(mapList.get(0).charAt(i))
				{
					case 32: //' '
						Map[iCounter][i].setContent(EMPTY);
						break;
					case 35: //'#'
						Map[iCounter][i].setContent(WALL);
						break;
					case 46: //'.'
						Map[iCounter][i].setContent(FREE);
						break;
					case 69: //'E'
						Map[iCounter][i].setContent(EXIT);
						break;
					default:
						Map[iCounter][i].setContent(EMPTY);
						break;
				}				
			}
			iCounter++;
			mapList.remove(0);
		}
		return Map;
	}
	private boolean checkSurroundings(int iCheckWhereX, int iCheckWhereY, int iCheckHowFar, int iCheckForWhat, boolean bCheckIfThere)
	{
		for (int i = iCheckWhereX - iCheckHowFar; i <= iCheckWhereX + iCheckHowFar; i++)
		{
			for (int j = iCheckWhereY - iCheckHowFar; j <= iCheckWhereY + iCheckHowFar; j++)
			{
				if((i >= 0) && (j >= 0))
				{
					if(Map[i][j].getContent() == iCheckForWhat == bCheckIfThere)					
					{
						return true;
					}
				}
			}
				
		}
		return false;
		//in allen Feldern, die sich in einem Abstand von maximal iCheckHowFar von [iCheckWhereX][iCheckWhereY] befinden wird das Vorhandensein von iCheckForWhat geprueft.
		//ist bCheckIfThere auf true zurueckgesetzt, so wird true zurueckgegeben, wenn das gesuchte Element mindestens einmal gefunden wurde.
		//ist bCheckIfThere auf false gesetzt, so wird true zurueckgegeben, wenn das gesuchte Element mindestens einmal nicht gefunden wurde. 
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
		//zaehlt Anzahl leerer Stellen in Map 
	}
	
	private void createRandomExit()
	{
		int iRand = (int)(Math.random() * iCountFreeSpace());
		//waehlt das x-te freie Feld fuer den Ausgang aus
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
		//geht das Spielfeld von oben links nach unten rechts durch und fuegt abhaengig von iRand an der x.ten Stelle einen Ausgang ein. Terminiert nach hinzufuegen des Ausgangs
	}
	
	private void setWalls()
	{
		switch(iModus)
		{
		case 0:
			for (int i = 0; i < Map.length; i++)
			{
				for (int j = 0; j < Map[0].length; j++)
				{	
					if((i % 2 == 0) && (j % 2 == 0) && (Map[i][j].getContent() != EMPTY))
					{
							Map[i][j].setContent(WALL);
							//jedes 2. Feld wird Mauern belegt					
					}
				}			
			}		
		break;
		
		case 1:
			if(iModus == 1)
			{
				for (int i = 0; i < Map.length; i++)
				{
					for (int j = 0; j < Map[0].length; j++)
					{			
						if(checkSurroundings(i, j, 1, WALL, true) == false)
						{
							if(Math.random() <= fRandomChance)
							{
								Map[i][j].setContent(WALL);
								//Feld wird mit einer x-prozentigen Chance mit einer Mauer belegt, wenn auf keinem Umliegenden Feld eine Mauer ist
							} 
						}
					}				
				}
			}	
		break;
		
		case 2:
			if(iModus == 2)
			{							
				int iPosition = 0;
				int iRandomPosition;
				int iRandom[] = new int[2];
				int iTmpRandomAmount = iRandomAmount;
				List<int[]> iRandomList = new ArrayList<int[]>();
				for (int i = 2; i < Map.length - 2; i++)
				{
					for (int j = 2; j < Map[0].length - 2; j++)
					{			
						if((i % 2 == 0) && (j % 2 == 0))
						{
							iRandomList.add(new int[2]);
							iRandomList.get(iPosition)[0]=i;
							iRandomList.get(iPosition)[1]=j;
							iPosition++;
						}															
					} 					
					//die Koordinaten aller moeglichen Mauerstuecke werden in einer Liste gespeichert 
				}											
				if(iTmpRandomAmount > iRandomList.size())
				{
					iTmpRandomAmount = iRandomList.size();
					//falls die Anzahl der zu setzenden Mauerstuecke > der Anzahl der moeglichen Plaetze ist, wird die Anzahl reduziert 
				}									
				while(iTmpRandomAmount > 0)
				{
					iRandomPosition = (int)(Math.random() * iRandomList.size());
					iRandom = iRandomList.get(iRandomPosition);
					Map[iRandom[0]][iRandom[1]].setContent(WALL);
					iRandomList.remove(iRandomPosition);
					iTmpRandomAmount--;
					//aus der Liste werden zufaellig Koordinaten ausgewaehlt an deren Stellen Mauerstuecke platziert werden, bis die gewuenschte Anzahl erreicht ist
				}
			}
		}		
	}
}
