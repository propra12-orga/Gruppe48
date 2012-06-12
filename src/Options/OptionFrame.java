package Options;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Diese Klasse erstellt einen Frame, das Panel und dessen Komponenten für die Optionen.
 * Die Optionen sollen folgendes beinhalten:
 * -Einstellungsmöglichkeiten der Spielfeldgröße,
 * -Einstellungsmöglichkeiten der Dichte der zerstörbaren Blöcke,
 * -Spielmodus
 */
/**
 * 
 * @author Carsten Stegmann
 * 
 */

public class OptionFrame extends JFrame implements WindowListener {
	/**	
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private final Game field;

	private int changedMap;
	private int changedDense;
	private boolean savedOptions = true;

	private final JLabel mapLabel = new JLabel();
	private final JLabel denseLabel = new JLabel();
	private final JPanel buttons;

	/**
	 * Erzeugt ein OpitonFrame und zeigt dieses an. Es besitzt verschiedene
	 * Komponenten
	 * 
	 * @param game
	 *            Das zugehörige Spiel an das das OptionFrame gebunden ist
	 */
	public OptionFrame() {
		// this.game = game;
		// changedMap = game.getGameOptions().getMap();
		// changedDense = game.getGameOptions().getDense();
		// Fenstereinstellungen
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setSize(500, 500);
		setLayout(new BorderLayout());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width / 2 - (getWidth() / 2);
		int y = dim.height / 2 - (getHeight() / 2);
		setLocation(x, y);
		setResizable(false);

		JPanel center = new JPanel(new GridLayout(0, 3, 3, 3)); // das zentrale
																// Panel auf dem
																// die
																// Opitonenliegen

		// Zeile 1
		JLabel lb1 = new JLabel("Spielfeldgröße:");
		JSlider mapSlider = new JSlider(16, 30);
		updateMapLabel();

		mapSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changedMap = ((JSlider) e.getSource()).getValue();
				savedOptions = false; //
				updateMapLabel();
			}
		});

		// nach 3 Opjekten wird eine neue Zeile begonnen
		center.add(lb1); // Zeile 1 Spalte 1
		center.add(mapSlider);// Zeile 1 Spalte 2
		center.add(mapLabel); // Zeile 1 Spalte 3
		// Zeile 1 ENDE

		// Zeile 2

		JLabel lb2 = new JLabel("Mauerdichte:");
		JSlider denseSlider = new JSlider(25, 80);
		updateDenseLabel();

		denseSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changedDense = ((JSlider) e.getSource()).getValue();
				savedOptions = false;
				updateDenseLabel();
			}
		});

		center.add(lb2); // Zeile 1 Spalte 1
		center.add(denseSlider);// Zeile 1 Spalte 2
		center.add(denseLabel); // Zeile 1 Spalte 3

		// Zeile 3

		/**
		 * SOUTH.Panel Belegung: Neue Buttons für die Optionen "OK",
		 * "Übernehmen", "Abbrechen" der festgelegten Einstellungen
		 */

		buttons = new JPanel(new FlowLayout());

		JButton saveBt = new JButton("Übernehmen");
		saveBt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				acceptOptions();

			}
		});

		JButton cancelBt = new JButton("Abbrechen");
		cancelBt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				checkCancelWithoutSave();
			}

		});

		JButton okBt = new JButton("OK");
		okBt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// acceptOptions();
				dispose();
			}

		});

		// Von links nach rechts sortiert
		buttons.add(okBt);
		buttons.add(saveBt);
		buttons.add(cancelBt);

		// Panels aufs Frame
		// add(center, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);

		// Endkalibrieren
		setVisible(true);
		pack();
	}

	/**
	 * Speichert die Eingestellten Optionen
	 * 
	 */

	private void acceptOptions() {

		savedOptions = true;
	}

	/**
	 * Setzt das Spiellabel auf die im Slider eingestellte Groesse
	 */

	private void updateMapLabel() {
		mapLabel.setText(new Integer(changedMap).toString());
		mapLabel.repaint();
	}

	private void updateDenseLabel() {
		denseLabel.setText(new Integer(changedDense).toString());
		denseLabel.repaint();
	}

	/**
	 * Sicherheitsabfrage ob man ohne die Änderungzuspeichern das Fenster
	 * schließen möchte
	 */
	private void checkCancelWithoutSave() {
		if (!savedOptions) {
			String yesNoOptions[] = { "Ja", "Nein" };
			int n = JOptionPane.showOptionDialog(null,
					"Abbrechen ohne zu speichern ?", "Abbrechen",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, yesNoOptions, yesNoOptions[0]);

			if (n == JOptionPane.YES_OPTION) {
				dispose();

			}
		} else {
			dispose();
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		checkCancelWithoutSave();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

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
