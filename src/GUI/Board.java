package GUI;
import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;
import javax.swing.JPanel;

public class Board extends JPanel { 

    private final int OFFSET = 20;
    private final int SPACE = 32;
   

    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Exit> exits = new ArrayList<Exit>();
    private ArrayList<Free> frees = new ArrayList<Free>();
 //   private Player bomber;
    private int w = 0;
    private int h = 0;
 
    
   
    
private String level =    
     "22222222222\n"
    +"21111111112\n"
    +"21212121212\n"
    +"21111111112\n"
    +"21212121212\n"
    +"21111111112\n"
    +"21212121212\n"
    +"21113111112\n"
    +"21212121212\n"
    +"21111111112\n"
    +"22222222222\n";
    
    /* Ich kann leider immer noch keine "fieldgenerator-Map" benutzen
     * deshalb steht hier noch eine Testmap.
     */
    
    
    
    
    

    public Board() {

     
        setFocusable(true);
        initWorld();
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    public final void initWorld() {
        
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
          /*  } else if (item == '@') {
                bomber = new Player(x, y);
                x += SPACE;
          */         
            }

            h = y;
        }
    }

    public void buildWorld(Graphics g) {

        g.setColor(new Color(200, 200, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<MoveControll> world = new ArrayList<MoveControll>();
        world.addAll(walls);
        world.addAll(frees);
        world.addAll(exits);
      //  world.add(bomber);

       
        
        /*
         * Karte zeichnen
         */
        
        
        for (int i = 0; i < world.size(); i++) {

        	MoveControll item = (MoveControll) world.get(i);

        
                g.drawImage(item.getImage(), item.x(), item.y(), this);
            }

       
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        buildWorld(g);
    }
}