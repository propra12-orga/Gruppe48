import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;

//Diese Klasse sollte eigentlich die Map laden und grafisch umsetzen. Tut sie aber nicht!!!
//Werde später noch Sprites usw. hinzufügen.
public class GUIfeld extends JPanel {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final int mapHeight;
private final int mapWidth;
private final int tileHeight;
private final int tileWidth;
private final Image[] tileImage;
private final int[][] map;

public GUIfeld(int mapWidth, int mapHeight, int tileWidth, int tileHeight) {
this.map = new int[mapWidth][mapHeight];
this.mapHeight = mapHeight;
this.mapWidth = mapWidth;
this.tileHeight = tileHeight;
this.tileWidth = tileWidth;

//hier werden die images aus dem Ordner "/images" geladen. diese sehen natürlich nur zu Testzwecken so dämlich aus.
this.tileImage = new Image[6];
/*this.tileImage[0] = new ImageIcon(this.getClass().getResource("/images/empty.png")).getImage();*/
this.tileImage[1] = new ImageIcon(this.getClass().getResource("/images/free.png")).getImage();
this.tileImage[2] = new ImageIcon(this.getClass().getResource("/images/wall.png")).getImage();
this.tileImage[3] = new ImageIcon(this.getClass().getResource("/images/exit.png")).getImage();
this.tileImage[4] = new ImageIcon(this.getClass().getResource("/images/bomb.png")).getImage();
//this.tileImage[5] = new ImageIcon(this.getClass().getResource("/images/player.png")).getImage();
}

public void draw(Graphics g) {
super.paint(g);
for (int iXCoord = 0; iXCoord < mapWidth; iXCoord++) {
for (int iYCoord = 0; iYCoord < mapHeight; iYCoord++) {
g.drawImage(tileImage[map[iYCoord][iXCoord]], iXCoord * tileWidth,iYCoord * tileHeight, null);
}
}

}

 

}