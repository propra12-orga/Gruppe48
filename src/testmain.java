// Dummy Klasse zu Testzwecken
public class testmain {

	field testfield;
	fieldGenerator testGenerator;
	public testmain ()
	{
		testGenerator = new fieldGenerator();
		testfield = new field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		testfield.insertMap(testGenerator.createSquareMap(11));
		for (int i = 0; i < 11; i++)
		{
			for (int j = 0; j < 11; j++)
			{
				System.out.print(testfield.iGetContent(i,j));
			}
			System.out.println("");
		}
	}
	

	public static void main(String args[])
	{
		new testmain();
	}
}
