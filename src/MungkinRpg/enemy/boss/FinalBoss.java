package  MungkinRpg.enemy.boss;

import  MungkinRpg.enemy.Enemy;
import java.awt.Graphics2D;
import java.awt.Color;

public class FinalBoss extends Enemy {
    private int attackPattern;
    private int patternTimer;

    public FinalBoss(int x, int y) {
        super(x, y, 9999, 999, 2);
        this.expReward = 10000;
        this.goldReward = 5000;
        this.attackPattern = 0;
        this.patternTimer = 0;
    }

    @Override
    public void update() {
        updateHitCooldown(); // FIX
        patternTimer++;
        if (patternTimer > 120) {
            attackPattern = (attackPattern + 1) % 3;
            patternTimer = 0;
        }

        switch (attackPattern) {
            case 0 -> x += speed * 2;
            case 1 -> y += speed;
            case 2 -> { if (hp < maxHp) hp += 5; }
        }

        // Keep boss on screen
        x = Math.max(50, Math.min(x, 736));
        y = Math.max(50, Math.min(y, 536));
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.fillRect(x, y, 64, 64);
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 15, 64, 8);
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 15, (int)(64.0 * hp / maxHp), 8);
        g2.setColor(Color.WHITE);
        g2.drawString("FINAL BOSS", x, y - 20);
    }
}