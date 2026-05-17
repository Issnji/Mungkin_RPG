package MungkinRpg.player;

public class LevelSystem {
    private Player player;
    private int level;
    private int exp;
    private int expToNextLevel;

    public LevelSystem(Player player) {
        this.player = player;
        this.level = 1;
        this.exp = 0;
        this.expToNextLevel = 100;
    }

    public void gainExp(int amount) {
        exp += amount;
        while (exp >= expToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        exp -= expToNextLevel;
        level++;
        expToNextLevel = (int)(expToNextLevel * 1.5);

        // Naikkan stat
        player.getStats().increaseMaxHp(20);
        player.getStats().increaseAttack(5);
        player.getStats().increaseDefense(2);
        player.heal(999); // Full heal saat level up

        System.out.println("LEVEL UP! Sekarang level " + level);
    }

    public void addDungeonClearBonus() {
        // Bonus besar saat menyelesaikan dungeon
        gainExp(expToNextLevel); // Langsung level up
        player.addGold(100 * level);
    }

    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getExpToNextLevel() { return expToNextLevel; }
}