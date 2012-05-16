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
		// testfield.insertMap(testGenerator.createSquareMap(11));
		testfield.insertMap(testGenerator.readMap("TestMap.txt"));
		// testfield.saveMap("savetest.txt", 1);
		// FieldGenerator testGenerator = new FieldGenerator(0, 50, 5);
		// gameField = new Field(testGenerator.createSquareMap(11));
		// gameField.setPlayer(1, 1);
		// gameField.setRandomEnemies(5);
		gameState = GameStates.INITIALIZED;
	}

	public Field getField() {
		return gameField;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			// GAME LOGIC
			if (gameState == GameStates.STARTED)
				System.out.println("Game started");
			// gameField.printField();
			if (gameState == GameStates.PAUSED)
				System.out.println("Game paused");
			// etc.
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
