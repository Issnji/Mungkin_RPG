package MungkinRpg.enemy;

import MungkinRpg.util.AssetLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Goblin extends Enemy implements EnemyAI {
    private int jumpTimer = 0;
    private boolean jumping = false;
    private int jumpDX = 0, jumpDY = 0;
    private int jumpSteps = 0;

    private BufferedImage image;

    public Goblin(int x, int y) {
        super(x, y, 60, 12, 3);
        this.expReward  = 30;
        this.goldReward = 20;

        // Memuat gambar goblin.png
        this.image = AssetLoader.loadImage("goblin.png");
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
            updateMovement(this, targetX, targetY);
        }

        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        if (image != null) {
            // Render gambar (diperbesar menjadi 64x64 dengan offset agar center di hitbox)
            g2.drawImage(image, x - 16, y - 32, 64, 64, null);
        } else {
            g2.setColor(new Color(50, 150, 50));
            g2.fillOval(x, y, 28, 28);
        }

        // HP bar (dinaikkan posisinya agar tidak tertutup gambar)
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 35, 32, 5);

        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 35, (int)(32.0 * hp / maxHp), 5);
    }
}