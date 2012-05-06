public class player {
	public static int x, y;

	public player() {

	}

	public int getPosition(int z) {
		int[] a = new int[2];
		x = field.x;
		y = field.y;
		a[0] = x;
		a[1] = y;
		return a[z];
	}

	public void move() {

	}
}
