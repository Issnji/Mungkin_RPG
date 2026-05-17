package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Color;

public class Slime extends Enemy {

    public Slime(int x, int y) {
        super(x, y, 50, 10, 1);
        this.expReward = 20;
        this.goldReward = 15;
    }

    @Override
    public void update() {
        // AI sederhana: gerak acak atau ke arah player
        y += speed;
        if (y > 500) y = 100;
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.fillOval(x, y, 32, 32);
        // HP bar
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 10, 32, 5);
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 10, (int)(32.0 * hp / maxHp), 5);
    }
}