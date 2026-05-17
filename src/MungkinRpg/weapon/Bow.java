package MungkinRpg.weapon;

import com.MungkinRpg.weapon.skills.bow.TripleArrow;
import com.MungkinRpg.weapon.skills.bow.RainArrow;
import com.MungkinRpg.weapon.skills.bow.PiercingShot;

public class Bow extends Weapon {

    public Bow() {
        super("Wooden Bow", 20);
        this.skills[0] = new TripleArrow();
        this.skills[1] = new RainArrow();
        this.skills[2] = new PiercingShot();
    }

    @Override
    public void attack(int playerX, int playerY, int direction) {
        System.out.println("Bow basic shot!");
        // Logic panah single shot
    }
}