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
 * BoardPanel.java Diese Klasse erzeugt ein Panel im Frame der GUI. Auf diesem
 * Panel wird die Karte gezeichnet, die Explosion hinzugefuegt und geloescht
 * 
 * @author Carsten Stegmann
 * 
 * 
 */
public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	BufferedImage imgExit;
	BufferedImage imgWall;
	BufferedImage imgFree;
	BufferedImage imgPlayer;
	BufferedImage imgPlayer2;
	BufferedImage imgBomb;
	BufferedImage imgBoom;
	BufferedImage imgStone;
	BufferedImage imgFireItem;
	BufferedImage imgBombItem;
	Field boardField;
	List<int[]> iIntList;
	List<ArrayList> iBoomList;

	/**
	 * Erzeugt ein Objekt der Klasse BoardPanel Liest Bilddateien der
	 * anzuzeigenden Objekte ein und initialisiert interne Listen
	 * 
	 */

	public BoardPanel() {
		iBoomList = new ArrayList<ArrayList>();
		iIntList = new ArrayList<int[]>();
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
			imgPlayer2 = ImageIO.read(ImageIO.class
					.getResource("/images/player2.png"));
			imgBoom = ImageIO.read(ImageIO.class
					.getResource("/images/boom.png"));
			imgStone = ImageIO.read(ImageIO.class
					.getResource("/images/stone.png"));
			imgFireItem = ImageIO.read(ImageIO.class
					.getResource("/images/boomIcon.png"));
			imgBombItem = ImageIO.read(ImageIO.class
					.getResource("/images/bombItem.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Fuegt die Explosionen einem internen Array hinzu, aus welchem die
	 * Informationen zum zeichnen der Explosionen ausgelesen werden
	 * 
	 * @param list
	 *            Liste der Felder mit Explosionen
	 */
	public void addExplosions(ArrayList list) {
		iBoomList.add(list);
	}

	/**
	 * Entfernt die Explosionen aus dem Array
	 */
	public void removeExplosions() {
		if (iBoomList.size() > 0) {
			iBoomList.remove(0);
		}
	}

	/**
	 * Fuegt neues Spielfeld in Panel zum anzeigen ein
	 * 
	 * @param field
	 *            Einzufuegendes Spielfeld
	 */
	public void insertField(Field field) {
		boardField = field;
	}

	/**
	 * Durchsucht Spielfeld nach zu zeichnenden Objekten und erzeugt an den
	 * entsprechenden Positionen die passenden Bilder ein
	 * 
	 * @param g
	 *            Die zu zeichnende Grafik
	 */
	public void buildWorld(Graphics g) {
		// g.setColor(new Color(200, 200, 170));
		if (boardField == null)
			return;
		g.fillRect(0, 0, this.getWidth() * 32, this.getHeight() * 32);
		for (int i = 0; i < boardField.getMap().length; i++) {
			for (int j = 0; j < boardField.getMap()[0].length; j++) {

				if (boardField.getField(i, j).getPlayer() != null) {
					if (boardField.getField(i, j).getPlayer().getID() == 1) {
						g.drawImage(imgPlayer, i * 32, j * 32, 32, 32, null);
					} else {
						g.drawImage(imgPlayer2, i * 32, j * 32, 32, 32, null);
					} // Zeichnet
						// die
						// Spieler
				} else {
					if (boardField.getField(i, j).getBomb() != null) {
						g.drawImage(imgBomb, i * 32, j * 32, 32, 32, null); // Zeichnet
																			// die
																			// Bombe
					} else {
						switch (boardField.getField(i, j).getContent()) {
						case 1:
							if (boardField.getField(i, j).isExit()) {
								g.drawImage(imgExit, i * 32, j * 32, 32, 32, // Zeichnet
																				// den
																				// Ausgang
										null);
							} else {
								g.drawImage(imgFree, i * 32, j * 32, 32, 32, // Zeichnet
																				// ein
																				// leeres
																				// Feld
										null);
							}
							{

								if (boardField.getField(i, j).isFireItem()) {
									g.drawImage(imgFireItem, i * 32, j * 32,
											32, 32, // Zeichnet das Item fuer
													// die Erhoehung des Radius
											null);

								}
								if (boardField.getField(i, j).isBombItem()) {
									g.drawImage(imgBombItem, i * 32, j * 32,
											32, 32, null); // Zeichnet das Item
															// fuer die
															// Steigerung an
															// maximalen Bomben
								}
								break;
							}

						case 2:
							g.drawImage(imgWall, i * 32, j * 32, 32, 32, null); // Zeichnet
																				// unzerstoerbare
																				// Wand
							break;
						case 6:
							g.drawImage(imgStone, i * 32, j * 32, 32, 32, null); // Zeichnet
																					// zerstoerbaren
																					// Stein
							break;

						}
					}
				}
			}
		}
	}

	/**
	 * Methode, die zur Darstellung der Karte (buildWorld) und der Explosion
	 * (paintBoom) aufgerufen wird
	 */
	public void paint(Graphics g) {
		super.paint(g);
		buildWorld(g);
		paintBoom(g);
	}

	/**
	 * Durchsucht Array nach Explosionselementen und zeichnet diese
	 * 
	 * @param g
	 *            Die zu zeichnende Grafik
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
