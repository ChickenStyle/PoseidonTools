package me.chickenstyle.poseidontools.events;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.ToolType;
import me.chickenstyle.poseidontools.abilities.BlackHoleAbility;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import me.chickenstyle.poseidontools.gui.menus.UpgradeToolMenu;
import me.chickenstyle.poseidontools.utils.Message;
import me.chickenstyle.poseidontools.utils.PoseidonTool;
import me.chickenstyle.poseidontools.utils.ToolBuilder;
import me.chickenstyle.poseidontools.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ToolXPEvents implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;


        PoseidonTools.getInstance().getMenuHandler().openMenu(player, new UpgradeToolMenu(
                ToolBuilder.fromItemStack(player.getItemInHand())
        ));
    }

    @EventHandler
    public void onPlayerKillEvent(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player player = e.getEntity().getKiller();
        if (player == null) return;
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;

        PoseidonTool tool = ToolBuilder.fromItemStack(player.getItemInHand());
        boolean toolLeveledUp = false;
        if (tool.getToolType() != ToolType.MOBSWORD) return;


        for (String info : PoseidonTools.getInstance().getConfig().getStringList("toolsData.MOBSWORD.xpSource")) {
            String[] data = info.split(":");
            if (entity.getType().toString().equalsIgnoreCase(data[0])) {
                toolLeveledUp = tool.addXP(Integer.parseInt(data[1]));
                break;
            }
        }
        player.setItemInHand(ToolBuilder.toItemStack(tool));
        player.updateInventory();
        if (toolLeveledUp) {
            String path = "toolsData." + tool.getToolType() + ".";
            player.sendMessage(Utils.color(Message.LEVEL_UP_MESSAGE.toMSG().replaceAll("%item-name%",
                            PoseidonTools.getInstance().getConfig().getString(path + "itemName"))
                    .replaceAll("%item-level%", tool.getCurrentLevel() + "")));
        }
    }

    @EventHandler
    public void onPickaxeBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;

        PoseidonTool tool = ToolBuilder.fromItemStack(player.getItemInHand());
        boolean toolLeveledUp = false;

        if (tool.getToolType() != ToolType.ATLANTIS_PICKAXE) return;
        Block block = e.getBlock();
        if (PoseidonTools.getInstance().hasWorldGuard()) {
            if (!PoseidonTools.getInstance().getWorldGuard().canBuild(player,block)) return;
        }


        for (String info : PoseidonTools.getInstance().getConfig()
                .getStringList("toolsData.ATLANTIS_PICKAXE.xpSource")) {
            String[] data = info.split(":");

            if (data[0].contains(",")) {
                if (block.getType().toString().equalsIgnoreCase(data[0].split(",")[0]) &&
                        block.getState().getData().getData() == Byte.parseByte(data[0].split(",")[1])) {
                    toolLeveledUp = tool.addXP(Integer.parseInt(data[1]));
                    break;
                }
            } else {
                if (block.getType().toString().equalsIgnoreCase(data[0])) {
                    toolLeveledUp = tool.addXP(Integer.parseInt(data[1]));
                    break;
                }
            }
        }
        player.setItemInHand(ToolBuilder.toItemStack(tool));
        player.updateInventory();
        if (toolLeveledUp) {
            String path = "toolsData." + tool.getToolType() + ".";
            player.sendMessage(Utils.color(Message.LEVEL_UP_MESSAGE.toMSG().replaceAll("%item-name%",
                            PoseidonTools.getInstance().getConfig().getString(path + "itemName"))
                    .replaceAll("%item-level%", tool.getCurrentLevel() + "")));
        }

    }

    @EventHandler
    public void onAxeBlockBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;

        PoseidonTool tool = ToolBuilder.fromItemStack(player.getItemInHand());
        boolean toolLeveledUp = false;

        if (tool.getToolType() != ToolType.POSEIDONS_AXE) return;
        Block block = e.getBlock();

        if (PoseidonTools.getInstance().hasWorldGuard()) {
            if (!PoseidonTools.getInstance().getWorldGuard().canBuild(player,block)) return;
        }

        for (String info : PoseidonTools.getInstance().getConfig()
                .getStringList("toolsData.POSEIDONS_AXE.xpSource")) {
            String[] data = info.split(":");

            if (data[0].contains(",")) {
                if (block.getType().toString().equalsIgnoreCase(data[0].split(",")[0]) &&
                        block.getState().getData().getData() == Byte.parseByte(data[0].split(",")[1])) {
                    toolLeveledUp = tool.addXP(Integer.parseInt(data[1]));
                    break;
                }
            } else {
                if (block.getType().toString().equalsIgnoreCase(data[0])) {
                    toolLeveledUp = tool.addXP(Integer.parseInt(data[1]));
                    break;
                }
            }
        }
        player.setItemInHand(ToolBuilder.toItemStack(tool));
        player.updateInventory();
        if (toolLeveledUp) {
            String path = "toolsData." + tool.getToolType() + ".";
            player.sendMessage(Utils.color(Message.LEVEL_UP_MESSAGE.toMSG().replaceAll("%item-name%",
                            PoseidonTools.getInstance().getConfig().getString(path + "itemName"))
                    .replaceAll("%item-level%", tool.getCurrentLevel() + "")));
        }

    }
}
