package MungkinRpg.dungeon;

import MungkinRpg.enemy.Enemy;
import MungkinRpg.player.Player;
import MungkinRpg.weapon.Weapon;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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

    /**
     * FIX: replaces the broken checkSkillHits().
     * Uses WeaponManager.getAllActiveHitboxes() which covers both
     * basic attacks and skill hitboxes. Call this from every subclass update().
     */
    protected void checkPlayerAttacks() {
        if (!player.getWeaponManager().hasWeapon()) return;

        List<Rectangle> hitboxes = player.getWeaponManager().getAllActiveHitboxes();
        if (hitboxes.isEmpty()) return;

        Weapon weapon = player.getWeaponManager().getEquippedWeapon();
        int totalDamage = player.getStats().getAttack() + weapon.getBaseDamage();

        for (Rectangle attackBox : hitboxes) {
            for (Enemy e : enemies) {
                if (!e.isDead() && attackBox.intersects(e.getHitbox())) {
                    e.takeDamage(totalDamage);
                }
            }
        }
    }

    public void clearDungeon() {
        this.cleared = true;
        System.out.println(name + " CLEARED!");
    }

    public boolean isCleared()  { return cleared; }
    public String getName()     { return name; }
    public int getDungeonId()   { return dungeonId; }
}