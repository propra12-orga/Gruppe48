package Network;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Stellt einen ObjectInputstream als eigenen Thread zu verf�gung
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
	 *            ObjectInputStream �ber dem der NetworkInputStream ge�ffnet
	 *            werden soll
	 */
	public NetworkInputStream(ObjectInputStream oStream) {
		input = oStream;
	}

	/**
	 * Run Methode des threads. H�lt diesen am leben, tut sonst nichts.
	 */
	public void run() {

		while (true) {
		}
	}

	/**
	 * Gibt n�chstes Objekt des Streams als String zur�ck
	 * 
	 * @return N�chstes Streamobjekt
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
	 * Gibt n�chstes Objekt des Streams zur�ck
	 * 
	 * @return N�chstes Streamobjekt
	 */
	public Object getNextObject() throws ClassNotFoundException, IOException {
		return input.readObject();
	}

	/**
	 * Gibt n�chstes Objekt des Streams als Int zur�ck
	 * 
	 * @return N�chstes Streamobjekt
	 */
	public int getNextInt() throws ClassNotFoundException, IOException {
		return input.readInt();
	}

	/**
	 * Gibt zur�ck, ob ein neues Objekt im Stream vorhanden ist
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
