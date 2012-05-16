
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

// hier wird das Fenster erstellt. 

public class GUI {
	field testfield;

	
	//Legt die Größe des Feldes fest:
	//public static int mapHeight = 11;
	//public static int mapWidth = 11;

	//Legt gie Größe der einzelnen Kacheln fest:
	//public static int tileHeight = 32;
	//public static int tileWidth = 32;

public static void main(String[] args) {

	
JFrame applikation = new JFrame("Bomberman Gruppe 48");
int frameWidth = 1000; //Breite des frames
int frameHeight = 800;//Höhe des frames
applikation.setSize(frameWidth, frameHeight);
applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
applikation.setResizable(true);   //Größe kann (noch) verändert werden
applikation.setVisible(true);
applikation.setLocationRelativeTo(null);   //Fenster öffnet in der Mitte des Bildschirms
}



private final Image[] tileImage;
{
testfield = new field();
	
	
//GUI.tileHeight = tileHeight;
//GUI.tileWidth = tileWidth;

//hier werden die images aus dem Ordner "/images" geladen. diese sehen natürlich nur zu Testzwecken so dämlich aus.
this.tileImage = new Image[6];
this.tileImage[0] = new ImageIcon(this.getClass().getResource("/images/empty.png")).getImage();
this.tileImage[1] = new ImageIcon(this.getClass().getResource("/images/free.png")).getImage();
this.tileImage[2] = new ImageIcon(this.getClass().getResource("/images/wall.png")).getImage();
this.tileImage[3] = new ImageIcon(this.getClass().getResource("/images/exit.png")).getImage();
this.tileImage[4] = new ImageIcon(this.getClass().getResource("/images/bomb.png")).getImage();
this.tileImage[5] = new ImageIcon(this.getClass().getResource("/images/player.png")).getImage();

}
public void draw(Graphics g) {

for (int iXCoord = 0; iXCoord < testfield.getMap().length; iXCoord++) {
for (int iYCoord = 0; iYCoord < testfield.getMap()[0].length; iYCoord++) {
g.drawImage(tileImage[testfield.iGetContent(iXCoord,iYCoord)], iXCoord ,iYCoord, null);
}
}

}

 


}

