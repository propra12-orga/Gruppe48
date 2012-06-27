package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

import Engine.Game;
import Field.Field;
import Objects.Player;

public class Server implements Runnable {

	ServerSocket server;
	Socket client1;
	Socket client2;
	Scanner input1;
	Scanner input2;
	PrintWriter output1;
	PrintWriter output2;
	ObjectOutputStream objectOutput1;
	ObjectOutputStream objectOutput2;
	Thread inputThread1;
	Thread inputThread2;
	Calendar calendar;
	long time;
	long timeout;
	Game game;
	Field gameField;

	public Server(Field field) {
		client1 = null;
		client2 = null;
		gameField = field;
		time = calendar.getInstance().getTimeInMillis();
		try {
			server = new ServerSocket(30000);
		} catch (IOException e) {
			System.out.println(e);
		}
		run();
	}

	public void run() {
		timeout = calendar.getInstance().getTimeInMillis();
		while (time < timeout + 60000) {

			try {
				client1 = server.accept();

				// inputThread1 =

				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							input1 = new Scanner(client1.getInputStream());
						} catch (IOException e) {
						}
					}
				}).start();

				output1 = new PrintWriter(client1.getOutputStream());
				objectOutput1 = new ObjectOutputStream(
						client1.getOutputStream());
				// objectInput1 = new
				// ObjectInputStream(client1.getInputStream());
			} catch (IOException e) {
				System.out.println(e);
			}
			if (client1 != null)
				break;
			time = calendar.getInstance().getTimeInMillis();
		}
		if (client1 == null)
			return;
		output1.println("waiting");
		timeout = calendar.getInstance().getTimeInMillis();
		while (time < timeout + 60000) {
			try {
				client2 = server.accept();
				input2 = new Scanner(client2.getInputStream());
				output2 = new PrintWriter(client2.getOutputStream());
				objectOutput2 = new ObjectOutputStream(
						client2.getOutputStream());
				objectInput2 = new ObjectInputStream(client2.getInputStream());
			} catch (IOException e) {
				System.out.println(e);
			}
			if (client2 != null)
				break;
			time = calendar.getInstance().getTimeInMillis();
		}
		if (client2 == null) {
			try {
				client1.close();
			} catch (IOException e) {
				System.out.println(e);
			}
			return;
		}
		output1.println("ready");
		output2.println("ready");
		output1.println("1");
		output2.println("2");
		while (true) {
			try {
				objectOutput1.writeObject(gameField);
				objectOutput2.writeObject(gameField);
				objectOutput1.flush();
				objectOutput2.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void handleMovement(Player player, int[] direction) {
		boolean moved = false;
		switch (gameField.getField(player.getPosition()[1] + direction[0],
				player.getPosition()[0] + direction[1]).getContent()) {
		case 1:
			moved = true;
			break;
		}
		if (moved) {
			gameField
					.getField(player.getPosition()[1], player.getPosition()[0])
					.removePlayer();
			switch (direction[0]) {
			case -1:
				player.moveLeft();
				break;
			case 1:
				player.moveRight();
				break;
			case 0:
				if (direction[1] == 1) {
					player.moveDown();
				} else {
					player.moveUp();
				}
				break;
			}
			gameField.getField(player.getPosition()[1] + direction[0],
					player.getPosition()[0] + direction[1])
					.insertPlayer(player);

		}
	}
}
