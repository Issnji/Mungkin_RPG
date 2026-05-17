package MungkinRpg.core;

import com.MungkinRpg.player.Player;
import com.MungkinRpg.ui.HUD;
import com.MungkinRpg.town.TownMap;
import com.MungkinRpg.dungeon.DungeonManager;
import com.MungkinRpg.util.Constants;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private GameLoop gameLoop;
    private SceneManager sceneManager;
    private InputHandler inputHandler;

    private Player player;
    private HUD hud;

    public GamePanel() {
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        this.inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);

        this.player = new Player();
        this.hud = new HUD(player);
        this.sceneManager = new SceneManager(this, player);
        this.gameLoop = new GameLoop(this);
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        gameLoop.start();
    }

    public void update() {
        sceneManager.update();
        hud.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        sceneManager.draw(g2);
        hud.draw(g2);
        g2.dispose();
    }

    public InputHandler getInputHandler() { return inputHandler; }
    public Player getPlayer() { return player; }
    public SceneManager getSceneManager() { return sceneManager; }
}