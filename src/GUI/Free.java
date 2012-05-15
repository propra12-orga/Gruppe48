package GUI;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Free extends Player {

    public Free(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("/images/free.png");
       ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}
