package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Field.Field;
import Objects.Player;

public class Client extends Thread {
	Socket socket;
	NetworkInputStream input;
	PrintWriter output;
	ObjectOutputStream objectOutput;
	ObjectInputStream objectInput;
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
			output = new PrintWriter(socket.getOutputStream());
			input = new NetworkInputStream(new Scanner(socket.getInputStream()));
			input.start();
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.flush();
			objectInput = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				if (input.nextEventAvailible()) {

					event = input.getNextEvent();
					System.out
							.println("client " + clientNumber + " = " + event);
					if (event.equals("waiting")) {
					}
					if (event.equals("ready")) {
						clientNumber = Integer.parseInt(input.getNextEvent());
					}
					if (event.equals("map")) {
						localField = (Field) objectInput.readObject();
					}
					if (event.equals("player")) {
						localPlayer = (Player) objectInput.readObject();
					}
					if (event.equals("initialized")) {
						output.println("ok");
						output.flush();
					}
					newEvent = true;
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

		System.out.println("taste gedrückt");
		try {
			output.println("move");
			output.flush();
			objectOutput.writeObject(localPlayer);
			objectOutput.writeInt(x);
			objectOutput.writeInt(y);
			objectOutput.flush();
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

	public void setField(Field newField) {
		localField = newField;
	}

}
