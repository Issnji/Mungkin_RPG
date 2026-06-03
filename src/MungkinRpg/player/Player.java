package MungkinRpg.player;

import MungkinRpg.core.InputHandler;
import MungkinRpg.util.AssetLoader;
import MungkinRpg.util.Constants;
import MungkinRpg.weapon.WeaponManager;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player {

    private BufferedImage playerFront;
    private BufferedImage playerBack;
    private BufferedImage playerLeft;
    private BufferedImage playerRight;

    private int x, y;
    private int speed;
    private Rectangle hitbox;

    private PlayerStats stats;
    private LevelSystem levelSystem;
    private Inventory inventory;
    private WeaponManager weaponManager;

    private boolean invincible = false;
    private int invincibleCounter = 0;

    // 0 = atas, 1 = bawah, 2 = kiri, 3 = kanan
    private int facingDirection = 1;
    public enum WeaponType {
        NONE,
        SWORD,
        SPEAR,
        BOW
    }

    private WeaponType currentWeapon = WeaponType.NONE;
    // Tambahkan di bawah variabel currentWeapon

    public void setWeaponType(WeaponType weaponType) {
        this.currentWeapon = weaponType;
        loadCharacterSprites();
    }

    public WeaponType getCurrentWeapon() {
        return currentWeapon;
    }
    private boolean isAttacking() {
        return weaponManager != null &&
                weaponManager.isBasicAttackActive();
    }
    public Player() {
        this.x = Constants.SCREEN_WIDTH / 2;
        this.y = Constants.SCREEN_HEIGHT / 2;
        this.speed = 4;
        this.hitbox = new Rectangle(x, y, 32, 32);

        this.stats = new PlayerStats();
        this.levelSystem = new LevelSystem(this);
        this.inventory = new Inventory();
        this.weaponManager = new WeaponManager(this);

        // Load sprite karakter
        loadCharacterSprites();
    }

    public void update(InputHandler input) {

        if (input.up) {
            y -= speed;
            facingDirection = 0;
        }

        if (input.down) {
            y += speed;
            facingDirection = 1;
        }

        if (input.left) {
            x -= speed;
            facingDirection = 2;
        }

        if (input.right) {
            x += speed;
            facingDirection = 3;
        }

        // Boundary check
        x = Math.max(0, Math.min(x, Constants.SCREEN_WIDTH - 32));
        y = Math.max(0, Math.min(y, Constants.SCREEN_HEIGHT - 32));

        hitbox.setLocation(x, y);

        // Update weapon
        weaponManager.update(input);
        loadCharacterSprites();

        // Invincibility frame
        if (invincible) {
            invincibleCounter++;

            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void loadCharacterSprites() {

        boolean attacking = isAttacking();

        switch (currentWeapon) {

            case NONE -> {
                playerFront = AssetLoader.loadImage("karakter/player.png");
                playerBack = AssetLoader.loadImage("karakter/playerbelakang.png");
                playerLeft = AssetLoader.loadImage("karakter/playerkiri.png");
                playerRight = AssetLoader.loadImage("karakter/playerkanan.png");
            }

            case SWORD -> {

                if (attacking) {
                    playerFront = AssetLoader.loadImage("weaponserang/swordserang.png");
                    playerBack = AssetLoader.loadImage("weaponserang/swordserangbelakang.png");
                    playerLeft = AssetLoader.loadImage("weaponserang/swordserangkiri.png");
                    playerRight = AssetLoader.loadImage("weaponserang/swordserangkanan.png");
                } else {
                    playerFront = AssetLoader.loadImage("weapon/sword.png");
                    playerBack = AssetLoader.loadImage("weapon/swordbelakang.png");
                    playerLeft = AssetLoader.loadImage("weapon/swordkiri.png");
                    playerRight = AssetLoader.loadImage("weapon/swordkanan.png");
                }
            }

            case SPEAR -> {

                if (attacking) {
                    playerFront = AssetLoader.loadImage("weaponserang/spearserang.png");
                    playerBack = AssetLoader.loadImage("weaponserang/spearserangbelakang.png");
                    playerLeft = AssetLoader.loadImage("weaponserang/spearserangkiri.png");
                    playerRight = AssetLoader.loadImage("weaponserang/spearserangkanan.png");
                } else {
                    playerFront = AssetLoader.loadImage("weapon/spear.png");
                    playerBack = AssetLoader.loadImage("weapon/spearatas.png");
                    playerLeft = AssetLoader.loadImage("weapon/spearkiri.png");
                    playerRight = AssetLoader.loadImage("weapon/spearkanan.png");
                }
            }

            case BOW -> {

                if (attacking) {
                    playerFront = AssetLoader.loadImage("weaponserang/bowserang.png");
                    playerBack = AssetLoader.loadImage("weaponserang/bowserangbelakang.png");
                    playerLeft = AssetLoader.loadImage("weaponserang/bowserangkiri.png");
                    playerRight = AssetLoader.loadImage("weaponserang/bowserangkanan.png");
                } else {
                    playerFront = AssetLoader.loadImage("weapon/bow.png");
                    playerBack = AssetLoader.loadImage("weapon/bowbelakang.png");
                    playerLeft = AssetLoader.loadImage("weapon/bowkiri.png");
                    playerRight = AssetLoader.loadImage("weapon/bowkanan.png");
                }
            }
        }
    }
    public void draw(Graphics2D g2) {

        BufferedImage currentSprite = null;

        switch (facingDirection) {

            case 0:
                currentSprite = playerBack;
                break;

            case 1:
                currentSprite = playerFront;
                break;

            case 2:
                currentSprite = playerLeft;
                break;

            case 3:
                currentSprite = playerRight;
                break;
        }

        if (currentSprite != null) {
            g2.drawImage(currentSprite, x - 16, y - 32, 64, 104, null);
        } else {
            g2.setColor(new java.awt.Color(50, 150, 250));
            g2.fillRect(x, y, 32, 32);
        }

        weaponManager.draw(g2);
    }

    public void takeDamage(int damage) {
        if (!invincible) {

            int actualDamage =
                    Math.max(1, damage - stats.getDefense());

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public LevelSystem getLevelSystem() {
        return levelSystem;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public int getFacingDirection() {
        return facingDirection;
    }
}