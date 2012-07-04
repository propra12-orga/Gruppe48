package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

import Engine.Game;
import Field.Field;
import Objects.Player;

public class Server extends Thread {

	ServerSocket server;
	Socket client1;
	Socket client2;
	NetworkInputStream input1;
	NetworkInputStream input2;
	NetworkInputStream input;
	PrintWriter output1;
	PrintWriter output2;
	ObjectOutputStream objectOutput1;
	ObjectOutputStream objectOutput2;
	ObjectInputStream objectInput;
	ObjectInputStream objectInput1;
	ObjectInputStream objectInput2;
	Calendar calendar;
	String event;
	Player player;
	Player player1;
	Player player2;
	long time;
	long timeout;
	Game game;
	Field gameField;
	int[] moveArray;

	public Server(Field field, Player player1, Player player2) {
		client1 = null;
		client2 = null;
		input = null;
		this.player1 = player1;
		this.player2 = player2;
		gameField = field;
		moveArray = new int[2];
		time = calendar.getInstance().getTimeInMillis();

	}

	public void run() {
		try {
			server = new ServerSocket(30000);
		} catch (IOException e) {
			System.out.println(e);
		}
		timeout = calendar.getInstance().getTimeInMillis();
		while (time < timeout + 60000) {

			try {
				client1 = server.accept();
				output1 = new PrintWriter(client1.getOutputStream());
				input1 = new NetworkInputStream(new Scanner(
						client1.getInputStream()));
				input1.start();
				objectOutput1 = new ObjectOutputStream(
						client1.getOutputStream());
				objectInput1 = new ObjectInputStream(client1.getInputStream());
			} catch (IOException e) {
				System.out.println(e);
			}
			if (client1 != null)
				break;
			time = calendar.getInstance().getTimeInMillis();
		}
		if (client1 == null) {
			System.out.println("null");
			return;
		}
		System.out.println("sending message");
		output1.println("waiting");
		output1.flush();
		System.out.println("message sent");
		timeout = calendar.getInstance().getTimeInMillis();

		while (time < timeout + 60000) {
			try {
				client2 = server.accept();
				output2 = new PrintWriter(client2.getOutputStream());
				input2 = new NetworkInputStream(new Scanner(
						client2.getInputStream()));
				input2.start();
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
		output1.flush();
		output2.flush();
		try {
			output1.println("map");
			output2.println("map");
			output1.flush();
			output2.flush();
			objectOutput1.writeObject(gameField);
			objectOutput2.writeObject(gameField);
			objectOutput1.flush();
			objectOutput2.flush();
			output1.println("player");
			output2.println("player");
			output1.flush();
			output2.flush();
			objectOutput1.writeObject(player1);
			objectOutput2.writeObject(player2);
			objectOutput1.flush();
			objectOutput2.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				System.out.println("run");
				if (((input1.nextEventAvailible()) || (input2
						.nextEventAvailible()))) {
					System.out.println("availble");
					if (input1.nextEventAvailible()) {
						input = input1;
						System.out.println(1);
					} else {
						input = input2;
						System.out.println(2);
					}
					if (input1.nextEventAvailible()) {
						objectInput = objectInput1;
						System.out.println(1);
					} else {
						objectInput = objectInput2;
						System.out.println(2);
					}
					if (input != null) {
						event = input.getNextEvent();
						System.out.println(event);
						if (event.equals("move")) {
							System.out.println("moving");
							try {
								player = (Player) objectInput1.readObject();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							System.out.println("player accepted");
							moveArray[0] = objectInput.readInt();
							moveArray[1] = objectInput.readInt();
							System.out.println("handling movement");
							System.out.println(player);
							System.out.println(moveArray[0]);
							System.out.println(moveArray[1]);
							handleMovement(player, moveArray);
							System.out.println("handled movement");
							output1.println("map");
							output2.println("map");
							output1.flush();
							output2.flush();
							System.out.println("wrote map");
							objectOutput1.writeObject(gameField);
							objectOutput2.writeObject(gameField);
							objectOutput1.flush();
							objectOutput2.flush();
							System.out.println("wrote object");
							if (input == input1) {
								output1.println("player");
								output1.flush();
								objectOutput1.writeObject(player);
								objectOutput1.flush();
							} else {
								output2.println("player");
								output2.flush();
								objectOutput2.writeObject(player);
								objectOutput2.flush();
							}

						} else {
							if (event.equals("win")) {
								output1.println("win");
								output2.println("win");
								if (input == input1) {
									output1.println("1");
									output2.println("1");
								} else {
									output1.println("2");
									output2.println("2");
								}
								output1.flush();
								output2.flush();
							} else {
								if (event.equals("stop")) {
									output1.println("stop");
									output2.println("stop");
									output1.flush();
									output2.flush();
								} else {
									if (event.equals("player")) {
										if (input == input1) {
											player1 = (Player) objectInput
													.readObject();
										} else {
											player2 = (Player) objectInput
													.readObject();
										}
									}
								}
							}
						}
					}

				}
			} catch (IOException e) {
				input1.drop();
				input2.drop();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("xyz");
			// Thread.sleep(20);
			objectInput = null;
			input = null;

		}
	}

	public InetAddress getIP() {
		return server.getInetAddress();
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

	public void initializePlayers(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
}
