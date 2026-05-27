package MungkinRpg.dungeon.dungeon2;

import MungkinRpg.dungeon.Dungeon;
import MungkinRpg.enemy.Enemy;
import MungkinRpg.enemy.Goblin;
import MungkinRpg.enemy.Skeleton;
import MungkinRpg.player.Player;
import MungkinRpg.util.AssetLoader;
import MungkinRpg.util.Constants;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DungeonTwo extends Dungeon {
    private int enemiesToKill = 8, enemiesKilled = 0, spawnTimer = 0;
    private BufferedImage bgImage; // Menyimpan background PNG

    public DungeonTwo(Player player) {
        super("Goblin Camp", 2, player);
        // Memuat background dari AssetLoader
        bgImage = AssetLoader.loadImage("dungeon2.png");
    }

    @Override
    public void init() {
        enemies.clear();
        enemies.add(new Skeleton(150, 150));
        enemies.add(new Skeleton(600, 400));
        enemies.add(new Goblin(400, 200));
        enemiesKilled = 0;
        spawnTimer = 0;
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
            e.setTarget(player.getX(), player.getY());
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
        // Render background PNG memenuhi layar
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        }

        for (Enemy e : enemies) e.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Enemies: " + enemiesKilled + "/" + enemiesToKill, 20, 140);
    }

    @Override
    public boolean isComplete() {
        return enemiesKilled >= enemiesToKill;
    }
}