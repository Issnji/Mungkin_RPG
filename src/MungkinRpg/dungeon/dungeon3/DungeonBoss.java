package MungkinRpg.dungeon.dungeon3;

import com.MungkinRpg.dungeon.Dungeon;
import com.MungkinRpg.enemy.boss.FinalBoss;
import com.MungkinRpg.player.Player;
import java.awt.Graphics2D;
import java.awt.Color;

public class DungeonBoss extends Dungeon {
    private FinalBoss boss;

    public DungeonBoss(Player player) {
        super("Boss Chamber", 3, player);
    }

    @Override
    public void init() {
        enemies.clear();
        boss = new FinalBoss(400, 200); // Boss dengan stat masif
        enemies.add(boss);
    }

    @Override
    public void update() {
        boss.update();
        if (boss.getHitbox().intersects(player.getHitbox())) {
            player.takeDamage(boss.getDamage());
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 800, 600);
        boss.draw(g2);
    }

    @Override
    public boolean isComplete() {
        return boss.isDead(); // Hampir mustahil tanpa grind level
    }
}