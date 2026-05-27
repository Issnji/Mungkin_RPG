package MungkinRpg.dungeon.dungeon3;

import MungkinRpg.dungeon.Dungeon;
import MungkinRpg.enemy.boss.FinalBoss;
import MungkinRpg.player.Player;
import MungkinRpg.util.TileManager;
import java.awt.*;

public class DungeonBoss extends Dungeon {
    private FinalBoss boss;
    private TileManager tileManager;

    private static final int FL = TileManager.FLOOR_DUNGEON;
    private static final int WL = TileManager.WALL_STONE;

    private static final int[][] MAP = {
            {WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,FL,WL},
            {WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL},
    };

    public DungeonBoss(Player player) {
        super("Boss Chamber", 3, player);
        tileManager = new TileManager();
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
        boss.setTarget(player.getX(), player.getY()); // FIX
        boss.update();
        if (boss.getHitbox().intersects(player.getHitbox()))
            player.takeDamage(boss.getDamage());
    }

    @Override
    public void draw(Graphics2D g2) {
        tileManager.drawMap(g2, MAP, 32);
        // Glow merah di tengah arena
        Graphics2D g2c = (Graphics2D) g2.create();
        g2c.setColor(new Color(150, 0, 50, 30));
        g2c.fillOval(200, 100, 400, 400);
        g2c.dispose();
        boss.draw(g2);
        g2.setColor(new Color(220, 50, 50));
        g2.setFont(new Font("Serif", Font.BOLD, 16));
        g2.drawString("BOSS CHAMBER", 320, 25);
    }

    @Override
    public boolean isComplete() { return boss.isDead(); }
}