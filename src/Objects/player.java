package Objects;


public class Player {
	public int x, y;
	// public static float a,b;
	public boolean stillAlive = true;

	public Player(int iXPos, int iYPos) {
		x = iXPos;
		y = iYPos;
	}

	public int[] getPosition() {
		int[] getPosition = new int[2];
		getPosition[0] = x;
		getPosition[1] = y;
		return getPosition;
	}

	public void setPosition(int iXPos, int iYPos) {
		x = iXPos;
		y = iYPos;
	}

	public void moveUp() {
		x -= 1;
	}

	public void moveDown() {
		x += 1;
	}

	public void moveLeft() {
		y -= 1;
	}

	public void moveRight() {
		y += 1;
	}

	/*
	 * public float[] getPositionfloat() { Für eine flüssige Bewegung anstatt
	 * int mit float bewegen float[] getPosition = new float[2]; getPosition[0]
	 * = x; getPosition[1] = y; return getPosition; } public float moveUPf(){
	 * float u; u = getPositionfloat()[1] + 0.1f; b = u; return b;
	 * 
	 * } public float moveDOWNf(){ float d; d = getPositionfloat()[1] - 0.1f; b
	 * = d; return b;
	 * 
	 * } public float moveLEFTf(){ float l; l = getPositionfloat()[0] - 0.1f; a
	 * = l; return a;
	 * 
	 * } public float moveRIGHTf(){ float r; r = getPositionfloat()[0] + 0.1f; a
	 * = r; return a;
	 * 
	 * }
	 */
}
