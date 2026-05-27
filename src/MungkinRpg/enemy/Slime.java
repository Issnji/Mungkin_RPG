package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Color;

public class Slime extends Enemy implements EnemyAI {

    public Slime(int x, int y) {

        super(x, y, 50, 10, 1);

        this.expReward = 20;
        this.goldReward = 15;
    }

    @Override
    public void update() {

        updateHitCooldown();

        if (targetX >= 0) {
            updateMovement(this, targetX, targetY);
        }

        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));

        hitbox.setLocation(x, y);
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
    public void draw(Graphics2D g2) {

        g2.setColor(new Color(50, 200, 80));
        g2.fillOval(x, y, 32, 32);

        g2.setColor(Color.BLACK);
        g2.fillOval(x + 8, y + 10, 5, 5);
        g2.fillOval(x + 19, y + 10, 5, 5);

        g2.setColor(Color.RED);
        g2.fillRect(x, y - 10, 32, 5);

        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 10, (int)(32.0 * hp / maxHp), 5);
    }
}