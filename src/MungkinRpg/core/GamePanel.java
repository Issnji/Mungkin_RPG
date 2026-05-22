package MungkinRpg.core;

import MungkinRpg.player.Player;
import MungkinRpg.ui.HUD;
import MungkinRpg.util.Constants;
import javax.swing.JPanel;
import java.awt.*;

/**
 * GamePanel: panel utama game.
 * Dengan pendekatan Swing + CardLayout, GamePanel tidak perlu lagi
 * mengurus scene MAIN_MENU — itu ditangani oleh MainMenuPanel.
 * SceneManager sekarang hanya punya: TOWN, DUNGEON, DUNGEON_CLEAR, GAME_OVER.
 */
public class GamePanel extends JPanel implements Runnable {

    private Thread      gameThread;
    private GameLoop    gameLoop;
    private SceneManager sceneManager;
    private InputHandler inputHandler;
    private Player      player;
    private HUD         hud;

    private boolean started = false; // cegah double start

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setDoubleBuffered(true);
        setFocusable(true);

        inputHandler = new InputHandler();
        addKeyListener(inputHandler);

        player       = new Player();
        hud          = new HUD(player);
        sceneManager = new SceneManager(this, player);
        gameLoop     = new GameLoop(this);
    }

    /** Dipanggil oleh Main.java saat PLAY diklik — hanya sekali. */
    public void startGame() {
        if (started) return;
        started = true;
        // Scene awal langsung TOWN (main menu sudah di layer Swing)
        sceneManager.changeScene(SceneManager.Scene.TOWN);
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        gameLoop.start();
    }

    public void update() {
        sceneManager.update(inputHandler);
        hud.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        sceneManager.draw(g2);
        hud.draw(g2);
        g2.dispose();
    }

    public InputHandler  getInputHandler()  { return inputHandler; }
    public Player        getPlayer()        { return player; }
    public SceneManager  getSceneManager()  { return sceneManager; }
}