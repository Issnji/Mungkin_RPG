package MungkinRpg.ui;

import com.MungkinRpg.core.SceneManager;
import java.awt.Graphics2D;
import java.awt.Color;

public class MainMenu {
    private SceneManager sceneManager;

    public MainMenu(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 800, 600);
        g2.setColor(Color.WHITE);
        g2.drawString("DUNGEON RPG", 350, 250);
        g2.drawString("Press ENTER to Start", 330, 300);
        g2.drawString("Press L to Load Game", 330, 330);
    }
}