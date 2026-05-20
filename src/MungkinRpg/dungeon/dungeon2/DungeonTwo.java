package  MungkinRpg.dungeon.dungeon2;

import  MungkinRpg.dungeon.Dungeon;
import  MungkinRpg.enemy.Enemy;
import  MungkinRpg.enemy.Skeleton;
import  MungkinRpg.enemy.Goblin;
import  MungkinRpg.player.Player;
import java.awt.Graphics2D;
import java.awt.Color;

public class DungeonTwo extends Dungeon {
    private int enemiesToKill;
    private int enemiesKilled;
    private int spawnTimer;

    public DungeonTwo(Player player) {
        super("Cursed Cave", 2, player);
    }

    @Override
    public void init() {
        enemies.clear();
        enemiesKilled = 0;
        spawnTimer = 0;
        enemies.add(new Skeleton(150, 150));
        enemies.add(new Skeleton(600, 400));
        enemies.add(new Goblin(400, 200));
        enemiesToKill = 8;
    }

    @Override
    public void update() {
        checkPlayerAttacks(); // FIX: enemies can now take damage from player

        spawnTimer++;
        if (spawnTimer > 180 && enemies.size() < 5 && (enemiesKilled + enemies.size()) < enemiesToKill) {
            enemies.add(new Goblin((int)(Math.random() * 700) + 50, (int)(Math.random() * 500) + 50));
            spawnTimer = 0;
        }

        for (Enemy e : enemies) {
            e.update();
            if (e.getHitbox().intersects(player.getHitbox())) {
                player.takeDamage(e.getDamage());
            }
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
        g2.setColor(new Color(40, 30, 50));
        g2.fillRect(0, 0, 800, 600);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, 800, 50);
        g2.fillRect(0, 550, 800, 50);
        g2.fillRect(0, 0, 50, 600);
        g2.fillRect(750, 0, 50, 600);
        for (Enemy e : enemies) e.draw(g2);
        g2.setColor(Color.WHITE);
        g2.drawString("Enemies: " + enemiesKilled + "/" + enemiesToKill, 20, 580);
    }

    @Override
    public boolean isComplete() {
        return enemiesKilled >= enemiesToKill;
    }
}