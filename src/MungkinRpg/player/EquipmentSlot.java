package MungkinRpg.player;

import MungkinRpg.weapon.Weapon;

public class EquipmentSlot {
    private Weapon equipped;
    private boolean locked;

    public EquipmentSlot() {
        this.equipped = null;
        this.locked = false;
    }

    public void equip(Weapon weapon) {
        if (!locked) {
            this.equipped = weapon;
        }
    }

    public void unequip() {
        if (!locked) {
            this.equipped = null;
        }
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() { return locked; }
    public Weapon getEquipped() { return equipped; }
    public boolean hasWeapon() { return equipped != null; }
}