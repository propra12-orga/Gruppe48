package Objects;

import java.util.Timer;

public class Bomb {
	public boolean isExploded = false;
	public Timer bombe = new Timer();
	long delay = 3 * 1000;
	long explosionTime;
	int x;
	int y;
	int radius = 3;

	public Bomb(int xPos, int yPos, long time) {
		x = xPos;
		y = yPos;
		explosionTime = time + delay;
		BOOM();
	}

	public int[] getPosition() {
		int output[] = new int[2];
		output[0] = x;
		output[1] = y;
		return output;
	}

	public long getTimer() {
		return explosionTime;
	}

	public void BOOM() {
		System.out.println("Bombe wurde gesetzt");
	}

	public int getRadius() {
		return radius;
	}

	public void detonate() {
		explosionTime -= delay;
	}
	/*
	 * public int radius(int x) { // radius verstellbar für später aufgaben
	 * +1... // erhöhen
	 * 
	 * int a[] = new int[3]; a[0] = xKoordinate + 1; // rechts vom spieler
	 * radius(0) a[1] = xKoordinate - 1; // links vom spieler radius(1) a[2] =
	 * yKoordinate + 1; // über dem spieler radius(2) a[3] = yKoordinate - 1; //
	 * unter dem spieler radius(3)
	 * 
	 * return a[x]; }
	 */

}
