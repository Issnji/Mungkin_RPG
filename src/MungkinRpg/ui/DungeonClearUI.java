package MungkinRpg.ui;

import MungkinRpg.core.SceneManager;
import MungkinRpg.core.InputHandler;
import MungkinRpg.util.AssetLoader;
import java.awt.*;
import java.awt.image.BufferedImage;
import MungkinRpg.audio.SoundEffect;

// Layar reward setelah dungeon selesai

public class DungeonClearUI {

    // ── Palette ──────────────────────────────────────────────────────
    private static final Color OVERLAY        = new Color(0,   0,   0,   160);
    private static final Color PANEL_BG_TOP   = new Color(225, 185, 115);
    private static final Color PANEL_BG_BOT   = new Color(195, 150,  78);
    private static final Color WOOD_DARK      = new Color( 90,  50,  20);
    private static final Color WOOD_MID       = new Color(140,  80,  30);
    private static final Color WOOD_LIGHT     = new Color(180, 110,  45);
    private static final Color WOOD_HIGHLIGHT = new Color(210, 145,  70);
    private static final Color BANNER_DARK    = new Color( 80,  40,  10);
    private static final Color BANNER_TEXT    = new Color(255, 235, 130);
    private static final Color BANNER_SHADOW  = new Color( 60,  25,   5);
    private static final Color CARD_BG        = new Color(200, 158,  80);
    private static final Color CARD_BORDER    = new Color(150, 100,  35);
    private static final Color CARD_LABEL     = new Color( 80,  45,  10);
    private static final Color BTN_NORMAL_TOP = new Color(140,  80,  25);
    private static final Color BTN_NORMAL_BOT = new Color( 90,  48,  12);
    private static final Color BTN_SEL_TOP    = new Color(200, 140,  40);
    private static final Color BTN_SEL_BOT    = new Color(150,  95,  20);
    private static final Color BTN_BORDER     = new Color( 60,  28,   5);
    private static final Color BTN_TEXT_NORM  = new Color(200, 165, 100);
    private static final Color BTN_TEXT_SEL   = new Color(255, 240, 150);

    // ── Layout ───────────────────────────────────────────────────────
    private static final int PX = 175, PY = 85, PW = 450, PH = 400;

    // ── State ────────────────────────────────────────────────────────
    private final SceneManager sceneManager;
    private String  dungeonName    = "";
    private int     expGained      = 0;
    private int     goldGained     = 0;
    private int     newLevel       = 1;
    private int     selectedOption = 0;
    private static final String[] OPTIONS = { "Back to Town", "Exit Game" };
    private int     tick           = 0;
    private int     revealTick     = 0;
    private boolean inputLocked    = true;
    private int     flashTick      = 0;
    private static final int FLASH_DUR = 8;

    // ── Assets ───────────────────────────────────────────────────────
    private BufferedImage imgExp;
    private BufferedImage imgGold;
    private BufferedImage imgLevel;
    private BufferedImage imgTownBg;

    // ── Font ─────────────────────────────────────────────────────────
    private Font fontTitle;
    private Font fontSub;
    private Font fontCardLabel;
    private Font fontCardValue;
    private Font fontBtn;
    private Font fontHint;

    // ─────────────────────────────────────────────────────────────────
    public DungeonClearUI(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        fontTitle     = new Font("Monospaced", Font.BOLD, 24);
        fontSub       = new Font("Sans Serif", Font.BOLD, 28);
        fontCardLabel = new Font("Monospaced", Font.BOLD, 13);
        fontCardValue = new Font("Monospaced", Font.BOLD, 20);
        fontBtn       = new Font("Monospaced", Font.BOLD, 17);
        fontHint      = new Font("Monospaced", Font.PLAIN, 11);

        // Pakai AssetLoader sama persis seperti TownMap
        imgExp   = AssetLoader.loadImage("exp.png");
        imgGold  = AssetLoader.loadImage("gold.png");
        imgLevel = AssetLoader.loadImage("level.png");
        imgTownBg = AssetLoader.loadImage("town_bg.png");
    }

    // ─────────────────────────────────────────────────────────────────
    public void setRewards(String dungeonName, int expGained, int goldGained, int newLevel) {
        this.dungeonName    = dungeonName;
        this.expGained      = expGained;
        this.goldGained     = goldGained;
        this.newLevel       = newLevel;
        this.selectedOption = 0;
        this.tick           = 0;
        this.revealTick     = 0;
        this.inputLocked    = true;
        this.flashTick      = FLASH_DUR;
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

    // ─────────────────────────────────────────────────────────────────
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        // Flash saat pertama muncul
        if (flashTick > 0) {
            g2.setColor(new Color(255, 245, 200, (int)((flashTick / (float)FLASH_DUR) * 200)));
            g2.fillRect(0, 0, 800, 600);
            return;
        }

        if (imgTownBg != null) {
            g2.drawImage(imgTownBg, 0, 0, 800, 600, null);
        }

        g2.setColor(new Color(0,0,0,120));
        g2.fillRect(0,0,800,600);

        drawWoodFrame(g2, PX - 14, PY - 14, PW + 28, PH + 28);
        drawParchmentPanel(g2, PX, PY, PW, PH);
        drawBanner(g2);

        // Nama dungeon
        if (revealTick >= 15) {
            g2.setFont(fontSub);
            String dn = "\"" + dungeonName + "\"";
            FontMetrics fm = g2.getFontMetrics();
            int dx = PX + (PW - fm.stringWidth(dn)) / 2;
            int dy = PY + 118;
            // Teks utama coklat gelap
            g2.setColor(new Color(70, 35, 8));
            g2.drawString(dn, dx, dy);
        }

        // 3 kartu reward (EXP, Gold, Level) — layout vertikal: ikon atas, nilai bawah
        if (revealTick >= 25) {
            int cardW  = 120, cardH = 110;
            int totalW = cardW * 3 + 20 * 2;
            int startX = PX + (PW - totalW) / 2;
            int cardY  = PY + 135;

            drawRewardCard(g2, startX,            cardY, cardW, cardH,
                    imgExp,   "EXP",   "+" + expGained,  new Color(100, 180, 255));
            drawRewardCard(g2, startX + cardW + 20, cardY, cardW, cardH,
                    imgGold,  "Gold",  "+" + goldGained, new Color(255, 210,  40));
            drawRewardCard(g2, startX + (cardW + 20) * 2, cardY, cardW, cardH,
                    imgLevel, "Level", "Lv." + newLevel, new Color(120, 220, 100));
        }

        // Tombol
        if (revealTick >= 48) drawButtons(g2);

        // Hint kontrol
        if (!inputLocked) {
            g2.setFont(fontHint);
            g2.setColor(new Color(70, 35, 8));

            drawCentered(g2,
                    "Click W/S to Navigate and Enter/E to Choose",
                    PX, PW, PY + PH - 20);
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
        g2.setColor(WOOD_DARK);
        g2.fillRect(PX + 60,      PY - 20, 8, 22);
        g2.fillRect(PX + PW - 68, PY - 20, 8, 22);
        g2.setColor(BANNER_DARK); g2.fillRoundRect(bx, by, bw, bh, 10, 10);
        g2.setPaint(new GradientPaint(bx, by, new Color(150, 75, 20), bx, by + bh, new Color(100, 48, 10)));
        g2.fillRoundRect(bx + 3, by + 3, bw - 6, bh - 6, 8, 8);
        g2.setColor(new Color(190, 110, 40, 120));
        g2.fillRoundRect(bx + 5, by + 5, bw - 10, 14, 6, 6);

        boolean blink = revealTick < 25 ? (tick / 8) % 2 == 0 : true;
        if (blink) {
            g2.setFont(fontTitle);
            g2.setColor(BANNER_SHADOW); drawCentered(g2, "YOU WIN!", bx, bw, by + 45);
            g2.setColor(BANNER_TEXT);   drawCentered(g2, "YOU WIN!", bx, bw, by + 43);
        }
    }

    /**
     * Kartu reward vertikal: ikon di tengah atas, label kecil, nilai besar di bawah.
     */
    private void drawRewardCard(Graphics2D g2, int x, int y, int w, int h,
                                BufferedImage icon, String label, String value, Color valueColor) {
        // Shadow kartu
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(x + 3, y + 3, w, h, 10, 10);

        // Background kartu
        g2.setColor(CARD_BG);
        g2.fillRoundRect(x, y, w, h, 10, 10);

        // Highlight atas kartu
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillRoundRect(x + 2, y + 2, w - 4, h / 3, 8, 8);

        // Border kartu
        g2.setColor(CARD_BORDER);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, w, h, 10, 10);
        g2.setStroke(new BasicStroke(1f));

        // Ikon — ditengah, ukuran 48x48
        int iconSize = 48;
        int iconX = x + (w - iconSize) / 2;
        int iconY = y + 8;
        if (icon != null) {
            g2.drawImage(icon, iconX, iconY, iconSize, iconSize, null);
        } else {
            // Fallback: lingkaran berwarna + singkatan
            g2.setColor(new Color(valueColor.getRed(), valueColor.getGreen(), valueColor.getBlue(), 210));
            g2.fillOval(iconX, iconY, iconSize, iconSize);
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillOval(iconX + 2, iconY + 2, iconSize - 4, iconSize - 4);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            String abbr = label.length() >= 2 ? label.substring(0, 2) : label;
            g2.drawString(abbr, iconX + (iconSize - fm.stringWidth(abbr)) / 2, iconY + iconSize / 2 + 5);
        }

        // Label (tengah, kecil)
        g2.setFont(fontCardLabel);
        g2.setColor(CARD_LABEL);
        drawCentered(g2, label, x, w, iconY + iconSize + 16);

        // Nilai (tengah, besar, berwarna)
        g2.setFont(fontCardValue);
        // Shadow nilai
        g2.setColor(new Color(50, 25, 5));
        drawCentered(g2, value, x, w, iconY + iconSize + 36 + 1);
        // Nilai utama
        g2.setColor(valueColor);
        drawCentered(g2, value, x, w, iconY + iconSize + 36);
    }

    private void drawButtons(Graphics2D g2) {
        int btnW = 240, btnH = 44;
        int btnX = PX + (PW - btnW) / 2;
        int[] btnYs = { PY + 260, PY + 314 };

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

            // Teks (shadow + foreground)
            g2.setFont(fontBtn);
            g2.setColor(new Color(40, 15, 3));
            drawCentered(g2, OPTIONS[i], bx, btnW, by + 28);
            g2.setColor(sel ? BTN_TEXT_SEL : BTN_TEXT_NORM);
            drawCentered(g2, OPTIONS[i], bx, btnW, by + 27);
        }
    }

    // ── Helper ───────────────────────────────────────────────────────

    private void drawCentered(Graphics2D g2, String text, int cx, int cw, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, cx + (cw - fm.stringWidth(text)) / 2, y);
    }
}