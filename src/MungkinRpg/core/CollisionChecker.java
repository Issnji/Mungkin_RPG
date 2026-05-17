package MungkinRpg.core;

import com.MungkinRpg.player.Player;
import java.awt.Rectangle;

public class CollisionChecker {

    public static boolean checkEntityCollision(Rectangle hitbox1, Rectangle hitbox2) {
        return hitbox1.intersects(hitbox2);
    }

    public static boolean checkTileCollision(Player player, int[][] tileMap, int tileSize) {
        Rectangle hitbox = player.getHitbox();

        int leftCol = hitbox.x / tileSize;
        int rightCol = (hitbox.x + hitbox.width) / tileSize;
        int topRow = hitbox.y / tileSize;
        int bottomRow = (hitbox.y + hitbox.height) / tileSize;

        // Cek tile di 4 sudut hitbox
        if (isSolid(tileMap, leftCol, topRow)) return true;
        if (isSolid(tileMap, rightCol, topRow)) return true;
        if (isSolid(tileMap, leftCol, bottomRow)) return true;
        if (isSolid(tileMap, rightCol, bottomRow)) return true;

        return false;
    }

    private static boolean isSolid(int[][] tileMap, int col, int row) {
        if (row < 0 || row >= tileMap.length || col < 0 || col >= tileMap[0].length) {
            return true; // Di luar map = solid (boundary)
        }
        return tileMap[row][col] == 1; // 1 = solid wall
    }
}