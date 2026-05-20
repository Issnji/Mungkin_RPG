package MungkinRpg.weapon.skills.sword;

import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class SpinAttack extends Skill {
    private Rectangle spinBox;
    private int radius = 50;
    private int angle = 0;

    public SpinAttack() {
        super("Spin Attack", 35, 120, 30); // 0.5 detik spin
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        spinBox = new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void onUpdate() {
        angle += 30; // Rotate effect
        spinBox.setLocation(startX - radius, startY - radius);
    }

    @Override
    public void onEnd() {
        spinBox = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;

        // Lingkaran putar
        g2.setColor(new Color(255, 100, 100, 150));
        g2.drawOval(startX - radius, startY - radius, radius * 2, radius * 2);

        // Blade effect
        g2.setColor(new Color(255, 255, 255, 200));
        int bx = startX + (int)(Math.cos(Math.toRadians(angle)) * radius);
        int by = startY + (int)(Math.sin(Math.toRadians(angle)) * radius);
        g2.drawLine(startX, startY, bx, by);

        // Hitbox
        g2.setColor(new Color(255, 0, 0, 60));
        g2.fill(spinBox);
    }

    @Override
    public Rectangle getHitbox() {
        return active ? spinBox : null;
    }
}