package Options;

public class Options {
	private int dense;
	private int map;

	public Options() {
		setDense(30);
		setMap(16);
	}

	public int getDense() {
		return dense;
	}

	public void setDense(int dense) {
		this.dense = dense;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

}
