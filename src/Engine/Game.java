package Engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Field.Field;
import Field.FieldGenerator;
import GUI.GUI;
import Objects.Bomb;
import Objects.Player;

public class Game implements Runnable {

	private Field gameField;
	public GameStates gameState;

	private GUI gui;
	public char key;
	long gameSpeed;
	Player player;
	List<Bomb> bombList;
	List<long[]> explosionList;
	Calendar calendar;
	long time;
	private Field testfield;

	public Game(Field field, Player player) {
		this.player = player;
		explosionList = new ArrayList<long[]>();
		time = Calendar.getInstance().getTimeInMillis();
		bombList = new ArrayList<Bomb>();
		gameSpeed = 100;
		gameState = GameStates.INITIALIZED;
		testfield = field;
	}

	public Field getField() {
		return gameField;
	}

	public void insertGUI(GUI gui) {
		this.gui = gui;
	}

	public void restart(Field field, Player player, Boolean restart) {
		for (int i = 0; i <= bombList.size() + 1; i++) {
			gui.panel.removeExplosions();
		}
		this.player = player;
		explosionList = new ArrayList<long[]>();
		time = Calendar.getInstance().getTimeInMillis();
		bombList = new ArrayList<Bomb>();
		gameSpeed = 100;
		gameState = GameStates.INITIALIZED;
		testfield = field;
		if (restart)
			gameState = GameStates.STARTED;
	}

	@Override
	public void run() {
		Boolean doRestart = false;
		while (true && !doRestart) {
			switch (gameState) {
			case STARTED:
				start();
				System.out.println("Game started");
				break;
			case VICTORY:
				System.out.println("VICTORY");
				doRestart = true;
				break;
			case GAMEOVER:
				System.out.println("GAMEOVER");
				doRestart = true;
				break;
			case INITIALIZED:
				System.out.println("INITIALIZED");
				break;
			default:
				System.out.println("default");
				break;
			}
		}
		if (doRestart) {
			restart(Game.createNewField(), new Player(1, 1), true);
			run();
		}
	}

	public void pauseGame() {
		gameState = GameStates.PAUSED;
	}

	public void start() {

		time = Calendar.getInstance().getTimeInMillis();
		handleBombs();
		handleMovement();
	//	gui.insertField(testfield);
		gui.repaint();
		try {
			Thread.sleep(gameSpeed);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public static Field createNewField() {
		FieldGenerator testGenerator = new FieldGenerator();
		Field testfield = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		testfield.insertMap(testGenerator.createSquareMap(15));
		Player player = new Player(1, 1);
		testfield.setPlayer(player);
		return testfield;
	}

	/**
	 * Fragt Timer der Bomben ab. Falls eine Bombe explodiert, werden die
	 * betroffenen Felder an die GUI zur Darstellung uebergeben sowie weitere
	 * betroffene Bomben gezuendet. Steht der Spieler auf einem betroffenen
	 * Feld, so wird der Spielstatus auf GAMEOVER gesetzt
	 */
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

	/**
	 * Fragt gedrueckte Tasten ab und bewegt den Spieler entsprechend, wenn das
	 * neue Feld begehbar ist. w = hoch, a = links, s = unten, d = rechts Wird
	 * Space gedrueckt, so wird eine Bombe gelegt.
	 */
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

	public static void main(String args[]) {
		Field field = createNewField();
		Game game = new Game(field, new Player(1, 1));
		GUI gui = new GUI(field, game);
		gui.setVisible(true);
		game.insertGUI(gui);
		Thread gameThread = new Thread(game);
		gameThread.start();

	}
}
