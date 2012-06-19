package Options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

	private static Game game1;

	private int changedMap;
	private double changedDensity;
	private int changedVerticalMap;
	private int changedHorizontalMap;
	private boolean savedOptions = true;
	public boolean mapModus = true;

	public Options(Game game) {
		super("BomberMan-Options");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(430, 750);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width / 2 - (getWidth() / 2);
		int y = dim.height / 2 - (getHeight() / 2);
		setLocation(x, y);
		setResizable(true);
		addWindowListener(this);
		add(new MiddlePanel(0, 0, 400, 150));

		this.game1 = game;
		changedMap = game1.startMapSize;
		changedDensity = game1.startDensity;
		changedVerticalMap = game1.rectangleMapHight;
		changedHorizontalMap = game1.rectangleMapWidht;
	}

	public class MiddlePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		// private JPanel borderDummy;
		private JPanel densityArea;
		private JPanel squareMapArea;
		// private JPanel borderDummy;
		JPanel radioButtonMapModus = new JPanel(new GridLayout(0, 1));
		private JPanel mapWidht;
		private JPanel mapHight;
		private JPanel buttons;
		private JPanel mapPic;
		private JLabel lblBild;
		private ImageIcon pic = new ImageIcon(
				ClassLoader.getSystemResource("images/MapOptionIcon.jpg"));
		private JPanel borderDummy;
		JPanel textForRectangle = new JPanel(new GridLayout(0, 1));

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
			final JButton saveBt = new JButton("Save");
			JButton okBt = new JButton("OK");
			JButton cancelBt = new JButton("Cancel");
			JSlider densitySlider = new JSlider(0, 100, 70);
			final JSlider mapSlider = new JSlider(10, 25, 15);
			densityArea = new JPanel();
			densityArea.setBounds(6, 550, 400, 80);
			densityArea.setToolTipText("densityarea");
			densityArea.add(densitySlider);
			// densityArea.setBackground(Color.CYAN);
			TitledBorder density;
			density = BorderFactory.createTitledBorder("Density of Walls");

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

			radioButtonMapModus = new JPanel(new GridLayout(1, 0));
			radioButtonMapModus.setBounds(6, 6, 400, 50);
			radioButtonMapModus.setToolTipText("radioButtonMapModus");
			// radioButtonMapModus.setBackground(Color.BLUE);
			radioButtonMapModus.add(squareButton);
			radioButtonMapModus.add(rectangleButton);
			radioButtonMapModus.setBorder(new BevelBorder(BevelBorder.RAISED));

			final JSlider horizontalSlider = new JSlider(5, 40, 20);
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
			buttons = new JPanel(new FlowLayout());
			buttons.setBounds(6, 650, 400, 45);
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
			borderDummy = new JPanel();
			borderDummy.setBounds(25, 270, 300, 145);
			borderDummy.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));
			borderDummy.setVisible(true);
			// borderDummy.setOpaque(true);
			textForRectangle.setBounds(6, 200, 400, 320);
			textForRectangle.add(new JLabel(""));
			final TitledBorder rectangle;
			rectangle = BorderFactory
					.createTitledBorder("Choose the Size of a Rectangle-Map");
			textForRectangle.setBorder(rectangle);
			rectangle.setTitleColor(Color.lightGray);

			add(borderDummy);
			add(densityArea);
			add(squareMapArea);
			add(radioButtonMapModus);
			add(mapWidht);
			add(mapHight);
			add(buttons);
			add(mapPic);
			add(textForRectangle);

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
				}
			});

			horizontalSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedHorizontalMap = ((JSlider) e.getSource()).getValue();

					System.out.println("Neuer Wert Spielfeldbreite: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					updateMapLabel();
				}
			});

			mapSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					changedMap = ((JSlider) e.getSource()).getValue();
					System.out.println("Neuer Wert Spielfeldgröße: "
							+ ((JSlider) e.getSource()).getValue());
					savedOptions = false;
					// updateMapLabel();
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

			squareButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					mapModus = true;
					rectangleButton.setSelected(false);
					System.out.println(mapModus + ": Square");

					verticalSlider.setEnabled(false);
					horizontalSlider.setEnabled(false);
					mapSlider.setEnabled(true);
					borderDummy.setVisible(true);
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
					borderDummy.setVisible(false);
					mapPic.setVisible(true);
					square.setTitleColor(Color.lightGray);
					rectangle.setTitleColor(Color.BLUE);

				}

			});
		}

		private void acceptOptions() {

			game1.setGameMapOptions(changedMap);
			game1.setGameDensityOptions(changedDensity);
			game1.setMapModus(mapModus);
			game1.setGameMapHight(changedVerticalMap);
			game1.setGameMapWidht(changedHorizontalMap);
			savedOptions = true;

		}

		/**
		 * Sicherheitsabfrage ob man ohne die Änderungzuspeichern das Fenster
		 * schließen möchte
		 */

	}

	private void updateMapLabel() {
		repaint();
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