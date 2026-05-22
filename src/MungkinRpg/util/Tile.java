package MungkinRpg.util;

import java.awt.Color;

/**
 * Satu tipe tile — menyimpan nama, warna dasar, warna detail, dan apakah solid.
 * Tambahkan tile baru di TileManager.
 */
public class Tile {
    private String name;
    private Color  baseColor;
    private Color  detailColor; // untuk pola di dalam tile
    private boolean solid;

    public Tile(String name, Color baseColor, Color detailColor, boolean solid) {
        this.name        = name;
        this.baseColor   = baseColor;
        this.detailColor = detailColor;
        this.solid       = solid;
    }

    public String  getName()        { return name; }
    public Color   getBaseColor()   { return baseColor; }
    public Color   getDetailColor() { return detailColor; }
    public boolean isSolid()        { return solid; }
}