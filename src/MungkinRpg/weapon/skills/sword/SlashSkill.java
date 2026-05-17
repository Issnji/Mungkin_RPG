package MungkinRpg.weapon.skills.sword;

import com.MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class SlashSkill extends Skill {
    private Rectangle slashBox;
    private int range = 60;

    public SlashSkill() {
        super("Slash", 40, 60, 12);
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        updateHitbox(x, y);
    }

    @Override
    public void onUpdate() {
        // Slash tetap di depan player selama durasi
        // (Asumsi player tidak bergerak terlalu jauh dalam 12 frame)
    }

    private void updateHitbox(int x, int y) {
        int hx = x + dirX * 30;
        int hy = y + dirY * 30;
        slashBox = new Rectangle(hx - 20, hy - 20, 40, 40);
    }

    @Override
    public void onEnd() {
        slashBox = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active || slashBox == null) return;

        // Efek slash berupa arc cyan
        g2.setColor(new Color(0, 255, 255, 180));
        g2.drawArc(startX + dirX * 20 - 30, startY + dirY * 20 - 30, 60, 60,
                getArcStart(), 90);

        // Hitbox debug (hapus jika tidak perlu)
        g2.setColor(new Color(255, 0, 0, 80));
        g2.fill(slashBox);
    }

    private int getArcStart() {
        return switch ((dirX + 1) + (dirY + 1) * 3) {
            case 1 -> 90;  // UP
            case 7 -> 270; // DOWN
            case 3 -> 0;   // RIGHT
            case 5 -> 180; // LEFT
            default -> 0;
        };
    }

    @Override
    public Rectangle getHitbox() {
        return active ? slashBox : null;
    }
}