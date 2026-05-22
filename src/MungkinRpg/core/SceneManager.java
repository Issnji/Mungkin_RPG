package MungkinRpg.core;

import MungkinRpg.player.Player;
import MungkinRpg.town.TownMap;
import MungkinRpg.dungeon.DungeonManager;
import MungkinRpg.ui.GameOverUI;
import MungkinRpg.ui.DungeonClearUI;
import java.awt.Graphics2D;

/**
 * SceneManager — sekarang tidak ada MAIN_MENU.
 * Main menu ditangani oleh MainMenuPanel (Swing layer).
 * Scene yang ada: TOWN, DUNGEON, DUNGEON_CLEAR, GAME_OVER.
 */
public class SceneManager {
    public enum Scene { TOWN, DUNGEON, DUNGEON_CLEAR, GAME_OVER }

    private Scene          currentScene;
    private GamePanel      panel;
    private Player         player;

    private TownMap        townMap;
    private DungeonManager dungeonManager;
    private GameOverUI     gameOverUI;
    private DungeonClearUI dungeonClearUI;

    public SceneManager(GamePanel panel, Player player) {
        this.panel  = panel;
        this.player = player;
        this.currentScene = Scene.TOWN; // langsung mulai di TOWN

        townMap        = new TownMap(panel, player, this);
        dungeonManager = new DungeonManager(panel, player, this);
        gameOverUI     = new GameOverUI(this);
        dungeonClearUI = new DungeonClearUI(this);
    }

    public void update(InputHandler input) {
        switch (currentScene) {
            case TOWN          -> townMap.update();
            case DUNGEON       -> dungeonManager.update();
            case DUNGEON_CLEAR -> dungeonClearUI.update(input);
            case GAME_OVER     -> { gameOverUI.update(); gameOverUI.handleInput(input); }
        }
    }

    public void draw(Graphics2D g2) {
        switch (currentScene) {
            case TOWN          -> townMap.draw(g2);
            case DUNGEON       -> dungeonManager.draw(g2);
            case DUNGEON_CLEAR -> dungeonClearUI.draw(g2);
            case GAME_OVER     -> gameOverUI.draw(g2);
        }
    }

    public void changeScene(Scene scene) {
        this.currentScene = scene;
        if (scene == Scene.DUNGEON) dungeonManager.enterDirect();
    }

    /** Dipanggil DungeonManager saat dungeon selesai. */
    public void showDungeonClear(String name, int exp, int gold, int level) {
        dungeonClearUI.setRewards(name, exp, gold, level);
        currentScene = Scene.DUNGEON_CLEAR;
    }

    public Scene          getCurrentScene()   { return currentScene; }
    public DungeonManager getDungeonManager() { return dungeonManager; }
}