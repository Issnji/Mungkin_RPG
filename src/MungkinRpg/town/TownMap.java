package MungkinRpg.town;

import com.MungkinRpg.core.GamePanel;
import com.MungkinRpg.core.InputHandler;
import com.MungkinRpg.core.SceneManager;
import com.MungkinRpg.player.Player;
import com.MungkinRpg.shop.Shop;
import com.MungkinRpg.util.Constants;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

public class TownMap {
    private GamePanel panel;
    private Player player;
    private SceneManager sceneManager;
    private Shop shop;
    private Rectangle shopArea;
    private boolean nearShop;
    private NPC npc;
    private TeleportGate gate;

    public TownMap(GamePanel panel, Player player, SceneManager sceneManager) {
        this.panel = panel;
        this.player = player;
        this.sceneManager = sceneManager;
        this.shop = new Shop(player);
        this.shopArea = new Rectangle(350, 250, 100, 80);
        this.npc = new NPC(500, 300, "Villager", new String[]{
                "Hati-hati di dungeon!",
                "Beli senjata dulu sebelum masuk.",
                "Bos di dungeon 3 sangat kuat."
        });
        this.gate = new TeleportGate(100, 300, sceneManager);
    }

    public void update() {
        InputHandler input = panel.getInputHandler();
        player.update(input);

        // --- SHOP LOGIC ---
        nearShop = player.getHitbox().intersects(shopArea);
        if (nearShop && input.isInteract() && !shop.isOpen()) {
            shop.open();
        }
        if (shop.isOpen()) {
            shop.update(input);
            if (input.isEscape()) shop.close();
            return; // block input lain
        }

        // --- NPC LOGIC (TOMBOL SAJA) ---
        if (player.getHitbox().intersects(npc.getHitbox()) && input.isInteract() && !npc.isTalking()) {
            npc.interact();
        }
        if (npc.isTalking()) {
            // ⬇⬇⬇ TARUH 1: Logika tombol next dialog ⬇⬇⬇
            if (input.isInteract()) {
                npc.nextDialogue();
            }
            return; // block input lain saat ngomong
        }

        // --- GATE LOGIC ---
        boolean nearGate = player.getHitbox().intersects(gate.getHitbox());
        gate.update(nearGate);
        if (nearGate && input.isInteract()) {
            gate.activate();
        }

        // --- KE DUNGEON LOBBY ---
        if (input.isEscape() && !shop.isOpen() && !npc.isTalking()) {
            sceneManager.changeScene(SceneManager.Scene.DUNGEON);
        }
    }

    // ============================================================
    // 2. METHOD DRAW — Tempat gambar (ADA g2!)
    // ============================================================
    public void draw(Graphics2D g2) {
        // Background kota
        g2.setColor(new Color(100, 150, 100));
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        // Jalan
        g2.setColor(new Color(150, 120, 80));
        g2.fillRect(200, 300, 400, 100);

        // Gate & NPC
        gate.draw(g2);
        npc.draw(g2);

        // Stand Shop
        g2.setColor(Color.ORANGE);
        g2.fillRect(shopArea.x, shopArea.y, shopArea.width, shopArea.height);
        g2.setColor(Color.BLACK);
        g2.drawString("WEAPON SHOP", shopArea.x + 10, shopArea.y + 45);

        // Player
        player.draw(g2);

        // Prompts
        if (nearShop && !shop.isOpen() && !npc.isTalking()) {
            g2.setColor(Color.WHITE);
            g2.drawString("Press E to open Shop", shopArea.x, shopArea.y - 10);
        }
        boolean nearGate = player.getHitbox().intersects(gate.getHitbox());
        if (nearGate && !npc.isTalking() && !shop.isOpen()) {
            g2.setColor(Color.WHITE);
            g2.drawString("Press E to Enter Dungeon", gate.getHitbox().x - 20, gate.getHitbox().y - 10);
        }
        if (player.getHitbox().intersects(npc.getHitbox()) && !npc.isTalking() && !shop.isOpen()) {
            g2.setColor(Color.WHITE);
            g2.drawString("Press E to Talk", npc.getHitbox().x, npc.getHitbox().y - 10);
        }

        // --- UI OVERLAY (GAMBAR DI ATAS SEMUA) ---
        if (shop.isOpen()) {
            shop.draw(g2);
        }

        // ⬇⬇⬇ TARUH 2: Gambar dialog box ⬇⬇⬇
        if (npc.isTalking()) {
            npc.drawDialogue(g2);
        }

        g2.setColor(Color.WHITE);
        g2.drawString("Press ESC to go to Dungeon Lobby", 10, 20);
    }
}