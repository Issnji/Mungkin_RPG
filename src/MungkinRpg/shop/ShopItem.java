package MungkinRpg.shop;

import com.MungkinRpg.weapon.Weapon;

public class ShopItem {
    private String name;
    private String description;
    private int price;
    private Weapon weapon;

    public ShopItem(String name, String description, int price, Weapon weapon) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weapon = weapon;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public Weapon getWeapon() { return weapon; }
}