package MungkinRpg.weapon;

import MungkinRpg.player.Player;
import MungkinRpg.core.InputHandler;
import MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class WeaponManager {
    private Player  player;
    private Weapon  equippedWeapon;
    private boolean attackCooldown;
    private int     attackTimer;
    // FIX: turunkan dari 30 → 15 agar bisa menyerang lebih cepat
    private int     attackCooldownDuration = 15;

    private Rectangle basicAttackBox;
    private boolean   basicAttackActive;
    private int       basicAttackDuration;

    public WeaponManager(Player player) {
        this.player = player;
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println("Equipped: " + weapon.getName());
    }

    public void unequip() {
        equippedWeapon    = null;
        basicAttackActive = false;
        basicAttackBox    = null;
    }

    public void update(InputHandler input) {
        if (attackCooldown) {
            if (++attackTimer >= attackCooldownDuration) {
                attackCooldown = false;
                attackTimer    = 0;
            }
        }

        if (basicAttackActive) {
            if (--basicAttackDuration <= 0) {
                basicAttackActive = false;
                basicAttackBox    = null;
            } else {
                updateBasicAttackPosition();
            }
        }

        if (equippedWeapon != null) {
            equippedWeapon.update();

            if (input.attack && !attackCooldown) {
                performBasicAttack();
                input.attack = false;
            }
            if (input.skill1) { useSkill(0); input.skill1 = false; }
            if (input.skill2) { useSkill(1); input.skill2 = false; }
            if (input.skill3) { useSkill(2); input.skill3 = false; }
        }
    }

    private void performBasicAttack() {
        if (equippedWeapon == null) return;
        attackCooldown        = true;
        attackTimer           = 0;
        basicAttackActive     = true;
        basicAttackDuration   = 10;
        equippedWeapon.attack(player.getX(), player.getY(), player.getFacingDirection());
        createBasicAttackHitbox();
    }

    private void createBasicAttackHitbox() {
        int x = player.getX(), y = player.getY();
        int dir = player.getFacingDirection();
        int range = getBasicAttackRange();
        int bx = x, by = y, bw = 32, bh = 32;
        switch (dir) {
            case 0 -> { bx=x-10; by=y-range; bw=52; bh=range; }
            case 1 -> { bx=x-10; by=y+32;    bw=52; bh=range; }
            case 2 -> { bx=x-range; by=y-10; bw=range; bh=52; }
            case 3 -> { bx=x+32;   by=y-10;  bw=range; bh=52; }
        }
        basicAttackBox = new Rectangle(bx, by, bw, bh);
    }

    private void updateBasicAttackPosition() {
        if (basicAttackBox != null && basicAttackActive) createBasicAttackHitbox();
    }

    private int getBasicAttackRange() {
        if (equippedWeapon instanceof Bow)   return 150; // FIX: naikkan range bow
        if (equippedWeapon instanceof Spear) return 60;
        return 50; // Sword
    }

    private void useSkill(int index) {
        if (equippedWeapon != null)
            equippedWeapon.useSkill(index, player.getX(), player.getY(),
                    player.getFacingDirection());
    }

    public void draw(Graphics2D g2) {
        if (equippedWeapon != null)
            equippedWeapon.draw(g2, player.getX(), player.getY());
        if (basicAttackActive && basicAttackBox != null) {
            g2.setColor(new java.awt.Color(255, 255, 255, 100));
            g2.fill(basicAttackBox);
        }
    }

    public Weapon    getEquippedWeapon()       { return equippedWeapon; }
    public boolean   hasWeapon()               { return equippedWeapon != null; }
    public boolean   isBasicAttackActive()     { return basicAttackActive; }
    public Rectangle getBasicAttackHitbox()    { return basicAttackBox; }

    public java.util.List<Rectangle> getAllActiveHitboxes() {
        java.util.List<Rectangle> list = new java.util.ArrayList<>();
        if (basicAttackActive && basicAttackBox != null) list.add(basicAttackBox);
        if (equippedWeapon != null) {
            for (Skill s : equippedWeapon.getSkills()) {
                if (s != null && s.isActive()) {
                    Rectangle sb = s.getHitbox();
                    if (sb != null) list.add(sb);
                }
            }
        }
        return list;
    }
}