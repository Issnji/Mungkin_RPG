package MungkinRpg.weapon.skills.spear;

import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class ThrustSkill extends Skill {
    private Rectangle thrustBox;
    private int reach = 80;
    private int extend = 0;

    public ThrustSkill() {
        super("Thrust", 45, 75, 10);
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        extend = 0;
    }

    @Override
    public void onUpdate() {
        extend += 8; // Cepat maju
        if (extend > reach) extend = reach;

        int hx = startX + dirX * extend;
        int hy = startY + dirY * extend;
        thrustBox = new Rectangle(hx - 10, hy - 10, 20, 20);
    }

    @Override
    public void onEnd() {
        thrustBox = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;

        // Gambar spear thrust
        g2.setColor(Color.RED);
        int ex = startX + dirX * extend;
        int ey = startY + dirY * extend;
        g2.drawLine(startX, startY, ex, ey);

        // Ujung spear
        g2.setColor(Color.ORANGE);
        g2.fillOval(ex - 6, ey - 6, 12, 12);

        // Hitbox
        g2.setColor(new Color(255, 0, 0, 80));
        g2.fill(thrustBox);
    }

    @Override
    public Rectangle getHitbox() {
        return active ? thrustBox : null;
    }
}