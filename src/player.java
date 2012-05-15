public class player {
	public static int x, y;
	//public static float a,b;
	public boolean stillAlive = true;

	public player() {
		x = field.x;
		y = field.y;
	}

	public int[] getPosition() {
		int[] getPosition = new int[2];
		getPosition[0] = x;
		getPosition[1] = y;
		return getPosition;
	}

	/*public float[] getPositionfloat() {                Für eine flüssige Bewegung anstatt int mit float bewegen
		float[] getPosition = new float[2];
		getPosition[0] = x;
		getPosition[1] = y;
		return getPosition;
	}
	public float moveUPf(){
		float u;
		u = getPositionfloat()[1] + 0.1f;
		b = u;
		return b;
		
	}
	public float moveDOWNf(){
		float d;
		d = getPositionfloat()[1] - 0.1f;
		b = d;
		return b;
		
	}
	public float moveLEFTf(){
		float l;
		l = getPositionfloat()[0] - 0.1f;
		a = l;
		return a;
		
	}
	public float moveRIGHTf(){
		float r;
		r = getPositionfloat()[0] + 0.1f;
		a = r;
		return a;
		
	}*/

	public void move() {
		if (x == y/* tastendruck w=true&& wand im weg? */) {
			moveUP();
		} else if (x == y/* tastendruck s=true && wand im weg? */) {
			moveDOWN();
		} else if (x == y/* tastendruck a=true && wand im weg? */) {
			moveLEFT();
		} else if (x == y/* tastendruck d=true && wand im weg? */) {
			moveRIGHT();

		}

	}

	public int moveUP() {
		int u;
		u = getPosition()[1] + 1;
		y = u;
		return y;
	}

	public int moveDOWN() {
		int d;
		d = getPosition()[1] - 1;
		y = d;
		return y;
	}

	public int moveLEFT() {
		int l;
		l = getPosition()[0] - 1;
		x = l;
		return x;
	}

	public int moveRIGHT() {
		int r;
		r = getPosition()[0] + 1;
		x = r;
		return x;
	}
}
