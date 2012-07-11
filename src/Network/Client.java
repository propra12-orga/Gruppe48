package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import Field.Field;
import Objects.Player;

/**
 * 
 * @author Alexander Client Klasse, welche Funktionen zur Kommunikation mit
 *         einem Gameserver zu Verfügung stellt
 */
public class Client extends Thread {
	Socket socket;
	NetworkInputStream input;

	ObjectOutputStream output;
	String ip;
	int port;
	int clientNumber;
	boolean won;;
	Field localField;
	Player localPlayer;
	String event;
	int eventCount;
	String eventType;
	ArrayList<int[]> exList;
	boolean running;
	long time;
	long lastping;

	/**
	 * Erzeugt ein Objekt der Klasse Client
	 * 
	 * @param ip
	 *            IP-Adresse des Servers zu dem Verbindung aufgenommen werden
	 *            soll
	 * @param port
	 *            Portnummer des Servers zu dem Verbindung aufgenommen werden
	 *            soll
	 */
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
		clientNumber = 0;
		eventCount = 0;
		eventType = " ";
		running = true;
		won = false;
		time = Calendar.getInstance().getTimeInMillis();
		lastping = time;
	}

	/**
	 * Hauptmethode des Clients, die sämtliche Anfragen des Servers regelst
	 * sowie dem Server Tastendrücke mitteilt
	 */
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
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		while (running) {
			try {
				time = Calendar.getInstance().getTimeInMillis();
				if (input == null) {
					eventType = "notConnected";
					eventCount++;
				} else {
					if (input.nextEventAvailible()) {

						event = input.getNextEvent();
						if (event.equals("waiting")) {
						}
						if (event.equals("timeout")) {
							eventType = "timeout";
							eventCount++;
						}
						if (event.equals("ping")) {

						}
						if (event.equals("ready")) {
							clientNumber = Integer.parseInt(input
									.getNextEvent());
						}
						if (event.equals("map")) {
							eventType = "map";
							try {
								localField = (Field) input.getNextObject();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							eventCount++;

						}
						if (event.equals("initialized")) {
							output.writeUTF("ok");
							output.flush();
							output.reset();
							eventType = "initialized";
							eventCount++;
						}
						if (event.equals("exList")) {
							eventType = "exList";
							try {
								exList = (ArrayList) input.getNextObject();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							eventCount++;

						}
						if (event.equals("removeExplosion")) {
							eventType = "removeExplosion";
							eventCount++;
						}
						if (event.equals("pickup")) {
							eventType = "pickup";
							eventCount++;
						}

						if (event.equals("eoc")) {
							eventType = "eoc";
							eventCount++;
						}
						if (event.equals("gameover")) {
							try {
								if (input.getNextInt() == clientNumber) {
									won = true;
								}
							} catch (ClassNotFoundException e) {
							}
							eventType = "gameover";
							eventCount++;
						}
						if (event.equals("accepted")) {
							output.reset();
						}
					}
					if ((lastping + 10000) < time) {
						output.writeUTF("ping");
						output.flush();
						lastping = time;
					}
				}
			} catch (IOException e) {
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gibt zurück, ob eine neue Nachricht vom server eingetroffen ist
	 * 
	 * @return Ist = true, falls neue Nachricht vorhanden ist, ist sonst = false
	 */
	public int getEventCount() {
		return eventCount;
	}

	/**
	 * Methode zum zurücksetzen der Nachrichtmarkierung
	 */
	public void resetEvent() {
		eventCount--;
	}

	/**
	 * Gibt die Art des vom Server erhaltenen Ereignisses zurück
	 * 
	 * @return Vom Server erhaltenes Ereignis;
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Teilt dem Server mit, dass eine Bombe gelegt wurde
	 */
	public void placeBomb() {
		try {
			output.writeUTF("bomb");
			output.writeInt(clientNumber);
			output.flush();
		} catch (IOException e) {
		}
	}

	/**
	 * Teilt dem Server mit, dass der Spieler sich bewegen will
	 * 
	 * @param x
	 *            Erste Richtungskoordinate
	 * @param y
	 *            Zweite Richtungskoordinate
	 */
	public void movePlayer(int x, int y) {
		try {
			output.writeUTF("move");
			output.writeObject(localPlayer);
			output.writeInt(x);
			output.writeInt(y);
			output.flush();
		} catch (IOException e) {
		}
	}

	/**
	 * Gibt den vom Server erhaltenen Spieler zurück
	 * 
	 * @return Vom Server erhaltener Spieler
	 */
	public Player getPlayer() {
		return localPlayer;
	}

	/**
	 * Gibt den vom Server erhaltenes Spielfeld zurück
	 * 
	 * @return Vom Server erhaltene Spielfeld
	 */
	public Field getField() {
		return localField;
	}

	/**
	 * Gibt eine vom Server erhaltene Liste an anzuzegenden Explosionen zurück
	 * 
	 * @return Vom Server erhaltene Explosionsliste
	 */
	public ArrayList getExList() {
		return exList;
	}

	/**
	 * Stoppt den Client und gibt alle Streams wieder frei
	 */
	public void stopClient() {
		try {
			if (input != null) {
				input.stopStream();

				output.close();
			}
			running = false;
		} catch (IOException e) {

		}
	}

	/**
	 * Gibt zurück, ob der Spieler gewonnen oder verloren hat
	 * 
	 * @return Gibt true zurück, falls der Spieler gewonnen hat. Gibt sonst
	 *         false zurück
	 */
	public boolean getWon() {
		return won;
	}
}
