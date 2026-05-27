package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Enemy {

    protected int x, y;
    protected int hp, maxHp;
    protected int damage, speed;
    protected Rectangle hitbox;
    protected boolean dead;
    protected int expReward, goldReward;

    protected int targetX = -1, targetY = -1;
    protected int hitCooldown = 0;

    public Enemy(int x, int y, int hp, int damage, int speed) {

        this.x = x;
        this.y = y;

        this.maxHp = hp;
        this.hp = hp;

        this.damage = damage;
        this.speed = speed;

        this.hitbox = new Rectangle(x, y, 32, 32);
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2);

    public void setTarget(int px, int py) {
        this.targetX = px;
        this.targetY = py;
    }

    protected void updateHitCooldown() {
        if (hitCooldown > 0) {
            hitCooldown--;
        }
    }

    public void takeDamage(int amount) {

        if (hitCooldown > 0) return;

        hp -= amount;
        hitCooldown = 10;

        if (hp <= 0) {
            dead = true;
        }
    }

    // ======================
    // GETTER & SETTER
    // ======================

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isDead() {
        return dead;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getDamage() {
        return damage;
    }

    public int getExpReward() {
        return expReward;
    }

    public int getGoldReward() {
        return goldReward;
    }
}