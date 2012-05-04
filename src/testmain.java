public class testmain {

	field testfield;
	fieldGenerator testGenerator;
	public testmain ()
	{
		testGenerator = new fieldGenerator();
		testfield = new field();
		testfield.insertMap(testGenerator.createSquareMap(7,7));
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 7; j++)
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
