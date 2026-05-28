package MungkinRpg.dungeon.dungeon1;

import MungkinRpg.dungeon.Dungeon;
import MungkinRpg.enemy.Enemy;
import MungkinRpg.enemy.Slime;
import MungkinRpg.player.Player;
import MungkinRpg.util.AssetLoader;
import MungkinRpg.util.Constants;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DungeonOne extends Dungeon {
    private int enemiesToKill, enemiesKilled;
    private BufferedImage bgImage; // Menyimpan background PNG
    private boolean rewardGiven = false;

    public DungeonOne(Player player) {
        super("Slime Cave", 1, player);
        // Memuat background dari AssetLoader
        bgImage = AssetLoader.loadImage("dungeon1.png");
    }

    @Override
    public void init() {
        enemies.clear();
        enemies.add(new Slime(200, 250));
        enemies.add(new Slime(580, 180));
        enemies.add(new Slime(380, 420));
        enemiesToKill = enemies.size();
        enemiesKilled = 0;
    }

    @Override
    public void update() {
        checkPlayerAttacks();

        for (Enemy e : enemies) {
            e.setTarget(player.getX(), player.getY());
            e.update();
            if (e.getHitbox().intersects(player.getHitbox()))
                player.takeDamage(e.getDamage());
        }

        enemies.removeIf(e -> {
            if (e.isDead()) {
                enemiesKilled++;
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

        // Render musuh
        for (Enemy e : enemies) e.draw(g2);

        // Render counter musuh
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Enemies: " + enemiesKilled + "/" + enemiesToKill, 20, 140);
    }

    @Override
    public boolean isComplete() {
        if (enemiesKilled >= enemiesToKill && !rewardGiven){
            // bonus clear dungeon
            player.getLevelSystem().addDungeonClearBonus();

            rewardGiven = true;
        }

        return enemiesKilled >= enemiesToKill;
    }
}