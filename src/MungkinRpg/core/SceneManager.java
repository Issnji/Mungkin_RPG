package  MungkinRpg.core;

import  MungkinRpg.player.Player;
import  MungkinRpg.ui.MainMenu;
import  MungkinRpg.town.TownMap;
import  MungkinRpg.dungeon.DungeonManager;
import  MungkinRpg.ui.GameOverUI;
import java.awt.Graphics2D;

public class SceneManager {
    public enum Scene { MAIN_MENU, TOWN, DUNGEON, GAME_OVER }

    private Scene currentScene;
    private GamePanel panel;
    private Player player;

    private MainMenu mainMenu;
    private TownMap townMap;
    private DungeonManager dungeonManager;
    private GameOverUI gameOverUI;

    public SceneManager(GamePanel panel, Player player) {
        this.panel = panel;
        this.player = player;
        this.currentScene = Scene.MAIN_MENU;

        this.mainMenu = new MainMenu(this);
        this.townMap = new TownMap(panel, player, this);
        this.dungeonManager = new DungeonManager(panel, player, this);
        this.gameOverUI = new GameOverUI(this);
    }

    // FIX: accept InputHandler so MAIN_MENU and GAME_OVER can read input
    public void update(InputHandler input) {
        switch (currentScene) {
            case MAIN_MENU -> mainMenu.update(input);
            case TOWN      -> townMap.update();
            case DUNGEON   -> dungeonManager.update();
            case GAME_OVER -> {
                gameOverUI.update();
                gameOverUI.handleInput(input); // FIX: was never called before
            }
        }
    }

    public void draw(Graphics2D g2) {
        switch (currentScene) {
            case MAIN_MENU -> mainMenu.draw(g2);
            case TOWN      -> townMap.draw(g2);
            case DUNGEON   -> dungeonManager.draw(g2);
            case GAME_OVER -> gameOverUI.draw(g2);
        }
    }

    public void changeScene(Scene scene) {
        this.currentScene = scene;
        if (scene == Scene.DUNGEON) {
            dungeonManager.enterLobby();
        }
    }

    public Scene getCurrentScene() { return currentScene; }
    public DungeonManager getDungeonManager() { return dungeonManager; }
}