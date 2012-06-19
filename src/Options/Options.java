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

	private static Game gameOption;

	private int changedMap;
	private double changedDensity;
	private int changedVerticalMap;
	private int changedHorizontalMap;
	private boolean savedOptions = true;
	public boolean mapModus = true;
	public int fillModus;
	public int changedRandomAmount;
	public int changedProbability;

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

		this.gameOption = game;
		changedMap = gameOption.startMapSize;
		changedDensity = gameOption.startDensity;
		changedVerticalMap = gameOption.rectangleMapHight;
		changedHorizontalMap = gameOption.rectangleMapWidht;
		fillModus = gameOption.fillModus;
		changedRandomAmount = gameOption.startRandomAmount;
		changedProbability = gameOption.startProbability;

	}

	public class MiddlePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private JPanel densityArea;
		private JPanel squareMapArea;
		private JPanel randomAmountArea;
		private JPanel probabilityArea;

		private JPanel mapWidht;
		private JPanel mapHight;
		private JPanel buttons;
		private JPanel mapPic;
		private JLabel lblBild;
		private JPanel grayedPic;
		JPanel radioButtonMapModus = new JPanel(new GridLayout(1, 0));
		JPanel radioMapFillModus = new JPanel(new GridLayout(0, 3));
		JPanel borderForRectangle = new JPanel(new GridLayout(0, 1));
		private ImageIcon pic = new ImageIcon(
				ClassLoader.getSystemResource("images/MapOptionIcon.jpg"));

		public MiddlePanel(int x, int y, int w, int h) {
			super(null);
			setToolTipText("MiddlePanel");
			setBounds(x, y, w, h);
			initComp();

		}

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
			final JSlider mapSlider = new JSlider(10, 25, 15);
			densityArea = new JPanel();
			densityArea.setBounds(221, 540, 400, 80);
			densityArea.setToolTipText("densityarea");
			densityArea.add(densitySlider);
			// densityArea.setBackground(Color.CYAN);
			TitledBorder density;
			density = BorderFactory.createTitledBorder("Density of Walls");
			density.setTitleColor(Color.blue);

			densityArea.setBorder(density);
			// densityArea.setBorder(new BevelBorder(BevelBorder.RAISED));

			squareMapArea = new JPanel();
			squareMapArea.setBounds(6, 90, 400, 80);
			squareMapArea.setToolTipText("squareMapArea");
			// squareMapArea.setBackground(Color.red);
			squareMapArea.add(mapSlider);
			final TitledBorder square;
			square = BorderFactory
					.createTitledBorder("Choose the Size of a Square-Map");
			squareMapArea.setBorder(square);
			square.setTitleColor(Color.blue);

			// radioButtonMapModus = new JPanel(new GridLayout(1, 0));
			radioButtonMapModus.setBounds(6, 6, 400, 50);
			radioButtonMapModus.setToolTipText("radioButtonMapModus");
			// radioButtonMapModus.setBackground(Color.BLUE);
			radioButtonMapModus.add(squareButton);
			radioButtonMapModus.add(rectangleButton);
			radioButtonMapModus.setBorder(new BevelBorder(BevelBorder.RAISED));

			final JSlider horizontalSlider = new JSlider(5, 50, 20);
			mapWidht = new JPanel();
			mapWidht.setBounds(60, 435, 200, 47);
			mapWidht.setToolTipText("mapWidht");
			mapWidht.add(horizontalSlider);
			// mapWidht.setBackground(Color.yellow);
			mapWidht.setVisible(true);

			final JSlider verticalSlider = new JSlider(JSlider.VERTICAL, 5, 25,
					15);
			verticalSlider.setEnabled(true);
			mapHight = new JPanel();
			mapHight.setBounds(350, 240, 47, 205);
			mapHight.setToolTipText("mapHight");
			// mapHight.setBackground(Color.GREEN);
			mapHight.add(verticalSlider);
			buttons = new JPanel(new GridLayout(0, 3));
			buttons.setBounds(221, 640, 400, 45);
			buttons.setToolTipText("buttons");
			// buttons.setBackground(Color.PINK);
			buttons.add(okBt);
			buttons.add(saveBt);
			buttons.add(cancelBt);
			buttons.setBorder(new BevelBorder(BevelBorder.RAISED));

			mapPic = new JPanel();
			mapPic.setBounds(25, 265, 300, 150);
			// mapPic.setBackground(Color.yellow);

			lblBild = new JLabel(pic);

			mapPic.add(lblBild);
			grayedPic = new JPanel();
			grayedPic.setBounds(25, 270, 300, 145);
			grayedPic.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
			grayedPic.setVisible(true);

			borderForRectangle.setBounds(6, 200, 400, 320);
			borderForRectangle.add(new JLabel(""));
			final TitledBorder rectangle;
			rectangle = BorderFactory
					.createTitledBorder("Choose the Size of a Rectangle-Map");
			borderForRectangle.setBorder(rectangle);
			rectangle.setTitleColor(Color.lightGray);

			radioMapFillModus = new JPanel(new GridLayout(0, 1));
			radioMapFillModus.setBounds(430, 6, 400, 300);
			radioMapFillModus.setToolTipText("radioButtonMapModus");
			// radioMapFillModus.setBackground(Color.BLUE);
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

			randomAmountArea = new JPanel();
			randomAmountArea.setBounds(430, 440, 400, 80);
			randomAmountArea.setToolTipText("densityarea");
			randomAmountArea.add(randomAmountSlider);
			// densityArea.setBackground(Color.CYAN);
			final TitledBorder amount;
			amount = BorderFactory
					.createTitledBorder("Amount of Blocks in Modus 2");
			amount.setTitleColor(Color.lightGray);
			randomAmountArea.setBorder(amount);
			randomAmountSlider.setEnabled(false);

			probabilityArea = new JPanel();
			probabilityArea.setBounds(430, 340, 400, 80);
			probabilityArea.setToolTipText("probabilityArea");
			probabilityArea.add(probabilitySlider);
			// densityArea.setBackground(Color.CYAN);
			final TitledBorder probability;
			probability = BorderFactory
					.createTitledBorder("Probability of Modus 1");
			probability.setTitleColor(Color.lightGray);
			probabilityArea.setBorder(probability);
			probabilitySlider.setEnabled(false);

			add(grayedPic);
			add(densityArea);
			add(squareMapArea);
			add(radioButtonMapModus);
			add(mapWidht);
			add(mapHight);
			add(buttons);
			add(mapPic);
			add(borderForRectangle);
			add(radioMapFillModus);
			add(randomAmountArea);
			add(probabilityArea);

			densitySlider.setMinorTickSpacing(5);
			densitySlider.setMajorTickSpacing(25);
			densitySlider.setPaintTicks(true);
			densitySlider.setPaintLabels(true);

			mapSlider.setMinorTickSpacing(1);
			mapSlider.setMajorTickSpacing(5);
			mapSlider.setPaintTicks(true);
			mapSlider.setPaintLabels(true);
			mapSlider.setEnabled(true);

			horizontalSlider.setMinorTickSpacing(1);
			horizontalSlider.setMajorTickSpacing(5);
			horizontalSlider.setPaintTicks(true);
			horizontalSlider.setPaintLabels(true);
			horizontalSlider.setEnabled(false);

			verticalSlider.setMinorTickSpacing(1);
			verticalSlider.setMajorTickSpacing(5);
			verticalSlider.setPaintTicks(true);
			verticalSlider.setPaintLabels(true);
			verticalSlider.setEnabled(false);

			randomAmountSlider.setMinorTickSpacing(5);
			randomAmountSlider.setMajorTickSpacing(25);
			randomAmountSlider.setPaintTicks(true);
			randomAmountSlider.setPaintLabels(true);

			probabilitySlider.setMinorTickSpacing(5);
			probabilitySlider.setMajorTickSpacing(25);
			probabilitySlider.setPaintTicks(true);
			probabilitySlider.setPaintLabels(true);

			saveBt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					acceptOptions();

				}
			});

			cancelBt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					checkCancelWithoutSave();
				}

			});

			okBt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					acceptOptions();
					dispose();
				}

			});
			verticalSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedVerticalMap = ((JSlider) e.getSource()).getValue();
					System.out.println("Neuer Wert Spielfeldhöhe: "
							+ ((JSlider) e.getSource()).getValue());

					savedOptions = false;
					// updateVerticalMapLabel();
					// repaint();
				}
			});

			horizontalSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedHorizontalMap = ((JSlider) e.getSource()).getValue();

					System.out.println("Neuer Wert Spielfeldbreite: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;

				}
			});

			mapSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedMap = ((JSlider) e.getSource()).getValue();
					System.out.println("Neuer Wert Spielfeldgröße: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;

				}
			});
			densitySlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedDensity = ((JSlider) e.getSource()).getValue();
					System.out.println("Neuer Wert Blockdichte: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					// updateDensityLabel();
				}
			});

			probabilitySlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedProbability = ((JSlider) e.getSource()).getValue();
					System.out.println("Wahrscheinlichkeit Modus1: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					// updateDensityLabel();
				}
			});

			randomAmountSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedRandomAmount = ((JSlider) e.getSource()).getValue();
					System.out.println("Amount Modus 2:: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;

				}
			});

			squareButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					mapModus = true;
					rectangleButton.setSelected(false);
					System.out.println(mapModus + ": Square");

					verticalSlider.setEnabled(false);
					horizontalSlider.setEnabled(false);
					mapSlider.setEnabled(true);
					grayedPic.setVisible(true);
					square.setTitleColor(Color.BLUE);
					rectangle.setTitleColor(Color.lightGray);

				}

			});

			rectangleButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					mapModus = false;
					squareButton.setSelected(false);
					System.out.println(mapModus + ": Rectangle");
					verticalSlider.setEnabled(true);
					horizontalSlider.setEnabled(true);
					mapSlider.setEnabled(false);
					grayedPic.setVisible(false);
					mapPic.setVisible(true);
					square.setTitleColor(Color.lightGray);
					rectangle.setTitleColor(Color.BLUE);

				}

			});

			modusZero.addActionListener(new ActionListener() {

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

		private void acceptOptions() {

			gameOption.setGameMapOptions(changedMap);
			gameOption.setGameDensityOptions(changedDensity);
			gameOption.setMapModus(mapModus);
			gameOption.setGameMapHight(changedVerticalMap);
			gameOption.setGameMapWidht(changedHorizontalMap);
			gameOption.setFillModus(fillModus);
			gameOption.setProbability(changedProbability);
			gameOption.setRAmount(changedRandomAmount);
			savedOptions = true;

		}

		/**
		 * Sicherheitsabfrage ob man ohne die Änderungzuspeichern das Fenster
		 * schließen möchte
		 */

	}

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

	/**
	 * @param args
	 *            the command line arguments
	 */

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
		checkCancelWithoutSave();
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