package game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Audio extends GameObject {

	private MediaPlayer mediaPlayer;
	private Media sound;

	public Audio(String src, boolean autoplay) {
		super("audio");

		sound = new Media(new File(src).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);

		if (autoplay)
			play();
	}

	public Audio(String src) {
		this(src, false);
	}

	public Audio setVolume(double digit) {
		digit = digit > 1 ? 1 : digit;
		digit = digit < 0 ? 0 : digit;
		mediaPlayer.setVolume(digit);
		return this;
	}

	public void play() {
		mediaPlayer.play();
	}
}
