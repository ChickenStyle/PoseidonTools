package me.chickenstyle.poseidontools.gui.menus;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.gui.Button;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import me.chickenstyle.poseidontools.gui.Menu;
import me.chickenstyle.poseidontools.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantUpgradeMenu extends Menu {

    private PoseidonTool tool;
    private List<Button> enchants;
    private int currentItem;
    private Player player;

    public EnchantUpgradeMenu(PoseidonTool tool, Player player) {
        super(Bukkit.createInventory(null, 45,
                Utils.color(PoseidonTools.getInstance().getMenuConfig().getString("enchantmentUpgrade.title"))
                ), true);
        this.tool = tool;
        this.player = player;
        loadEnchantItems();
        this.currentItem = 0;
        fillBackground();

        List<Button> items = getShowingEnchants();

        for (int i = 0; i < 3; i++) {
            setButton(21 + i, items.get(i));
        }

        setButton(38, new Button(ItemStackBuilder.buildItemStack(
                PoseidonTools.getInstance().getMenuConfig().getConfig(),
                "enchantmentUpgrade.buttons.previousEnchantment",null
        )) {
            @Override
            public void onClick(Menu menu, InventoryClickEvent event) {
                if (currentItem == 0) {
                    currentItem = enchants.size();
                }
                currentItem--;

                List<Button> items = getShowingEnchants();

                for (int i = 0; i < 3; i++) {
                    menu.setButton(21 + i, items.get(i));
                }
                menu.loadButtons();
            }
        });
        setButton(42, new Button(ItemStackBuilder.buildItemStack(
                PoseidonTools.getInstance().getMenuConfig().getConfig(),
                "enchantmentUpgrade.buttons.nextEnchantment",null
        )) {
            @Override
            public void onClick(Menu menu, InventoryClickEvent event) {
                if (currentItem == enchants.size() - 1) {
                    currentItem = -1;
                }
                currentItem++;
                List<Button> items = getShowingEnchants();
                for (int i = 0; i < 3; i++) {
                    menu.setButton(21 + i, items.get(i));
                }
                menu.loadButtons();
            }
        });
    }

    private List<Button> getShowingEnchants() {
        List<Button> items = new ArrayList<>();
        int currentItem = this.currentItem;
        for (int i = 0; i < 3; i++) {
            if (currentItem + 1 > enchants.size()) currentItem = 0;
            items.add(enchants.get(currentItem));
            currentItem++;
        }
        return items;
    }

    private void loadEnchantItems() {
        this.enchants = new ArrayList<>();
        for (String enchant : tool.getToolType().getEnchantments()) {

            int upgradePrice = PoseidonTools.getInstance().getConfig()
                    .getInt("toolsData." + tool.getToolType() + ".enchantments." + enchant
                            + ".upgradePrice");

            int tokenAmount = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                if (PoseidonTools.getInstance().getNMS().hasTag(item, "Token")) {
                    tokenAmount += item.getAmount();
                }
            }

            int anotherTokenAmount = tokenAmount;

            String canAfford = PoseidonTools.getInstance().getMenuConfig().getString("enchantmentUpgrade.canAfford");
            String cannotAfford = PoseidonTools.getInstance().getMenuConfig()
                    .getString("enchantmentUpgrade.cannotAfford");

            enchants.add(new Button(
                    ItemStackBuilder.buildItemStack(
                            PoseidonTools.getInstance().getMenuConfig().getConfig(),
                            "enchantmentUpgrade.enchantments." + enchant,
                            new PlaceHolder("current-level", tool.getEnchantmentsLevel().get(enchant)+ ""),
                            new PlaceHolder("max-level", tool.getMaxLevel() + ""),
                            new PlaceHolder("upgrade-price", upgradePrice + ""),
                            new PlaceHolder("can-afford",
                                    tokenAmount >= upgradePrice ? canAfford : cannotAfford))) {
                @Override
                public void onClick(Menu menu, InventoryClickEvent event) {
                    if (anotherTokenAmount >= upgradePrice) {
                        ItemStack token = ItemStackBuilder.buildToken();
                        token.setAmount(upgradePrice);
                        player.getInventory().removeItem(token);
                        tool.getEnchantmentsLevel().put(
                                enchant, tool.getEnchantmentsLevel().get(enchant) + 1
                        );
                        player.setItemInHand(ToolBuilder.toItemStack(tool));
                        player.updateInventory();
                        loadEnchantItems();
                        List<Button> items = getShowingEnchants();
                        for (int i = 0; i < 3; i++) {
                            menu.setButton(21 + i, items.get(i));
                        }
                        menu.loadButtons();
                        player.sendMessage(Message.ENCHANTMENT_UPGRADE.toMSG(
                                new PlaceHolder("enchantment",
                                        enchant.replace("_", " ")),
                                new PlaceHolder("enchantment-level",
                                        tool.getEnchantmentsLevel().get(enchant) + "")
                        ));
                    } else {
                        player.sendMessage(Message.FAILED_ENCHANTMENT_UPGRADE.toMSG(
                            new PlaceHolder("enchantment", enchant.replace("_", " "))
                        ));
                    }
                }
            });
        }
    }

}
