package MungkinRpg.weapon;

import com.MungkinRpg.weapon.skills.spear.ThrustSkill;
import com.MungkinRpg.weapon.skills.spear.SpearThrow;
import com.MungkinRpg.weapon.skills.spear.ChargeStrike;

public class Spear extends Weapon {

    public Spear() {
        super("Iron Spear", 22);
        this.skills[0] = new ThrustSkill();
        this.skills[1] = new SpearThrow();
        this.skills[2] = new ChargeStrike();
    }

    @Override
    public void attack(int playerX, int playerY, int direction) {
        System.out.println("Spear thrust!");
        // Logic thrust dengan range sedikit lebih jauh dari sword
    }
}