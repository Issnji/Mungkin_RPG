package MungkinRpg.weapon;

import com.MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;

public abstract class Weapon {
    protected String name;
    protected int baseDamage;
    protected Skill[] skills;
    protected int cooldown;
    protected int currentCooldown;

    public Weapon(String name, int baseDamage) {
        this.name = name;
        this.baseDamage = baseDamage;
        this.skills = new Skill[3];
        this.cooldown = 0;
    }

    public abstract void attack(int playerX, int playerY, int direction);

    public void update() {
        if (currentCooldown > 0) currentCooldown--;
        for (Skill skill : skills) {
            if (skill != null) skill.update();
        }
    }

    public void draw(Graphics2D g2, int playerX, int playerY) {
        for (Skill skill : skills) {
            if (skill != null && skill.isActive()) {
                skill.draw(g2);
            }
        }
    }

    public void useSkill(int index, int playerX, int playerY, int direction) {
        if (index >= 0 && index < skills.length && skills[index] != null) {
            skills[index].activate(playerX, playerY, direction);
        }
    }

    public String getName() { return name; }
    public int getBaseDamage() { return baseDamage; }
    public Skill[] getSkills() { return skills; }
}