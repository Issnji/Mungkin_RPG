package MungkinRpg.weapon.skills.bow;

import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class TripleArrow extends Skill {
    private Arrow[] arrows;
    private int arrowSpeed = 8;

    public TripleArrow() {
        super("Triple Arrow", 30, 90, 60); // Arrow hidup 1 detik
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        arrows = new Arrow[3];

        // Arrow tengah
        arrows[0] = new Arrow(x, y, dirX, dirY);

        // Arrow kiri & kanan (offset 45 derajat)
        if (dirX == 0) { // Vertical
            arrows[1] = new Arrow(x, y, -1, dirY);
            arrows[2] = new Arrow(x, y, 1, dirY);
        } else { // Horizontal
            arrows[1] = new Arrow(x, y, dirX, -1);
            arrows[2] = new Arrow(x, y, dirX, 1);
        }
    }

    @Override
    public void onUpdate() {
        for (Arrow a : arrows) {
            if (a != null && a.active) a.update();
        }
    }

    @Override
    public void onEnd() {
        arrows = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (arrows == null) return;
        for (Arrow a : arrows) {
            if (a != null && a.active) a.draw(g2);
        }
    }

    @Override
    public Rectangle getHitbox() {
        if (arrows == null) return null;
        for (Arrow a : arrows) {
            if (a != null && a.active) return a.hitbox;
        }
        return null;
    }

    // Inner class projectile
    private class Arrow {
        int x, y, dx, dy;
        Rectangle hitbox;
        boolean active;

        Arrow(int x, int y, int dx, int dy) {
            this.x = x; this.y = y;
            this.dx = dx; this.dy = dy;
            this.active = true;
            this.hitbox = new Rectangle(x, y, 12, 12);
        }

        void update() {
            x += dx * arrowSpeed;
            y += dy * arrowSpeed;
            hitbox.setLocation(x, y);
            // Boundary check
            if (x < 0 || x > 800 || y < 0 || y > 600) active = false;
        }

        void draw(Graphics2D g2) {
            g2.setColor(Color.YELLOW);
            g2.fillRect(x, y, 12, 4);
            g2.setColor(new Color(255, 0, 0, 80));
            g2.fill(hitbox);
        }
    }
}