import javax.swing.JFrame;
import javax.swing.JTextField;

//nur zu testzwecken wird nachher gelöscht
public class playertest {
	public static PlayerButtons a;

	public playertest() {

		a = new PlayerButtons();
		a.move();
		
		
//		System.out.println(a.getPosition()[0]);
//		System.out.println(a.getPosition()[1]);
//		a.moveUP();
//		System.out.println("Positionen nachdem der Spieler nach oben bewegt wurde:");
//		System.out.println(a.getPosition()[0]);
//		System.out.println(a.getPosition()[1]);
//		a.moveLEFT();
//		System.out.println("Positionen nachdem der Spieler nach links bewegt wurde:");
//		System.out.println(a.getPosition()[0]);
//		System.out.println(a.getPosition()[1]);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Popup JComboBox");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JTextField textField = new JTextField();
	    textField.addKeyListener(a);
	    

	    frame.add(textField);
	   frame.setSize(300, 200);
	   frame.setVisible(true);
		new playertest();
	}
}
