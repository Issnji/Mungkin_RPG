package MungkinRpg.town;

import MungkinRpg.util.AssetLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC {
    private int x, y;
    private String name;
    private String[] dialogues;
    private int currentDialogue;
    private Rectangle hitbox;
    private boolean talking;

    // Optional sprite sheet — put "villager.png" in assets/images/
    // Expected: single standing-facing sprite, at least 32×48 pixels
    private BufferedImage sprite;

    // Bob animation
    private int animTick = 0;

    public NPC(int x, int y, String name, String[] dialogues) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.dialogues = dialogues;
        this.currentDialogue = 0;
        this.hitbox = new Rectangle(x, y, 32, 48);
        this.talking = false;

        sprite = AssetLoader.loadImage("villager.png");
    }

    public void interact() {
        talking = true;
    }

    public boolean isTalking() {
        return talking;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void nextDialogue() {
        currentDialogue++;
        if (currentDialogue >= dialogues.length) {
            currentDialogue = 0;
            talking = false;
        }
    }

    // ----------------------------------------------------------------
    public void draw(Graphics2D g2) {
        animTick++;
        int bob = (int) (Math.sin(animTick * 0.05f) * 2);

        if (sprite != null) {
            // Skala pembesaran (contoh: 2x lipat)
            int scale = 2;
            int drawW = 100 * scale; // Hasil: 64
            int drawH = 60 * scale; // Hasil: 96

            // Geser posisi X dan Y agar sprite yang membesar tetap berada di tengah hitbox
            int drawX = x - ((drawW - hitbox.width) / 2);
            int drawY = y - (drawH - hitbox.height);

            // Render gambar dengan ukuran yang sudah dikali skala
            g2.drawImage(sprite, drawX, drawY + bob, drawW, drawH, null);
        } else {
            // Fallback jika gambar belum diload
            g2.setColor(Color.PINK);
            g2.fillRect(x, y + bob, hitbox.width, hitbox.height);
        }

        // --- Kode Papan Nama (tetap dipertahankan) ---
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(name);
        int nx = x + 16 - tw / 2;
        int ny = y + bob - 10; // Sedikit dinaikkan agar tidak tertutup kepala villager yang membesar

        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(nx - 4, ny - 13, tw + 8, 16, 6, 6);
        g2.setColor(new Color(255, 230, 150));
        g2.drawString(name, nx, ny);
    }

    // ----------------------------------------------------------------
    public void drawDialogue(Graphics2D g2) {
        if (!talking) return;

        // Box Utama Dialog
        g2.setColor(new Color(20, 15, 30, 220));
        g2.fillRoundRect(80, 445, 640, 130, 16, 16);
        g2.setColor(new Color(200, 160, 50));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(80, 445, 640, 130, 16, 16);
        g2.setStroke(new BasicStroke(1f));

        // Portrait Area (Diperbesar agar muat sprite ukuran besar)
        g2.setColor(new Color(50, 35, 20));
        // Awalnya 60x60, diubah menjadi 80x100
        g2.fillRoundRect(96, 458, 80, 100, 10, 10);

        if (sprite != null) {
            // Render sprite menjadi ukuran 64x96 di dalam kotak portrait
            g2.drawImage(sprite, 26, 440, 220, 130, null);
        }

        // Teks Dialog
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(name, 190, 480);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString(dialogues[currentDialogue], 190, 510);

        // Indikator panah (tekan E)
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.YELLOW);
        g2.drawString("Press E ▼", 630, 560);
    }
}