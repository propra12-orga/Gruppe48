package Options;

public class Options {
	private int map;
	private int dense;

	public Options() {
		setDense(50);
		setMap(20);

	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public int getDense() {
		return dense;
	}

	public void setDense(int dense) {
		this.dense = dense;
	}

}
