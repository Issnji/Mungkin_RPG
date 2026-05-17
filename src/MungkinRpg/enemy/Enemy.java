package MungkinRpg.enemy;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Enemy {
    protected int x, y;
    protected int hp;
    protected int maxHp;
    protected int damage;
    protected int speed;
    protected Rectangle hitbox;
    protected boolean dead;

    protected int expReward;
    protected int goldReward;

    public Enemy(int x, int y, int hp, int damage, int speed) {
        this.x = x;
        this.y = y;
        this.maxHp = hp;
        this.hp = hp;
        this.damage = damage;
        this.speed = speed;
        this.hitbox = new Rectangle(x, y, 32, 32);
        this.dead = false;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2);

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) dead = true;
    }

    public boolean isDead() { return dead; }
    public Rectangle getHitbox() { return hitbox; }
    public int getDamage() { return damage; }
    public int getExpReward() { return expReward; }
    public int getGoldReward() { return goldReward; }
}