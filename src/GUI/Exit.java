package GUI;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Exit extends Player {

    public Exit(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("/pic/exit.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}
