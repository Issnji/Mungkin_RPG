package MungkinRpg.ui;

import  MungkinRpg.player.Player;
import  MungkinRpg.util.Constants;
import java.awt.Graphics2D;
import java.awt.Color;

public class HUD {
    private Player player;

    public HUD(Player player) {
        this.player = player;
    }

    public void update() {}

    public void draw(Graphics2D g2) {
        // HP Bar
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(20, 20, 200, 20);
        g2.setColor(Color.RED);
        int hpWidth = (int)(200.0 * player.getStats().getCurrentHp() / player.getStats().getMaxHp());
        g2.fillRect(20, 20, hpWidth, 20);
        g2.setColor(Color.WHITE);
        g2.drawString(player.getStats().getCurrentHp() + "/" + player.getStats().getMaxHp(), 90, 35);

        // Level & Gold
        g2.drawString("Lv: " + player.getLevelSystem().getLevel(), 20, 70);
        g2.drawString("Gold: " + player.getInventory().getGold(), 20, 90);

        // Weapon Info
        if (player.getWeaponManager().hasWeapon()) {
            g2.drawString("Weapon: " + player.getWeaponManager().getEquippedWeapon().getName(), 20, 110);
            // Skill cooldowns
            var skills = player.getWeaponManager().getEquippedWeapon().getSkills();
            for (int i = 0; i < skills.length; i++) {
                String status = skills[i].isReady() ? "READY" : skills[i].getCurrentCooldown() + "";
                g2.drawString("Skill " + (i+1) + ": " + status, 20, 130 + (i * 20));
            }
        } else {
            g2.setColor(Color.RED);
            g2.drawString("NO WEAPON! Buy at shop!", 20, 110);
        }
    }
}