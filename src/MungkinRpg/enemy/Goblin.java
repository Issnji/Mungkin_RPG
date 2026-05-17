package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Color;

public class Goblin extends Enemy {
    private int jumpTimer;
    private boolean jumping;
    private int jumpSpeed;

    public Goblin(int x, int y) {
        super(x, y, 60, 12, 3);
        this.expReward = 30;
        this.goldReward = 20;
        this.jumpTimer = 0;
        this.jumping = false;
    }

    @Override
    public void update() {
        jumpTimer++;

        // Goblin bergerak cepat dengan lompatan
        if (!jumping && jumpTimer > 40) {
            jumping = true;
            jumpSpeed = 5;
            jumpTimer = 0;
        }

        if (jumping) {
            x += (Math.random() > 0.5 ? 1 : -1) * jumpSpeed;
            y += (Math.random() > 0.5 ? 1 : -1) * jumpSpeed;
            jumpSpeed--;
            if (jumpSpeed <= 0) jumping = false;
        }

        x = Math.max(50, Math.min(x, 750));
        y = Math.max(50, Math.min(y, 550));
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(50, 150, 50));
        g2.fillOval(x, y, 28, 28);

        // Goblin ears
        g2.fillPolygon(new int[]{x, x - 5, x + 5}, new int[]{y + 5, y - 5, y + 10}, 3);
        g2.fillPolygon(new int[]{x + 28, x + 33, x + 23}, new int[]{y + 5, y - 5, y + 10}, 3);

        // HP bar
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 10, 28, 5);
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - 10, (int)(28.0 * hp / maxHp), 5);
    }
}