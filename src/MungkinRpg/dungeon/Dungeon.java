package MungkinRpg.dungeon;

import com.MungkinRpg.enemy.Enemy;
import com.MungkinRpg.player.Player;
import com.MungkinRpg.weapon.Weapon;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Dungeon {
    protected String name;
    protected int dungeonId;
    protected boolean cleared;
    protected List<Enemy> enemies;
    protected Player player;

    public Dungeon(String name, int dungeonId, Player player) {
        this.name = name;
        this.dungeonId = dungeonId;
        this.player = player;
        this.enemies = new ArrayList<>();
        this.cleared = false;
    }

    public abstract void init();
    public abstract void update();
    public abstract void draw(Graphics2D g2);
    public abstract boolean isComplete();

    public void clearDungeon() {
        this.cleared = true;
        System.out.println(name + " CLEARED!");
    }

    public boolean isCleared() { return cleared; }
    public String getName() { return name; }
    public int getDungeonId() { return dungeonId; }
    // Tambahkan di method update() dungeon:
    private void checkSkillHits() {
        if (!player.getWeaponManager().hasWeapon()) return;

        Weapon weapon = player.getWeaponManager().getEquippedWeapon();
        for (Skill skill : weapon.getSkills()) {
            if (skill != null && skill.isActive()) {
                Rectangle hitbox = skill.getHitbox();
                if (hitbox == null) continue;

                for (Enemy e : enemies) {
                    if (!e.isDead() && hitbox.intersects(e.getHitbox())) {
                        // Hindari double hit berkali-kali dalam 1 aktivasi
                        // (Bisa ditambahkan flag "hasHit" per enemy per skill activation)
                        e.takeDamage(skill.getDamage() + player.getStats().getAttack());

                        // Knockback sederhana
                        // e.applyKnockback(dirX * 10, dirY * 10);
                    }
                }
            }
        }
    }
}