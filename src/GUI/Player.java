package GUI;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Player extends MoveControll {

    public Player(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("/pic/bomb.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }

  
}