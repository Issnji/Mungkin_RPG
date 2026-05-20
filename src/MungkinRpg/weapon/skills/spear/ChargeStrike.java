package MungkinRpg.weapon.skills.spear;

import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class ChargeStrike extends Skill {
    private Rectangle chargeBox;
    private int chargeDist = 0;
    private int chargeSpeed = 12;
    private int maxDist = 160;
    private boolean charging;
    private int windUp = 0;

    public ChargeStrike() {
        super("Charge Strike", 70, 180, 40);
    }

    @Override
    public void onActivate(int x, int y, int direction) {
        chargeDist = 0;
        charging = false;
        windUp = 15; // 0.25 detik wind-up
    }

    @Override
    public void onUpdate() {
        if (windUp > 0) {
            windUp--;
            if (windUp == 0) charging = true;
            return;
        }

        if (charging) {
            chargeDist += chargeSpeed;
            if (chargeDist >= maxDist) charging = false;

            int cx = startX + dirX * chargeDist;
            int cy = startY + dirY * chargeDist;
            chargeBox = new Rectangle(cx - 24, cy - 24, 48, 48);
        }
    }

    @Override
    public void onEnd() {
        chargeBox = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;

        // Wind-up indicator
        if (windUp > 0) {
            g2.setColor(new Color(255, 255, 0, 100 + windUp * 10));
            g2.drawOval(startX - 30, startY - 30, 60, 60);
            g2.drawString("!", startX - 3, startY + 5);
            return;
        }

        if (!charging) return;

        // Charge effect
        int cx = startX + dirX * chargeDist;
        int cy = startY + dirY * chargeDist;

        // Trail
        g2.setColor(new Color(255, 50, 50, 150));
        for (int i = 0; i < chargeDist; i += 15) {
            int tx = startX + dirX * i;
            int ty = startY + dirY * i;
            g2.fillOval(tx - 8, ty - 8, 16, 16);
        }

        // Impact area
        g2.setColor(new Color(255, 100, 100, 200));
        g2.fillOval(cx - 20, cy - 20, 40, 40);

        // Hitbox
        g2.setColor(new Color(255, 0, 0, 80));
        g2.fill(chargeBox);
    }

    @Override
    public Rectangle getHitbox() {
        return (active && charging) ? chargeBox : null;
    }
}