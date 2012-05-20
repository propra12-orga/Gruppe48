package Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Field.Field;
import Field.FieldGenerator;
import GUI.GUI;
import Objects.Bomb;
import Objects.Player;

public class Game implements Runnable, KeyListener {

	private Field gameField;
	private GameStates gameState;
	private final FieldGenerator testGenerator;
	private final Field testfield;
	private final GUI gui;
	char key;
	long gameSpeed;
	Player player;
	List<Bomb> bombList;
	List<long[]> explosionList;
	long time;

	public Game() {
		explosionList = new ArrayList<long[]>();
		time = Calendar.getInstance().getTimeInMillis();
		bombList = new ArrayList<Bomb>();
		gameSpeed = 100;
		player = new Player(1, 1);
		testGenerator = new FieldGenerator();
		testfield = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		testfield.insertMap(testGenerator.createSquareMap(15));
		testfield.setPlayer(player);
		gui = new GUI(testfield);
		gui.setVisible(true);
		gameState = GameStates.STARTED;
		gui.addKeyListener(this);
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
				restart();
				break;
			case GAMEOVER:
				System.out.println("GAMEOVER");
				restart();
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
		while (gameState == GameStates.STARTED) {
			time = Calendar.getInstance().getTimeInMillis();
			if (gui.isVisible() == false) {
				System.exit(0);
			}
			handleBombs();
			handleMovement();
			gui.insertField(testfield);
			gui.repaint();
			try {
				Thread.sleep(gameSpeed);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		key = e.getKeyChar();

	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private void handleBombs() {
		ArrayList<int[]> exList;
		for (int i = 0; i < bombList.size(); i++) {
			if (bombList.get(i).getTimer() <= time) {
				exList = new ArrayList<int[]>();
				exList.add(new int[2]);
				exList.get(exList.size() - 1)[0] = bombList.get(i)
						.getPosition()[1];
				exList.get(exList.size() - 1)[1] = bombList.get(i)
						.getPosition()[0];
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] - j).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
					}
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] - j).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0] - j;
						tmp[1] = bombList.get(i).getPosition()[1];
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] - j).getContent() == 1) {
						exList.add(new int[2]);
						exList.get(exList.size() - 1)[0] = bombList.get(i)
								.getPosition()[1];
						exList.get(exList.size() - 1)[1] = bombList.get(i)
								.getPosition()[0] - j;
					} else {
						break;
					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] + j).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
					}
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] + j).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0] + j;
						tmp[1] = bombList.get(i).getPosition()[1];
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					if (testfield.getField(bombList.get(i).getPosition()[1],
							bombList.get(i).getPosition()[0] + j).getContent() == 1) {
						exList.add(new int[2]);
						exList.get(exList.size() - 1)[0] = bombList.get(i)
								.getPosition()[1];
						exList.get(exList.size() - 1)[1] = bombList.get(i)
								.getPosition()[0] + j;
					} else {
						break;
					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(
							bombList.get(i).getPosition()[1] - j,
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
					}
					if (testfield.getField(
							bombList.get(i).getPosition()[1] - j,
							bombList.get(i).getPosition()[0]).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0];
						tmp[1] = bombList.get(i).getPosition()[1] - j;
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					if (testfield.getField(
							bombList.get(i).getPosition()[1] - j,
							bombList.get(i).getPosition()[0]).getContent() == 1) {
						exList.add(new int[2]);
						exList.get(exList.size() - 1)[0] = bombList.get(i)
								.getPosition()[1] - j;
						exList.get(exList.size() - 1)[1] = bombList.get(i)
								.getPosition()[0];
					} else {
						break;
					}
				}
				for (int j = 1; j < bombList.get(i).getRadius(); j++) {
					if (testfield.getField(
							bombList.get(i).getPosition()[1] + j,
							bombList.get(i).getPosition()[0]).getPlayer() != null) {
						gameState = GameStates.GAMEOVER;
					}
					if (testfield.getField(
							bombList.get(i).getPosition()[1] + j,
							bombList.get(i).getPosition()[0]).getBomb() != null) {
						int tmp[] = new int[2];
						tmp[0] = bombList.get(i).getPosition()[0];
						tmp[1] = bombList.get(i).getPosition()[1] + j;
						for (int k = 0; k < bombList.size(); k++) {
							if ((bombList.get(k).getPosition()[0] == tmp[0])
									&& (bombList.get(k).getPosition()[1] == tmp[1])) {
								bombList.get(k).detonate();
								i = 0;
							}
						}
					}
					if (testfield.getField(
							bombList.get(i).getPosition()[1] + j,
							bombList.get(i).getPosition()[0]).getContent() == 1) {
						exList.add(new int[2]);
						exList.get(exList.size() - 1)[0] = bombList.get(i)
								.getPosition()[1] + j;
						exList.get(exList.size() - 1)[1] = bombList.get(i)
								.getPosition()[0];
					} else {
						break;
					}
				}

				explosionList.add(new long[1]);
				explosionList.get(0)[0] = Calendar.getInstance()
						.getTimeInMillis() + 500;
				gui.panel.addExplosions(exList);
				exList = null;
				testfield.removeBomb(bombList.get(i));
				bombList.remove(i);
			}
		}
		for (int i = 0; i < explosionList.size(); i++) {
			if (explosionList.get(i)[0] < (Calendar.getInstance()
					.getTimeInMillis())) {
				gui.panel.removeExplosions();
				explosionList.remove(0);
			}
		}
	}

	private void handleMovement() {
		switch (key) {
		case 'w':
			switch (testfield.getField(player.getPosition()[1],
					player.getPosition()[0] - 1).getContent()) {
			case 1:
				if (testfield.getField(player.getPosition()[1],
						player.getPosition()[0] - 1).getBomb() != null) {
					break;
				}
				testfield.removePlayer(player);
				player.moveUp();
				testfield.setPlayer(player);
				break;
			case 3:
				gameState = GameStates.VICTORY;
			}
			break;
		case 'a':
			switch (testfield.getField(player.getPosition()[1] - 1,
					player.getPosition()[0]).getContent()) {
			case 1:
				if (testfield.getField(player.getPosition()[1] - 1,
						player.getPosition()[0]).getBomb() != null) {
					break;
				}
				testfield.removePlayer(player);
				player.moveLeft();
				testfield.setPlayer(player);
				break;
			case 3:
				gameState = GameStates.VICTORY;
			}
			break;
		case 's':
			switch (testfield.getField(player.getPosition()[1],
					player.getPosition()[0] + 1).getContent()) {
			case 1:
				if (testfield.getField(player.getPosition()[1],
						player.getPosition()[0] + 1).getBomb() != null) {
					break;
				}
				testfield.removePlayer(player);
				player.moveDown();
				testfield.setPlayer(player);
				break;
			case 3:
				gameState = GameStates.VICTORY;
			}
			break;
		case 'd':
			switch (testfield.getField(player.getPosition()[1] + 1,
					player.getPosition()[0]).getContent()) {
			case 1:
				if (testfield.getField(player.getPosition()[1] + 1,
						player.getPosition()[0]).getBomb() != null) {
					break;
				}
				testfield.removePlayer(player);
				player.moveRight();
				testfield.setPlayer(player);
				break;
			case 3:
				gameState = GameStates.VICTORY;
			}
			break;
		case KeyEvent.VK_SPACE:
			bombList.add(new Bomb(player.getPosition()[0],
					player.getPosition()[1], time));
			testfield.setBomb(bombList.get(bombList.size() - 1));
			break;
		}
		key = 0;
	}

	public void restart() {
		for (int i = 0; i <= bombList.size(); i++) {
			gui.panel.removeExplosions();
		}
		bombList.clear();
		player.setPosition(1, 1);
		testfield.insertMap(testGenerator.createSquareMap(15));
		testfield.setPlayer(player);
		gui.insertField(testfield);
		gui.repaint();
		gameState = GameStates.STARTED;
	}

	public static void main(String args[]) {
		new Game();
	}
}
