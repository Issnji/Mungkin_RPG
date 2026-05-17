package MungkinRpg.util;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private int timer;
    private int speed;
    private boolean looping;
    private boolean finished;

    public Animation(BufferedImage[] frames, int speed, boolean looping) {
        this.frames = frames;
        this.speed = speed;
        this.looping = looping;
        this.currentFrame = 0;
        this.timer = 0;
        this.finished = false;
    }

    public void update() {
        if (finished) return;

        timer++;
        if (timer >= speed) {
            timer = 0;
            currentFrame++;
            if (currentFrame >= frames.length) {
                if (looping) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.length - 1;
                    finished = true;
                }
            }
        }
    }

    public void reset() {
        currentFrame = 0;
        timer = 0;
        finished = false;
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    public boolean isFinished() { return finished; }
}