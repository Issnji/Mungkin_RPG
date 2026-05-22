package MungkinRpg;

import MungkinRpg.core.GamePanel;
import MungkinRpg.ui.MainMenu;
import javax.swing.*;
import java.awt.*;

/**
 * Entry point — menggunakan CardLayout untuk berpindah antara
 * MainMenuPanel (Swing) dan GamePanel (Graphics2D game loop).
 *
 * Keuntungan pendekatan ini:
 *  - Main menu punya JButton asli: hover, klik, aksesibilitas
 *  - Game tetap menggunakan rendering loop 60 FPS seperti biasa
 *  - Mudah menambah layar lain (settings, credits, dll.)
 */
public class Main {
    public static void main(String[] args) {
        // Semua Swing harus jalan di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            JFrame window = new JFrame("Dungeon RPG");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);

            // ---- CardLayout sebagai "scene manager" level atas ----
            CardLayout cardLayout = new CardLayout();
            JPanel     container  = new JPanel(cardLayout);

            GamePanel      gamePanel = new GamePanel();

            // Callback: dipanggil saat PLAY diklik di main menu
            MainMenu menuPanel = new MainMenu(() -> {
//                menuPanel.stopAnimation();              // hemat CPU
                cardLayout.show(container, "game");
                gamePanel.startGame();
                // Penting: fokuskan gamePanel agar InputHandler menerima keyboard
                SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
            });

            container.add(menuPanel, "menu");
            container.add(gamePanel, "game");

            window.add(container);
            window.pack();
            window.setLocationRelativeTo(null); // tengah layar
            window.setVisible(true);

            // Mulai dari main menu
            cardLayout.show(container, "menu");
            menuPanel.startAnimation();
        });
    }
}