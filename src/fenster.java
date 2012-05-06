import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

public class fenster implements ActionListener {
     
	
	
     JFrame applikation;
     Container container;
     
     // Menüleiste
     JMenuBar menueLeiste;
     
     // Menüleiste Elemente
     JMenu Game;
     JMenu hilfe;
     
     // Datei
     JMenuItem newGame;
     JMenuItem quit;
     
     // Hilfe
     JMenuItem dummy;
     JMenuItem about;

     
     public fenster() {
          applikation = new JFrame("Bomberman Gruppe48");
          container = applikation.getContentPane();
          
          // Menüleiste erzeugen
          menueLeiste = new JMenuBar();
          
          // Menüelemente erzeugen
          Game = new JMenu("Game");
          hilfe = new JMenu("Help");
          
          // Untermenüelemente erzeugen
          newGame = new JMenuItem("New Game");
          newGame.addActionListener(this);
          quit = new JMenuItem("Quit");
          quit.addActionListener(this);
          dummy = new JMenuItem("dummy");
          dummy.addActionListener(this);
          about = new JMenuItem("About");
          about.addActionListener(this);
          
          // Menüelemente hinzufügen
          menueLeiste.add(Game);
          menueLeiste.add(hilfe);
          
          // Untermenüelemente hinzufügen
          Game.add(newGame);
          Game.add(quit);
          hilfe.add(dummy);
          hilfe.add(about);

                  
          
          applikation.add(menueLeiste, BorderLayout.NORTH);
          
          applikation.setResizable(false);

          applikation.setSize(800, 600);
          applikation.setLocationRelativeTo(null);
          applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          applikation.setVisible(true);
          applikation.setBackground(Color.RED);

          
          JPanel Hintergrund = new JPanel();
          Hintergrund.setBackground(Color.RED);
     
         
     
     
     
     }
     
     public void actionPerformed(ActionEvent object) {
          if (object.getSource() == newGame){
               System.out.println("New Game wurde angeklickt");
          }
          if (object.getSource() == quit){
        	  System.exit(0);
          }
          if (object.getSource() == dummy){
               System.out.println("dummy wurde angeklickt");
          }
          if (object.getSource() == about){
               System.out.println("about wurde angeklickt");
          }
     }
     
     public static void main(String[] args) {
	   
    	 
    	 new fenster();
     }

	
	
}
