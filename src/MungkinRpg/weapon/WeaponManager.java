package MungkinRpg.weapon;

import com.MungkinRpg.player.Player;
import com.MungkinRpg.core.InputHandler;
import com.MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class WeaponManager {
    private Player player;
    private Weapon equippedWeapon;
    private boolean attackCooldown;
    private int attackTimer;
    private int attackCooldownDuration = 30; // 0.5 detik

    // Basic attack hitbox (untuk melee/range basic)
    private Rectangle basicAttackBox;
    private boolean basicAttackActive;
    private int basicAttackDuration;

    public WeaponManager(Player player) {
        this.player = player;
        this.equippedWeapon = null;
        this.attackCooldown = false;
        this.attackTimer = 0;
        this.basicAttackActive = false;
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println("Equipped: " + weapon.getName());
    }

    public void unequip() {
        this.equippedWeapon = null;
        this.basicAttackActive = false;
        this.basicAttackBox = null;
    }

    public void update(InputHandler input) {
        // Handle basic attack cooldown
        if (attackCooldown) {
            attackTimer++;
            if (attackTimer >= attackCooldownDuration) {
                attackCooldown = false;
                attackTimer = 0;
            }
        }

        // Handle basic attack duration (hitbox aktif sebentar)
        if (basicAttackActive) {
            basicAttackDuration--;
            if (basicAttackDuration <= 0) {
                basicAttackActive = false;
                basicAttackBox = null;
            } else {
                updateBasicAttackPosition();
            }
        }

        // Weapon & skill update
        if (equippedWeapon != null) {
            equippedWeapon.update();

            // Basic Attack (J key)
            if (input.attack && !attackCooldown) {
                performBasicAttack();
                input.attack = false; // Reset supaya tidak spam
            }

            // Skill 1 (K key)
            if (input.skill1) {
                useSkill(0);
                input.skill1 = false;
            }

            // Skill 2 (L key)
            if (input.skill2) {
                useSkill(1);
                input.skill2 = false;
            }

            // Skill 3 (; key)
            if (input.skill3) {
                useSkill(2);
                input.skill3 = false;
            }
        }
    }

    private void performBasicAttack() {
        if (equippedWeapon == null) return;

        attackCooldown = true;
        attackTimer = 0;
        basicAttackActive = true;
        basicAttackDuration = 10; // Hitbox aktif 10 frame

        // Panggil animasi/efek weapon
        equippedWeapon.attack(player.getX(), player.getY(), player.getFacingDirection());

        // Buat hitbox sesuai jenis weapon dan arah
        createBasicAttackHitbox();
    }

    private void createBasicAttackHitbox() {
        int x = player.getX();
        int y = player.getY();
        int dir = player.getFacingDirection();
        int range = getBasicAttackRange();

        int bx = x, by = y, bw = 32, bh = 32;

        switch (dir) {
            case 0 -> { // UP
                bx = x - 10;
                by = y - range;
                bw = 52;
                bh = range;
            }
            case 1 -> { // DOWN
                bx = x - 10;
                by = y + 32;
                bw = 52;
                bh = range;
            }
            case 2 -> { // LEFT
                bx = x - range;
                by = y - 10;
                bw = range;
                bh = 52;
            }
            case 3 -> { // RIGHT
                bx = x + 32;
                by = y - 10;
                bw = range;
                bh = 52;
            }
        }

        basicAttackBox = new Rectangle(bx, by, bw, bh);
    }

    private void updateBasicAttackPosition() {
        // Update posisi hitbox mengikuti player (untuk melee yang bergerak saat attack)
        if (basicAttackBox != null && basicAttackActive) {
            createBasicAttackHitbox();
        }
    }

    private int getBasicAttackRange() {
        if (equippedWeapon instanceof Bow) return 120;
        if (equippedWeapon instanceof Spear) return 50;
        return 40; // Sword default
    }

    private void useSkill(int index) {
        if (equippedWeapon == null) return;
        equippedWeapon.useSkill(index, player.getX(), player.getY(), player.getFacingDirection());
    }

    public void draw(Graphics2D g2) {
        if (equippedWeapon != null) {
            equippedWeapon.draw(g2, player.getX(), player.getY());
        }

        // Debug/Visual basic attack (hapus jika sudah ada sprite)
        if (basicAttackActive && basicAttackBox != null) {
            g2.setColor(new java.awt.Color(255, 255, 255, 100));
            g2.fill(basicAttackBox);
        }
    }

    // Getters
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public boolean hasWeapon() { return equippedWeapon != null; }
    public boolean isBasicAttackActive() { return basicAttackActive; }
    public Rectangle getBasicAttackHitbox() { return basicAttackBox; }

    /** Mengembalikan semua hitbox aktif (basic + skills) untuk dicek dungeon */
    public java.util.List<Rectangle> getAllActiveHitboxes() {
        java.util.List<Rectangle> hitboxes = new java.util.ArrayList<>();

        if (basicAttackActive && basicAttackBox != null) {
            hitboxes.add(basicAttackBox);
        }

        if (equippedWeapon != null) {
            for (Skill skill : equippedWeapon.getSkills()) {
                if (skill != null && skill.isActive()) {
                    Rectangle sb = skill.getHitbox();
                    if (sb != null) {
                        hitboxes.add(sb);
                    }
                }
            }
        }

        return hitboxes;
    }
}