package me.chickenstyle.poseidontools.gui.menus;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.gui.Button;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import me.chickenstyle.poseidontools.gui.Menu;
import me.chickenstyle.poseidontools.utils.PoseidonTool;
import me.chickenstyle.poseidontools.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UpgradeToolMenu extends Menu {

    private PoseidonTool tool;

    public UpgradeToolMenu(PoseidonTool tool) {
        super(Bukkit.createInventory(null, 45,
                Utils.color(PoseidonTools.getInstance().getMenuConfig().getString("upgradeInventory.title")))
                , true);
        this.tool = tool;
        fillBackground();
        initButtons();
    }

    private void initButtons() {
        // 21 23

        setButton(
                21,
                new Button(PoseidonTools.getInstance().getNMS()
                                .addGlow(ItemStackBuilder
                                        .buildItemStack("upgradeInventory.buttons.enchantmentsButton"))) {
                    @Override
                    public void onClick(Menu menu, InventoryClickEvent event) {
                        PoseidonTools.getInstance().getMenuHandler().closeMenu((Player) event.getWhoClicked());

                        PoseidonTools.getInstance().getMenuHandler().openMenu((Player) event.getWhoClicked(),
                                new EnchantUpgradeMenu(tool, (Player) event.getWhoClicked()));
                    }
                }
        );

        setButton(
                23,
                new Button(
                        PoseidonTools.getInstance().getNMS()
                                .addGlow(ItemStackBuilder
                                        .buildItemStack("upgradeInventory.buttons.abilitesButton"))) {
                    @Override
                    public void onClick(Menu menu, InventoryClickEvent event) {
                        PoseidonTools.getInstance().getMenuHandler().closeMenu((Player) event.getWhoClicked());

                        PoseidonTools.getInstance().getMenuHandler().openMenu((Player) event.getWhoClicked(),
                                new AbilityUpgradeMenu(tool, (Player) event.getWhoClicked()));
                    }
                }
        );
    }
}
