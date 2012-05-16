package Objects;

import java.util.Timer;

import Field.Field;

public class bomb {
	public int xKoordinate = player.x;
	public int yKoordinate = player.y;
	public static boolean isExploded = false;
	public static Timer bombe = new Timer();
	long delay = 3 * 1000;

	public bomb() {
		BOOM();
	}

	public void BOOM() {

		System.out.println("Bombe wurde gesetzt");
		bombe.schedule(new Task(), delay);

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
