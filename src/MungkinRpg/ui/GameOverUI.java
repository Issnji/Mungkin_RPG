package MungkinRpg.ui;

import MungkinRpg.core.SceneManager;
import MungkinRpg.core.InputHandler;
import MungkinRpg.util.AssetLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import MungkinRpg.audio.SoundEffect;

// Layar Game Over — palet kayu/perkamen sama seperti DungeonClearUI

public class GameOverUI {

    // ── Palette (sama persis dengan DungeonClearUI) ───────────────────
    private static final Color PANEL_BG_TOP   = new Color(225, 185, 115);
    private static final Color PANEL_BG_BOT   = new Color(195, 150,  78);
    private static final Color WOOD_DARK      = new Color( 90,  50,  20);
    private static final Color WOOD_MID       = new Color(140,  80,  30);
    private static final Color WOOD_LIGHT     = new Color(180, 110,  45);
    private static final Color WOOD_HIGHLIGHT = new Color(210, 145,  70);
    private static final Color BANNER_DARK    = new Color( 80,  40,  10);
    private static final Color BANNER_TEXT    = new Color(255, 235, 130);
    private static final Color BANNER_SHADOW  = new Color( 60,  25,   5);
    private static final Color BTN_NORMAL_TOP = new Color(140,  80,  25);
    private static final Color BTN_NORMAL_BOT = new Color( 90,  48,  12);
    private static final Color BTN_SEL_TOP    = new Color(200, 140,  40);
    private static final Color BTN_SEL_BOT    = new Color(150,  95,  20);
    private static final Color BTN_BORDER     = new Color( 60,  28,   5);
    private static final Color BTN_TEXT_NORM  = new Color(200, 165, 100);
    private static final Color BTN_TEXT_SEL   = new Color(255, 240, 150);

    // ── Layout ───────────────────────────────────────────────────────
    private static final int PX = 200, PY = 100, PW = 400, PH = 360;

    // ── State ─────────────────────────────────────────────────────────
    private final SceneManager sceneManager;
    private int     selectedOption = 0;
    private static final String[] OPTIONS = { "Back to Town", "Exit Game" };
    private int     tick        = 0;
    private int     revealTick  = 0;
    private boolean inputLocked = true;
    private int     flashTick   = 0;
    private static final int FLASH_DUR = 8;

    // ── Assets ────────────────────────────────────────────────────────
    private BufferedImage imgDied;
    private BufferedImage imgTownBg;

    // ── Font ──────────────────────────────────────────────────────────
    private Font fontTitle;
    private Font fontBtn;
    private Font fontHint;

    // ─────────────────────────────────────────────────────────────────
    public GameOverUI(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        fontTitle = new Font("Monospaced", Font.BOLD, 24);
        fontBtn   = new Font("Monospaced", Font.BOLD, 17);
        fontHint  = new Font("Monospaced", Font.PLAIN, 11);

        imgDied   = AssetLoader.loadImage("died.png");
        imgTownBg = AssetLoader.loadImage("town_bg.png");
    }

    // ─────────────────────────────────────────────────────────────────
    public void reset() {
        selectedOption = 0;
        tick        = 0;
        revealTick  = 0;
        inputLocked = true;
        flashTick   = FLASH_DUR;
    }

    // ─────────────────────────────────────────────────────────────────
    public void update(InputHandler input) {
        tick++;
        if (flashTick  > 0)  flashTick--;
        if (revealTick < 90) revealTick++;
        if (revealTick >= 45) inputLocked = false;

        if (inputLocked) return;
        if (input.up)   { selectedOption = (selectedOption - 1 + OPTIONS.length) % OPTIONS.length; input.up   = false; }
        if (input.down) { selectedOption = (selectedOption + 1) % OPTIONS.length;                  input.down = false; }
        if (input.enter || input.interact) {
            input.enter = input.interact = false;
            if (selectedOption == 0) sceneManager.changeScene(SceneManager.Scene.TOWN);
            else                     System.exit(0);
        }
    }

    /** Kompatibel dengan pemanggilan lama tanpa parameter input. */
    public void update() {
        tick++;
        if (flashTick  > 0)  flashTick--;
        if (revealTick < 90) revealTick++;
        if (revealTick >= 45) inputLocked = false;
    }

    /** Kompatibel dengan pemanggilan lama handleInput() terpisah. */
    public void handleInput(InputHandler input) {
        if (inputLocked) return;
        if (input.up)   { selectedOption = (selectedOption - 1 + OPTIONS.length) % OPTIONS.length; input.up   = false; }
        if (input.down) { selectedOption = (selectedOption + 1) % OPTIONS.length;                  input.down = false; }
        if (input.enter || input.interact) {
            input.enter = input.interact = false;
            if (selectedOption == 0) sceneManager.changeScene(SceneManager.Scene.TOWN);
            else                     System.exit(0);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        // Flash putih/kuning saat pertama muncul (sama seperti DungeonClearUI)
        if (flashTick > 0) {
            g2.setColor(new Color(255, 245, 200, (int)((flashTick / (float) FLASH_DUR) * 200)));
            g2.fillRect(0, 0, 800, 600);
            return;
        }

        // Background
        if (imgTownBg != null) {
            g2.drawImage(imgTownBg, 0, 0, 800, 600, null);
        }

        // Overlay gelap
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, 800, 600);

        // Frame + panel
        drawWoodFrame(g2, PX - 14, PY - 14, PW + 28, PH + 28);
        drawParchmentPanel(g2, PX, PY, PW, PH);
        drawBanner(g2);

        // Gambar tengkorak (died.png)
        if (revealTick >= 25) {
            drawDiedImage(g2);
        }

        // Tombol
        if (revealTick >= 48) drawButtons(g2);

        // Hint kontrol
        if (!inputLocked) {
            g2.setFont(fontHint);
            g2.setColor(new Color(120, 80, 30, 180));
            drawCentered(g2, "Click W/S to Navigate and Enter/E to Choose", PX, PW, PY + PH - 15);
        }
    }

    // ── Komponen visual ──────────────────────────────────────────────

    private void drawWoodFrame(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(WOOD_DARK);      g2.fillRoundRect(x,      y,      w,      h,      18, 18);
        g2.setColor(WOOD_MID);       g2.fillRoundRect(x + 4,  y + 4,  w - 8,  h - 8,  14, 14);
        g2.setColor(WOOD_LIGHT);     g2.fillRoundRect(x + 8,  y + 8,  w - 16, h - 16, 10, 10);
        g2.setColor(WOOD_HIGHLIGHT); g2.fillRoundRect(x + 10, y + 10, w - 20, 12,      8,  8);

        g2.setColor(new Color(60, 30, 8, 40));
        for (int i = 0; i < 6; i++) g2.fillRect(x + 10, y + 22 + i * (h / 7), w - 20, 1);

        int[][] rivets = {{x+14,y+14},{x+w-22,y+14},{x+14,y+h-22},{x+w-22,y+h-22}};
        for (int[] r : rivets) drawRivet(g2, r[0], r[1]);
    }

    private void drawRivet(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(80,  50,  15)); g2.fillOval(x,     y,     10, 10);
        g2.setColor(new Color(180, 130, 50)); g2.fillOval(x + 2, y + 2,  5,  5);
        g2.setColor(new Color(220, 180, 90)); g2.fillOval(x + 3, y + 3,  2,  2);
    }

    private void drawParchmentPanel(Graphics2D g2, int x, int y, int w, int h) {
        g2.setPaint(new GradientPaint(x, y, PANEL_BG_TOP, x, y + h, PANEL_BG_BOT));
        g2.fillRoundRect(x, y, w, h, 8, 8);
        g2.setColor(new Color(160, 110, 50, 18));
        for (int i = -h; i < w + h; i += 12) g2.drawLine(x + i, y, x + i - h, y + h);
    }

    private void drawBanner(Graphics2D g2) {
        int bx = PX - 30, bw = PW + 60, by = PY - 5, bh = 72;

        // Tiang banner
        g2.setColor(WOOD_DARK);
        g2.fillRect(PX + 60,      PY - 20, 8, 22);
        g2.fillRect(PX + PW - 68, PY - 20, 8, 22);

        // Banner background
        g2.setColor(BANNER_DARK);
        g2.fillRoundRect(bx, by, bw, bh, 10, 10);
        g2.setPaint(new GradientPaint(bx, by, new Color(150, 75, 20), bx, by + bh, new Color(100, 48, 10)));
        g2.fillRoundRect(bx + 3, by + 3, bw - 6, bh - 6, 8, 8);

        // Highlight atas banner
        g2.setColor(new Color(190, 110, 40, 120));
        g2.fillRoundRect(bx + 5, by + 5, bw - 10, 14, 6, 6);

        // Teks "YOU DIED" — kedip di awal, solid setelahnya
        boolean blink = revealTick < 25 ? (tick / 8) % 2 == 0 : true;
        if (blink) {
            g2.setFont(fontTitle);
            g2.setColor(BANNER_SHADOW); drawCentered(g2, "YOU DIED!", bx, bw, by + 45);
            g2.setColor(BANNER_TEXT);   drawCentered(g2, "YOU DIED!", bx, bw, by + 43);
        }
    }

    private void drawDiedImage(Graphics2D g2) {
        int iconSize = 96;
        int iconX = PX + (PW - iconSize) / 2;
        int iconY = PY + 110;

        if (imgDied != null) {
            g2.drawImage(imgDied, iconX, iconY, iconSize, iconSize, null);
        }
        // Tidak ada fallback — kalau died.png tidak ditemukan, area ini kosong
    }

    private void drawButtons(Graphics2D g2) {
        int btnW = 240, btnH = 44;
        int btnX = PX + (PW - btnW) / 2;
        int[] btnYs = { PY + 230, PY + 284 };

        for (int i = 0; i < OPTIONS.length; i++) {
            boolean sel = (i == selectedOption) && !inputLocked;
            int bx = btnX, by = btnYs[i];

            // Shadow
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRoundRect(bx + 3, by + 3, btnW, btnH, 10, 10);

            // Gradient tombol
            g2.setPaint(new GradientPaint(bx, by,        sel ? BTN_SEL_TOP : BTN_NORMAL_TOP,
                    bx, by + btnH, sel ? BTN_SEL_BOT : BTN_NORMAL_BOT));
            g2.fillRoundRect(bx, by, btnW, btnH, 10, 10);

            // Highlight atas tombol
            g2.setColor(new Color(255, 255, 255, sel ? 70 : 30));
            g2.fillRoundRect(bx + 3, by + 3, btnW - 6, btnH / 2 - 2, 8, 8);

            // Border
            g2.setColor(BTN_BORDER);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(bx, by, btnW, btnH, 10, 10);
            g2.setStroke(new BasicStroke(1f));

            // Teks tombol
            g2.setFont(fontBtn);
            g2.setColor(new Color(40, 15, 3));
            drawCentered(g2, OPTIONS[i], bx, btnW, by + 28);
            g2.setColor(sel ? BTN_TEXT_SEL : BTN_TEXT_NORM);
            drawCentered(g2, OPTIONS[i], bx, btnW, by + 27);
        }
    }

    // ── Helper ────────────────────────────────────────────────────────

    private void drawCentered(Graphics2D g2, String text, int cx, int cw, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, cx + (cw - fm.stringWidth(text)) / 2, y);
    }
}