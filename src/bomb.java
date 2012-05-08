public class bomb extends player {
	public int xKoordinate;
	public int yKoordinate;

	public bomb() {
		createBomb();
		BOOM();
	}

	public void createBomb() { // erzeugt eine Bombe abhängig von der
								// Spielerposition
		getPlayerPosition();
		bomb s = new bomb();
		setBomb(s, xKoordinate, yKoordinate);
	}

	public void getPlayerPosition() { // greift auf die Spielerposition im
										// player.java
		xKoordinate = player.x;
		yKoordinate = player.y;

	}

	public void BOOM() {
		// timer schreiben

	}

	public int radius(int x) { // radius verstellbar für später aufgaben +1...
								// erhöhen
		int a[] = new int[3];
		a[0] = xKoordinate + 1; // rechts vom spieler radius(0)
		a[1] = xKoordinate - 1; // links vom spieler radius(1)
		a[2] = yKoordinate + 1; // über dem spieler radius(2)
		a[3] = yKoordinate - 1; // unter dem spieler radius(3)

		return a[x];
	}

}
