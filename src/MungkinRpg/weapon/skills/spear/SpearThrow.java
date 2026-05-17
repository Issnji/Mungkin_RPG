package MungkinRpg.weapon.skills.spear;

import com.MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class SpearThrow extends Skill {
    private Rectangle spearBox;
    private int sx, sy;
    private int speed = 10;
    private int distance = 0;
    private int maxDistance = 250;
    private boolean returning;

    public SpearThrow() {
        super("Spear Throw", 50, 120, 80);
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        sx = x; sy = y;
        distance = 0;
        returning = false;
    }

    @Override
    public void onUpdate() {
        if (!returning) {
            distance += speed;
            if (distance >= maxDistance) returning = true;
        } else {
            distance -= speed;
            if (distance <= 0) {
                active = false; // Selesai saat kembali
            }
        }

        sx = startX + dirX * distance;
        sy = startY + dirY * distance;
        spearBox = new Rectangle(sx - 8, sy - 8, 16, 16);
    }

    @Override
    public void onEnd() {
        spearBox = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;

        // Spear visual
        g2.setColor(new Color(200, 200, 220));
        int len = 24;
        int bx = sx - dirX * len;
        int by = sy - dirY * len;
        g2.drawLine(bx, by, sx, sy);

        // Ujung tajam
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillOval(sx - 6, sy - 6, 12, 12);

        // Hitbox
        g2.setColor(new Color(255, 0, 0, 80));
        g2.fill(spearBox);
    }

    @Override
    public Rectangle getHitbox() {
        return active ? spearBox : null;
    }
}