package MungkinRpg.enemy;

import MungkinRpg.util.AssetLoader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Skeleton extends Enemy {

    // 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
    private int facingDirection = 1;

    private int strafeTimer = 0;
    private int strafeDir = 1;

    private BufferedImage skeletonFront;
    private BufferedImage skeletonBack;
    private BufferedImage skeletonLeft;
    private BufferedImage skeletonRight;

    public Skeleton(int x, int y) {

        super(x, y, 80, 15, 2);

        this.expReward = 35;
        this.goldReward = 25;

        loadSprites();
    }

    private void loadSprites() {

        skeletonFront = AssetLoader.loadImage("karakter/skeleton.png");
        skeletonBack  = AssetLoader.loadImage("karakter/skeletonbelakang.png");
        skeletonLeft  = AssetLoader.loadImage("karakter/skeletonkiri.png");
        skeletonRight = AssetLoader.loadImage("karakter/skeletonkanan.png");
    }

    @Override
    public void update() {

        updateHitCooldown();

        strafeTimer++;

        if (strafeTimer > 40) {
            strafeDir *= -1;
            strafeTimer = 0;
        }

        if (targetX >= 0) {

            int dx = targetX - x;
            int dy = targetY - y;

            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > 8) {

                if (Math.abs(dx) > Math.abs(dy)) {
                    facingDirection = dx > 0 ? 3 : 2;
                } else {
                    facingDirection = dy > 0 ? 1 : 0;
                }

                x += (int) (dx / dist * speed);
                y += (int) (dy / dist * speed);

                x += (int) (-dy / dist * strafeDir);
            }
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
                currentSprite = skeletonBack;
                break;

            case 1:
                currentSprite = skeletonFront;
                break;

            case 2:
                currentSprite = skeletonLeft;
                break;

            case 3:
                currentSprite = skeletonRight;
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

            g2.setColor(new Color(220, 220, 220));
            g2.fillRect(x, y, 32, 48);
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