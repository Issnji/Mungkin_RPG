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

        if (level == 2){
            expToNextLevel = 200;
        }

        else if (level == 3) {
            expToNextLevel = 300;
        }


        // Naikkan stat
        player.getStats().increaseMaxHp(20);
        player.getStats().increaseAttack(5);
        player.getStats().increaseDefense(2);
        player.heal(999); // Full heal saat level up

        System.out.println("LEVEL UP! Sekarang level " + level);
    }

    // Bonus setelah menyelesaikan dungeon (default)
    public void addDungeonClearBonus() {
        gainExp(100);
        player.addGold(100);
    }

    // Bonus setelah menyelesaikan dungeon (exp custom)
    public void addDungeonClearBonus(int expReward, int goldReward){
        gainExp(expReward);
        player.addGold(goldReward);
    }

    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getExpToNextLevel() { return expToNextLevel; }
}