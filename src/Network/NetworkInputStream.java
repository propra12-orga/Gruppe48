package Network;

import java.util.Scanner;

public class NetworkInputStream extends Thread {

	Scanner input;
	boolean streamStatus;

	public NetworkInputStream(Scanner sStream) {
		input = sStream;
	}

	public void run() {

		while (true) {
		}
	}

	public String getNextEvent() {
		return input.nextLine();
	}

	public void drop() {
		while (input.hasNext())
			input.nextLine();
	}

	public boolean nextEventAvailible() {
		return input.hasNext();
	}
}
