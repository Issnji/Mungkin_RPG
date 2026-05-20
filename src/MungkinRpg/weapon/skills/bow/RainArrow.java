package MungkinRpg.weapon.skills.bow;

import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class RainArrow extends Skill {
    private List<Drop> drops;
    private int spawnTimer;
    private int targetX, targetY;

    public RainArrow() {
        super("Rain Arrow", 25, 180, 90); // 1.5 detik rain
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        drops = new ArrayList<>();
        spawnTimer = 0;
        // Target area di depan player
        targetX = x + dirX * 100;
        targetY = y + dirY * 100;
    }

    @Override
    public void onUpdate() {
        spawnTimer++;
        if (spawnTimer % 5 == 0) { // Spawn tiap 5 frame
            int rx = targetX + (int)(Math.random() * 120) - 60;
            int ry = targetY + (int)(Math.random() * 120) - 60;
            drops.add(new Drop(rx, -20)); // Jatuh dari atas
        }

        drops.forEach(Drop::update);
        drops.removeIf(d -> !d.active);
    }

    @Override
    public void onEnd() {
        drops.clear();
    }

    @Override
    public void draw(Graphics2D g2) {
        if (drops == null) return;

        // Target area indicator
        g2.setColor(new Color(255, 255, 0, 50));
        g2.fillOval(targetX - 60, targetY - 60, 120, 120);

        for (Drop d : drops) d.draw(g2);
    }

    @Override
    public Rectangle getHitbox() {
        if (drops == null || drops.isEmpty()) return null;
        // Return hitbox drop pertama yang aktif
        for (Drop d : drops) {
            if (d.active) return d.hitbox;
        }
        return null;
    }

    private class Drop {
        int x, y;
        int speed = 10;
        Rectangle hitbox;
        boolean active;

        Drop(int x, int y) {
            this.x = x; this.y = y;
            this.hitbox = new Rectangle(x, y, 8, 20);
            this.active = true;
        }

        void update() {
            y += speed;
            hitbox.setLocation(x, y);
            if (y > 600) active = false;
        }

        void draw(Graphics2D g2) {
            g2.setColor(Color.ORANGE);
            g2.fillRect(x, y, 4, 16);
            g2.setColor(new Color(255, 0, 0, 80));
            g2.fill(hitbox);
        }
    }
}