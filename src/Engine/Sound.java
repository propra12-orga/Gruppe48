package Engine;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Sound stellt Methoden zu Verfuegung um Toene im Spiel abspielen zu koennen
 * 
 * @author Leonid Panich
 * 
 */
public enum Sound {

	BOMB("sounds/bomb.wav"), GAME_OVER("sounds/gameover.wav"), WELCOME(
			"sounds/welcome.wav"), YOUWIN("sounds/youwin.wav"), LOOP(
			"sounds/loop1.wav"), ITEM("sounds/item.wav");
	/**
	 * 
	 * Gibt die verfuegbaren Lautstaerken an
	 * 
	 */
	public static enum Volume {
		MUTE, LOW, MEDIUM, HIGH
	}

	/**
	 * Enthaelt die aktuelle Lautstaerke
	 */
	public static Volume volume = Volume.LOW;
	private Clip clip;

	/**
	 * Konstruktor der Klasse Sound
	 * 
	 * @param soundFileName
	 *            Pfad des Sounds
	 */
	Sound(String soundFileName) {
		try {
			URL url = this.getClass().getClassLoader()
					.getResource(soundFileName);
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Spielt einen Sound ab
	 */
	public void play() {
		if (volume != Volume.MUTE) {
			if (clip.isRunning())
				clip.stop();
			clip.setFramePosition(0);
			clip.start();

		}
	}

	/**
	 * Spielt endlos einen Sound
	 */
	public void loop() {
		if (volume != Volume.MUTE) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		}
	}

	/**
	 * Stoppt einen Sound
	 */
	public void loopStop() {
		if (clip.isRunning())
			clip.stop();
	}

	/**
	 * Pre-laden Sound-Dateien
	 */
	static void init() {
		values();
	}
}