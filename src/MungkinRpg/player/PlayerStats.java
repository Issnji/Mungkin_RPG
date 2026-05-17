package MungkinRpg.player;

public class PlayerStats {
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;
    private int speed;

    public PlayerStats() {
        // Basic stat awal tanpa equipment
        this.maxHp = 100;
        this.currentHp = 100;
        this.attack = 10;
        this.defense = 5;
        this.speed = 4;
    }

    public void reduceHp(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void increaseMaxHp(int amount) { maxHp += amount; }
    public void increaseAttack(int amount) { attack += amount; }
    public void increaseDefense(int amount) { defense += amount; }

    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
}