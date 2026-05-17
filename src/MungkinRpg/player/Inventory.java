package MungkinRpg.player;

public class Inventory {
    private int gold;

    public Inventory() {
        this.gold = 50; // Gold awal untuk beli senjata pertama
    }

    public void addGold(int amount) { gold += amount; }
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }
    public int getGold() { return gold; }
}