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
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
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

public class OptionFrame extends JFrame implements WindowListener,
		ActionListener {
	/**	
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Game game1;

	private int changedMap;
	private double changedDensity;
	private int changedVerticalMap;
	private int changedHorizontalMap;
	private boolean savedOptions = true;
	public static boolean mapModus = true;

	private final JLabel mapLabel = new JLabel();
	private final JLabel densityLabel = new JLabel();
	private final JPanel buttons;
	private final JLabel verticalLabel = new JLabel();
	private final JLabel horizontalLabel = new JLabel();

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
		changedVerticalMap = game1.rectangleMapHight;
		changedHorizontalMap = game1.rectangleMapWidht;

		// Fenstereinstellungen
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setSize(500, 500);
		setLayout(new BorderLayout());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width / 2 - (getWidth() / 2);
		int y = dim.height / 2 - (getHeight() / 2);
		setLocation(x, y);
		setResizable(true);
		JPanel controls = new JPanel(new GridLayout(0, 3));
		JPanel center = new JPanel(new GridLayout(0, 3));
		// JPanel west = new JPanel(new GridLayout(0, 1));
		final JRadioButton squareButton = new JRadioButton("Create Squaremap",
				true);
		final JRadioButton rectangleButton = new JRadioButton(
				"Create Rectanglemap", false);
		controls.add(squareButton);
		controls.add(rectangleButton);

		center.setBorder(new BevelBorder(BevelBorder.RAISED));
		controls.setBorder(new BevelBorder(BevelBorder.RAISED));
		JLabel lb4 = new JLabel("Spielfeldbreite:");
		JSlider horizontalSlider = new JSlider(5, 30, 20);
		horizontalSlider.setMinorTickSpacing(1);
		horizontalSlider.setMajorTickSpacing(5);
		horizontalSlider.setPaintTicks(true);
		horizontalSlider.setPaintLabels(true);

		horizontalSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changedHorizontalMap = ((JSlider) e.getSource()).getValue();
				System.out.println("Neuer Wert Spielfeldbreite: "
						+ ((JSlider) e.getSource()).getValue());
				savedOptions = false;
				updateHorizontalMapLabel();
			}
		});
		JLabel lb3 = new JLabel("Spielfeldhöhe:");
		JSlider verticalSlider = new JSlider(5, 30, 15);
		verticalSlider.setMinorTickSpacing(1);
		verticalSlider.setMajorTickSpacing(5);
		verticalSlider.setPaintTicks(true);
		verticalSlider.setPaintLabels(true);

		verticalSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				changedVerticalMap = ((JSlider) e.getSource()).getValue();
				System.out.println("Neuer Wert Spielfeldhöhe: "
						+ ((JSlider) e.getSource()).getValue());
				savedOptions = false;
				updateVerticalMapLabel();
			}
		});

		// Zeile 1
		JLabel lb1 = new JLabel("Spielfeldgröße:");
		JSlider mapSlider = new JSlider(5, 30, changedMap);
		mapSlider.setMinorTickSpacing(1);
		mapSlider.setMajorTickSpacing(5);
		mapSlider.setPaintTicks(true);
		mapSlider.setPaintLabels(true);
		updateMapLabel();

		squareButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mapModus = true;
				rectangleButton.setSelected(false);
				System.out.println(mapModus + ": Square");
			}

		});

		rectangleButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mapModus = false;
				squareButton.setSelected(false);
				System.out.println(mapModus + ": Rectangle");
			}

		});

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
		buttons = new JPanel(new FlowLayout());

		// nach 3 Opjekten wird eine neue Zeile begonnen

		// Zeile 1 ENDE

		// Zeile 2

		JLabel lb2 = new JLabel("Mauerdichte:");
		JSlider densitySlider = new JSlider(0, 100, 70);
		densitySlider.setMinorTickSpacing(5);
		densitySlider.setMajorTickSpacing(25);
		densitySlider.setPaintTicks(true);
		densitySlider.setPaintLabels(true);

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
		center.add(new JLabel(""));
		center.add(new JLabel("Settings SquareMap"));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(lb1);
		center.add(mapSlider);
		center.add(mapLabel);
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JSeparator());
		center.add(new JSeparator());
		center.add(new JSeparator());
		center.add(new JLabel(""));
		center.add(new JLabel("Settings RectangleMap"));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(lb4);
		center.add(horizontalSlider);
		center.add(horizontalLabel);
		center.add(lb3);
		center.add(verticalSlider);
		center.add(verticalLabel);
		buttons.add(lb2);
		buttons.add(densitySlider);
		buttons.add(densityLabel);
		buttons.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));

		/**
		 * SOUTH.Panel Belegung: Neue Buttons für die Optionen "OK",
		 * "Übernehmen", "Abbrechen" der festgelegten Einstellungen
		 */

		// buttons = new JPanel(new FlowLayout());

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
		buttons.setBorder(new BevelBorder(BevelBorder.RAISED));
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
		game1.setMapModus(mapModus);
		game1.setGameMapHight(changedVerticalMap);
		game1.setGameMapWidht(changedHorizontalMap);
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

	private void updateVerticalMapLabel() {
		verticalLabel.setText(new Integer(changedVerticalMap).toString());
		verticalLabel.repaint();
	}

	private void updateHorizontalMapLabel() {
		horizontalLabel.setText(new Integer(changedHorizontalMap).toString());
		horizontalLabel.repaint();
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
