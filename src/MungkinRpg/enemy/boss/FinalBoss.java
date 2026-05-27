package MungkinRpg.enemy.boss;

import MungkinRpg.enemy.Enemy;
import MungkinRpg.enemy.EnemyAI;
import MungkinRpg.util.AssetLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FinalBoss extends Enemy implements EnemyAI {

    private int attackPattern = 0;
    private int patternTimer  = 0;
    private float glowPhase   = 0f;

    private BufferedImage image;

    public FinalBoss(int x, int y) {
        super(x, y, 9999, 999, 2);
        this.expReward  = 10000;
        this.goldReward = 5000;

        // Memuat gambar boss.png
        this.image = AssetLoader.loadImage("boss.png");
    }

    @Override
    public void update() {
        updateHitCooldown();
        glowPhase += 0.08f;
        patternTimer++;

        if (patternTimer > 120) {
            attackPattern = (attackPattern + 1) % 3;
            patternTimer = 0;
        }

        switch (attackPattern) {
            case 0 -> {
                if (targetX >= 0) {
                    int dx = targetX - x;
                    int dy = targetY - y;
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    if (dist > 0) {
                        x += (int)(dx / dist * speed * 3);
                        y += (int)(dy / dist * speed * 3);
                    }
                }
            }
            case 1 -> {
                if (targetX >= 0) {
                    int dx = targetX - x;
                    int dy = targetY - y;
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    if (dist > 100) {
                        x += (int)(dx / dist * speed);
                        y += (int)(dy / dist * speed);
                    }
                    x += (int)(-dy / Math.max(dist, 1) * speed * 2);
                    y += (int)( dx / Math.max(dist, 1) * speed * 2);
                }
            }
            case 2 -> {
                if (hp < maxHp) {
                    hp += 10;
                }
                if (targetX >= 0) {
                    updateMovement(this, targetX, targetY);
                }
            }
        }

        x = Math.max(30, Math.min(x, 736));
        y = Math.max(30, Math.min(y, 504));
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
        // Efek Glow Aura (dipertahankan karena keren untuk Boss)
        int glow = (int)(Math.sin(glowPhase) * 30 + 60);
        g2.setColor(new Color(150, 0, glow, 80));
        g2.fillOval(x - 16, y - 16, 96, 96);

        if (image != null) {
            // Gambar Boss dibuat berukuran 96x96
            g2.drawImage(image, x - 16, y - 48, 96, 96, null);
        } else {
            g2.setColor(new Color(180, 0, 180));
            g2.fillRect(x, y, 64, 64);
        }

        // Teks dan HP bar dinaikkan posisinya agar muat dengan sprite yang lebih besar
        g2.setColor(Color.WHITE);
        g2.drawString("FINAL BOSS", x, y - 50);

        String phase = switch (attackPattern) {
            case 0 -> "CHARGE";
            case 1 -> "CIRCLE";
            default -> "REGEN";
        };

        g2.setColor(new Color(255, 180, 50));
        g2.drawString("[" + phase + "]", x + 4, y - 36);

        // HP Bar Boss
        g2.setColor(Color.RED);
        g2.fillRect(x - 16, y - 45, 96, 7);

        g2.setColor(new Color(200, 50, 200));
        g2.fillRect(x - 16, y - 45, (int)(96.0 * hp / maxHp), 7);
    }
}