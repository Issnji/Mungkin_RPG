package MungkinRpg.enemy;

import MungkinRpg.util.AssetLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Skeleton extends Enemy {
    private int strafeTimer = 0;
    private int strafeDir   = 1;

    private BufferedImage image;

    public Skeleton(int x, int y) {
        super(x, y, 80, 15, 2);
        this.expReward  = 35;
        this.goldReward = 25;

        // Memuat gambar skeleton.png
        this.image = AssetLoader.loadImage("skeleton.png");
    }

    @Override
    public void update() {
        updateHitCooldown();
        strafeTimer++;
        if (strafeTimer > 40) { strafeDir *= -1; strafeTimer = 0; }

        if (targetX >= 0) {
            int dx = targetX - x, dy = targetY - y;
            double dist = Math.sqrt(dx*dx + dy*dy);
            if (dist > 8) {
                x += (int)(dx / dist * speed);
                y += (int)(dy / dist * speed);
                x += (int)(-dy / dist * strafeDir);
            }
        }

        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        if (image != null) {
            // Render gambar (diperbesar menjadi 64x64 dengan offset agar center di hitbox)
            g2.drawImage(image, x - 16, y - 32, 64, 64, null);
        } else {
            g2.setColor(new Color(220, 220, 220));
            g2.fillRect(x, y, 32, 48);
        }

        // HP bar (dinaikkan posisinya agar tidak tertutup gambar)
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 35, 32, 5);

        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 35, (int)(32.0 * hp / maxHp), 5);
    }
}