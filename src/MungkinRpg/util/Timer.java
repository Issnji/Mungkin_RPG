package MungkinRpg.util;

public class Timer {
    private int duration;
    private int current;
    private boolean running;
    private boolean done;

    public Timer(int duration) {
        this.duration = duration;
        this.current = 0;
        this.running = false;
        this.done = false;
    }

    public void start() {
        running = true;
        current = 0;
        done = false;
    }

    public void update() {
        if (!running || done) return;
        current++;
        if (current >= duration) {
            done = true;
            running = false;
        }
    }

    public boolean isDone() { return done; }
    public boolean isRunning() { return running; }
    public float getProgress() { return (float) current / duration; }
    public void reset() { current = 0; done = false; running = false; }
}