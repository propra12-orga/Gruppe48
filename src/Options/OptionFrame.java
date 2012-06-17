package Options;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Engine.Game;

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
	private final Game game1;

	private int changedMap;
	private double changedDensity;
	private boolean savedOptions = true;

	private final JLabel mapLabel = new JLabel();
	private final JLabel densityLabel = new JLabel();
	private final JPanel buttons;
	static final String gapList[] = { "0", "2", "4", "6", "8", "10", "12",
			"14", "16", "18" };
	final static int maxGap = 20;
	JComboBox horizontalGapComboBox;
	JComboBox verticalGapComboBox;

	public void initGaps() {
		horizontalGapComboBox = new JComboBox(gapList);
		verticalGapComboBox = new JComboBox(gapList);
	}

	/**
	 * Erzeugt ein OpitonFrame und zeigt dieses an. Es besitzt verschiedene
	 * Komponenten
	 * 
	 * @param game
	 *            Das zugehörige Spiel an das das OptionFrame gebunden ist
	 */
	public OptionFrame(Game game) {

		this.game1 = game;
		changedMap = game1.startMapSize;
		changedDensity = game1.startDensity;
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
		initGaps();
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(0, 3));

		JPanel center = new JPanel(new GridLayout(0, 3)); // das zentrale
															// Panel auf dem
															// die
															// Opitonenliegen

		center.setBorder(new BevelBorder(BevelBorder.RAISED));
		controls.setBorder(new BevelBorder(BevelBorder.RAISED));
		// controls.add(new Label(""));
		controls.add(new Label(""));

		controls.add(new Label("Settings for Rectangle Map", Label.CENTER));
		controls.add(new Label(""));

		// controls.add(new Label(""));

		controls.add(new Label("width:"));
		controls.add(new Label(""));
		// controls.add(new Label(""));
		controls.add(new Label("hight:"));
		// controls.add(new Label(""));
		// controls.add(new Label(""));
		controls.add(horizontalGapComboBox);
		controls.add(new Label(""));
		controls.add(verticalGapComboBox);
		controls.add(new Label(""));
		controls.add(new Label(""));
		controls.add(new Label(""));

		// Zeile 1
		JLabel lb1 = new JLabel("Spielfeldgröße:");
		JSlider mapSlider = new JSlider(13, 25, changedMap);
		mapSlider.setMinorTickSpacing(1);
		mapSlider.setMajorTickSpacing(10);
		mapSlider.setPaintTicks(true);
		updateMapLabel();

		center.add(new Label(""));
		center.add(new Label("Settings for Square Map", Label.CENTER));
		center.add(new Label(""));

		center.add(new Label(""));
		center.add(new Label(""));
		center.add(new Label(""));
		center.add(lb1); // Zeile 1 Spalte 1
		center.add(mapSlider);// Zeile 1 Spalte 2
		center.add(mapLabel); // Zeile 1 Spalte 3

		mapSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changedMap = ((JSlider) e.getSource()).getValue();
				System.out.println("Neuer Wert Spielfeldgröße: "
						+ ((JSlider) e.getSource()).getValue());
				savedOptions = false;
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
		JSlider densitySlider = new JSlider(1, 100);
		densitySlider.setMinorTickSpacing(1);
		densitySlider.setMajorTickSpacing(10);
		densitySlider.setPaintTicks(true);

		updateDensityLabel();

		densitySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changedDensity = ((JSlider) e.getSource()).getValue();
				System.out.println("Neuer Wert Blockdichte: "
						+ ((JSlider) e.getSource()).getValue());
				savedOptions = false;
				updateDensityLabel();
			}
		});

		center.add(lb2); // Zeile 2 Spalte 1
		center.add(densitySlider);// Zeile 2 Spalte 2
		center.add(densityLabel); // Zeile 2 Spalte 3

		// Zeile 3

		/**
		 * SOUTH.Panel Belegung: Neue Buttons für die Optionen "OK",
		 * "Übernehmen", "Abbrechen" der festgelegten Einstellungen
		 */

		buttons = new JPanel(new FlowLayout());

		JButton saveBt = new JButton("Save");
		saveBt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				acceptOptions();

			}
		});

		JButton cancelBt = new JButton("Cancel");
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
				acceptOptions();
				dispose();
			}

		});

		// Von links nach rechts sortiert
		buttons.add(okBt);
		buttons.add(saveBt);
		buttons.add(cancelBt);

		// Panels aufs Frame
		add(controls, BorderLayout.NORTH);
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

		game1.setGameMapOptions(changedMap);
		game1.setGameDensityOptions(changedDensity);
		savedOptions = true;
	}

	/**
	 * Setzt das Spiellabel auf die im Slider eingestellte Groesse
	 */

	private void updateMapLabel() {
		mapLabel.setText(new Integer(changedMap).toString());
		mapLabel.repaint();
	}

	private void updateDensityLabel() {
		densityLabel.setText(new BigDecimal(changedDensity).toString());
		densityLabel.repaint();
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
