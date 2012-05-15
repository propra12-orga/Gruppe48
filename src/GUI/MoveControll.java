/*
 * Diese Klasse sollte als Collisioncheck dienen.
 * Denke aber, dass das wenig Sinn macht, wegen der Steuerung.
 * 
 * 
 * 
 * 
 */




package GUI;

import java.awt.Image;

public class MoveControll {

  //  private final int SPACE = 32;

    private int x;
    private int y;
    private Image image;

    public MoveControll(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image img) {
        image = img;
    }

   public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
