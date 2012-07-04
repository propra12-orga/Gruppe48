package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Engine.Game;
import Field.Field;
import Objects.Bomb;
import Objects.Player;

/**
 * 
 * @author Alexander Serverklasse, welche Funktionen zum hosten eines spiels
 *         bereitstellt
 */
public class Server extends Thread {

	ServerSocket server;
	Socket client1;
	Socket client2;
	NetworkInputStream input1;
	NetworkInputStream input2;
	NetworkInputStream input;
	ObjectOutputStream output1;
	ObjectOutputStream output2;
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
	List<Bomb> bombList;

	/**
	 * Erzeugt ein Objekt vom Typ Server und übergibt diesem ein Spielfeld sowie
	 * beide Spieler
	 * 
	 * @param field
	 *            Spielfeld auf dem gespielt werden soll
	 * @param player1
	 *            Erster Spieler
	 * @param player2
	 *            Zweiter Spieler
	 */
	public Server(Field field, Player player1, Player player2) {
		client1 = null;
		client2 = null;
		input = null;
		bombList = new ArrayList<Bomb>();
		this.player1 = player1;
		this.player2 = player2;
		gameField = field;
		moveArray = new int[2];
		time = calendar.getInstance().getTimeInMillis();

	}

	/**
	 * Hauptmethode des Servers. Initialisiert die Verbindun und regelt die
	 * gesamte Spielmechanik
	 */
	public void run() {
		try {
			server = new ServerSocket(30000);

			timeout = calendar.getInstance().getTimeInMillis();
			while (time < timeout + 60000) {

				try {
					client1 = server.accept();
					output1 = new ObjectOutputStream(client1.getOutputStream());
					output1.writeInt(1);
					output1.flush();
					input1 = new NetworkInputStream(new ObjectInputStream(
							client1.getInputStream()));
					input1.start();
					input1.getNextInt();
				} catch (IOException e) {
					System.out.println(e);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (client1 != null)
					break;
				time = calendar.getInstance().getTimeInMillis();
			}
			if (client1 == null) {
				System.out.println("null");
				return;
			}
			output1.writeUTF("waiting");
			output1.flush();
			timeout = calendar.getInstance().getTimeInMillis();

			while (time < timeout + 60000) {
				try {
					client2 = server.accept();
					output2 = new ObjectOutputStream(client2.getOutputStream());
					output2.writeInt(2);
					output2.flush();
					input2 = new NetworkInputStream(new ObjectInputStream(
							client2.getInputStream()));
					input2.start();
					input2.getNextInt();
				} catch (IOException e) {
					System.out.println(e);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			output1.writeUTF("ready");
			output2.writeUTF("ready");
			output1.writeUTF("1");
			output2.writeUTF("2");
			output1.flush();
			output2.flush();
			output1.writeUTF("map");
			output2.writeUTF("map");
			output1.flush();
			output2.flush();
			output1.writeObject(gameField);
			output2.writeObject(gameField);
			output1.flush();
			output2.flush();
			output1.writeUTF("player");
			output2.writeUTF("player");
			output1.flush();
			output2.flush();
			output1.writeObject(player1);
			output2.writeObject(player2);
			output1.flush();
			output2.flush();
			output1.writeUTF("initialized");
			output2.writeUTF("initialized");
			output1.flush();
			output2.flush();
			while (!input1.nextEventAvailible()) {

			}
			while (!input2.nextEventAvailible()) {

			}
			input1.getNextEvent();
			input2.getNextEvent();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (true) {
			try {
				time = calendar.getInstance().getTimeInMillis();
				System.out.println("run");
				if (((input1.nextEventAvailible()) || (input2
						.nextEventAvailible()))) {
					if (input1.nextEventAvailible()) {
						input = input1;
						System.out.println(1);
					} else {
						input = input2;
						System.out.println(2);
					}
					if (input != null) {
						event = input.getNextEvent();
						System.out.println(event);
						if (event.equals("move")) {
							System.out.println("moving");
							try {
								player = (Player) input.getNextObject();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							moveArray[0] = input.getNextInt();
							moveArray[1] = input.getNextInt();
							handleMovement(player, moveArray);
							output1.writeUTF("map");
							output2.writeUTF("map");
							output1.flush();
							output2.flush();
							System.out.println("Server 2, 1 = "
									+ gameField.getField(1, 2).getPlayer());
							System.out.println("Server Player = "
									+ player.getPosition()[0] + " "
									+ player.getPosition()[1]);
							output1.writeObject(gameField);
							output2.writeObject(gameField);
							output1.flush();
							output2.flush();
							if (input == input1) {
								output1.writeUTF("player");
								output1.flush();
								output1.writeObject(player);
								output1.flush();
							} else {
								output2.writeUTF("player");
								output2.flush();
								output2.writeObject(player);
								output2.flush();
							}

						} else {
							if (event.equals("win")) {
								output1.writeUTF("win");
								output2.writeUTF("win");
								if (input == input1) {
									output1.writeUTF("1");
									output2.writeUTF("1");
								} else {
									output1.writeUTF("2");
									output2.writeUTF("2");
								}
								output1.flush();
								output2.flush();
							} else {
								if (event.equals("stop")) {
									output1.writeUTF("stop");
									output2.writeUTF("stop");
									output1.flush();
									output2.flush();
								} else {
									if (event.equals("player")) {
										if (input == input1) {
											player1 = (Player) input
													.getNextObject();
										} else {
											player2 = (Player) input
													.getNextObject();
										}
									} else {
										if (event.equals("bomb")) {
											setBomb(input.getNextInt());
										}
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			input = null;
			input = null;

		}
	}

	private void setBomb(int playernumber) {
		if (playernumber == 1) {
			player = player1;
		} else {
			player = player2;
		}
		gameField.setBomb(new Bomb(player.getPosition()[0], player
				.getPosition()[1], time, player.getBombRadius()));
		bombList.add(gameField.getField(player.getPosition()[0],
				player.getPosition()[1]).getBomb());
	}

	private void handleBombs() {
		for (int i = 0; i < bombList.size(); i++) {
			if (bombList.get(i).getTimer() <= time) {
				bombList.remove(i);
			}
		}
	}

	private void handleMovement(Player player, int[] direction) {
		boolean moved = false;

		switch (gameField.getField(player.getPosition()[1] + direction[0],
				player.getPosition()[0] + direction[1]).getContent()) {
		case 1:
			if (gameField.getField(player.getPosition()[1] + direction[0],
					player.getPosition()[0] + direction[1]).getPlayer() != null) {
				break;
			}
			if (gameField.getField(player.getPosition()[1] + direction[0],
					player.getPosition()[0] + direction[1]).isExit()) {
				break;
			}
			if (gameField.getField(player.getPosition()[1] + direction[0],
					player.getPosition()[0] + direction[1]).getBomb() != null) {
				break;
			}
			if (gameField.getField(player.getPosition()[1] + direction[0],
					player.getPosition()[0] + direction[1]).isFireItem()) {
				player.setBombRadius();
				gameField.getField(player.getPosition()[1] + direction[0],
						player.getPosition()[0] + direction[1])
						.removeFireItem();
				moved = true;
				break;
			}
			if (gameField.getField(player.getPosition()[1] + direction[0],
					player.getPosition()[0] + direction[1]).isBombItem()) {
				if (player.getID() == 1) {
					Bomb.setBombMax();
				} else {
					Bomb.setBombMaxP2();
				}
				gameField.getField(player.getPosition()[1] + direction[0],
						player.getPosition()[0] + direction[1])
						.removeBombItem();
				moved = true;
				break;
			}
			moved = true;
			break;
		}
		if (moved) {
			switch (direction[0]) {
			case -1:
				gameField.removePlayer(player);
				player.moveLeft();
				gameField.setPlayer(player);
				break;
			case 1:
				gameField.removePlayer(player);
				player.moveRight();
				gameField.setPlayer(player);
				break;
			case 0:
				if (direction[1] == 1) {
					gameField.removePlayer(player);
					player.moveDown();
					gameField.setPlayer(player);
				} else {
					gameField.removePlayer(player);
					player.moveUp();
					gameField.setPlayer(player);
				}
				break;
			}
		}
	}
}
