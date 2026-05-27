package MungkinRpg.dungeon.dungeon1;

import MungkinRpg.dungeon.Dungeon;
import MungkinRpg.enemy.Enemy;
import MungkinRpg.enemy.Slime;
import MungkinRpg.player.Player;
import MungkinRpg.util.TileManager;
import java.awt.*;

public class DungeonOne extends Dungeon {
    private int enemiesToKill, enemiesKilled;
    private TileManager tileManager;

    // ---- MAP TILEMAP ----
    // 25 kolom × 19 baris  (layar 800×600, tile 32px)
    // PT=PATH_STONE  PD=PATH_STONE_DARK  WL=WALL_STONE  FL=FLOOR_DUNGEON
    private static final int PT = TileManager.PATH_STONE;
    private static final int PD = TileManager.PATH_STONE_DARK;
    private static final int WL = TileManager.WALL_STONE;
    private static final int FL = TileManager.FLOOR_DUNGEON;

    private static final int[][] MAP = {
            {WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL},
            {WL,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,WL},
            {WL,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,WL},
            {WL,PT,PD,WL,WL,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,WL,WL,PT,PD,PT,PD,PT,WL},
            {WL,PD,PT,WL,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,WL,PT,PD,PT,PD,PT,PD,WL},
            {WL,PT,PD,WL,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,WL,PD,PT,PD,PT,PD,PT,WL},
            {WL,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,WL},
            {WL,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,WL},
            {WL,PD,PT,PD,PT,PD,PT,PD,WL,WL,WL,PT,PD,WL,WL,WL,PD,PT,PD,PT,PD,PT,PD,PD,WL},
            {WL,PT,PD,PT,PD,PT,PD,PT,WL,PD,WL,PD,PT,WL,PT,WL,PT,PD,PT,PD,PT,PD,PT,PT,WL},
            {WL,PD,PT,PD,PT,PD,PT,PD,WL,PT,WL,PT,PD,WL,PD,WL,PD,PT,PD,PT,PD,PT,PD,PD,WL},
            {WL,PT,PD,PT,PD,PT,PD,PT,PD,PT,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PT,WL},
            {WL,PD,PT,PD,PT,PD,PT,PD,PT,PD,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PD,WL},
            {WL,PT,PD,PT,PD,WL,WL,PT,PD,PT,PT,PD,PT,PD,PT,PD,PT,WL,WL,PD,PT,PD,PT,PT,WL},
            {WL,PD,PT,PD,PT,WL,PD,PD,PT,PD,PD,PT,PD,PT,PD,PT,PD,WL,PT,PT,PD,PT,PD,PD,WL},
            {WL,PT,PD,PT,PD,WL,WL,PT,PD,PT,PT,PD,PT,PD,PT,PD,PT,WL,WL,PD,PT,PD,PT,PT,WL},
            {WL,PD,PT,PD,PT,PD,PT,PD,PT,PD,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PD,WL},
            {WL,PT,PD,PT,PD,PT,PD,PT,PD,PT,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PD,PT,PT,WL},
            {WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL,WL},
    };

    public DungeonOne(Player player) {
        super("Forest Ruins", 1, player);
        tileManager = new TileManager();
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
            e.setTarget(player.getX(), player.getY()); // FIX: beri tahu posisi player
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
        tileManager.drawMap(g2, MAP, 32); // FIX: gambar tilemap dulu
        for (Enemy e : enemies) e.draw(g2);
        // Counter
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Enemies: " + enemiesKilled + "/" + enemiesToKill, 20, 580);
        // Nama dungeon
        g2.setColor(new Color(255, 200, 80));
        g2.drawString("Forest Ruins", 350, 25);
    }

    @Override
    public boolean isComplete() { return enemiesKilled >= enemiesToKill; }
}