package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Field.Field;

public class boardPanel extends JPanel {

	BufferedImage imgExit;
	BufferedImage imgWall;
	BufferedImage imgFree;
	BufferedImage imgPlayer;
	BufferedImage imgBomb;
	BufferedImage imgBoom;
	Field boardField;
	List<int[]> iIntList;
	List<ArrayList> iBoomList;

	public boardPanel(Field field) {
		iBoomList = new ArrayList<ArrayList>();
		iIntList = new ArrayList<int[]>();
		boardField = field;
		try {
			imgExit = ImageIO.read(ImageIO.class
					.getResource("/images/exit.png"));
			imgWall = ImageIO.read(ImageIO.class
					.getResource("/images/wall.png"));
			imgFree = ImageIO.read(ImageIO.class
					.getResource("/images/free.png"));
			imgBomb = ImageIO.read(ImageIO.class
					.getResource("/images/bomb.png"));
			imgPlayer = ImageIO.read(ImageIO.class
					.getResource("/images/player.png"));
			imgBoom = ImageIO.read(ImageIO.class
					.getResource("/images/boom.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void addExplosions(ArrayList list) {
		iBoomList.add(list);
	}

	public void removeExplosions() {
		if (iBoomList.size() > 0) {
			iBoomList.remove(0);
		}
	}

	public void insertField(Field field) {
		boardField = field;
	}

	public void buildWorld(Graphics g) {
		g.setColor(new Color(200, 200, 170));
		g.fillRect(0, 0, this.getWidth() * 32, this.getHeight() * 32);
		for (int i = 0; i < boardField.getMap().length; i++) {
			for (int j = 0; j < boardField.getMap()[0].length; j++) {

				if (boardField.getField(i, j).getPlayer() != null) {
					g.drawImage(imgPlayer, i * 32, j * 32, 32, 32, null);
				} else {
					if (boardField.getField(i, j).getBomb() != null) {
						g.drawImage(imgBomb, i * 32, j * 32, 32, 32, null);
					} else {
						switch (boardField.getField(i, j).getContent()) {
						case 1:
							g.drawImage(imgFree, i * 32, j * 32, 32, 32, null);
							break;
						case 2:
							g.drawImage(imgWall, i * 32, j * 32, 32, 32, null);
							break;
						case 3:
							g.drawImage(imgExit, i * 32, j * 32, 32, 32, null);
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		buildWorld(g);
		paintBoom(g);
	}

	public void paintBoom(Graphics g) {
		if (iBoomList.size() > 0) {
			for (int i = 0; i < iBoomList.size(); i++) {
				for (int j = 0; j < iBoomList.get(i).size(); j++) {
					g.drawImage(imgBoom,
							((int[]) iBoomList.get(i).get(j))[0] * 32,
							((int[]) iBoomList.get(i).get(j))[1] * 32, 32, 32,
							null);
				}
			}
		}
	}
}
