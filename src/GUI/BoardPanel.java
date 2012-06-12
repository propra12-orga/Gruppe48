package GUI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Field.Field;

/**
 * 
 * @author Carsten Stegmann
 * 
 * 
 */
public class BoardPanel extends JPanel {

	BufferedImage imgExit;
	BufferedImage imgWall;
	BufferedImage imgFree;
	BufferedImage imgPlayer;
	BufferedImage imgBomb;
	BufferedImage imgBoom;
	BufferedImage imgStone;
	Field boardField;
	List<int[]> iIntList;
	List<ArrayList> iBoomList;

	/**
	 * erzeugt Inhalt des Panels
	 * 
	 * @param field
	 *            Spielfeld ueber das das Panel erzeugt werden soll
	 */

	public BoardPanel() {
		iBoomList = new ArrayList<ArrayList>();
		iIntList = new ArrayList<int[]>();
		// boardField = field;
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
			imgStone = ImageIO.read(ImageIO.class
					.getResource("/images/stone.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * f�gt Explosion dem Array zu
	 * 
	 * @param list
	 *            Liste der Felder mit Explosionen
	 */
	public void addExplosions(ArrayList list) {
		iBoomList.add(list);
	}

	/**
	 * entfernt Explosion aus dem Array
	 * 
	 * @param iBoomList
	 *            :
	 */
	public void removeExplosions() {
		if (iBoomList.size() > 0) {
			iBoomList.remove(0);
		}
	}

	public void insertField(Field field) {
		boardField = field;
	}

	/**
	 * durchsucht Array und ordnet mittels switch case den Arrayelementen dessen
	 * Images zu
	 * 
	 * @param g
	 */
	public void buildWorld(Graphics g) {
		// g.setColor(new Color(200, 200, 170));
		if (boardField == null)
			return;
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
							if (boardField.getField(i, j).isExit()) {
								g.drawImage(imgExit, i * 32, j * 32, 32, 32,
										null);
							} else {
								g.drawImage(imgFree, i * 32, j * 32, 32, 32,
										null);
							}
							break;
						case 2:
							g.drawImage(imgWall, i * 32, j * 32, 32, 32, null);
							break;
						case 6:
							g.drawImage(imgStone, i * 32, j * 32, 32, 32, null);
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

	/**
	 * durchsucht Array nach Explosionselementen und zeichnet diese
	 * 
	 * @param g
	 */
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
