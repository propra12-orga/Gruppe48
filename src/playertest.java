//nur zu testzwecken wird nachher gelöscht
public class playertest {
	player a;

	public playertest() {
		a = new player();
		System.out.println(a.getPosition()[0]);
		System.out.println(a.getPosition()[1]);
		a.moveUP();
		System.out.println(a.getPosition()[0]);
		System.out.println(a.getPosition()[1]);
		a.moveLEFT();
		System.out.println(a.getPosition()[0]);
		System.out.println(a.getPosition()[1]);
	}

	public static void main(String[] args) {
		new playertest();
	}
}
