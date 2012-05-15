import java.util.Timer;

public class bomb extends field {
	public int xKoordinate;
	public int yKoordinate;
	public boolean isExploded = false;
	public static Timer bombe = new Timer();
	long delay = 3 * 1000;

	//bomb s = new bomb();

	public bomb() {
		//createBomb();
		BOOM();
	}

	public void createBomb() { // erzeugt eine Bombe abhängig von der Spielerposition

		getPlayerPosition();

		//	setBomb(s, xKoordinate, yKoordinate);
	}

	public void getPlayerPosition() { // greift auf die Spielerposition in player.java

		xKoordinate = player.x;
		yKoordinate = player.y;

	}

	public void BOOM() {
		// timer schreiben
		System.out.println("HALLO");
		bombe.schedule(new Task(), delay);

	}

	public int radius(int x) { // radius verstellbar für später aufgaben +1... erhöhen

		int a[] = new int[3];
		a[0] = xKoordinate + 1; // rechts vom spieler radius(0)
		a[1] = xKoordinate - 1; // links vom spieler radius(1)
		a[2] = yKoordinate + 1; // über dem spieler radius(2)
		a[3] = yKoordinate - 1; // unter dem spieler radius(3)

		return a[x];
	}

}
