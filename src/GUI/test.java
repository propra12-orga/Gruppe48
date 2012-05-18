package GUI;
import Field.*;
public class test {
	GUI gui;
	Field field;
	FieldGenerator generator;
	public test ()
	{
		generator = new FieldGenerator();
		field = new Field();
		field.insertMap(generator.createRectangleMap(11,7));
		gui = new GUI(field);	
		gui.setVisible(true);
		try{
		Thread.sleep(5000);
		}catch (Exception e)
		{}
		field.setField(1,1,3);
		gui.repaint();
	}
	public static void main (String args[])
	{
		new test();
	}
}
