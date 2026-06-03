package MungkinRpg.enemy;

import MungkinRpg.util.AssetLoader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Goblin extends Enemy implements EnemyAI {

    // Direction: 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
    private int facingDirection = 1;

    private int jumpTimer = 0;
    private boolean jumping = false;
    private int jumpDX = 0;
    private int jumpDY = 0;
    private int jumpSteps = 0;

    private BufferedImage goblinFront;
    private BufferedImage goblinBack;
    private BufferedImage goblinLeft;
    private BufferedImage goblinRight;

    public Goblin(int x, int y) {

        super(x, y, 60, 12, 3);

        this.expReward = 30;
        this.goldReward = 20;

        loadSprites();
    }

    private void loadSprites() {

        goblinFront = AssetLoader.loadImage("karakter/goblin.png");
        goblinBack  = AssetLoader.loadImage("karakter/goblingbelakang.png");
        goblinLeft  = AssetLoader.loadImage("karakter/goblinkiri.png");
        goblinRight = AssetLoader.loadImage("karakter/goblinkanan.png");
    }

    @Override
    public void updateMovement(Enemy enemy, int playerX, int playerY) {

        int enemyX = enemy.getX();
        int enemyY = enemy.getY();

        int dx = playerX - enemyX;
        int dy = playerY - enemyY;

        if (Math.abs(dx) > Math.abs(dy)) {

            if (dx > 0) {
                enemyX += speed;
                facingDirection = 3;
            } else if (dx < 0) {
                enemyX -= speed;
                facingDirection = 2;
            }

        } else {

            if (dy > 0) {
                enemyY += speed;
                facingDirection = 1;
            } else if (dy < 0) {
                enemyY -= speed;
                facingDirection = 0;
            }
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

                int dx = targetX - x;
                int dy = targetY - y;

                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist > 0) {

                    jumpDX = (int) (dx / dist * (speed + 3));
                    jumpDY = (int) (dy / dist * (speed + 3));

                    if (Math.abs(dx) > Math.abs(dy)) {
                        facingDirection = dx > 0 ? 3 : 2;
                    } else {
                        facingDirection = dy > 0 ? 1 : 0;
                    }
                }
            }
        }

        if (jumping) {

            x += jumpDX;
            y += jumpDY;

            jumpSteps--;

            if (jumpSteps <= 0) {
                jumping = false;
            }

        } else {

            updateMovement(this, targetX, targetY);
        }

        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));

        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {

        BufferedImage currentSprite = null;

        switch (facingDirection) {

            case 0:
                currentSprite = goblinBack;
                break;

            case 1:
                currentSprite = goblinFront;
                break;

            case 2:
                currentSprite = goblinLeft;
                break;

            case 3:
                currentSprite = goblinRight;
                break;
        }

        if (currentSprite != null) {

            g2.drawImage(
                    currentSprite,
                    x - 16,
                    y - 32,
                    64,
                    64,
                    null
            );

        } else {

            g2.setColor(new Color(50, 150, 50));
            g2.fillOval(x, y, 28, 28);
        }

        // HP Bar
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 35, 32, 5);

        g2.setColor(Color.GREEN);
        g2.fillRect(
                x,
                y - 35,
                (int) (32.0 * hp / maxHp),
                5
        );
    }
}