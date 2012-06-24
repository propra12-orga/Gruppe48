package Field;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Alexander Die Klasse MapCreator stellt dem Benutzer eine
 *         Schnittstelle sowie einige Werkzeuge zu Verfügung um mit geringem
 *         Aufwand selber Spielfelder zu erzeugen Sie erzeugt ihr eigenes
 *         Fenster
 */
public class MapCreator extends JFrame implements WindowListener,
		ActionListener {

	/**
	 * Erzeugt ein Objekt der Klasse MapCreator Das benutzte Fenster wird mit
	 * einer Groesse von 800x600 Pixeln initialisiert, ist sichtbar und laesst
	 * seine Größe nicht verändern
	 */
	public MapCreator() {
		super("Create your OWN Map");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width / 2 - (getWidth() / 2);
		int y = dim.height / 2 - (getHeight() / 2);
		setLocation(x, y);
		setResizable(false);
		addWindowListener(this);
	}

	/**
	 * Erzeugt ein Panel im Fenster von MapCreator, über welches sämtliche
	 * Schaltflächen verfügbar gemacht werden
	 * 
	 * @author Alexander
	 * 
	 */
	public class CreationPanel extends JPanel {
		private ImageIcon player = new ImageIcon(
				ClassLoader.getSystemResource("images/player.png"));
		private ImageIcon exit = new ImageIcon(
				ClassLoader.getSystemResource("images/exit.png"));
		private ImageIcon stone = new ImageIcon(
				ClassLoader.getSystemResource("images/stone.png"));
		private ImageIcon wall = new ImageIcon(
				ClassLoader.getSystemResource("images/wall.png"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
