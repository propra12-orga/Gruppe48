package Network;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Stellt einen ObjectInputstream als eigenen Thread zu verfügung
 * 
 * @author Alexander
 * 
 */
public class NetworkInputStream extends Thread {

	ObjectInputStream input;
	boolean streamStatus;

	/**
	 * Erstellt ein neues Objekt vom Typ NetworkInputStream
	 * 
	 * @param oStream
	 *            ObjectInputStream über dem der NetworkInputStream geöffnet
	 *            werden soll
	 */
	public NetworkInputStream(ObjectInputStream oStream) {
		input = oStream;
	}

	/**
	 * Run Methode des threads. Hält diesen am leben, tut sonst nichts.
	 */
	public void run() {

		while (true) {
		}
	}

	/**
	 * Gibt nächstes Objekt des Streams als String zurück
	 * 
	 * @return Nächstes Streamobjekt
	 */
	public String getNextEvent() {
		try {
			return input.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Gibt nächstes Objekt des Streams zurück
	 * 
	 * @return Nächstes Streamobjekt
	 */
	public Object getNextObject() throws ClassNotFoundException, IOException {
		return input.readObject();
	}

	/**
	 * Gibt nächstes Objekt des Streams als Int zurück
	 * 
	 * @return Nächstes Streamobjekt
	 */
	public int getNextInt() throws ClassNotFoundException, IOException {
		return input.readInt();
	}

	/**
	 * Gibt zurück, ob ein neues Objekt im Stream vorhanden ist
	 * 
	 * @return Ist true, falls neues Objekt vorhanden ist, ist sonst false
	 */
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
