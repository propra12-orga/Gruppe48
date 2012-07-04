package Network;

import java.io.IOException;
import java.io.ObjectInputStream;

public class NetworkInputStream extends Thread {

	ObjectInputStream input;
	boolean streamStatus;

	public NetworkInputStream(ObjectInputStream oStream) {
		input = oStream;
	}

	public void run() {

		while (true) {
		}
	}

	public String getNextEvent() {
		try {
			return input.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public Object getNextObject() throws ClassNotFoundException, IOException {
		return input.readObject();
	}

	public int getNextInt() throws ClassNotFoundException, IOException {
		return input.readInt();
	}

	public boolean nextEventAvailible() {
		try {
			if (input.available() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
