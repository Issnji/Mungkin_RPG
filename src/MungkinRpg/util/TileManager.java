package MungkinRpg.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * ====================================================
 *  TILE ID LEGEND  (dipakai di array TownMap.MAP_DATA)
 * ====================================================
 *  0  GRASS           hijau terang
 *  1  GRASS_DARK      hijau gelap (checkerboard)
 *  2  PATH_STONE      batu jalan terang
 *  3  PATH_STONE_DARK batu jalan gelap (alternating)
 *  4  PATH_EDGE       garis tepi jalan (coklat gelap)
 *  5  DIRT            tanah coklat
 *  6  WATER           air biru (untuk masa depan)
 *  7  WALL_STONE      dinding batu (solid)
 *  8  FLOOR_DUNGEON   lantai dungeon gelap
 *  9  SAND            pasir/jalan kering
 * ====================================================
 *
 * Cara menambah tile baru:
 *   1. Tambah konstanta int di bawah
 *   2. Tambah definisi tiles[ID] di initTiles()
 *   3. Gambar di switch di drawTile()
 *   4. Pakai ID tersebut di array MAP_DATA
 */
public class TileManager {

    // ---- Konstanta ID tile (mudah dibaca saat menulis MAP_DATA) ----
    public static final int GRASS            = 0;
    public static final int GRASS_DARK       = 1;
    public static final int PATH_STONE       = 2;
    public static final int PATH_STONE_DARK  = 3;
    public static final int PATH_EDGE        = 4;
    public static final int DIRT             = 5;
    public static final int WATER            = 6;
    public static final int WALL_STONE       = 7;
    public static final int FLOOR_DUNGEON    = 8;
    public static final int SAND             = 9;

    private Tile[] tiles;

    // Optional sprite sheet — taruh "tileset.png" di assets/images/
    // tiap tile berukuran TILE_SIZE x TILE_SIZE dalam satu baris
    private BufferedImage tileset;

    public TileManager() {
        tiles = new Tile[32];
        initTiles();
        tileset = AssetLoader.loadImage("tileset.png");
    }

    // ----------------------------------------------------------------
    // Definisi warna tiap tile
    // ----------------------------------------------------------------
    private void initTiles() {
        //              ID             Nama               Warna Dasar                    Warna Detail              Solid?
        tiles[GRASS]           = new Tile("Grass",           new Color( 72, 130,  60), new Color( 58, 110,  48), false);
        tiles[GRASS_DARK]      = new Tile("Grass Dark",      new Color( 60, 110,  50), new Color( 48,  92,  40), false);
        tiles[PATH_STONE]      = new Tile("Path Stone",      new Color(170, 145, 100), new Color(140, 118,  80), false);
        tiles[PATH_STONE_DARK] = new Tile("Path Stone Dark", new Color(150, 128,  88), new Color(122, 100,  68), false);
        tiles[PATH_EDGE]       = new Tile("Path Edge",       new Color(120,  95,  58), new Color( 90,  70,  40), false);
        tiles[DIRT]            = new Tile("Dirt",            new Color(140, 110,  70), new Color(115,  90,  55), false);
        tiles[WATER]           = new Tile("Water",           new Color( 60, 130, 210), new Color( 40, 100, 175), false);
        tiles[WALL_STONE]      = new Tile("Wall Stone",      new Color( 90,  80,  72), new Color( 65,  58,  50), true );
        tiles[FLOOR_DUNGEON]   = new Tile("Floor Dungeon",   new Color( 45,  38,  55), new Color( 30,  25,  40), false);
        tiles[SAND]            = new Tile("Sand",            new Color(210, 185, 130), new Color(185, 162, 108), false);
    }

    // ----------------------------------------------------------------
    // Render seluruh peta
    // ----------------------------------------------------------------
    public void drawMap(Graphics2D g2, int[][] mapData, int tileSize) {
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                drawTile(g2, mapData[row][col], col * tileSize, row * tileSize, tileSize);
            }
        }
    }

    // ----------------------------------------------------------------
    // Render satu tile
    // ----------------------------------------------------------------
    public void drawTile(Graphics2D g2, int id, int x, int y, int size) {
        if (id < 0 || id >= tiles.length || tiles[id] == null) return;

        // Jika ada tileset PNG, ambil dari sprite sheet
        if (tileset != null) {
            int sw = tileset.getWidth()  / size;  // jumlah kolom sprite
            int srcX = (id % sw) * size;
            int srcY = (id / sw) * size;
            g2.drawImage(tileset, x, y, x + size, y + size,
                         srcX, srcY, srcX + size, srcY + size, null);
            return;
        }

        // Fallback: gambar dengan Graphics2D
        Tile t = tiles[id];
        g2.setColor(t.getBaseColor());
        g2.fillRect(x, y, size, size);

        switch (id) {
            case GRASS, GRASS_DARK -> drawGrassDetail(g2, t, x, y, size);
            case PATH_STONE, PATH_STONE_DARK -> drawCobblestone(g2, t, x, y, size);
            case PATH_EDGE -> drawPathEdge(g2, t, x, y, size);
            case WATER     -> drawWater(g2, t, x, y, size);
            case WALL_STONE -> drawWallStone(g2, t, x, y, size);
            case FLOOR_DUNGEON -> drawDungeonFloor(g2, t, x, y, size);
        }
    }

    // ----------------------------------------------------------------
    // Detail per tile
    // ----------------------------------------------------------------

    // Rumput: garis-garis blade halus di tepi atas
    private void drawGrassDetail(Graphics2D g2, Tile t, int x, int y, int size) {
        g2.setColor(t.getDetailColor());
        // Outline tipis bawah dan kanan agar tile terlihat
        g2.drawLine(x, y + size - 1, x + size - 1, y + size - 1);
        g2.drawLine(x + size - 1, y, x + size - 1, y + size - 1);
        // Beberapa blade rumput kecil
        g2.setColor(new Color(t.getDetailColor().getRed(),
                              t.getDetailColor().getGreen() + 10,
                              t.getDetailColor().getBlue(), 160));
        for (int bx = x + 4; bx < x + size - 4; bx += 8) {
            g2.drawLine(bx, y + size - 4, bx - 2, y + size - 8);
            g2.drawLine(bx + 3, y + size - 4, bx + 5, y + size - 9);
        }
    }

    // Batu jalan: gambar batu-batu kecil (cobblestone)
    private void drawCobblestone(Graphics2D g2, Tile t, int x, int y, int size) {
        g2.setColor(t.getDetailColor());
        // Pola batu 2×2 di dalam satu tile 32×32
        // Batu kiri-atas
        g2.drawRoundRect(x + 2,          y + 2,          size/2 - 4, size/2 - 4, 3, 3);
        // Batu kanan-atas
        g2.drawRoundRect(x + size/2 + 1, y + 2,          size/2 - 4, size/2 - 4, 3, 3);
        // Batu kiri-bawah
        g2.drawRoundRect(x + 2,          y + size/2 + 1, size/2 - 4, size/2 - 4, 3, 3);
        // Batu kanan-bawah
        g2.drawRoundRect(x + size/2 + 1, y + size/2 + 1, size/2 - 4, size/2 - 4, 3, 3);
        // Highlight sudut putih kecil
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillRect(x + 3, y + 3, 4, 2);
        g2.fillRect(x + size/2 + 2, y + 3, 4, 2);
    }

    // Tepi jalan: garis-garis horizontal gelap
    private void drawPathEdge(Graphics2D g2, Tile t, int x, int y, int size) {
        g2.setColor(t.getDetailColor());
        for (int ly = y + 4; ly < y + size; ly += 6) {
            g2.drawLine(x, ly, x + size, ly);
        }
        // Garis tepi atas/bawah lebih tebal
        g2.setColor(new Color(t.getDetailColor().getRed() - 20,
                              t.getDetailColor().getGreen() - 20,
                              t.getDetailColor().getBlue() - 20));
        g2.fillRect(x, y, size, 3);
    }

    // Air: gelombang horizontal animasi sederhana (statik)
    private void drawWater(Graphics2D g2, Tile t, int x, int y, int size) {
        g2.setColor(new Color(255, 255, 255, 30));
        for (int wy = y + 6; wy < y + size; wy += 8) {
            g2.drawArc(x + 2, wy, 10, 4, 0, 180);
            g2.drawArc(x + 14, wy, 10, 4, 0, 180);
            g2.drawArc(x + 22, wy, 10, 4, 0, 180);
        }
    }

    // Dinding batu: pola bata horizontal
    private void drawWallStone(Graphics2D g2, Tile t, int x, int y, int size) {
        g2.setColor(t.getDetailColor());
        // Baris bata
        for (int row = 0; row < 2; row++) {
            int ry   = y + row * (size / 2);
            int offX = (row % 2 == 0) ? 0 : size / 4;
            g2.drawRect(x + offX, ry + 2, size / 2 - 2, size / 2 - 4);
            g2.drawRect(x + offX + size / 2, ry + 2, size / 2 - 2, size / 2 - 4);
        }
        g2.setColor(new Color(255, 255, 255, 25));
        g2.fillRect(x + 2, y + 2, size / 4, 3);
    }

    // Lantai dungeon: pola kotak gelap
    private void drawDungeonFloor(Graphics2D g2, Tile t, int x, int y, int size) {
        g2.setColor(t.getDetailColor());
        g2.drawRect(x + 2, y + 2, size - 4, size - 4);
        g2.setColor(new Color(255, 255, 255, 12));
        g2.fillRect(x + 3, y + 3, size / 2 - 2, size / 2 - 2);
    }

    // ----------------------------------------------------------------
    public Tile getTile(int id) {
        if (id < 0 || id >= tiles.length) return null;
        return tiles[id];
    }

    public boolean isSolid(int id) {
        Tile t = getTile(id);
        return t != null && t.isSolid();
    }
}