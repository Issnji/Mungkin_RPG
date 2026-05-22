package MungkinRpg.ui;

import MungkinRpg.core.SceneManager;
import MungkinRpg.core.InputHandler;
import java.awt.*;

/**
 * Layar reward setelah dungeon selesai.
 * Tampilkan nama dungeon, EXP & Gold yang didapat, level baru,
 * lalu pilihan "Return to Town" atau "Exit Game".
 */
public class DungeonClearUI {
    private SceneManager sceneManager;

    // Reward data — diisi oleh DungeonManager saat dungeon selesai
    private String dungeonName = "";
    private int    expGained   = 0;
    private int    goldGained  = 0;
    private int    newLevel    = 1;

    private int selectedOption = 0; // 0 = Return to Town, 1 = Exit
    private static final String[] OPTIONS = { "Return to Town", "Exit Game" };

    private int  animTick    = 0;
    private int  revealTick  = 0;  // controls staggered reveal animation
    private boolean inputLocked = true; // short lock so player can't skip instantly

    public DungeonClearUI(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /** Call this right before switching to DUNGEON_CLEAR scene. */
    public void setRewards(String dungeonName, int expGained, int goldGained, int newLevel) {
        this.dungeonName   = dungeonName;
        this.expGained     = expGained;
        this.goldGained    = goldGained;
        this.newLevel      = newLevel;
        this.selectedOption = 0;
        this.animTick       = 0;
        this.revealTick     = 0;
        this.inputLocked    = true;
    }

    // ----------------------------------------------------------------
    public void update(InputHandler input) {
        animTick++;
        if (revealTick < 120) revealTick++;
        if (revealTick >= 60) inputLocked = false;

        if (inputLocked) return;

        if (input.up) {
            selectedOption = (selectedOption - 1 + OPTIONS.length) % OPTIONS.length;
            input.up = false;
        }
        if (input.down) {
            selectedOption = (selectedOption + 1) % OPTIONS.length;
            input.down = false;
        }
        if (input.enter || input.interact) {
            input.enter    = false;
            input.interact = false;
            if (selectedOption == 0) {
                sceneManager.changeScene(SceneManager.Scene.TOWN);
            } else {
                System.exit(0);
            }
        }
    }

    // ----------------------------------------------------------------
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Dark overlay ---
        float overlayAlpha = Math.min(1f, revealTick / 30f);
        g2.setColor(new Color(0f, 0f, 0f, overlayAlpha * 0.75f));
        g2.fillRect(0, 0, 800, 600);

        if (revealTick < 10) return; // let overlay fade in first

        // --- Star particles ---
        drawStars(g2);

        // --- Main panel ---
        int px = 160, py = 80, pw = 480, ph = 440;
        drawPanel(g2, px, py, pw, ph);

        // --- DUNGEON CLEARED! title ---
        if (revealTick >= 15) {
            float titleAlpha = Math.min(1f, (revealTick - 15) / 20f);
            drawTitle(g2, px, py, pw, titleAlpha);
        }

        // --- Dungeon name ---
        if (revealTick >= 30) {
            float a = Math.min(1f, (revealTick - 30) / 15f);
            g2.setFont(new Font("Serif", Font.ITALIC, 20));
            g2.setColor(new Color(1f, 0.9f, 0.6f, a));
            drawCentered(g2, "\"" + dungeonName + "\"", px, pw, py + 90);
        }

        // --- Reward cards ---
        if (revealTick >= 45) {
            float a = Math.min(1f, (revealTick - 45) / 20f);
            drawRewardCard(g2, px + 40,  py + 115, 120, 80,
                "EXP", "+" + expGained, new Color(80, 160, 255), a);
            drawRewardCard(g2, px + 180, py + 115, 120, 80,
                "GOLD", "+" + goldGained, new Color(255, 200, 40), a);
            drawRewardCard(g2, px + 320, py + 115, 120, 80,
                "LEVEL", "Lv. " + newLevel, new Color(100, 220, 100), a);
        }

        // --- Divider ---
        if (revealTick >= 60) {
            g2.setColor(new Color(255, 200, 50, 120));
            g2.fillRect(px + 30, py + 215, pw - 60, 2);
        }

        // --- "Excellent!" flavor text ---
        if (revealTick >= 65) {
            float a = Math.min(1f, (revealTick - 65) / 15f);
            g2.setFont(new Font("Serif", Font.ITALIC, 16));
            g2.setColor(new Color(1f, 0.85f, 0.5f, a));
            drawCentered(g2, "You emerged victorious! What's next?", px, pw, py + 245);
        }

        // --- Menu options ---
        if (revealTick >= 75) {
            float a = Math.min(1f, (revealTick - 75) / 15f);
            drawOptions(g2, px, py, pw, a);
        }

        // --- Controls hint ---
        if (!inputLocked) {
            g2.setFont(new Font("Arial", Font.PLAIN, 13));
            g2.setColor(new Color(150, 130, 100));
            drawCentered(g2, "W / S  to navigate    |    Enter / E  to confirm",
                px, pw, py + ph - 18);
        }
    }

    // ----------------------------------------------------------------
    private void drawPanel(Graphics2D g2, int px, int py, int pw, int ph) {
        // Outer glow
        g2.setColor(new Color(200, 160, 30, 50));
        g2.fillRoundRect(px - 8, py - 8, pw + 16, ph + 16, 24, 24);

        // Panel background
        GradientPaint bg = new GradientPaint(px, py, new Color(30, 20, 50),
                                             px, py + ph, new Color(15, 10, 25));
        g2.setPaint(bg);
        g2.fillRoundRect(px, py, pw, ph, 20, 20);

        // Gold border
        g2.setColor(new Color(200, 160, 40));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRoundRect(px, py, pw, ph, 20, 20);
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawTitle(Graphics2D g2, int px, int py, int pw, float alpha) {
        // Shadow
        g2.setFont(new Font("Serif", Font.BOLD, 42));
        g2.setColor(new Color(0f, 0f, 0f, alpha * 0.6f));
        drawCentered(g2, "DUNGEON CLEARED!", px, pw, py + 58);

        // Gradient text
        GradientPaint grad = new GradientPaint(
            px, py + 20, new Color(1f, 0.95f, 0.3f, alpha),
            px, py + 60, new Color(0.9f, 0.55f, 0.1f, alpha));
        g2.setPaint(grad);
        drawCentered(g2, "DUNGEON CLEARED!", px, pw, py + 55);
    }

    private void drawRewardCard(Graphics2D g2, int cx, int cy, int cw, int ch,
                                 String label, String value, Color accent, float alpha) {
        // Card bg
        g2.setColor(new Color(0f, 0f, 0f, alpha * 0.4f));
        g2.fillRoundRect(cx, cy, cw, ch, 12, 12);
        // Accent top stripe
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(),
                              (int)(alpha * 200)));
        g2.fillRoundRect(cx, cy, cw, 6, 6, 6);
        // Border
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(),
                              (int)(alpha * 150)));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(cx, cy, cw, ch, 12, 12);
        g2.setStroke(new BasicStroke(1f));

        // Label
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(),
                              (int)(alpha * 220)));
        drawCentered(g2, label, cx, cw, cy + 30);

        // Value
        g2.setFont(new Font("Serif", Font.BOLD, 22));
        g2.setColor(new Color(1f, 1f, 1f, alpha));
        drawCentered(g2, value, cx, cw, cy + 60);
    }

    private void drawOptions(Graphics2D g2, int px, int py, int pw, float alpha) {
        int boxW = 200, boxH = 46;
        int startY = py + 270;
        int gap    = 58;
        int startX = px + (pw - boxW) / 2;

        for (int i = 0; i < OPTIONS.length; i++) {
            int bx = startX;
            int by = startY + i * gap;
            boolean sel = (i == selectedOption);

            if (sel && !inputLocked) {
                float pulse = (float)(Math.sin(animTick * 0.15) * 0.5 + 0.5);
                int gAlpha = (int)((50 + pulse * 40) * alpha);
                g2.setColor(new Color(200, 160, 0, gAlpha));
                g2.fillRoundRect(bx - 6, by - 6, boxW + 12, boxH + 12, 14, 14);

                GradientPaint btn = new GradientPaint(bx, by, new Color(110, 75, 0),
                                                      bx, by + boxH, new Color(65, 42, 0));
                g2.setPaint(btn);
                g2.fillRoundRect(bx, by, boxW, boxH, 10, 10);
                g2.setColor(new Color(255, 210, 50, (int)(alpha * 255)));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(bx, by, boxW, boxH, 10, 10);
                g2.setStroke(new BasicStroke(1f));
                // Arrow
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString("▶", bx - 24, by + 30);
            } else {
                g2.setColor(new Color(0f, 0f, 0f, alpha * 0.35f));
                g2.fillRoundRect(bx, by, boxW, boxH, 10, 10);
                g2.setColor(new Color(80, 65, 100, (int)(alpha * 180)));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(bx, by, boxW, boxH, 10, 10);
                g2.setStroke(new BasicStroke(1f));
            }

            g2.setFont(new Font("Serif", Font.BOLD, 22));
            g2.setColor(sel && !inputLocked
                ? new Color(1f, 0.9f, 0.3f, alpha)
                : new Color(0.65f, 0.58f, 0.75f, alpha));
            drawCentered(g2, OPTIONS[i], bx, boxW, by + 30);
        }
    }

    private void drawStars(Graphics2D g2) {
        for (int i = 0; i < 30; i++) {
            int sx = (i * 137 + 23) % 800;
            int sy = (i * 89  + 11) % 600;
            float phase = (float)(Math.sin(animTick * 0.08 + i * 0.7) * 0.5 + 0.5);
            int a = (int)(80 + phase * 100);
            g2.setColor(new Color(255, 230, 150, a));
            g2.fillOval(sx, sy, 2, 2);
        }
    }

    private void drawCentered(Graphics2D g2, String text, int containerX, int containerW, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = containerX + (containerW - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
}