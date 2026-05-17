package MungkinRpg.save;

import java.io.Serializable;

public class PlayerData implements Serializable {
    public int level;
    public int exp;
    public int gold;
    public int maxHp;
    public int attack;
    public int defense;
    public boolean dungeon1Cleared;
    public boolean dungeon2Cleared;
    public String equippedWeapon;
}