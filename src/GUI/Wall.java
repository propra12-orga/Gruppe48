package GUI;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Wall extends MoveControll {

    private Image image;

    public Wall(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("/pic/wall.png");
        ImageIcon iia = new ImageIcon(loc);
        image = iia.getImage();
        this.setImage(image);

    }
}

