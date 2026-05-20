package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Color;

public class Skeleton extends Enemy {
    private int moveTimer;
    private int dirX, dirY;

    public Skeleton(int x, int y) {
        super(x, y, 80, 15, 2);
        this.expReward = 35;
        this.goldReward = 25;
        this.moveTimer = 0;
        changeDirection();
    }

    private void changeDirection() {
        dirX = (int)(Math.random() * 3) - 1;
        dirY = (int)(Math.random() * 3) - 1;
    }

    @Override
    public void update() {
        updateHitCooldown(); // FIX
        moveTimer++;
        if (moveTimer > 60) {
            changeDirection();
            moveTimer = 0;
        }

        x += dirX * speed;
        y += dirY * speed;
        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(220, 220, 220));
        g2.fillRect(x, y, 32, 48);
        g2.setColor(Color.BLACK);
        g2.drawRect(x + 8, y + 8, 16, 8);
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 10, 32, 5);
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 10, (int)(32.0 * hp / maxHp), 5);
    }
}