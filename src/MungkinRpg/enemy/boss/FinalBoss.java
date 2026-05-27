package MungkinRpg.enemy.boss;

import MungkinRpg.enemy.Enemy;
import MungkinRpg.enemy.EnemyAI;

import java.awt.Graphics2D;
import java.awt.Color;

public class FinalBoss extends Enemy implements EnemyAI {

    private int attackPattern = 0;
    private int patternTimer  = 0;
    private float glowPhase   = 0f;

    public FinalBoss(int x, int y) {

        super(x, y, 9999, 999, 2);

        this.expReward  = 10000;
        this.goldReward = 5000;
    }

    @Override
    public void update() {

        updateHitCooldown();

        glowPhase += 0.08f;
        patternTimer++;

        // Ganti pattern setiap beberapa detik
        if (patternTimer > 120) {

            attackPattern = (attackPattern + 1) % 3;
            patternTimer = 0;
        }

        // =========================
        // AI BOSS
        // =========================

        switch (attackPattern) {

            // =====================
            // CHARGE MODE
            // =====================
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

            // =====================
            // CIRCLE MODE
            // =====================
            case 1 -> {

                if (targetX >= 0) {

                    int dx = targetX - x;
                    int dy = targetY - y;

                    double dist = Math.sqrt(dx * dx + dy * dy);

                    // Mendekat ke player
                    if (dist > 100) {

                        x += (int)(dx / dist * speed);
                        y += (int)(dy / dist * speed);
                    }

                    // Gerakan melingkar
                    x += (int)(-dy / Math.max(dist, 1) * speed * 2);
                    y += (int)( dx / Math.max(dist, 1) * speed * 2);
                }
            }

            // =====================
            // REGEN MODE
            // =====================
            case 2 -> {

                // Heal perlahan
                if (hp < maxHp) {
                    hp += 10;
                }

                // Tetap mengejar player
                if (targetX >= 0) {
                    updateMovement(this, targetX, targetY);
                }
            }
        }

        // =========================
        // BATAS MAP
        // =========================

        x = Math.max(30, Math.min(x, 736));
        y = Math.max(30, Math.min(y, 504));

        // Update hitbox
        hitbox.setLocation(x, y);
    }

    // =========================
    // MOVEMENT AI
    // =========================

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

        // Glow effect
        int glow = (int)(Math.sin(glowPhase) * 30 + 60);

        g2.setColor(new Color(150, 0, glow, 80));
        g2.fillOval(x - 16, y - 16, 96, 96);

        // Body
        g2.setColor(new Color(180, 0, 180));
        g2.fillRect(x, y, 64, 64);

        // Mata
        g2.setColor(new Color(255, 50, 50));
        g2.fillOval(x + 8, y + 14, 14, 14);
        g2.fillOval(x + 42, y + 14, 14, 14);

        g2.setColor(Color.BLACK);
        g2.fillOval(x + 12, y + 18, 6, 6);
        g2.fillOval(x + 46, y + 18, 6, 6);

        // Mulut
        g2.setColor(new Color(255, 50, 50));
        g2.drawArc(x + 16, y + 36, 32, 14, 200, 140);

        // Nama Boss
        g2.setColor(Color.WHITE);
        g2.drawString("FINAL BOSS", x, y - 22);

        // Status Pattern
        String phase = switch (attackPattern) {
            case 0 -> "CHARGE";
            case 1 -> "CIRCLE";
            default -> "REGEN";
        };

        g2.setColor(new Color(255, 180, 50));
        g2.drawString("[" + phase + "]", x + 4, y - 8);

        // HP Bar
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 18, 64, 7);

        g2.setColor(new Color(200, 50, 200));
        g2.fillRect(x, y - 18, (int)(64.0 * hp / maxHp), 7);
    }
}