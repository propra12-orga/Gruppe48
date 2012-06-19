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

public class MapCreator extends JFrame implements WindowListener,
		ActionListener {

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
