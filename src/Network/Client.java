package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
	Field localField;
	Player localPlayer;
	String event;
	boolean newEvent;

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
		newEvent = false;

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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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

						newEvent = true;
					}
					if (event.equals("player")) {
						localPlayer = (Player) input.getNextObject();

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

	/**
	 * Gibt zurück, ob eine neue Nachricht vom server eingetroffen ist
	 * 
	 * @return Ist = true, falls neue Nachricht vorhanden ist, ist sonst = false
	 */
	public boolean getEvent() {
		return newEvent;
	}

	/**
	 * Methode zum zurücksetzen der Nachrichtmarkierung
	 */
	public void resetEvent() {
		newEvent = false;
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
			e.printStackTrace();
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
			output.flush();
			output.writeObject(localPlayer);
			output.writeInt(x);
			output.writeInt(y);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
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

}
