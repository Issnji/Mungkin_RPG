package MungkinRpg.dungeon.dungeon3;

import MungkinRpg.dungeon.Dungeon;
import MungkinRpg.enemy.boss.FinalBoss;
import MungkinRpg.player.Player;
import MungkinRpg.util.AssetLoader;
import MungkinRpg.util.Constants;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DungeonBoss extends Dungeon {
    private FinalBoss boss;
    private BufferedImage bgImage; // Menyimpan background PNG

    public DungeonBoss(Player player) {
        super("Boss Chamber", 3, player);
        // Memuat background dari AssetLoader
        bgImage = AssetLoader.loadImage("dungeon3.png");
    }

    @Override
    public void init() {
        enemies.clear();
        boss = new FinalBoss(368, 100);
        enemies.add(boss);
    }

    @Override
    public void update() {
        checkPlayerAttacks();
        boss.setTarget(player.getX(), player.getY());
        boss.update();
        if (boss.getHitbox().intersects(player.getHitbox())) {
            player.takeDamage(boss.getDamage());
        }
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

        if (boss != null && !boss.isDead()) {
            boss.draw(g2);
        }
    }

    @Override
    public boolean isComplete() {
        return boss != null && boss.isDead();
    }
}