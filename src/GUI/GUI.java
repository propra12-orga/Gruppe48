package GUI;
import Engine.Game;
import Engine.GameStates;
import Field.*;
import Objects.Player;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import javax.swing.ImageIcon;
import java.util.ArrayList;
//import javax.swing.JPanel;
//import javax.swing.JFrame;

public class GUI extends JFrame implements ActionListener, KeyListener { 

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

	//menu elements
	private JMenuItem startItem;
	private JMenuItem restartItem;
	private JMenuItem quitItem;
	private JMenuBar menu;
	private JMenu gameMenu;
	//
	
	private Game mainGame;
    
    public GUI(Field field, Game game) {
    	
        setFocusable(true);
        panel = new boardPanel(field);
        reinitialize(field, game);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//MENU
        menu = new JMenuBar();
		gameMenu = new JMenu("Game");
		startItem = new JMenuItem("Start Game");
		startItem.addActionListener(this);
		restartItem = new JMenuItem("Restart Game");
		restartItem.addActionListener(this);
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		gameMenu.add(startItem);
		gameMenu.add(restartItem);
		gameMenu.add(quitItem);
		menu.add(gameMenu);
//		this.add(menu, BorderLayout.NORTH);
		this.setJMenuBar(menu);
		//this.invalidate();
		//
        this.add(panel);
        //initWorld()
        this.addKeyListener(this);
    }
    
    public void reinitialize(Field field, Game game) {
    	this.mainGame = game;
    	gameField = field;
    	this.setSize((gameField.getMap().length)* 32, (gameField.getMap()[0].length + 1 )* 32 + 12);
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

	@Override
	public void actionPerformed(ActionEvent object) {
		if (object.getSource() == startItem){
			System.out.println("New Game wurde angeklickt");
			mainGame.gameState = GameStates.STARTED;
		}
		if (object.getSource() == quitItem){
			System.exit(0);
		}
		if (object.getSource() == restartItem){
			mainGame.restart(Game.createNewField(), new Player(1,1), true);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
//		mainGame.key = e.getKeyChar();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		mainGame.key = e.getKeyChar();
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