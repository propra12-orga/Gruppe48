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
import Field.FieldContent;
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
	int iItemChance = 30;
	List<long[]> explosionList;

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

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (client1 != null)
					break;
				time = calendar.getInstance().getTimeInMillis();
			}
			if (client1 == null) {
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

				} catch (ClassNotFoundException e) {
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
			output1.writeObject(gameField);
			output2.writeObject(gameField);
			output1.flush();
			output2.flush();
			output1.writeUTF("initialized");
			output2.writeUTF("initialized");
			output1.flush();
			output2.flush();
			output1.reset();
			output2.reset();
			while (!input1.nextEventAvailible()) {

			}
			while (!input2.nextEventAvailible()) {

			}
			input1.getNextEvent();
			input2.getNextEvent();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		explosionList = new ArrayList<long[]>();
		while (true) {
			try {
				time = calendar.getInstance().getTimeInMillis();
				handleBombs();
				if (((input1.nextEventAvailible()) || (input2
						.nextEventAvailible()))) {
					if (input1.nextEventAvailible()) {
						input = input1;
					} else {
						input = input2;
					}
					if (input != null) {
						event = input.getNextEvent();
						if (event.equals("move")) {
							try {
								player = (Player) input.getNextObject();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							moveArray[0] = input.getNextInt();
							moveArray[1] = input.getNextInt();
							if (input == input1) {
								player = player1;
							} else {
								player = player2;
							}
							handleMovement(player, moveArray);
							output1.writeUTF("map");
							output2.writeUTF("map");
							output1.writeObject(gameField);
							output2.writeObject(gameField);
							output1.flush();
							output2.flush();
							output1.reset();
							output2.reset();
							if (input == input1) {
								player1 = player.clonePlayer();
							} else {
								player2 = player.clonePlayer();
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
								output1.reset();
								output2.reset();
							} else {
								if (event.equals("stop")) {
									output1.writeUTF("stop");
									output2.writeUTF("stop");
									output1.flush();
									output2.flush();
									output1.reset();
									output2.reset();
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
											System.out.println("bomb");
											setBomb(input.getNextInt());
										} else {
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
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			input = null;
		}
	}

	private void setBomb(int playernumber) {
		if (playernumber == 1) {
			player = player1;
			if (Bomb.getBombStatus() == false) {
				bombList.add(new Bomb(player.getPosition()[0], player
						.getPosition()[1], time, player.getBombRadius()));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				Bomb.setCurrentPlacedBomb();
				try {
					output1.writeUTF("map");
					output2.writeUTF("map");
					output1.writeObject(gameField);
					output2.writeObject(gameField);
					output1.flush();
					output2.flush();
					output1.reset();
					output2.reset();
				} catch (IOException e) {

				}
			}
			Bomb.setBombStatus();

		} else {
			player = player2;
			if (Bomb.getBombStatusP2() == false) {
				Bomb.setCurrentPlacedBombP2();
				bombList.add(new Bomb(player.getPosition()[0], player
						.getPosition()[1], time, player.getBombRadius()));
				gameField.setBomb(bombList.get(bombList.size() - 1));
				try {
					output1.writeUTF("map");
					output2.writeUTF("map");
					output1.writeObject(gameField);
					output2.writeObject(gameField);
					output1.flush();
					output2.flush();
					output1.reset();
					output2.reset();
				} catch (IOException e) {

				}
			}
			Bomb.setBombStatusP2();

		}

	}

	private void handleBombs() {
		ArrayList<int[]> exList;
		FieldContent bombField = new FieldContent();
		int x = 0;
		int y = 0;
		for (int i = 0; i < bombList.size(); i++) {
			if (bombList.get(i).getTimer() <= time) { // Eine Liste aller Bomben
														// deren Timer
														// abgelaufen ist wird
														// generiert
				try {
					exList = new ArrayList<int[]>();
					exList.add(new int[2]);
					exList.get(exList.size() - 1)[0] = bombList.get(i)
							.getPosition()[1];
					exList.get(exList.size() - 1)[1] = bombList.get(i)
							.getPosition()[0];
					for (int z = 0; z < 4; z++) {
						for (int j = 1; j < bombList.get(i).getRadius(); j++) {
							switch (z) {
							case 0:
								x = bombList.get(i).getPosition()[1];
								y = bombList.get(i).getPosition()[0] - j;
								break;
							case 1:
								x = bombList.get(i).getPosition()[1];
								y = bombList.get(i).getPosition()[0] + j;
								break;
							case 2:
								x = bombList.get(i).getPosition()[1] - j;
								y = bombList.get(i).getPosition()[0];
								break;
							case 3:
								x = bombList.get(i).getPosition()[1] + j;
								y = bombList.get(i).getPosition()[0];
								break;
							}
							bombField = gameField.getField(x, y);
							if (bombField.getPlayer() != null) {
								// Überprueft Felder oberhalb der Bombe
								output1.writeUTF("gameover");
								output2.writeUTF("gameover");
								output1.writeInt(bombField.getPlayer().getID());
								output2.writeInt(bombField.getPlayer().getID());
								output1.flush();
								output2.flush();
								output1.reset();
								output2.reset();
							}
							if (bombField.getBomb() != null) {
								int tmp[] = new int[2];
								for (int k = 0; k < bombList.size(); k++) {
									if ((bombList.get(k).getPosition()[0] == y)
											&& (bombList.get(k).getPosition()[1] == x)) {
										bombList.get(k).detonate();
										i = 0;
									}
								}

							}
							try {
								if (bombField.getContent() == 1) {
									exList.add(new int[2]);
									exList.get(exList.size() - 1)[0] = bombList
											.get(i).getPosition()[1];
									exList.get(exList.size() - 1)[1] = bombList
											.get(i).getPosition()[0] - j;
								} else {
									if (bombField.getContent() == 6) {
										exList.add(new int[2]);
										exList.get(exList.size() - 1)[0] = bombList
												.get(i).getPosition()[1];
										exList.get(exList.size() - 1)[1] = bombList
												.get(i).getPosition()[0] - j;
										bombField.setContent(1);
										createRandomItem(x, y, iItemChance);
									}

									break;

								}
							} catch (Exception e) {
							}
						}
					}
					explosionList.add(new long[1]);
					explosionList.get(0)[0] = Calendar.getInstance()
							.getTimeInMillis() + 500;
					exList = null;
					gameField.removeBomb(bombList.get(i));
					bombList.remove(i);
					/*
					 * output1.writeUTF("exList"); output2.writeUTF("exList");
					 * output1.writeObject(exList); output2.writeObject(exList);
					 * output1.flush(); output2.flush(); output1.reset();
					 * output2.reset();
					 */
					output1.writeUTF("map");
					output2.writeUTF("map");
					output1.writeObject(gameField);
					output2.writeObject(gameField);
					output1.flush();
					output2.flush();
					output1.reset();
					output2.reset();
				} catch (IOException e) {

				}
			}
		}
		for (int i = 0; i < explosionList.size(); i++) {

			// abgelaufene explosionen werden entfernt
			if (explosionList.get(i)[0] < (Calendar.getInstance()
					.getTimeInMillis())) {
				try {
					output1.writeUTF("removeExplosion");
					output2.writeUTF("removeExplosion");
					output1.flush();
					output2.flush();
					output1.reset();
					output2.reset();
					explosionList.remove(0);
				} catch (IOException e) {

				}
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

	private void createRandomItem(int pos1, int pos2, int chance) {
		if ((Math.random() * 100) > chance) {
			return;
		}
		if ((Math.random()) < 0.5) {
			gameField.getField(pos1, pos2).setFireItem();
		} else {
			gameField.getField(pos1, pos2).setBombItem();
		}
	}
}
