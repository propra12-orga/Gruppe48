package Tests;
import Field.Field;
import Field.FieldGenerator;

// Dummy Klasse zu Testzwecken
public class testmain {

	Field testfield;
	FieldGenerator testGenerator;

	public testmain() {
		testGenerator = new FieldGenerator();
		testfield = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		 testfield.insertMap(testGenerator.createSquareMap(21));
		// testfield.insertMap(testGenerator.readMap("TestMap.txt"));
		// testfield.saveMap("savetest.txt", 1);
		for (int i = 0; i < testfield.getMap().length; i++) {
			for (int j = 0; j < testfield.getMap()[0].length; j++) {
				System.out.print(testfield.iGetContent(i, j));
			}
			System.out.println("");
		}
	}

	public static void main(String args[]) {
		new testmain();
	}
}
