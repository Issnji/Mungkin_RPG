package MungkinRpg.town;

import MungkinRpg.core.GamePanel;
import MungkinRpg.core.InputHandler;
import MungkinRpg.core.SceneManager;
import MungkinRpg.player.Player;
import MungkinRpg.shop.Shop;
import MungkinRpg.util.AssetLoader;
import MungkinRpg.util.Constants;
import MungkinRpg.util.TileManager;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TownMap {

    // ================================================================
    //  TILEMAP DATA
    //  Edit angka-angka di sini untuk mengubah tampilan peta kota.
    //
    //  ID Tile (lihat TileManager untuk daftar lengkap):
    //  0 = GRASS (hijau)        4 = PATH_EDGE (tepi jalan)
    //  1 = GRASS_DARK (gelap)   5 = DIRT (tanah)
    //  2 = PATH_STONE (batu)    7 = WALL_STONE (dinding)
    //  3 = PATH_STONE_DARK      9 = SAND (pasir)
    //
    //  Peta ini 25 kolom × 19 baris (layar 800×600, tile 32px)
    // ================================================================
    private static final int G  = TileManager.GRASS;
    private static final int GD = TileManager.GRASS_DARK;
    private static final int PS = TileManager.PATH_STONE;
    private static final int PD = TileManager.PATH_STONE_DARK;
    private static final int PE = TileManager.PATH_EDGE;
    private static final int DT = TileManager.DIRT;
    private static final int WS = TileManager.WALL_STONE;

    /**
     * MAP_DATA[baris][kolom]
     * Ubah nilai di sini untuk mendesain ulang peta kota.
     */
    public static final int[][] MAP_DATA = {
            // kol: 0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24
            /*  0 */ { G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G },
            /*  1 */ {GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD },
            /*  2 */ { G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G },
            /*  3 */ {GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD },
            /*  4 */ { G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G },
            /*  5 */ {GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD },
            /*  6 */ { G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G },
            /*  7 */ {GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD },
            /*  8 */ { G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G },
            /*  9 */ {PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE },
            /* 10 */ {PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS },
            /* 11 */ {PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD },
            /* 12 */ {PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS, PD, PS },
            /* 13 */ {PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE, PE },
            /* 14 */ {GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD },
            /* 15 */ { G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G },
            /* 16 */ {GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD },
            /* 17 */ { G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G },
            /* 18 */ {GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD,  G, GD },
    };

    // ================================================================

    private static final int TILE = Constants.TILE_SIZE; // 32

    private GamePanel     panel;
    private Player        player;
    private SceneManager  sceneManager;
    private Shop          shop;
    private Rectangle     shopArea;
    private boolean       nearShop;
    private NPC           npc;
    private TeleportGate  gate;
    private TileManager   tileManager;

    // Optional background image (overrides tilemap if present)
    private BufferedImage bgImage;
    private BufferedImage shopImage;

    public TownMap(GamePanel panel, Player player, SceneManager sceneManager) {
        this.panel        = panel;
        this.player       = player;
        this.sceneManager = sceneManager;
        this.shop         = new Shop(player);
        this.shopArea     = new Rectangle(350, 220, 110, 90);
        this.npc = new NPC(530, 305, "Villager", new String[]{
                "Hati-hati di dungeon!",
                "Beli senjata dulu sebelum masuk.",
                "Bos di dungeon 3 sangat kuat."
        });
        this.gate        = new TeleportGate(80, 280, sceneManager);
        this.tileManager = new TileManager();
        this.bgImage     = AssetLoader.loadImage("town_bg.png");
        this.shopImage = AssetLoader.loadImage("shop.png");
    }

    // ----------------------------------------------------------------
    public void update() {
        InputHandler input = panel.getInputHandler();
        player.update(input);

        nearShop = player.getHitbox().intersects(shopArea);
        if (nearShop && input.isInteract() && !shop.isOpen()) {
            shop.open();
            input.interact = false;
        }
        if (shop.isOpen()) {
            shop.update(input);
            return;
        }

        if (player.getHitbox().intersects(npc.getHitbox()) && input.isInteract() && !npc.isTalking()) {
            npc.interact();
            input.interact = false;
        }
        if (npc.isTalking()) {
            if (input.isInteract()) { npc.nextDialogue(); input.interact = false; }
            return;
        }

        boolean nearGate = player.getHitbox().intersects(gate.getHitbox());
        gate.update(nearGate);
        if (nearGate && input.isInteract()) {
            input.interact = false;
            gate.activate();
        }

        if (input.isEscape()) {
            input.escape = false;
            sceneManager.changeScene(SceneManager.Scene.DUNGEON);
        }
    }

    // ----------------------------------------------------------------
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- TILEMAP / BACKGROUND ---
        if (bgImage != null) {
            // Jika ada gambar background, pakai itu
            g2.drawImage(bgImage, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
        } else {
            // Render tilemap dari MAP_DATA
            tileManager.drawMap(g2, MAP_DATA, TILE);
            // Gambar dekorasi di atas tilemap
            drawFence(g2);
            drawTrees(g2);
        }

        // --- OBJEK ---
        gate.draw(g2);
        drawShop(g2);
        npc.draw(g2);
        player.draw(g2);

        // --- PROMPTS ---
        drawPrompts(g2);

        // --- OVERLAYS ---
        if (shop.isOpen())   shop.draw(g2);
        if (npc.isTalking()) npc.drawDialogue(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("Press ESC → Dungeon Lobby", 10, 18);
    }

    // ----------------------------------------------------------------
    // Dekorasi di atas tilemap
    // ----------------------------------------------------------------
    private void drawFence(Graphics2D g2) {
        // Pagar kayu di baris 9 (y = 9*32 = 288), tepat di atas jalan
        int fy = 9 * TILE - 10;
        g2.setColor(new Color(155, 105, 55));
        for (int fx = 0; fx < Constants.SCREEN_WIDTH; fx += TILE) {
            // Tiang
            g2.fillRoundRect(fx + 2, fy, 10, 22, 4, 4);
            // Rel horizontal
            if (fx + TILE < Constants.SCREEN_WIDTH) {
                g2.fillRect(fx + 12, fy + 6, TILE - 4, 4);
                g2.fillRect(fx + 12, fy + 14, TILE - 4, 3);
            }
            // Highlight kayu
            g2.setColor(new Color(190, 140, 80, 120));
            g2.fillRect(fx + 3, fy + 1, 4, 10);
            g2.setColor(new Color(155, 105, 55));
        }
    }

    private void drawTrees(Graphics2D g2) {
        // Posisi pohon [x, y] — sesuaikan sesuka hati
        int[][] treePos = {
                {32, 40}, {130, 10}, {200, 60}, {650, 20}, {730, 55}, {760, 160},
                {10, 165}, {48, 450}, {690, 460}, {600, 490}, {820, 400}
        };
        for (int[] p : treePos) drawTree(g2, p[0], p[1]);
    }

    private void drawTree(Graphics2D g2, int tx, int ty) {
        g2.setColor(new Color(90, 58, 25));
        g2.fillRect(tx + 10, ty + 40, 12, 28);
        g2.setColor(new Color(30, 95, 30));
        g2.fillOval(tx, ty + 14, 32, 30);
        g2.setColor(new Color(42, 115, 38));
        g2.fillOval(tx + 3, ty + 5, 28, 27);
        g2.setColor(new Color(52, 135, 46));
        g2.fillOval(tx + 7, ty, 20, 22);
        g2.setColor(new Color(80, 175, 55, 90));
        g2.fillOval(tx + 9, ty + 3, 10, 8);
    }

    private void drawShop(Graphics2D g2) {
        int sx = shopArea.x;
        int sy = shopArea.y;
        int sw = shopArea.width;
        int sh = shopArea.height;

        if (shopImage != null) {
            // Menggambar shop.png dengan offset koordinat agar bangunan berdiri pas di atas area jalan
            g2.drawImage(shopImage, sx - 175, sy - 135, sw + 320, sh + 200, null);
        } else {
            // Fallback render geometris lama jika file shop.png tidak ditemukan
            g2.setColor(new Color(130, 40, 20));
            g2.fillPolygon(new int[]{sx-15, sx+sw/2, sx+sw+15}, new int[]{sy, sy-40, sy}, 3);
            g2.setColor(new Color(180, 140, 90));
            g2.fillRect(sx, sy, sw, sh);

            // Papan nama WEAPONS
            g2.setColor(new Color(95, 58, 18));
            g2.fillRoundRect(sx+sw/2-45, sy+8, 90, 22, 6, 6);
            g2.setColor(new Color(255, 215, 75));
            g2.setFont(new Font("Serif", Font.BOLD, 13));
            g2.drawString("WEAPONS", sx+sw/2-33, sy+24);
        }
    }

    private void drawPrompts(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        if (nearShop && !shop.isOpen() && !npc.isTalking())
            drawPromptBox(g2, "Press E to open Shop",
                    shopArea.x + shopArea.width/2, shopArea.y - 8);

        boolean nearGate = player.getHitbox().intersects(gate.getHitbox());
        if (nearGate && !npc.isTalking() && !shop.isOpen())
            drawPromptBox(g2, "Press E to Enter Dungeon",
                    gate.getHitbox().x + gate.getHitbox().width/2, gate.getHitbox().y - 8);

        if (player.getHitbox().intersects(npc.getHitbox()) && !npc.isTalking() && !shop.isOpen())
            drawPromptBox(g2, "Press E to Talk",
                    npc.getHitbox().x + 16, npc.getHitbox().y - 8);
    }

    private void drawPromptBox(Graphics2D g2, String text, int cx, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        int bx = cx - tw/2 - 8;
        g2.setColor(new Color(0, 0, 0, 175));
        g2.fillRoundRect(bx, y-16, tw+16, 22, 8, 8);
        g2.setColor(Color.WHITE);
        g2.drawString(text, bx+8, y);
    }
}