package MungkinRpg.dungeon;

import MungkinRpg.core.GamePanel;
import MungkinRpg.core.SceneManager;
import MungkinRpg.dungeon.dungeon1.DungeonOne;
import MungkinRpg.dungeon.dungeon2.DungeonTwo;
import MungkinRpg.dungeon.dungeon3.DungeonBoss;
import MungkinRpg.player.Player;
import java.awt.*;

public class DungeonManager {
    private GamePanel    panel;
    private Player       player;
    private SceneManager sceneManager;

    private DungeonOne  dungeon1;
    private DungeonTwo  dungeon2;
    private DungeonBoss dungeon3;
    private Dungeon     currentDungeon;

    public DungeonManager(GamePanel panel, Player player, SceneManager sceneManager) {
        this.panel        = panel;
        this.player       = player;
        this.sceneManager = sceneManager;

        dungeon1 = new DungeonOne(player);
        dungeon2 = new DungeonTwo(player);
        dungeon3 = new DungeonBoss(player);
    }

    // ----------------------------------------------------------------
    /**
     * Dipanggil saat player masuk gate di kota.
     * Langsung masuk dungeon berikutnya tanpa layar pemilihan:
     *   - Dungeon 1 → jika belum clear
     *   - Dungeon 2 → jika dungeon 1 sudah clear
     *   - Dungeon 3 → jika dungeon 2 sudah clear
     *   - Dungeon 3 lagi → jika semua sudah clear (replay boss)
     */
    public void enterDirect() {
        if      (!dungeon1.isCleared()) startDungeon(1);
        else if (!dungeon2.isCleared()) startDungeon(2);
        else                            startDungeon(3);
    }

    // Untuk kompatibilitas dengan SceneManager lama
    public void enterLobby() { enterDirect(); }

    // ----------------------------------------------------------------
    public void startDungeon(int id) {
        switch (id) {
            case 1 -> currentDungeon = dungeon1;
            case 2 -> currentDungeon = dungeon2;
            case 3 -> currentDungeon = dungeon3;
            default -> { return; }
        }
        currentDungeon.init();
        player.resetPosition();
    }

    // ----------------------------------------------------------------
    public void update() {
        if (currentDungeon == null) return;

        currentDungeon.update();

        // --- Dungeon selesai ---
        if (currentDungeon.isComplete()) {
            currentDungeon.clearDungeon();

            // Hitung reward sebelum diberikan agar bisa ditampilkan di layar
            int expBonus  = player.getLevelSystem().getExpToNextLevel();
            int goldBonus = 100 * player.getLevelSystem().getLevel();

            // Berikan reward ke player
            player.getLevelSystem().addDungeonClearBonus();

            int newLevel = player.getLevelSystem().getLevel();

            // Tampilkan layar reward alih-alih langsung kembali ke kota
            sceneManager.showDungeonClear(
                    currentDungeon.getName(),
                    expBonus,
                    goldBonus,
                    newLevel
            );
            return;
        }

        // --- Player mati ---
        if (player.isDead()) {
            sceneManager.changeScene(SceneManager.Scene.GAME_OVER);
        }
    }

    // ----------------------------------------------------------------
    public void draw(Graphics2D g2) {
        if (currentDungeon != null) currentDungeon.draw(g2);
    }

    // ----------------------------------------------------------------
    public boolean isDungeonCleared(int id) {
        return switch (id) {
            case 1 -> dungeon1.isCleared();
            case 2 -> dungeon2.isCleared();
            case 3 -> dungeon3.isCleared();
            default -> false;
        };
    }
}