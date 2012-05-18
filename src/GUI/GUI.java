package GUI;
import Field.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import javax.swing.ImageIcon;
import java.util.ArrayList;
//import javax.swing.JPanel;
//import javax.swing.JFrame;

public class GUI extends JFrame { 

    private final int OFFSET = 20;
    private final int SPACE = 32;
   
 //   private Player bomber;
    private int w = 0;
    private int h = 0;
    Field gameField;
    BufferedImage imgExit;
    BufferedImage imgWall;
    BufferedImage imgFree;
    public  boardPanel panel; 
    /* Ich kann leider immer noch keine "fieldgenerator-Map" benutzen
     * deshalb steht hier noch eine Testmap.
     */

    public GUI(Field field) {
    	gameField = field;
        setFocusable(true);
        panel = new boardPanel(field);
        this.setSize((gameField.getMap().length + 1 )* 32, (gameField.getMap()[0].length + 1 )* 32);
        this.add(panel);
        //initWorld()
        
    }

    public void insertField(Field field)
    {
    	gameField = field;
    	panel.insertField(field);
    }
    
    public int getBoardWidth() {
        return gameField.getMap().length;
    }

    public int getBoardHeight() {
        return gameField.getMap()[0].length;
    }

   /* public final void initWorld() {
        
        int x = OFFSET;
        int y = OFFSET;
        
        Wall wall;
        Exit e;
        Free f;


        for (int i = 0; i < level.length(); i++) {

            char item = level.charAt(i);

            if (item == '\n') {
                y += SPACE;
                if (this.w < x) {
                    this.w = x;
                }

                x = OFFSET;
            } else if (item == '2') {
                wall = new Wall(x, y);
                walls.add(wall);
                x += SPACE;
            } else if (item == '1') {
                f = new Free(x, y);
                frees.add(f);
                x += SPACE;
            } else if (item == '3') {
                e = new Exit(x, y);
                exits.add(e);
                x += SPACE;
            } else if (item == '@') {
                bomber = new Player(x, y);
                x += SPACE;
                   
            }

            h = y;
        }
    }*/
}