package MungkinRpg.weapon.skills.sword;

import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class DashSlash extends Skill {
    private Rectangle dashBox;
    private int dashDistance = 120;
    private int currentDist = 0;
    private int dashSpeed = 15;

    public DashSlash() {
        super("Dash Slash", 55, 150, 20);
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        currentDist = 0;
    }

    @Override
    public void onUpdate() {
        currentDist += dashSpeed;
        if (currentDist > dashDistance) currentDist = dashDistance;

        int hx = startX + dirX * currentDist;
        int hy = startY + dirY * currentDist;
        dashBox = new Rectangle(hx - 20, hy - 20, 40, 40);
    }

    @Override
    public void onEnd() {
        dashBox = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;

        // Trail efek
        g2.setColor(new Color(100, 200, 255, 150));
        int ex = startX + dirX * currentDist;
        int ey = startY + dirY * currentDist;

        // Gambar trail dari awal sampai posisi sekarang
        for (int i = 0; i < currentDist; i += 10) {
            int tx = startX + dirX * i;
            int ty = startY + dirY * i;
            g2.fillOval(tx - 5, ty - 5, 10, 10);
        }

        // Slash di ujung dash
        g2.setColor(Color.CYAN);
        g2.drawRect(ex - 25, ey - 25, 50, 50);

        // Hitbox
        g2.setColor(new Color(255, 0, 0, 80));
        g2.fill(dashBox);
    }

    @Override
    public Rectangle getHitbox() {
        return active ? dashBox : null;
    }
}