package MungkinRpg.enemy;

import MungkinRpg.util.AssetLoader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Slime extends Enemy implements EnemyAI {

    // 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
    private int facingDirection = 1;

    private BufferedImage slimeFront;
    private BufferedImage slimeBack;
    private BufferedImage slimeLeft;
    private BufferedImage slimeRight;

    public Slime(int x, int y) {

        super(x, y, 50, 10, 1);

        this.expReward = 20;
        this.goldReward = 15;

        loadSprites();
    }

    private void loadSprites() {

        slimeFront = AssetLoader.loadImage("karakter/slime.png");
        slimeBack  = AssetLoader.loadImage("karakter/slimebelakang.png");
        slimeLeft  = AssetLoader.loadImage("karakter/slimekiri.png");
        slimeRight = AssetLoader.loadImage("karakter/slimekanan.png");
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
    public void draw(Graphics2D g2) {

        BufferedImage currentSprite = null;

        switch (facingDirection) {

            case 0:
                currentSprite = slimeBack;
                break;

            case 1:
                currentSprite = slimeFront;
                break;

            case 2:
                currentSprite = slimeLeft;
                break;

            case 3:
                currentSprite = slimeRight;
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

            g2.setColor(Color.MAGENTA);
            g2.fillRect(x, y, 32, 32);
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