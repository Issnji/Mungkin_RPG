package MungkinRpg.town;

import MungkinRpg.core.SceneManager;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

public class TeleportGate {
    private int x, y;
    private Rectangle hitbox;
    private SceneManager sceneManager;
    private boolean playerNear;

    public TeleportGate(int x, int y, SceneManager sceneManager) {
        this.x = x;
        this.y = y;
        this.sceneManager = sceneManager;
        this.hitbox = new Rectangle(x, y, 64, 80);
    }

    public Rectangle getHitbox() { return hitbox; }

    public void update(boolean near) {
        this.playerNear = near;
    }

    public void activate() {
        sceneManager.changeScene(SceneManager.Scene.DUNGEON);
    }

    public void draw(Graphics2D g2) {
        // Gate frame
        g2.setColor(new Color(100, 50, 150));
        g2.fillRect(x, y, 64, 80);
        // Portal effect
        g2.setColor(new Color(150, 100, 255, 150 + (int)(Math.sin(System.currentTimeMillis() / 200.0) * 50)));
        g2.fillOval(x + 10, y + 15, 44, 50);

        if (playerNear) {
            g2.setColor(Color.WHITE);
            g2.drawString("Press E to Enter Dungeon", x - 30, y - 10);
        }
    }
}