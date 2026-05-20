package MungkinRpg.ui;

import  MungkinRpg.core.SceneManager;
import  MungkinRpg.core.InputHandler;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class GameOverUI {
    private SceneManager sceneManager;
    private int timer;

    public GameOverUI(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.timer = 0;
    }

    public void update() {
        timer++;
    }

    public void handleInput(InputHandler input) {
        if (timer > 60 && input.interact) {
            sceneManager.changeScene(SceneManager.Scene.TOWN);
            input.interact = false;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, 800, 600);

        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.drawString("YOU DIED", 300, 250);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.drawString("Press E to Return to Town", 280, 320);
    }
}