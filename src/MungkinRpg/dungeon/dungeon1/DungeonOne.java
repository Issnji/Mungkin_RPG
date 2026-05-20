package  MungkinRpg.dungeon.dungeon1;

import  MungkinRpg.dungeon.Dungeon;
import  MungkinRpg.enemy.Enemy;   // FIX: was missing
import  MungkinRpg.enemy.Slime;
import  MungkinRpg.player.Player;
import java.awt.Graphics2D;
import java.awt.Color;

public class DungeonOne extends Dungeon {
    private int enemiesToKill;
    private int enemiesKilled;

    public DungeonOne(Player player) {
        super("Forest Ruins", 1, player);
    }

    @Override
    public void init() {
        enemies.clear();
        enemies.add(new Slime(200, 200));
        enemies.add(new Slime(400, 300));
        enemies.add(new Slime(600, 200));
        enemiesToKill = enemies.size();
        enemiesKilled = 0;
    }

    @Override
    public void update() {
        checkPlayerAttacks(); // FIX: enemies can now take damage from player

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
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, 800, 600);
        for (Enemy e : enemies) e.draw(g2);
        g2.setColor(Color.WHITE);
        g2.drawString("Enemies: " + enemiesKilled + "/" + enemiesToKill, 20, 580);
    }

    @Override
    public boolean isComplete() {
        return enemiesKilled >= enemiesToKill;
    }
}