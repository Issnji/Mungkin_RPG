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
            File file = new File("src/MungkinRpg/assets/audio/" + filename);

            System.out.println("Loading: " + file.getAbsolutePath());
            System.out.println("Exists : " + file.exists());

            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(file);

            currentClip = AudioSystem.getClip();
            currentClip.open(ais);

            currentClip.start();
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);

            volumeControl =
                    (FloatControl) currentClip.getControl(
                            FloatControl.Type.MASTER_GAIN);

            currentTrack = filename;

        } catch (Exception e) {
            e.printStackTrace();
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