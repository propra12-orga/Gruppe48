
import javax.swing.JFrame;

// hier wird das Fenster erstellt. 

public class GUI {

	
	//Legt die Größe des Feldes fest:
	public static int mapHeight = 11;
	public static int mapWidth = 11;

//Legt gie Größe der einzelnen Kacheln fest:
	public static int tileHeight = 32;
	public static int tileWidth = 32;

public static void main(String[] args) {

	
JFrame applikation = new JFrame("Bomberman Gruppe 48");
int frameWidth = mapWidth * tileWidth; //Breite des frames
int frameHeight = mapHeight * tileHeight;//Höhe des frames
applikation.setSize(frameWidth, frameHeight);
applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
applikation.setResizable(true);   //Größe kann (noch) verändert werden
applikation.setVisible(true);
applikation.setLocationRelativeTo(null);   //Fenster öffnet in der Mitte des Bildschirms


GUIfeld Gf = new GUIfeld(mapWidth, mapHeight, tileWidth, tileHeight);
applikation.add(Gf);


}

}