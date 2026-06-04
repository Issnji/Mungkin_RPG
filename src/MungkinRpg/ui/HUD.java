package MungkinRpg.ui;

import  MungkinRpg.player.Player;
import  MungkinRpg.util.Constants;

import java.awt.*;

public class HUD {
    private Player player;

    public HUD(Player player) {
        this.player = player;
    }

    public void update() {}

    public void draw(Graphics2D g2) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        // HP Bar
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(20, 35, 200, 20);

        g2.setColor(Color.RED);
        int hpWidth = (int)(200.0 * player.getStats().getCurrentHp() / player.getStats().getMaxHp());
        g2.fillRect(20, 35, hpWidth, 20);

        String hpText = player.getStats().getCurrentHp() + "/" + player.getStats().getMaxHp();

        g2.setColor(Color.BLACK);
        g2.drawString(hpText, 91, 50);

        g2.setColor(Color.WHITE);
        g2.drawString(hpText, 90, 49);

        // Level & Gold
        g2.setColor(Color.BLACK);
        g2.drawString("Lv: " + player.getLevelSystem().getLevel(), 21, 71);
        g2.drawString("Gold: " + player.getInventory().getGold(), 21, 91);

        g2.setColor(Color.WHITE);
        g2.drawString("Lv: " + player.getLevelSystem().getLevel(), 20, 70);
        g2.drawString("Gold: " + player.getInventory().getGold(), 20, 90);

        // Weapon Info
        if (player.getWeaponManager().hasWeapon()) {

            g2.setColor(Color.BLACK);
            g2.drawString("Weapon: " + player.getWeaponManager().getEquippedWeapon().getName(), 21, 111);

            g2.setColor(Color.WHITE);
            g2.drawString("Weapon: " + player.getWeaponManager().getEquippedWeapon().getName(), 20, 110);

            // Skill cooldowns
            var skills = player.getWeaponManager().getEquippedWeapon().getSkills();

            for (int i = 0; i < skills.length; i++) {

                String status = skills[i].isReady()
                        ? "READY"
                        : skills[i].getCurrentCooldown() + "";

                int skillY = 132 + (i * 20);

                g2.setColor(Color.BLACK);
                g2.drawString("Skill " + (i + 1) + ": " + status, 21, skillY + 1);

                g2.setColor(Color.WHITE);
                g2.drawString("Skill " + (i + 1) + ": " + status, 20, skillY);
            }

        } else {

            g2.setColor(Color.RED);
            g2.drawString("NO WEAPON! Buy at shop!", 20, 140);
        }
    }
}