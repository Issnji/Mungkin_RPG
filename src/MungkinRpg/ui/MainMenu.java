package  MungkinRpg.ui;

import  MungkinRpg.core.SceneManager;
import  MungkinRpg.core.InputHandler;
import java.awt.Graphics2D;
import java.awt.Color;

public class MainMenu {
    private SceneManager sceneManager;

    public MainMenu(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    // FIX: was missing; game was stuck on main menu forever
    public void update(InputHandler input) {
        if (input.enter || input.interact) {
            input.enter = false;
            input.interact = false;
            sceneManager.changeScene(SceneManager.Scene.TOWN);
        }
        // L key (skill2) = load game — hook up SaveManager here if needed
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 800, 600);
        g2.setColor(Color.WHITE);
        g2.drawString("DUNGEON RPG", 350, 250);
        g2.drawString("Press ENTER or E to Start", 310, 300);
        g2.drawString("Press L to Load Game", 330, 330);
    }
}