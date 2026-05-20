package MungkinRpg.save;

import MungkinRpg.player.Player;
import MungkinRpg.dungeon.DungeonManager;
import java.io.*;

public class SaveManager {
    private static final String SAVE_PATH = "save/player_save.dat";

    public static void save(Player player, DungeonManager dungeonManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
            PlayerData data = new PlayerData();
            data.level = player.getLevelSystem().getLevel();
            data.exp = player.getLevelSystem().getExp();
            data.gold = player.getInventory().getGold();
            data.maxHp = player.getStats().getMaxHp();
            data.attack = player.getStats().getAttack();
            data.defense = player.getStats().getDefense();
            data.dungeon1Cleared = dungeonManager.isDungeonCleared(1);
            data.dungeon2Cleared = dungeonManager.isDungeonCleared(2);

            if (player.getWeaponManager().hasWeapon()) {
                data.equippedWeapon = player.getWeaponManager().getEquippedWeapon().getName();
            }

            oos.writeObject(data);
            System.out.println("Game saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData load() {
        File file = new File(SAVE_PATH);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_PATH))) {
            return (PlayerData) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}