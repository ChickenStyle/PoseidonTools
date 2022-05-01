package me.chickenstyle.poseidontools.abilities;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpiritOfTheWoodsAbility extends Ability{
    public SpiritOfTheWoodsAbility(Location loc, Player player) {
        super(loc, player);
    }

    @Override
    public void run() {
        ItemStack item = ItemStackBuilder.buildItemStack(PoseidonTools.getInstance().getConfig(),
                "itemsFormat.spiritOfTheWoods");
        item = PoseidonTools.getInstance().getNMS().addStringTag(item, "Consumable", "SpiritOfTheWoods");
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
        getLocation().getWorld().dropItemNaturally(getLocation(), item);
    }
}
