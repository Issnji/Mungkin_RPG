package MungkinRpg.weapon;

import  MungkinRpg.weapon.skills.sword.SlashSkill;
import  MungkinRpg.weapon.skills.sword.SpinAttack;
import  MungkinRpg.weapon.skills.sword.DashSlash;

public class Sword extends Weapon {

    public Sword() {
        super("Iron Sword", 25);
        this.skills[0] = new SlashSkill();
        this.skills[1] = new SpinAttack();
        this.skills[2] = new DashSlash();
    }

    @Override
    public void attack(int playerX, int playerY, int direction) {
        System.out.println("Sword basic attack!");
        // Logic serangan basic (close range)
    }
}