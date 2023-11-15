
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameAudio {
	public Clip backgroundMusic, gamePlayMusic, gameMusic2;
	public Clip effect1, effect2, effect3;
	
	public GameAudio() {
		
		load();
	}
	
	public void load() {
		try {
			backgroundMusic = AudioSystem.getClip();
			File audioFile = new File("src/static/music/IntroMusic.wav");
			AudioInputStream audioStream1 = AudioSystem.getAudioInputStream(audioFile);
			backgroundMusic.open(audioStream1);
			
			
			gamePlayMusic = AudioSystem.getClip();
			audioFile = new File("src/static/music/gamePlayMusic.wav");
			AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(audioFile);
			gamePlayMusic.open(audioStream2);

		}
		catch(LineUnavailableException e) {e.printStackTrace();} 
		catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(String list) {
		
		switch(list) {
		case "background":
			backgroundMusic.setFramePosition(0);
			backgroundMusic.start();
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
			break;
		case "gamePlay":
			gamePlayMusic.setFramePosition(0);
			gamePlayMusic.start();
			gamePlayMusic.loop(Clip.LOOP_CONTINUOUSLY);
			break;
		}
	}
	
	public void allStop() {
		backgroundMusic.stop();
		gamePlayMusic.stop();
//		gameMusic2.stop();
	}
	
	public void stop(String list) {
		
		switch(list) {
		case "background":
			backgroundMusic.stop();
			break;
		case "gamePlay":
			gamePlayMusic.stop();

		}
	}
	
}