package  MungkinRpg.shop;

import  MungkinRpg.core.InputHandler;
import  MungkinRpg.player.Player;
import  MungkinRpg.weapon.Bow;
import  MungkinRpg.weapon.Spear;
import  MungkinRpg.weapon.Sword;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Shop {
    private Player player;
    private List<ShopItem> items;
    private boolean open;
    private int selectedIndex;

    public Shop(Player player) {
        this.player = player;
        this.items = new ArrayList<>();
        this.open = false;

        items.add(new ShopItem("Iron Sword",  "Pedang dasar dengan damage stabil",   50, new Sword()));
        items.add(new ShopItem("Wooden Bow",  "Bow untuk serangan jarak jauh",        50, new Bow()));
        items.add(new ShopItem("Iron Spear",  "Spear dengan jangkauan menengah",      50, new Spear()));
    }

    public void open() {
        open = true;
        selectedIndex = 0;
    }

    // FIX: close() was called from TownMap but didn't exist
    public void close() {
        open = false;
    }

    public void update(InputHandler input) {
        if (input.escape) {
            open = false;
            input.escape = false;
            return;
        }
        if (input.up) {
            selectedIndex = Math.max(0, selectedIndex - 1);
            input.up = false;
        }
        if (input.down) {
            selectedIndex = Math.min(items.size() - 1, selectedIndex + 1);
            input.down = false;
        }
        if (input.interact) {
            buyItem(selectedIndex);
            input.interact = false;
        }
    }

    private void buyItem(int index) {
        ShopItem item = items.get(index);
        if (player.getInventory().spendGold(item.getPrice())) {
            player.getWeaponManager().equipWeapon(item.getWeapon());
            System.out.println("Beli " + item.getName() + " berhasil!");
        } else {
            System.out.println("Gold tidak cukup!");
        }
    }

    public void draw(Graphics2D g2) {

        int x = 150;
        int y = 100;
        int w = 500;
        int h = 400;

        // =========================
        // PANEL SHOP
        // =========================

        // Shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(x + 5, y + 5, w, h, 18, 18);

        // Outer frame
        g2.setColor(new Color(90, 50, 20));
        g2.fillRoundRect(x, y, w, h, 18, 18);

        // Inner wood
        g2.setColor(new Color(140, 90, 40));
        g2.fillRoundRect(x + 6, y + 6, w - 12, h - 12, 14, 14);

        // Main panel
        g2.setColor(new Color(205, 170, 110));
        g2.fillRoundRect(x + 12, y + 12, w - 24, h - 24, 10, 10);

        // =========================
        // TITLE
        // =========================

        g2.setFont(new Font("Monospaced", Font.BOLD, 20));

        g2.setColor(new Color(60, 30, 10));
        g2.drawString("WEAPON SHOP", 185, 140);

        g2.setColor(new Color(255, 225, 140));
        g2.drawString("WEAPON SHOP", 183, 138);

        // Gold info
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));

        g2.setColor(new Color(70, 40, 15));
        g2.drawString("Gold: " + player.getInventory().getGold(), 185, 165);

        // =========================
        // ITEM LIST
        // =========================

        for (int i = 0; i < items.size(); i++) {

            int itemY = 210 + (i * 70);

            ShopItem item = items.get(i);

            // Selected item background
            if (i == selectedIndex) {

                g2.setColor(new Color(170, 120, 40));
                g2.fillRoundRect(170, itemY - 28, 460, 58, 12, 12);

                // highlight
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(172, itemY - 26, 456, 18, 10, 10);

                g2.setColor(new Color(255, 235, 170));

            } else {

                g2.setColor(new Color(75, 45, 20));
            }

            // Weapon name
            g2.setFont(new Font("Monospaced", Font.BOLD, 15));
            g2.drawString(item.getName() + " - " + item.getPrice() + "G", 190, itemY);

            // Description
            g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
            g2.setColor(new Color(95, 60, 30));
            g2.drawString(item.getDescription(), 190, itemY + 20);
        }

        // =========================
        // BOTTOM INFO
        // =========================

        g2.setFont(new Font("Monospaced", Font.BOLD, 13));

        g2.setColor(new Color(70, 40, 15));
        g2.drawString("E to Buy | ESC to Close", 185, 470);
    }

    public boolean isOpen() { return open; }
}