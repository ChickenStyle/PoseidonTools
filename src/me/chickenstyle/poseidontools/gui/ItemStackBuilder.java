package me.chickenstyle.poseidontools.gui;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.utils.PlaceHolder;
import me.chickenstyle.poseidontools.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ItemStackBuilder {

    public static ItemStack buildItemStack(String path) {
        return buildItemStack(PoseidonTools.getInstance().getMenuConfig().getConfig(), path);
    }

    public static ItemStack buildItemStack(FileConfiguration config, String path) {
        return  buildItemStack(config, path, null);
    }

    public static ItemStack buildItemStack(FileConfiguration config, String path, PlaceHolder... placeHolders) {

        ItemStack item = new ItemStack(Material.valueOf(
                config.getString(path + ".material")
        ));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.replacePlaceHolders(config.getString(path + ".displayName"), placeHolders));
        meta.setLore(
                Utils.replacePlaceHolders(config.getStringList(path + ".lore")
                        .stream().map(Utils::color).collect(Collectors.toList()), placeHolders)
        );
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack buildToken() {
        return PoseidonTools.getInstance().getNMS().addStringTag(
                buildItemStack(PoseidonTools.getInstance().getConfig(),
                        "itemsFormat.tokenFormat", null),
                "Token","Yes"
        );
    }
}
