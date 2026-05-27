package MungkinRpg.dungeon.dungeon2;

import MungkinRpg.dungeon.Dungeon;
import MungkinRpg.enemy.Enemy;
import MungkinRpg.enemy.Skeleton;
import MungkinRpg.enemy.Goblin;
import MungkinRpg.player.Player;
import MungkinRpg.util.TileManager;
import java.awt.*;

public class DungeonTwo extends Dungeon {
    private int enemiesToKill = 8, enemiesKilled = 0, spawnTimer = 0;
    private TileManager tileManager;

    private static final int FL = TileManager.FLOOR_DUNGEON;
    private static final int WL = TileManager.WALL_STONE;
    private static final int DT = TileManager.DIRT;

    private static final int[][] MAP = {
            {WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,WL,WL,WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL,WL,WL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,WL,FL,WL,FL,FL,FL,DT,DT,DT,DT,DT,FL,FL,WL,FL,WL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,WL,FL,WL,FL,FL,FL,DT,FL,FL,FL,DT,FL,FL,WL,FL,WL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,DT,FL,FL,FL,DT,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,DT,DT,DT,DT,DT,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,WL,WL,FL,FL,FL,FL,FL,FL,FL,WL,WL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,WL,WL,FL,FL,FL,FL,FL,FL,FL,WL,WL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,DT,DT,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,DT,DT,FL,FL,FL,FL,WL},
            {WL,FL,FL,DT,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,DT,FL,FL,FL,FL,WL},
            {WL,FL,FL,DT,DT,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,DT,DT,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL},
    };

    public DungeonTwo(Player player) {
        super("Cursed Cave", 2, player);
        tileManager = new TileManager();
    }

    @Override
    public void init() {
        enemies.clear();
        enemiesKilled = 0; spawnTimer = 0;
        enemies.add(new Skeleton(150, 150));
        enemies.add(new Skeleton(600, 400));
        enemies.add(new Goblin(400, 200));
    }

    @Override
    public void update() {
        checkPlayerAttacks();
        spawnTimer++;
        if (spawnTimer > 180 && enemies.size() < 5 && (enemiesKilled + enemies.size()) < enemiesToKill) {
            enemies.add(new Goblin((int)(Math.random()*600)+80, (int)(Math.random()*400)+80));
            spawnTimer = 0;
        }

        for (Enemy e : enemies) {
            e.setTarget(player.getX(), player.getY()); // FIX
            e.update();
            if (e.getHitbox().intersects(player.getHitbox()))
                player.takeDamage(e.getDamage());
        }

        enemies.removeIf(e -> {
            if (e.isDead()) {
                enemiesKilled++;
                player.getLevelSystem().gainExp(e.getExpReward());
                player.addGold(e.getGoldReward());
                return true;
            }
            return false;
        });
    }

    @Override
    public void draw(Graphics2D g2) {
        tileManager.drawMap(g2, MAP, 32);
        for (Enemy e : enemies) e.draw(g2);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Enemies: " + enemiesKilled + "/" + enemiesToKill, 20, 580);
        g2.setColor(new Color(150, 100, 255));
        g2.drawString("Cursed Cave", 350, 25);
    }

    @Override
    public boolean isComplete() { return enemiesKilled >= enemiesToKill; }
}