package MungkinRpg.dungeon;

import MungkinRpg.core.GamePanel;
import MungkinRpg.core.SceneManager;
import MungkinRpg.dungeon.dungeon1.DungeonOne;
import MungkinRpg.dungeon.dungeon2.DungeonTwo;
import MungkinRpg.dungeon.dungeon3.DungeonBoss;
import MungkinRpg.player.Player;
import java.awt.Graphics2D;

public class DungeonManager {
    private GamePanel    panel;
    private Player       player;
    private SceneManager sceneManager;

    private DungeonOne  dungeon1;
    private DungeonTwo  dungeon2;
    private DungeonBoss dungeon3;
    private Dungeon     currentDungeon;

    public DungeonManager(GamePanel panel, Player player, SceneManager sceneManager) {
        this.panel        = panel;
        this.player       = player;
        this.sceneManager = sceneManager;

        dungeon1 = new DungeonOne(player);
        dungeon2 = new DungeonTwo(player);
        dungeon3 = new DungeonBoss(player);
    }

    // ----------------------------------------------------------------
    public void enterDirect() {
        if      (!dungeon1.isCleared()) startDungeon(1);
        else if (!dungeon2.isCleared()) startDungeon(2);
        else                            startDungeon(3);
    }

    public void enterLobby() { enterDirect(); }

    public void startDungeon(int id) {
        switch (id) {
            case 1 -> currentDungeon = dungeon1;
            case 2 -> currentDungeon = dungeon2;
            case 3 -> currentDungeon = dungeon3;
            default -> { return; }
        }
        currentDungeon.init();
        player.resetPosition();
    }

    // ----------------------------------------------------------------
    public void update() {
        if (currentDungeon == null) return;

        // FIX 1: update player dengan input agar bisa bergerak & menyerang
        player.update(panel.getInputHandler());

        currentDungeon.update();

        // Dungeon selesai → tampilkan layar reward
        if (currentDungeon.isComplete()) {
            currentDungeon.clearDungeon();

            int expBonus  = player.getLevelSystem().getExpToNextLevel();
            int goldBonus = 100 * player.getLevelSystem().getLevel();
            player.getLevelSystem().addDungeonClearBonus();
            int newLevel  = player.getLevelSystem().getLevel();

            sceneManager.showDungeonClear(
                    currentDungeon.getName(), expBonus, goldBonus, newLevel);
            return;
        }

        // Player mati → game over
        if (player.isDead()) {
            sceneManager.changeScene(SceneManager.Scene.GAME_OVER);
        }
    }

    // ----------------------------------------------------------------
    public void draw(Graphics2D g2) {
        if (currentDungeon == null) return;

        currentDungeon.draw(g2);   // gambar dungeon + musuh dulu
        player.draw(g2);           // FIX 2: gambar player di atas dungeon
    }

    // ----------------------------------------------------------------
    public boolean isDungeonCleared(int id) {
        return switch (id) {
            case 1 -> dungeon1.isCleared();
            case 2 -> dungeon2.isCleared();
            case 3 -> dungeon3.isCleared();
            default -> false;
        };
    }
}