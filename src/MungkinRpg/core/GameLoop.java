package MungkinRpg.core;

public class GameLoop {
    private GamePanel panel;
    private final int FPS = 60;
    private final double DRAW_INTERVAL = 1000000000.0 / FPS;

    public GameLoop(GamePanel panel) {
        this.panel = panel;
    }

    public void start() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (panel != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / DRAW_INTERVAL;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                panel.update();
                panel.repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
}