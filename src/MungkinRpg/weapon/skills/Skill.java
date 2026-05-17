package MungkinRpg.weapon.skills;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public abstract class Skill {
    protected String name;
    protected int damage;
    protected int cooldown;
    protected int currentCooldown;
    protected boolean active;
    protected int duration;
    protected int currentDuration;

    // Posisi & arah saat skill diaktifkan
    protected int startX, startY;
    protected int dirX, dirY; // arah: -1, 0, 1

    public Skill(String name, int damage, int cooldown, int duration) {
        this.name = name;
        this.damage = damage;
        this.cooldown = cooldown;
        this.duration = duration;
        this.active = false;
        this.currentCooldown = 0;
    }

    public void activate(int x, int y, int direction) {
        if (currentCooldown <= 0 && !active) {
            this.active = true;
            this.currentDuration = duration;
            this.currentCooldown = cooldown;
            this.startX = x;
            this.startY = y;

            // Konversi direction ke dx/dy
            // 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
            switch (direction) {
                case 0 -> { dirX = 0; dirY = -1; }
                case 1 -> { dirX = 0; dirY = 1; }
                case 2 -> { dirX = -1; dirY = 0; }
                case 3 -> { dirX = 1; dirY = 0; }
                default -> { dirX = 1; dirY = 0; }
            }

            onActivate(x, y, direction);
        }
    }

    public abstract void onActivate(int x, int y, int direction);

    public void update() {
        if (currentCooldown > 0) currentCooldown--;
        if (active) {
            currentDuration--;
            onUpdate();
            if (currentDuration <= 0) {
                active = false;
                onEnd();
            }
        }
    }

    public abstract void onUpdate();
    public abstract void onEnd();
    public abstract void draw(Graphics2D g2);

    /** Mengembalikan hitbox serangan saat ini. Dungeon akan cek ini untuk hit enemy. */
    public abstract Rectangle getHitbox();

    public boolean isActive() { return active; }
    public boolean isReady() { return currentCooldown <= 0; }
    public String getName() { return name; }
    public int getCurrentCooldown() { return currentCooldown; }
    public int getDamage() { return damage; }
}