package Options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Engine.Game;

public class Options extends JFrame implements WindowListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private static Game gameOption;
	private final JLabel mapLabel = new JLabel();
	private final JLabel HorizontalLabel = new JLabel();
	private final JLabel VerticalLabel = new JLabel();
	private final JLabel ProbabilityLabel = new JLabel();
	private final JLabel AmountLabel = new JLabel();

	/**
	 * Variable fuer die geaenderte Groeße der quadratischen Karte
	 */
	private int changedMap;
	/**
	 * Variable fuer die geaenderte Dichte der zerstoerbaren Mauern
	 */
	private static double changedDensity;
	/**
	 * Variable fuer die geaenderte Hoehe der rechteckigen Karte
	 */
	private int changedVerticalMap;
	/**
	 * Variable fuer die geaenderte Breite der rechteckigen Karte
	 */
	private int changedHorizontalMap;
	private boolean savedOptions = true;
	/**
	 * Ist mapModus = true, so wird eine quadratische Map erstellt, sonst wird
	 * eine rechteckige Map erstellt
	 */
	public static boolean mapModus = true;
	/**
	 * Bestimmt den Modus der gespielt werden soll 1= Modus0, 2=Modus1, 3=Modus2
	 */
	public static int fillModus;
	/**
	 * Variable fuer die geaenderte Anzahl unzerstoerbarer Bloecke Modus2
	 */
	public int changedRandomAmount;
	/**
	 * Variable fuer die geaenderte Wahrscheinlichkeit Modus1
	 */
	public int changedProbability;

	double screenWidht = getToolkit().getScreenSize().getWidth() / 32;
	double screenHeight = getToolkit().getScreenSize().getHeight() / 32;
	int maxBoardWidht = (int) screenWidht;
	int maxBoardHeight = (int) screenHeight;
	private Game maingame;
	static int density = (int) getDensity();

	public static int getDens() {
		return density;
	}

	public static double getDensity() {
		return changedDensity;
	}

	public static boolean getMapModus() {
		return mapModus;
	}

	public static int getFillModus() {
		return fillModus;
	}

	/**
	 * Erstellt ein Objekt der Klasse Options und übergibt an Dieses die Game
	 * Klasse an die es angehaengt werden soll. Zusaetzlich wird ein Frame
	 * erzeugt und mit einer Groesse von 842 x 730 Pixeln initialisiert.
	 * 
	 * @param game
	 *            Die Game Klasse an die das Optionsmenue angehaengt werden soll
	 */
	public Options(Game game) {
		super("BomberMan-Options");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(842, 730);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width / 2 - (getWidth() / 2);
		int y = dim.height / 2 - (getHeight() / 2);
		setLocation(x, y);
		setResizable(false);
		addWindowListener(this);
		add(new MiddlePanel(0, 0, 400, 150));
		maingame = game;
		Options.gameOption = game;
		changedMap = gameOption.getStartMapSize();
		changedDensity = gameOption.getStartDensity();
		changedVerticalMap = gameOption.getGameMapWidht();
		changedHorizontalMap = gameOption.getGameMapHeight();
		fillModus = Game.getFillModus();
		changedRandomAmount = gameOption.getRAmount();
		changedProbability = gameOption.getProbability();

	}

	/**
	 * Das MiddlePanel ist ein Panel, auf dem saemtliche anderen Panels des
	 * Optionsmenues gelegt werden
	 * 
	 */
	public class MiddlePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private JPanel densityArea;
		private JPanel squareMapArea;
		private JPanel randomAmountArea;
		private JPanel probabilityArea;

		private JPanel mapWidht;
		private JPanel mapHeight;
		private JPanel buttons;
		private JPanel mapPic;
		private JLabel lblBild;
		private JPanel grayedPic;
		JPanel radioButtonMapModus = new JPanel(new GridLayout(1, 0));
		JPanel radioMapFillModus = new JPanel(new GridLayout(0, 3));
		JPanel borderForRectangle = new JPanel(new GridLayout(0, 1));
		private ImageIcon pic = new ImageIcon(
				ClassLoader.getSystemResource("images/MapOptionIcon.jpg"));

		/**
		 * Erzeugt ein Panel mit den angegebenen Massen auf dem die kleineren
		 * Panel erzeugt werden
		 * 
		 * @param x
		 *            Vertikale Position des Panels
		 * @param y
		 *            Horizontale Position des Panels
		 * @param w
		 *            Breite des Panels
		 * @param h
		 *            Hoehe des Panels
		 */
		public MiddlePanel(int x, int y, int w, int h) {
			super(null);
			setToolTipText("MiddlePanel");
			setBounds(x, y, w, h);
			initComp();

		}

		/**
		 * Die Komponenten fuer das MiddlePanel werden erzeugt
		 */
		private void initComp() {

			final JRadioButton squareButton = new JRadioButton(
					"Create Squaremap", true);
			final JRadioButton rectangleButton = new JRadioButton(
					"Create Rectanglemap", false);
			final JRadioButton modusZero = new JRadioButton("default:", true);
			final JRadioButton modusOne = new JRadioButton("Modus 1:", false);
			final JRadioButton modusTwo = new JRadioButton("Modus 2:", false);
			final JButton saveBt = new JButton("Save");
			JButton okBt = new JButton("OK");
			JButton cancelBt = new JButton("Cancel");
			final JSlider randomAmountSlider = new JSlider(0, 100, 5);
			final JSlider probabilitySlider = new JSlider(0, 100, 50);
			JSlider densitySlider = new JSlider(0, 100, 70);
			final JSlider mapSlider = new JSlider(10, maxBoardHeight, 15);
			updateMapLabel();
			mapLabel.setVisible(true);
			VerticalLabel.setVisible(false);
			HorizontalLabel.setVisible(false);
			/**
			 * Panel fuer den Slider der Dichte
			 */
			densityArea = new JPanel();
			densityArea.setBounds(221, 540, 400, 80);
			densityArea.add(densitySlider);
			TitledBorder density;
			density = BorderFactory.createTitledBorder("Density of Walls");
			density.setTitleColor(Color.blue);
			densityArea.setBorder(density);

			/**
			 * Panel fuer den Slider der Groeße quadratische Karte
			 */
			squareMapArea = new JPanel();
			squareMapArea.setBounds(6, 90, 400, 80);
			squareMapArea.add(mapSlider);
			final TitledBorder square;
			square = BorderFactory
					.createTitledBorder("Choose the Size of a Square-Map");
			squareMapArea.setBorder(square);
			square.setTitleColor(Color.blue);
			squareMapArea.add(mapLabel);
			/**
			 * Panel fuer die RadioButtons zur Auswahl quadratischer oder
			 * rechteckiger Karte
			 */
			radioButtonMapModus.setBounds(6, 6, 400, 50);
			radioButtonMapModus.add(squareButton);
			radioButtonMapModus.add(rectangleButton);
			radioButtonMapModus.setBorder(new BevelBorder(BevelBorder.RAISED));
			/**
			 * Panel fuer den Slider Breite rechteckige Karte
			 */
			final JSlider horizontalSlider = new JSlider(5, maxBoardWidht - 1,
					20);
			mapWidht = new JPanel();
			mapWidht.setBounds(40, 435, 280, 47);
			mapWidht.add(horizontalSlider);
			mapWidht.add(HorizontalLabel);
			mapWidht.setVisible(true);
			updateHorizontalMapLabel();
			// mapWidht.setBackground(Color.red);
			/**
			 * Panel fuer den Slider Höhe rechteckige Karte
			 */
			final JSlider verticalSlider = new JSlider(JSlider.VERTICAL, 5,
					maxBoardHeight - 2, 15);
			verticalSlider.setEnabled(true);
			mapHeight = new JPanel();
			mapHeight.setBounds(350, 240, 47, 245);
			mapHeight.add(verticalSlider);
			mapHeight.add(VerticalLabel);
			updateVerticalMapLabel();
			// mapHeight.setBackground(Color.red);

			/**
			 * Panel fuer die Buttons OK, SAVE, ABBRECHEN
			 */
			buttons = new JPanel(new GridLayout(0, 3));
			buttons.setBounds(221, 640, 400, 45);
			buttons.add(okBt);
			buttons.add(saveBt);
			buttons.add(cancelBt);
			buttons.setBorder(new BevelBorder(BevelBorder.RAISED));
			/**
			 * Panel fuer das Bild der Karte
			 */
			mapPic = new JPanel();
			mapPic.setBounds(25, 265, 300, 150);
			lblBild = new JLabel(pic);
			mapPic.add(lblBild);
			grayedPic = new JPanel();
			grayedPic.setBounds(25, 270, 300, 145);
			grayedPic.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
			grayedPic.setVisible(true);
			/**
			 * Panel fuer den Rahmen der Auswahl rechteckige Karte
			 */
			borderForRectangle.setBounds(6, 200, 400, 320);
			borderForRectangle.add(new JLabel(""));
			final TitledBorder rectangle;
			rectangle = BorderFactory
					.createTitledBorder("Choose the Size of a Rectangle-Map");
			borderForRectangle.setBorder(rectangle);
			rectangle.setTitleColor(Color.lightGray);
			/**
			 * Panel fuer die Auswahl der FüllModi
			 */
			radioMapFillModus = new JPanel(new GridLayout(0, 1));
			radioMapFillModus.setBounds(430, 6, 400, 300);
			radioMapFillModus.setToolTipText("radioButtonMapModus");
			radioMapFillModus.add(modusZero);
			radioMapFillModus
					.add(new JLabel(
							"          - In every second row and column a solid Block"));
			radioMapFillModus.add(modusOne);
			radioMapFillModus
					.add(new JLabel(
							"          - In every second row and column a solid Block with a "));
			radioMapFillModus.add(new JLabel(
					"             variable probability"));

			radioMapFillModus.add(modusTwo);
			radioMapFillModus
					.add(new JLabel(
							"          - an chosen Amount of solid Blocks will be filled randomly"));
			TitledBorder modus;
			modus = BorderFactory.createTitledBorder("Choose a Fill Modus");
			modus.setTitleColor(Color.blue);
			radioMapFillModus.setBorder(modus);
			/**
			 * Panel fuer den Slider Einstellungen Modus1
			 */
			randomAmountArea = new JPanel();
			randomAmountArea.setBounds(430, 440, 400, 80);
			randomAmountArea.add(randomAmountSlider);
			final TitledBorder amount;
			amount = BorderFactory
					.createTitledBorder("Amount of Blocks in Modus 2");
			amount.setTitleColor(Color.lightGray);
			randomAmountArea.setBorder(amount);
			randomAmountSlider.setEnabled(false);
			randomAmountArea.add(AmountLabel);
			updateAmountLabel();
			/**
			 * Panel fuer den Slider Einstellungen Modus2
			 */
			probabilityArea = new JPanel();
			probabilityArea.setBounds(430, 340, 400, 80);
			probabilityArea.add(probabilitySlider);
			final TitledBorder probability;
			probability = BorderFactory
					.createTitledBorder("Probability of Modus 1");
			probability.setTitleColor(Color.lightGray);
			probabilityArea.setBorder(probability);
			probabilitySlider.setEnabled(false);
			probabilityArea.add(ProbabilityLabel);
			updateProbabilityLabel();
			/**
			 * Fuegt alle Panel dem MiddelPanel hinzu
			 */
			add(grayedPic);
			add(densityArea);
			add(squareMapArea);
			add(radioButtonMapModus);
			add(mapWidht);
			add(mapHeight);
			add(buttons);
			add(mapPic);
			add(borderForRectangle);
			add(radioMapFillModus);
			add(randomAmountArea);
			add(probabilityArea);
			/**
			 * Eigenschaften fuer Slider Dichte der Mauern
			 */
			densitySlider.setMinorTickSpacing(5);
			densitySlider.setMajorTickSpacing(25);
			densitySlider.setPaintTicks(true);
			densitySlider.setPaintLabels(true);
			/**
			 * Eigenschaften fuer Slider quadratische Karte
			 */
			mapSlider.setMinorTickSpacing(1);
			mapSlider.setMajorTickSpacing(5);
			mapSlider.setPaintTicks(true);
			mapSlider.setPaintLabels(true);
			mapSlider.setEnabled(true);
			/**
			 * Eigenschaften fuer Slider Breite rechteckige Karte
			 */
			horizontalSlider.setMinorTickSpacing(1);
			horizontalSlider.setMajorTickSpacing(5);
			horizontalSlider.setPaintTicks(true);
			horizontalSlider.setPaintLabels(true);
			horizontalSlider.setEnabled(false);
			/**
			 * Eigenschaften fuer Slider Hoehe rechteckige Karte
			 */
			verticalSlider.setMinorTickSpacing(1);
			verticalSlider.setMajorTickSpacing(5);
			verticalSlider.setPaintTicks(true);
			verticalSlider.setPaintLabels(true);
			verticalSlider.setEnabled(false);
			/**
			 * Eigenschaften fuer Slider Modus2
			 */
			randomAmountSlider.setMinorTickSpacing(5);
			randomAmountSlider.setMajorTickSpacing(25);
			randomAmountSlider.setPaintTicks(true);
			randomAmountSlider.setPaintLabels(true);
			/**
			 * Eigenschaften fuer Slider Modus1
			 */
			probabilitySlider.setMinorTickSpacing(5);
			probabilitySlider.setMajorTickSpacing(25);
			probabilitySlider.setPaintTicks(true);
			probabilitySlider.setPaintLabels(true);
			saveBt.addActionListener(new ActionListener() {
				@Override
				/**
				 * Beim Klick auf den Save Button werden die Optionen gespeichert
				 */
				public void actionPerformed(ActionEvent e) {
					acceptOptions();
				}
			});
			cancelBt.addActionListener(new ActionListener() {
				@Override
				/**
				 * Beim Klick auf den Cancel Button wird das Fenster geschlossen ohne zu speichern
				 */
				public void actionPerformed(ActionEvent e) {
					checkCancelWithoutSave();
				}
			});
			okBt.addActionListener(new ActionListener() {
				@Override
				/**
				 * Beim Klick auf dne OK Button werden die Optionen gespeichert und das Fenster geschlossen
				 */
				public void actionPerformed(ActionEvent e) {
					acceptOptions();
					dispose();
					maingame.restart();
				}
			});
			verticalSlider.addChangeListener(new ChangeListener() {
				@Override
				/**
				 * Wird der vertikale Slider geaendert, so wird die Hoehe der zu erstellenden Map geaendert
				 */
				public void stateChanged(ChangeEvent e) {
					changedVerticalMap = ((JSlider) e.getSource()).getValue();
					System.out.println("Neuer Wert Spielfeldhöhe: "
							+ ((JSlider) e.getSource()).getValue());
					gameOption.setGameMapHeight(changedVerticalMap);
					maingame.restart();

					savedOptions = false;

					updateVerticalMapLabel();
					// repaint();
				}
			});
			horizontalSlider.addChangeListener(new ChangeListener() {
				@Override
				/**
				 * Wird der horizontale Slider geaendert, so wird die Breite der zu erstellenden Map geaendert
				 */
				public void stateChanged(ChangeEvent e) {
					changedHorizontalMap = ((JSlider) e.getSource()).getValue();

					System.out.println("Neuer Wert Spielfeldbreite: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					gameOption.setGameMapWidht(changedHorizontalMap);
					maingame.restart();
					updateHorizontalMapLabel();
				}
			});
			mapSlider.addChangeListener(new ChangeListener() {
				@Override
				/**
				 * Wird der mapSlider veraendert, so wird die Hoehe und Breite der zu erstellenden Map geaendert
				 */
				public void stateChanged(ChangeEvent e) {
					changedMap = ((JSlider) e.getSource()).getValue();
					System.out.println("Neuer Wert Spielfeldgröße: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					gameOption.setGameMapOptions(changedMap);
					maingame.restart();
					updateMapLabel();
				}
			});
			densitySlider.addChangeListener(new ChangeListener() {
				@Override
				/**
				 * Wird der densitySlayer veraendert, so wird die Dichte der Bloecke des zu erstellendne Feldes geaendert
				 */
				public void stateChanged(ChangeEvent e) {
					changedDensity = ((JSlider) e.getSource()).getValue();
					System.out.println("Neuer Wert Blockdichte: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					gameOption.setGameDensityOptions(changedDensity);
					maingame.restart();

				}
			});
			probabilitySlider.addChangeListener(new ChangeListener() {
				@Override
				/**
				 * Wird der probabilitySlider veraendert, so wird die Chance auf zerstoerbare Bloecke des zu erstellenden Feldes fuer Modus 1 geaendert
				 */
				public void stateChanged(ChangeEvent e) {
					changedProbability = ((JSlider) e.getSource()).getValue();
					System.out.println("Wahrscheinlichkeit Modus1: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					gameOption.setProbability(changedProbability);
					maingame.restart();
					updateProbabilityLabel();
					// updateDensityLabel();
				}
			});
			randomAmountSlider.addChangeListener(new ChangeListener() {
				@Override
				/**
				 * Wird der probabilitySlider veraendert, so wird die Chance auf zerstoerbare Bloecke des zu erstellenden Feldes fuer Modus 2 geaendert
				 */
				public void stateChanged(ChangeEvent e) {
					changedRandomAmount = ((JSlider) e.getSource()).getValue();
					System.out.println("Amount Modus 2:: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					gameOption.setRAmount(changedRandomAmount);
					maingame.restart();
					updateAmountLabel();
				}
			});
			squareButton.addActionListener(new ActionListener() {
				/**
				 * Wird der squareButton angeklickt, so wird als naechstes eine
				 * quadratische Map erstellt
				 */
				public void actionPerformed(ActionEvent e) {
					// maingame.restart();
					mapModus = true;
					rectangleButton.setSelected(false);
					squareButton.setSelected(true);
					System.out.println(mapModus + ": Square");

					verticalSlider.setEnabled(false);
					horizontalSlider.setEnabled(false);
					mapSlider.setEnabled(true);
					grayedPic.setVisible(true);
					square.setTitleColor(Color.BLUE);
					rectangle.setTitleColor(Color.lightGray);
					VerticalLabel.setVisible(false);
					HorizontalLabel.setVisible(false);
					mapLabel.setVisible(true);
				}
			});
			rectangleButton.addActionListener(new ActionListener() {
				/**
				 * Wird der rectangleButton angeklickt, so wird als naechstes
				 * eine rechteckige Map erstellt
				 */
				public void actionPerformed(ActionEvent e) {
					mapModus = false;
					squareButton.setSelected(false);
					rectangleButton.setSelected(true);
					System.out.println(mapModus + ": Rectangle");
					verticalSlider.setEnabled(true);
					horizontalSlider.setEnabled(true);
					mapSlider.setEnabled(false);
					grayedPic.setVisible(false);
					mapPic.setVisible(true);
					square.setTitleColor(Color.lightGray);
					rectangle.setTitleColor(Color.BLUE);
					mapLabel.setVisible(false);
					VerticalLabel.setVisible(true);
					HorizontalLabel.setVisible(true);

				}
			});
			modusZero.addActionListener(new ActionListener() {
				/**
				 * Wird der modusZero Button angeklickt, so wird der
				 * Generierungmodus auf Modus 0 gesetzt
				 */
				public void actionPerformed(ActionEvent e) {
					fillModus = 0;
					System.out.println("fillModus:" + fillModus);
					modusOne.setSelected(false);
					modusTwo.setSelected(false);
					modusZero.setSelected(true);
					randomAmountSlider.setEnabled(false);
					probabilitySlider.setEnabled(false);
					probability.setTitleColor(Color.lightGray);
					amount.setTitleColor(Color.lightGray);
				}
			});
			modusOne.addActionListener(new ActionListener() {
				/**
				 * Wird der modusOne Button angeklickt, so wird der
				 * Generierungmodus auf Modus 1 gesetzt
				 */
				public void actionPerformed(ActionEvent e) {
					fillModus = 1;
					System.out.println("fillModus:" + fillModus);
					modusZero.setSelected(false);
					modusTwo.setSelected(false);
					modusOne.setSelected(true);
					randomAmountSlider.setEnabled(false);
					probabilitySlider.setEnabled(true);
					probability.setTitleColor(Color.BLUE);
					amount.setTitleColor(Color.lightGray);
					// probabilityArea.setVisible(true);
					// randomAmountArea.setVisible(false);
				}
			});
			modusTwo.addActionListener(new ActionListener() {
				/**
				 * Wird der modusTwo Button angeklickt, so wird der
				 * Generierungmodus auf Modus 2 gesetzt
				 */
				public void actionPerformed(ActionEvent e) {
					fillModus = 2;
					System.out.println("fillModus:" + fillModus);
					modusZero.setSelected(false);
					modusOne.setSelected(false);
					modusTwo.setSelected(true);
					randomAmountSlider.setEnabled(true);
					probabilitySlider.setEnabled(false);
					probability.setTitleColor(Color.lightGray);
					amount.setTitleColor(Color.BLUE);
					// randomAmountArea.setVisible(true);
					// probabilityArea.setVisible(true);
				}
			});
		}

		/**
		 * Wird ausgefuehrt, bei Betaetigung des OK- u. Save Buttons
		 */
		private void acceptOptions() {
			gameOption.setGameMapOptions(changedMap);
			gameOption.setGameDensityOptions(changedDensity);
			gameOption.setMapModus(mapModus);
			gameOption.setGameMapHeight(changedVerticalMap);
			gameOption.setGameMapWidht(changedHorizontalMap);
			gameOption.setFillModus(fillModus);
			gameOption.setProbability(changedProbability);
			gameOption.setRAmount(changedRandomAmount);
			savedOptions = true;
		}
	}

	private void updateMapLabel() {
		mapLabel.setText(new Integer(changedMap).toString());
		mapLabel.repaint();
	}

	private void updateHorizontalMapLabel() {
		HorizontalLabel.setText(new Integer(changedHorizontalMap).toString());
		HorizontalLabel.repaint();
	}

	private void updateVerticalMapLabel() {
		VerticalLabel.setText(new Integer(changedVerticalMap).toString());
		VerticalLabel.repaint();
	}

	private void updateProbabilityLabel() {
		ProbabilityLabel.setText(new Integer(changedProbability).toString());
		ProbabilityLabel.repaint();
	}

	private void updateAmountLabel() {
		AmountLabel.setText(new Integer(changedRandomAmount).toString());
		AmountLabel.repaint();
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
	/**
	 * Leere Standardmethode, welche fuer die implementierte Klasse vorhanden sein muss
	 */
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	/**
	 * Leere Standardmethode, welche fuer die implementierte Klasse vorhanden sein muss
	 */
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * Leere Standardmethode, welche fuer die implementierte Klasse vorhanden sein muss
	 */
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * Beim schliessen des Fensters wird das Fenster geschlossen ohne die Optionen zu speichern
	 */
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		checkCancelWithoutSave();
	}

	@Override
	/**
	 * Leere Standardmethode, welche fuer die implementierte Klasse vorhanden sein muss
	 */
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * Leere Standardmethode, welche fuer die implementierte Klasse vorhanden sein muss
	 */
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Leere Standardmethode, welche fuer die implementierte Klasse vorhanden
	 * sein muss
	 */
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Leere Standardmethode, welche fuer die implementierte Klasse vorhanden
	 * sein muss
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
}