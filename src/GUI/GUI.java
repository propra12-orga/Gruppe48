package GUI;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Field.Field;

public class GUI extends JFrame {

	private final int OFFSET = 20;
	private final int SPACE = 32;
	private final int w = 0;
	private final int h = 0;
	Field gameField;
	BufferedImage imgExit;
	BufferedImage imgWall;
	BufferedImage imgFree;
	public boardPanel panel;

	public GUI(Field field) {
		gameField = field;
		setFocusable(true);
		setResizable(false);
		panel = new boardPanel(field);
		this.setSize((gameField.getMap().length) * 32 + 5,
				(gameField.getMap()[0].length) * 32 + 27);
		this.add(panel);
	}

	public void insertField(Field field) {
		gameField = field;
		panel.insertField(field);
	}

	public int getBoardWidth() {
		return gameField.getMap().length;
	}

	public int getBoardHeight() {
		return gameField.getMap()[0].length;
	}

}