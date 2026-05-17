package MungkinRpg.ui;

import com.MungkinRpg.dungeon.DungeonManager;
import com.MungkinRpg.core.InputHandler;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class DungeonSelectUI {
    private DungeonManager dungeonManager;
    private int selected;
    private boolean active;

    public DungeonSelectUI(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
        this.selected = 0;
        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.selected = 0;
    }

    public boolean isActive() { return active; }

    public void update(InputHandler input) {
        if (!active) return;

        if (input.up) {
            selected = Math.max(0, selected - 1);
            input.up = false;
        }
        if (input.down) {
            selected = Math.min(2, selected + 1);
            input.down = false;
        }

        if (input.interact) {
            dungeonManager.startDungeon(selected + 1);
            active = false;
            input.interact = false;
        }

        if (input.escape) {
            active = false;
            input.escape = false;
        }
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, 800, 600);

        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.setColor(Color.WHITE);
        g2.drawString("SELECT DUNGEON", 250, 150);

        String[] names = {"1. Forest Ruins", "2. Cursed Cave", "3. Boss Chamber"};
        boolean[] unlocked = {
                true,
                dungeonManager.isDungeonCleared(1),
                dungeonManager.isDungeonCleared(2)
        };

        g2.setFont(new Font("Arial", Font.PLAIN, 28));
        for (int i = 0; i < names.length; i++) {
            int y = 250 + (i * 60);

            if (i == selected) {
                g2.setColor(Color.YELLOW);
                g2.fillRect(240, y - 30, 320, 40);
            }

            if (unlocked[i]) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(Color.GRAY);
            }

            String status = "";
            if (dungeonManager.isDungeonCleared(i + 1)) status = " [CLEARED]";
            else if (!unlocked[i]) status = " [LOCKED]";

            g2.drawString(names[i] + status, 260, y);
        }

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.drawString("E to Enter | ESC to Cancel", 280, 480);
    }
}