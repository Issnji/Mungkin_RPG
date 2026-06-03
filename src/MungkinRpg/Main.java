package MungkinRpg;

import MungkinRpg.core.GamePanel;
import MungkinRpg.ui.MainMenu;
import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame window = new JFrame("Mungkin RPG");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);

            CardLayout cardLayout = new CardLayout();
            JPanel     container  = new JPanel(cardLayout);

            GamePanel      gamePanel = new GamePanel();

            MainMenu menuPanel = new MainMenu(() -> {
                cardLayout.show(container, "game");
                gamePanel.startGame();
                SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
            });

            container.add(menuPanel, "menu");
            container.add(gamePanel, "game");

            window.add(container);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            cardLayout.show(container, "menu");
            menuPanel.startAnimation();
        });
    }
}