package MungkinRpg.ui;

import com.MungkinRpg.player.Player;
import com.MungkinRpg.core.InputHandler;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class InventoryUI {
    private Player player;
    private boolean visible;

    public InventoryUI(Player player) {
        this.player = player;
        this.visible = false;
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() { return visible; }

    public void update(InputHandler input) {
        if (input.escape) {
            visible = false;
            input.escape = false;
        }
    }

    public void draw(Graphics2D g2) {
        if (!visible) return;

        g2.setColor(new Color(30, 30, 30, 220));
        g2.fillRect(200, 100, 400, 400);
        g2.setColor(Color.WHITE);
        g2.drawRect(200, 100, 400, 400);

        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.drawString("INVENTORY", 330, 140);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Gold: " + player.getInventory().getGold(), 230, 180);

        // Equipment slot
        g2.drawString("Equipped Weapon:", 230, 220);
        if (player.getWeaponManager().hasWeapon()) {
            g2.setColor(Color.YELLOW);
            g2.drawString(player.getWeaponManager().getEquippedWeapon().getName(), 230, 250);
        } else {
            g2.setColor(Color.GRAY);
            g2.drawString("None", 230, 250);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("ESC to close", 230, 470);
    }
}