public class player extends field {
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
		if (x == y/* tastendruck w=true */) {
			moveUP();
		} else if (x == y/* tastendruck s=true */) {
			moveDOWN();
		} else if (x == y/* tastendruck a=true */) {
			moveLEFT();
		} else if (x == y/* tastendruck d=true */) {
			moveRIGHT();

		}

	}

	public int moveUP() {
		int u;
		u = getPosition(1) + 1;
		y = u;
		return y;
	}

	public int moveDOWN() {
		int d;
		d = getPosition(1) - 1;
		y = d;
		return y;
	}

	public int moveLEFT() {
		int l;
		l = getPosition(0) - 1;
		x = l;
		return x;
	}

	public int moveRIGHT() {
		int r;
		r = getPosition(0) + 1;
		x = r;
		return x;
	}
}
