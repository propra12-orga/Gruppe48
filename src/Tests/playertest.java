package Tests;

import Objects.player;

//nur zu testzwecken wird nachher gel�scht
public class playertest {
	public static player a;

	public playertest() {

		a = new player();
		a.moveDOWN();
		System.out.println(a.getPosition()[0]);
		System.out.println(a.getPosition()[1]);
		a.moveUP();
		System.out
				.println("Positionen nachdem der Spieler nach oben bewegt wurde:");
		System.out.println(a.getPosition()[0]);
		System.out.println(a.getPosition()[1]);
		a.moveLEFT();
		System.out
				.println("Positionen nachdem der Spieler nach links bewegt wurde:");
		System.out.println(a.getPosition()[0]);
		System.out.println(a.getPosition()[1]);
	}

	public static void main(String[] args) {
		new playertest();
	}
}
