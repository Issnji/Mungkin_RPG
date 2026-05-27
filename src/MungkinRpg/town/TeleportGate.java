package MungkinRpg.town;

import MungkinRpg.core.SceneManager;
import MungkinRpg.util.AssetLoader; // Tambahkan import ini
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage; // Tambahkan import ini

public class TeleportGate {
    private int x, y;
    private Rectangle hitbox;
    private SceneManager sceneManager;
    private boolean playerNear;

    // Variabel untuk menyimpan gambar portal
    private BufferedImage portalImage;

    public TeleportGate(int x, int y, SceneManager sceneManager) {
        this.x = x;
        this.y = y;
        this.sceneManager = sceneManager;
        this.hitbox = new Rectangle(x, y, 64, 80);

        // Memuat gambar saat portal dibuat
        this.portalImage = AssetLoader.loadImage("portal.png");
    }

    public Rectangle getHitbox() { return hitbox; }

    public void update(boolean near) {
        this.playerNear = near;
    }

    public void activate() {
        sceneManager.changeScene(SceneManager.Scene.DUNGEON);
    }

    public void draw(Graphics2D g2) {
        if (portalImage != null) {
            // Gambar di-scale (diperbesar) sedikit melebihi hitbox agar proporsional
            // x - 16 dan y - 16 adalah offset agar gambar tetap berada di tengah hitbox
            g2.drawImage(portalImage, x - 90, y - 50, 250, 180, null);
        } else {
            // Fallback (jika gambar gagal dimuat, akan menampilkan kotak lama)
            g2.setColor(new Color(100, 50, 150));
            g2.fillRect(x, y, 64, 80);
            g2.setColor(new Color(150, 100, 255, 150 + (int)(Math.sin(System.currentTimeMillis() / 200.0) * 50)));
            g2.fillOval(x + 10, y + 15, 44, 50);
        }

        // --- Kode Papan Indikator (Press E) tetap dipertahankan ---
        // Logika indikator interaksi dipindahkan/diurus oleh drawPrompts di TownMap
    }
}