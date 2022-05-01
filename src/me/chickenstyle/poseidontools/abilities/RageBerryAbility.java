package me.chickenstyle.poseidontools.abilities;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RageBerryAbility extends Ability{
    public RageBerryAbility(Location loc, Player player) {
        super(loc, player);
    }

    @Override
    public void run() {
        ItemStack berry = ItemStackBuilder.buildItemStack(PoseidonTools.getInstance().getConfig(),
                "itemsFormat.rageBerry");
        berry = PoseidonTools.getInstance().getNMS().addStringTag(berry, "Consumable", "RageBerry");
        getLocation().getWorld().dropItemNaturally(getLocation(), berry);
    }
}
