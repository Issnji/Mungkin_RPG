package MungkinRpg.dungeon;

import com.MungkinRpg.core.GamePanel;
import com.MungkinRpg.core.SceneManager;
import com.MungkinRpg.dungeon.dungeon1.DungeonOne;
import com.MungkinRpg.dungeon.dungeon2.DungeonTwo;
import com.MungkinRpg.dungeon.dungeon3.DungeonBoss;
import com.MungkinRpg.ui.DungeonSelectUI;
import com.MungkinRpg.player.Player;
import java.awt.Graphics2D;

public class DungeonManager {
    private GamePanel panel;
    private Player player;
    private SceneManager sceneManager;
    private DungeonSelectUI dungeonSelectUI;

    private DungeonOne dungeon1;
    private DungeonTwo dungeon2;
    private DungeonBoss dungeon3;
    private Dungeon currentDungeon;

    private boolean inLobby; // Sebelum masuk dungeon, pilih senjata dulu
    private int selectedDungeon;

    public DungeonManager(GamePanel panel, Player player, SceneManager sceneManager) {
        this.panel = panel;
        this.player = player;
        this.sceneManager = sceneManager;

        this.dungeon1 = new DungeonOne(player);
        this.dungeon2 = new DungeonTwo(player);
        this.dungeon3 = new DungeonBoss(player);
        this.dungeonSelectUI = new DungeonSelectUI(this);
    }

    public void enterLobby() {
        inLobby = true;
        selectedDungeon = 1;
        dungeonSelectUI.setActive(true);

        // Player bisa ganti senjata di sini
    }

    public void startDungeon(int dungeonId) {
        inLobby = false;
        switch (dungeonId) {
            case 1 -> currentDungeon = dungeon1;
            case 2 -> {
                if (!dungeon1.isCleared()) return; // Harus clear dungeon 1 dulu
                currentDungeon = dungeon2;
            }
            case 3 -> {
                if (!dungeon2.isCleared()) return; // Harus clear dungeon 2 dulu
                currentDungeon = dungeon3;
            }
        }
        currentDungeon.init();
        player.resetPosition();
    }

    public void update() {
        if (inLobby) {
            // UI pemilihan dungeon & ganti senjata
            if (panel.getInputHandler().skill1) startDungeon(1);
            if (panel.getInputHandler().skill2 && dungeon1.isCleared()) startDungeon(2);
            if (panel.getInputHandler().skill3 && dungeon2.isCleared()) startDungeon(3);
            dungeonSelectUI.update(panel.getInputHandler());
        } else {
            currentDungeon.update();
            if (currentDungeon.isComplete()) {
                currentDungeon.clearDungeon();
                player.getLevelSystem().addDungeonClearBonus();
                sceneManager.changeScene(SceneManager.Scene.TOWN);
            }
            if (player.isDead()) {
                sceneManager.changeScene(SceneManager.Scene.GAME_OVER);
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (inLobby) {
            // Draw dungeon selection UI
            g2.drawString("DUNGEON LOBBY", 300, 200);
            g2.drawString("1. Dungeon One " + (dungeon1.isCleared() ? "[CLEARED]" : ""), 300, 250);
            g2.drawString("2. Dungeon Two " + (dungeon2.isCleared() ? "[CLEARED]" : "[LOCKED]"), 300, 280);
            g2.drawString("3. Boss Dungeon " + (dungeon3.isCleared() ? "[CLEARED]" : "[LOCKED]"), 300, 310);
            g2.drawString("Press K/L/; to select", 300, 350);
            dungeonSelectUI.draw(g2);
        } else {
            currentDungeon.draw(g2);
        }
    }

    public boolean isDungeonCleared(int id) {
        return switch (id) {
            case 1 -> dungeon1.isCleared();
            case 2 -> dungeon2.isCleared();
            case 3 -> dungeon3.isCleared();
            default -> false;
        };
    }
}