package me.chickenstyle.poseidontools.utils;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.ToolType;
import me.chickenstyle.poseidontools.nms.NMSHandler;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ToolBuilder {

    public static PoseidonTool generateDefaultTool(ToolType toolType) {
        return new PoseidonTool(toolType,1,0);
    }

    public static PoseidonTool fromItemStack(ItemStack item) {
        NMSHandler nms = PoseidonTools.getInstance().getNMS();

        ToolType type = ToolType.valueOf(nms.getStringData(item, "PoseidonTool"));
        int currentLevel = nms.getIntData(item, "CurrentLevel");
        int currentXP = nms.getIntData(item, "CurrentXP");

        Map<String,Integer> enchantments = new HashMap<>();

        for (String enchantment : type.getEnchantments()) {
            enchantments.put(enchantment, nms.getIntData(item, enchantment));
        }

        return new PoseidonTool(type, currentLevel, currentXP, enchantments);
    }

    public static ItemStack toItemStack(PoseidonTool tool) {
        ItemStack item = new ItemStack(tool.getToolType().getMaterial());
        ItemMeta meta = item.getItemMeta();

        PoseidonTools main = PoseidonTools.getInstance();
        FileConfiguration config = main.getConfig();
        NMSHandler nms = main.getNMS();

        String path = "toolsData." + tool.getToolType() + ".";

        //PlaceHolders
        PlaceHolder itemName = new PlaceHolder("item-name",
                config.getString(path + "itemName"));

        PlaceHolder itemLevel = new PlaceHolder("item-level",
                tool.getCurrentLevel() + "");

        PlaceHolder maxLevel = new PlaceHolder("item-max-level",
                tool.getMaxLevel() + "");

        // Progress Bar Calculation
        String bar = config.getString("progressBar.bar");
        String filledColor = config.getString("progressBar.filledColor");
        String unfilledColor = config.getString("progressBar.unfilledColor");

        float currentBar = (tool.getCurrentXP() / (float) tool.getXPNeeded());
        int barSize = (int) (currentBar * bar.length());
        bar = bar.substring(0 , barSize) + unfilledColor + bar.substring(barSize);
        bar = filledColor + bar;

        PlaceHolder progressBar = new PlaceHolder("progress-bar", bar);
        PlaceHolder itemXP = new PlaceHolder("item-xp", tool.getCurrentXP() + "");
        PlaceHolder xpNeeded = new PlaceHolder("item-xp-needed", tool.getXPNeeded() + "");

        meta.setDisplayName(
                Utils.replacePlaceHolders(config.getString("itemsFormat.toolFormat.displayName"),
                        itemName, itemLevel, maxLevel, progressBar, itemXP, xpNeeded)
        );

        meta.setLore(
                Utils.replacePlaceHolders(config.getStringList("itemsFormat.toolFormat.lore"),
                        itemName, itemLevel, maxLevel, progressBar, itemXP, xpNeeded)
        );


        for (int i = 0; i < meta.getLore().size(); i++) {
            if (meta.getLore().get(i).contains("%item-description%")) {
                List<String> lore = meta.getLore();
                lore.remove(i);

                List<String> description = config.getStringList(path + "description").stream().map(
                        str -> str = Utils.color(str)
                ).collect(Collectors.toList());

                lore.addAll(i, description);
                meta.setLore(lore);
                break;
            }
        }


        meta.addItemFlags(ItemFlag.values());
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);

        //Load NBTTags
        item = nms.addStringTag(item, "PoseidonTool", tool.getToolType().toString());
        item = nms.addIntTag(item, "CurrentLevel", tool.getCurrentLevel());
        item = nms.addIntTag(item, "CurrentXP", tool.getCurrentXP());

        for (String enchantment : tool.getToolType().getEnchantments()) {
            item = nms.addIntTag(item, enchantment, tool.getEnchantmentsLevel().get(enchantment));
        }

        return nms.addGlow(item);
    }


    public static boolean isPoseidonTool(ItemStack item){
        return PoseidonTools.getInstance().getNMS().hasTag(item, "PoseidonTool");
    }
}
