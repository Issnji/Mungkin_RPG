package MungkinRpg.weapon.skills.bow;

import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class PiercingShot extends Skill {
    private Rectangle beam;
    private int length = 400;
    private int width = 20;

    public PiercingShot() {
        super("Piercing Shot", 60, 200, 15);
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        int bx, by, bw, bh;

        if (dirX != 0) { // Horizontal
            bx = x + dirX * 20;
            by = y - width/2;
            bw = length;
            bh = width;
        } else { // Vertical
            bx = x - width/2;
            by = y + dirY * 20;
            bw = width;
            bh = length;
        }
        beam = new Rectangle(bx, by, bw, bh);
    }

    @Override
    public void onUpdate() {
        // Beam tetap di posisi relatif terhadap start (tidak mengikuti player)
    }

    @Override
    public void onEnd() {
        beam = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active || beam == null) return;

        // Efek laser putih-biru
        g2.setColor(new Color(200, 230, 255, 200));
        g2.fill(beam);
        g2.setColor(Color.WHITE);
        g2.draw(beam);

        // Hitbox
        g2.setColor(new Color(255, 0, 0, 60));
        g2.fill(beam);
    }

    @Override
    public Rectangle getHitbox() {
        return active ? beam : null;
    }
}