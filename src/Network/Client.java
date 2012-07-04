package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Field.Field;
import Objects.Player;

public class Client extends Thread {
	Socket socket;
	NetworkInputStream input;

	ObjectOutputStream output;
	String ip;
	int port;
	int clientNumber;
	Field localField;
	Player localPlayer;
	String event;
	boolean newEvent;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
		clientNumber = 0;
		newEvent = false;

	}

	public void run() {
		try {
			socket = new Socket(ip, port);
			output = new ObjectOutputStream(socket.getOutputStream());
			output.writeInt(1);
			output.flush();
			input = new NetworkInputStream(new ObjectInputStream(
					socket.getInputStream()));
			input.start();
			input.getNextInt();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				if (input.nextEventAvailible()) {

					event = input.getNextEvent();
					if (event.equals("waiting")) {
					}
					if (event.equals("ready")) {
						clientNumber = Integer.parseInt(input.getNextEvent());
					}
					if (event.equals("map")) {
						localField = (Field) input.getNextObject();
						System.out.println("map update");
						System.out.println("Client 2, 1 = "
								+ localField.getField(1, 2).getContent());
						newEvent = true;
					}
					if (event.equals("player")) {
						localPlayer = (Player) input.getNextObject();

						System.out.println("Client Player = "
								+ localPlayer.getPosition()[0] + " "
								+ localPlayer.getPosition()[1]);
					}
					if (event.equals("initialized")) {
						output.writeUTF("ok");
						output.flush();
					}

				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean getEvent() {
		return newEvent;
	}

	public void resetEvent() {
		newEvent = false;
	}

	public void movePlayer(int x, int y) {
		try {
			output.writeUTF("move");
			output.flush();
			output.writeObject(localPlayer);
			output.writeInt(x);
			output.writeInt(y);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPlayer(Player newPlayer) {
		localPlayer = newPlayer;
	}

	public Player getPlayer() {
		return localPlayer;
	}

	public Field getField() {
		return localField;
	}

}
