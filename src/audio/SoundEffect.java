package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffect {

    public static void play(String filename) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/com/MungkinRpg/assets/audio/" + filename));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("SFX error: " + e.getMessage());
        }
    }

    public static void playHit() { play("hit.wav"); }
    public static void playSlash() { play("slash.wav"); }
    public static void playArrow() { play("arrow.wav"); }
    public static void playLevelUp() { play("levelup.wav"); }
    public static void playCoin() { play("coin.wav"); }
}