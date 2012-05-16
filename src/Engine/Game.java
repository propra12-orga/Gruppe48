package Engine;

import Field.Field;
import Field.FieldGenerator;

public class Game implements Runnable {

	private Field gameField;
	private GameStates gameState;
	private final FieldGenerator testGenerator;
	private final Field testfield;

	public Game() {
		testGenerator = new FieldGenerator();
		testfield = new Field();
		testGenerator.setRandomAmount(5);
		testGenerator.setRandomChance(50);
		testGenerator.setModus(0);
		testfield.insertMap(testGenerator.readMap("TestMap.txt"));
		gameState = GameStates.INITIALIZED;
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
		gameState = GameStates.STARTED;

	}

	public void restart() {

	}

}
