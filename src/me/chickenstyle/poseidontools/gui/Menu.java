package me.chickenstyle.poseidontools.gui;

import java.awt.*;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Menu {
    private Inventory inv;
    private HashMap<Integer,Button> buttons;
    private boolean disableClick;

    public Menu(Inventory inv) {
        this(inv, false);
    }

    public Menu(Inventory inv, boolean disableClick) {
        this.inv = inv;
        buttons = new HashMap<>();
        this.disableClick = disableClick;
    }


    public void setButton(int slot,Button button) {
        buttons.put(slot, button);
    }

    public void performClick(InventoryClickEvent event) {

        if (buttons.get(event.getSlot()) != null) {
            buttons.get(event.getSlot()).onClick(this, event);

        }

    }

    public void loadButtons() {
        buttons.forEach((slot,button) -> {
            inv.setItem(slot, button);
        });
    }

    protected void open(Player player) {
        loadButtons();
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }

    public boolean isDisableClick() {
        return disableClick;
    }

    protected void fillBackground() {
        for (int i = 0; i < getInventory().getSize(); i++) {
            Button glass = new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7)) {
                @Override
                public void onClick(Menu menu, InventoryClickEvent event) {
                    event.setCancelled(true);
                }
            };

            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            meta.addItemFlags(ItemFlag.values());
            glass.setItemMeta(meta);
            getInventory().setItem(i, glass);
        }
    }
}
