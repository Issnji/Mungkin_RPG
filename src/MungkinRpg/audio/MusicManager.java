package MungkinRpg.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicManager {
    private Clip currentClip;
    private FloatControl volumeControl;
    private String currentTrack;

    public void play(String filename) {
        if (currentTrack != null && currentTrack.equals(filename)) return;
        stop();

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/com/MungkinRpg/assets/audio/" + filename));
            currentClip = AudioSystem.getClip();
            currentClip.open(ais);
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);

            volumeControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            currentTrack = filename;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Music error: " + e.getMessage());
        }
    }

    public void stop() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
        }
        currentTrack = null;
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            volumeControl.setValue(volume);
        }
    }
}