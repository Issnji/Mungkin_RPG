package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Color;

public class Goblin extends Enemy implements EnemyAI{
    private int jumpTimer = 0;
    private boolean jumping = false;
    private int jumpDX = 0, jumpDY = 0;
    private int jumpSteps = 0;

    public Goblin(int x, int y) {
        super(x, y, 60, 12, 3);
        this.expReward  = 30;
        this.goldReward = 20;
    }
    @Override
    public void updateMovement(Enemy enemy, int playerX, int playerY) {

        int enemyX = enemy.getX();
        int enemyY = enemy.getY();

        if (enemyX < playerX) {
            enemyX += speed;
        } else if (enemyX > playerX) {
            enemyX -= speed;
        }

        if (enemyY < playerY) {
            enemyY += speed;
        } else if (enemyY > playerY) {
            enemyY -= speed;
        }

        enemy.setX(enemyX);
        enemy.setY(enemyY);
    }

    @Override
    public void update() {
        updateHitCooldown();
        jumpTimer++;

        if (!jumping && jumpTimer > 35) {
            // FIX: lompat KE ARAH player
            jumping = true;
            jumpSteps = 12;
            jumpTimer = 0;
            if (targetX >= 0) {
                int dx = targetX - x, dy = targetY - y;
                double dist = Math.sqrt(dx*dx + dy*dy);
                if (dist > 0) {
                    jumpDX = (int)(dx / dist * (speed + 3));
                    jumpDY = (int)(dy / dist * (speed + 3));
                }
            }
        }

        if (jumping) {
            x += jumpDX;
            y += jumpDY;
            jumpSteps--;
            if (jumpSteps <= 0) jumping = false;
        } else {
            updateMovement(this, targetX, targetY); // berjalan biasa ke arah player
        }

        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(50, 150, 50));
        g2.fillOval(x, y, 28, 28);
        // Telinga
        g2.fillPolygon(new int[]{x, x-5, x+5},        new int[]{y+5, y-5, y+10}, 3);
        g2.fillPolygon(new int[]{x+28, x+33, x+23},   new int[]{y+5, y-5, y+10}, 3);
        // Mata
        g2.setColor(Color.YELLOW);
        g2.fillOval(x+6,  y+8, 5, 5);
        g2.fillOval(x+17, y+8, 5, 5);
        // HP bar
        g2.setColor(Color.RED);   g2.fillRect(x, y-10, 28, 5);
        g2.setColor(Color.GREEN); g2.fillRect(x, y-10, (int)(28.0*hp/maxHp), 5);
    }
}