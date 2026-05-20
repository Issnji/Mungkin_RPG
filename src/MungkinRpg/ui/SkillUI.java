package  MungkinRpg.ui;

import  MungkinRpg.player.Player;
import  MungkinRpg.weapon.skills.Skill;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class SkillUI {
    private Player player;

    public SkillUI(Player player) {
        this.player = player;
    }

    public void draw(Graphics2D g2) {
        if (!player.getWeaponManager().hasWeapon()) return;

        Skill[] skills = player.getWeaponManager().getEquippedWeapon().getSkills();
        int x = 550;
        int y = 500;

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(x - 10, y - 20, 240, 80);

        for (int i = 0; i < skills.length; i++) {
            Skill skill = skills[i];
            int drawY = y + (i * 25);

            if (skill.isReady()) {
                g2.setColor(Color.GREEN);
            } else {
                g2.setColor(Color.RED);
            }

            String key = switch (i) {
                case 0 -> "[K]";
                case 1 -> "[L]";
                case 2 -> "[;]";
                default -> "[?]";
            };

            g2.drawString(key + " " + skill.getName() + " (" + skill.getCurrentCooldown() + ")", x, drawY);
        }
    }
}