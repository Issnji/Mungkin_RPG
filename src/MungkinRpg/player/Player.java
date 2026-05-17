package MungkinRpg.player;

import com.MungkinRpg.core.GamePanel;
import com.MungkinRpg.core.InputHandler;
import com.MungkinRpg.weapon.Weapon;
import com.MungkinRpg.weapon.WeaponManager;
import com.MungkinRpg.util.Constants;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player {
    private int x, y;
    private int speed;
    private Rectangle hitbox;

    private PlayerStats stats;
    private LevelSystem levelSystem;
    private Inventory inventory;
    private WeaponManager weaponManager;

    private boolean invincible = false;
    private int invincibleCounter = 0;
    private int facingDirection = 3;

    public Player() {
        this.x = Constants.SCREEN_WIDTH / 2;
        this.y = Constants.SCREEN_HEIGHT / 2;
        this.speed = 4;
        this.hitbox = new Rectangle(x, y, 32, 32);

        this.stats = new PlayerStats();
        this.levelSystem = new LevelSystem(this);
        this.inventory = new Inventory();
        this.weaponManager = new WeaponManager(this);
    }

    public void update(InputHandler input) {
        if (input.up) { y -= speed; facingDirection = 0; }
        if (input.down) { y += speed; facingDirection = 1; }
        if (input.left) { x -= speed; facingDirection = 2; }
        if (input.right) { x += speed; facingDirection = 3; }

        // Boundary check
        x = Math.max(0, Math.min(x, Constants.SCREEN_WIDTH - 32));
        y = Math.max(0, Math.min(y, Constants.SCREEN_HEIGHT - 32));

        hitbox.setLocation(x, y);

        // Weapon update
        weaponManager.update(input);

        // Invincibility frame
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.fillRect(x, y, 32, 32); // Placeholder sprite
        weaponManager.draw(g2);
    }

    public void takeDamage(int damage) {
        if (!invincible) {
            int actualDamage = Math.max(1, damage - stats.getDefense());
            stats.reduceHp(actualDamage);
            invincible = true;
        }
    }

    public void heal(int amount) {
        stats.heal(amount);
    }

    public void addGold(int amount) {
        inventory.addGold(amount);
    }

    public boolean isDead() {
        return stats.getCurrentHp() <= 0;
    }

    public void resetPosition() {
        this.x = Constants.SCREEN_WIDTH / 2;
        this.y = Constants.SCREEN_HEIGHT / 2;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public Rectangle getHitbox() { return hitbox; }
    public PlayerStats getStats() { return stats; }
    public LevelSystem getLevelSystem() { return levelSystem; }
    public Inventory getInventory() { return inventory; }
    public WeaponManager getWeaponManager() { return weaponManager; }
    public int getFacingDirection() { return facingDirection; }
}