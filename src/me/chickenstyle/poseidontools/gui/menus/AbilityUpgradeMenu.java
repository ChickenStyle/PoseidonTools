package me.chickenstyle.poseidontools.gui.menus;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.gui.Button;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import me.chickenstyle.poseidontools.gui.Menu;
import me.chickenstyle.poseidontools.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AbilityUpgradeMenu extends Menu {

    private PoseidonTool tool;
    private final Player player;

    public AbilityUpgradeMenu(PoseidonTool tool, Player player) {
        super(Bukkit.createInventory(null, 45,
                Utils.color(PoseidonTools.getInstance().getMenuConfig().getString("abilityUpgrade.title"))
        ), true);
        this.tool = tool;
        this.player = player;

        fillBackground();
        loadAbilityButtons();
    }


    private void loadAbilityButtons(){

        System.out.println(tool.getToolType().getAbilities()[0] + ":" +
                tool.getAbilityLevel(tool.getToolType().getAbilities()[0]));

        System.out.println(tool.getToolType().getAbilities()[1] + ":" +
                tool.getAbilityLevel(tool.getToolType().getAbilities()[1]));

        String canAfford = PoseidonTools.getInstance().getMenuConfig()
                .getString("abilityUpgrade.canAffordAbility");
        String cannotAfford = PoseidonTools.getInstance().getMenuConfig()
                .getString("abilityUpgrade.cannotAffordAbility");

        int upgradePrice = tool.getAbilityLevel(tool.getToolType().getAbilities()[0])
                < tool.getAbilityMaxLevel(tool.getToolType().getAbilities()[0]) ? 1 : 0;

        setButton(21, new Button(ItemStackBuilder.buildItemStack(
                PoseidonTools.getInstance().getMenuConfig().getConfig(),
                "abilityUpgrade.abilities." + tool.getToolType().getAbilities()[0],
                new PlaceHolder("current-level",
                        tool.getAbilityLevel(tool.getToolType().getAbilities()[0]) + ""),
                new PlaceHolder("max-level",
                        tool.getAbilityMaxLevel(tool.getToolType().getAbilities()[0]) + ""),
                new PlaceHolder("upgrade-price",
                        upgradePrice + ""),
                new PlaceHolder("level-points",
                        tool.getLevelPoints() + ""),
                new PlaceHolder("can-afford",
                        tool.getLevelPoints() > 0 ? canAfford : cannotAfford)
        )) {

            private String ability =  tool.getToolType().getAbilities()[0];
            @Override
            public void onClick(Menu menu, InventoryClickEvent event) {
                if (tool.getLevelPoints() > 0 &&
                        tool.getAbilityLevel(ability) < tool.getAbilityMaxLevel(ability)) {
                    tool.setLevelPoints(tool.getLevelPoints() - 1);
                    tool.setAbilityLevel(ability, tool.getAbilityLevel(ability) + 1);
                    player.setItemInHand(ToolBuilder.toItemStack(tool));
                    player.updateInventory();
                    loadAbilityButtons();
                } else {
                    if (tool.getLevelPoints() <= 0 && upgradePrice != 0)
                        player.sendMessage(Message.FAILED_ABILITY_UPGRADE.toMSG(
                                new PlaceHolder("ability", ability.replaceAll("_", " "))
                        ));
                }
            }
        });

        setButton(23, new Button(ItemStackBuilder.buildItemStack(
                PoseidonTools.getInstance().getMenuConfig().getConfig(),
                "abilityUpgrade.abilities." + tool.getToolType().getAbilities()[1],
                new PlaceHolder("current-level",
                        tool.getAbilityLevel(tool.getToolType().getAbilities()[1]) + ""),
                new PlaceHolder("max-level",
                        tool.getAbilityMaxLevel(tool.getToolType().getAbilities()[1]) + ""),
                new PlaceHolder("upgrade-price",
                        upgradePrice + ""),
                new PlaceHolder("level-points",
                        tool.getLevelPoints() + ""),
                new PlaceHolder("can-afford",
                        tool.getLevelPoints() > 0 ? canAfford : cannotAfford)

        )) {

            private String ability =  tool.getToolType().getAbilities()[1];
            @Override
            public void onClick(Menu menu, InventoryClickEvent event) {
                if (tool.getLevelPoints() > 0 &&
                        tool.getAbilityLevel(ability) < tool.getAbilityMaxLevel(ability)) {
                    tool.setLevelPoints(tool.getLevelPoints() - 1);
                    tool.setAbilityLevel(ability, tool.getAbilityLevel(ability) + 1);
                    player.setItemInHand(ToolBuilder.toItemStack(tool));
                    player.updateInventory();
                    loadAbilityButtons();

                } else {
                    if (tool.getLevelPoints() <= 0 && upgradePrice != 0)
                        player.sendMessage(Message.FAILED_ABILITY_UPGRADE.toMSG(
                                new PlaceHolder("ability", ability.replaceAll("_", " "))
                        ));
                }
            }
        });

        setButton(40, new Button(ItemStackBuilder.buildItemStack(
                PoseidonTools.getInstance().getMenuConfig().getConfig(),
                "abilityUpgrade.buttons.backButton",null)) {
            @Override
            public void onClick(Menu menu, InventoryClickEvent event) {
                PoseidonTools.getInstance().getMenuHandler().closeMenu((Player) event.getWhoClicked());

                PoseidonTools.getInstance().getMenuHandler().openMenu((Player) event.getWhoClicked(),
                        new UpgradeToolMenu(tool));
            }
        });
        loadButtons();
    }
}
