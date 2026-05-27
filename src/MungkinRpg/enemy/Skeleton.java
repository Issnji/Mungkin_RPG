package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Color;

public class Skeleton extends Enemy{
    private int strafeTimer = 0;
    private int strafeDir   = 1; // -1 atau 1, untuk gerakan zigzag

    public Skeleton(int x, int y) {
        super(x, y, 80, 15, 2);
        this.expReward  = 35;
        this.goldReward = 25;
    }


    @Override
    public void update() {
        updateHitCooldown();
        strafeTimer++;
        if (strafeTimer > 40) { strafeDir *= -1; strafeTimer = 0; } // zigzag

        // FIX: kejar player + sedikit gerakan zigzag agar lebih menarik
        if (targetX >= 0) {
            int dx = targetX - x, dy = targetY - y;
            double dist = Math.sqrt(dx*dx + dy*dy);
            if (dist > 8) {
                x += (int)(dx / dist * speed);
                y += (int)(dy / dist * speed);
                // Komponen zigzag: tegak lurus arah gerak
                x += (int)(-dy / dist * strafeDir);
            }
        }

        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(220, 220, 220));
        g2.fillRect(x, y, 32, 48);
        // Mata (eye sockets)
        g2.setColor(Color.BLACK);
        g2.fillRect(x+8,  y+8, 6, 6);
        g2.fillRect(x+18, y+8, 6, 6);
        g2.setColor(new Color(200, 50, 50));
        g2.fillOval(x+9,  y+9,  4, 4);
        g2.fillOval(x+19, y+9,  4, 4);
        // Tulang iga
        g2.setColor(new Color(180, 180, 180));
        for (int i = 0; i < 3; i++) g2.drawLine(x+6, y+22+i*6, x+26, y+22+i*6);
        // HP bar
        g2.setColor(Color.RED);   g2.fillRect(x, y-10, 32, 5);
        g2.setColor(Color.GREEN); g2.fillRect(x, y-10, (int)(32.0*hp/maxHp), 5);
    }
}