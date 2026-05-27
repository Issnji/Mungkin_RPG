package MungkinRpg.enemy;

import MungkinRpg.util.AssetLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Slime extends Enemy implements EnemyAI {

    private BufferedImage image;

    public Slime(int x, int y) {
        super(x, y, 50, 10, 1);
        this.expReward = 20;
        this.goldReward = 15;

        // Memuat gambar slime.png menggunakan AssetLoader
        this.image = AssetLoader.loadImage("slime.png");
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

        if (enemyX < playerX) enemyX += speed;
        else if (enemyX > playerX) enemyX -= speed;

        if (enemyY < playerY) enemyY += speed;
        else if (enemyY > playerY) enemyY -= speed;

        enemy.setX(enemyX);
        enemy.setY(enemyY);
    }

    @Override
    public void draw(Graphics2D g2) {
        if (image != null) {
            // Render gambar (diperbesar menjadi 64x64 dengan offset agar center di hitbox)
            g2.drawImage(image, x - 16, y - 32, 64, 64, null);
        } else {
            g2.setColor(Color.MAGENTA);
            g2.fillRect(x, y, 32, 32);
        }

        // HP Bar (dinaikkan posisinya agar tidak tertutup gambar)
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 35, 32, 5);

        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 35, (int)(32.0 * hp / maxHp), 5);
    }
}