package Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Field.Field;
import Field.FieldGenerator;
import GUI.GUI;
import Objects.*;
public class Game implements Runnable, KeyListener{

	private Field gameField;
	private GameStates gameState;
	private final FieldGenerator testGenerator;
	private final Field testfield;
	private GUI gui;
	char key;
	long gameSpeed;
	Player player;
	List<Bomb> bombList;
	List<long[]> explosionList;
	Calendar calendar;
	long time;
	public Game() {
		explosionList = new ArrayList<long[]>();
		time = Calendar.getInstance().getTimeInMillis();
		bombList = new ArrayList<Bomb>();
		gameSpeed = 100;
		player = new Player(1,1);
		testGenerator = new FieldGenerator();
		testfield = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		testfield.insertMap(testGenerator.createSquareMap(15));
		testfield.setPlayer(player);
		gui = new GUI (testfield);
		gui.setVisible(true);
		gameState = GameStates.STARTED;
		gui.addKeyListener(this);
		System.out.println(gui.isFocusable());
		run();
	}

	public Field getField() {
		return gameField;
	}

	@Override
	public void run() {
		while (true) {
			switch (gameState) {
			case STARTED:
				start();
				System.out.println("Game started");
				break;
			case PAUSED:
				System.out.println("Game paused");
				pauseGame();
				break;
			case VICTORY:
				System.out.println("VICTORY");
				System.exit(1);
				break;
			case GAMEOVER:
				System.out.println("GAMEOVER");
				break;
			case INITIALIZED:
				System.out.println("INITIALIZED");
				break;
			default:
				System.out.println("default");
			}
		}
	}

	public void pauseGame() {
		gameState = GameStates.PAUSED;
	}

	public void start() {
		ArrayList<int[]> exList;
		while(gameState==GameStates.STARTED)
		{		
			
			time = Calendar.getInstance().getTimeInMillis();
			for(int i = 0; i < bombList.size(); i++)
			{
				if(bombList.get(i).getTimer() <= time)
				{
					exList = new ArrayList<int[]>();
					exList.add(new int[2]);
					exList.get(exList.size()-1)[0] = bombList.get(i).getPosition()[1];
					exList.get(exList.size()-1)[1] = bombList.get(i).getPosition()[0];
					for(int j = 1; j < bombList.get(i).getRadius(); j++)
					{					
						if(testfield.getField(bombList.get(i).getPosition()[1], bombList.get(i).getPosition()[0] - j).getPlayer() != null)
						{
							gameState = GameStates.GAMEOVER;
						}
						if(testfield.getField(bombList.get(i).getPosition()[1], bombList.get(i).getPosition()[0] - j).getContent() == 1)
						{
							exList.add(new int[2]);
							exList.get(exList.size()-1)[0] = bombList.get(i).getPosition()[1];
							exList.get(exList.size()-1)[1] = bombList.get(i).getPosition()[0] - j;							
						}
						else
						{
							break;							
						}
					}
					for(int j = 1; j < bombList.get(i).getRadius(); j++)
					{
						if(testfield.getField(bombList.get(i).getPosition()[1], bombList.get(i).getPosition()[0] + j).getPlayer() != null)
						{
							gameState = GameStates.GAMEOVER;
						}
						
						if(testfield.getField(bombList.get(i).getPosition()[1], bombList.get(i).getPosition()[0] + j).getContent() == 1)
						{
							exList.add(new int[2]);
							exList.get(exList.size()-1)[0] = bombList.get(i).getPosition()[1];
							exList.get(exList.size()-1)[1] = bombList.get(i).getPosition()[0] + j;			
						}
						else
						{
							break;							
						}
					}
					for(int j = 1; j < bombList.get(i).getRadius(); j++)
					{
						if(testfield.getField(bombList.get(i).getPosition()[1] - j, bombList.get(i).getPosition()[0]).getPlayer() != null)
						{
							gameState = GameStates.GAMEOVER;
						}
						
						if(testfield.getField(bombList.get(i).getPosition()[1] - j, bombList.get(i).getPosition()[0]).getContent() == 1)
						{
							exList.add(new int[2]);
							exList.get(exList.size()-1)[0] = bombList.get(i).getPosition()[1] - j;
							exList.get(exList.size()-1)[1] = bombList.get(i).getPosition()[0];			
						}
						else
						{
							break;							
						}
					}
					for(int j = 1; j < bombList.get(i).getRadius(); j++)
					{
						if(testfield.getField(bombList.get(i).getPosition()[1] + j, bombList.get(i).getPosition()[0]).getPlayer() != null)
						{
							gameState = GameStates.GAMEOVER;
						}
						
						if(testfield.getField(bombList.get(i).getPosition()[1] + j, bombList.get(i).getPosition()[0]).getContent() == 1)
						{
							exList.add(new int[2]);
							exList.get(exList.size()-1)[0] = bombList.get(i).getPosition()[1] + j;
							exList.get(exList.size()-1)[1] = bombList.get(i).getPosition()[0];			
						}
						else
						{
							break;							
						}
					}															
				
					explosionList.add(new long[1]);
					explosionList.get(0)[0] = calendar.getInstance().getTimeInMillis() + 500;
					gui.panel.addExplosions(exList);
					exList = null;
					testfield.removeBomb(bombList.get(i));
					bombList.remove(i);
				}
					
			}
			
			for(int i = 0; i < explosionList.size(); i++)
			{			
				if(explosionList.get(i)[0] <  (calendar.getInstance().getTimeInMillis()))
				{
					gui.panel.removeExplosions();
					explosionList.remove(0);
				}
			}
			switch(key)
			{
			case 'w':
				switch(testfield.getField(player.getPosition()[1], player.getPosition()[0] - 1).getContent())
				{
				case 1:
				testfield.removePlayer(player);
				player.moveUp();
				testfield.setPlayer(player);
				break;
				case 3:
				gameState = GameStates.VICTORY;
				}
				break;
			case 'a':
				switch(testfield.getField(player.getPosition()[1] - 1, player.getPosition()[0]).getContent())
				{
				case 1:
				testfield.removePlayer(player);
				player.moveLeft();
				testfield.setPlayer(player);
				break;
				case 3:
				gameState = GameStates.VICTORY;
				}
				break;
			case 's':		
				switch(testfield.getField(player.getPosition()[1], player.getPosition()[0] + 1).getContent())
				{
				case 1:
				testfield.removePlayer(player);
				player.moveDown();
				testfield.setPlayer(player);
				break;
				case 3:
				gameState = GameStates.VICTORY;
				}
				break;
			case 'd':
				switch(testfield.getField(player.getPosition()[1] + 1, player.getPosition()[0]).getContent())
				{
				case 1:
				testfield.removePlayer(player);
				player.moveRight();
				testfield.setPlayer(player);
				break;
				case 3:
				gameState = GameStates.VICTORY;
				}
				break;
			case KeyEvent.VK_SPACE:
				bombList.add(new Bomb(player.getPosition()[0], player.getPosition()[1], time));
				testfield.setBomb(bombList.get(bombList.size()-1));
				break;		
			}
			key = 0;
			gui.insertField(testfield);
			gui.repaint();
			try{
			Thread.sleep(gameSpeed);
			}catch(Exception ex)
			{
				System.out.println(ex);
			}
		}
	}
 
	public void keyTyped(KeyEvent e){
		key = e.getKeyChar();
	
	}
	
	public void keyPressed(KeyEvent e){
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	public void restart() {

	}
	public static void main(String args[])
	{
		new Game();
	}
}
