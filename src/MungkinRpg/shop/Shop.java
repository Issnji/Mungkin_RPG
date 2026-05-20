package  MungkinRpg.shop;

import  MungkinRpg.player.Player;
import  MungkinRpg.weapon.Sword;
import  MungkinRpg.weapon.Bow;
import  MungkinRpg.weapon.Spear;
import  MungkinRpg.core.InputHandler;
import java.awt.Graphics2D;
import java.awt.Color;
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
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(150, 100, 500, 400);
        g2.setColor(Color.WHITE);
        g2.drawRect(150, 100, 500, 400);
        g2.drawString("WEAPON SHOP - Gold: " + player.getInventory().getGold(), 170, 130);

        for (int i = 0; i < items.size(); i++) {
            int y = 170 + (i * 60);
            ShopItem item = items.get(i);
            if (i == selectedIndex) {
                g2.setColor(Color.YELLOW);
                g2.fillRect(160, y - 20, 480, 50);
            }
            g2.setColor(Color.WHITE);
            g2.drawString(item.getName() + " - " + item.getPrice() + "G", 180, y);
            g2.drawString(item.getDescription(), 180, y + 20);
        }
        g2.drawString("E to Buy | ESC to Close", 170, 470);
    }

    public boolean isOpen() { return open; }
}