package me.chickenstyle.poseidontools.abilities;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpicyTacoAbility extends Ability{
    public SpicyTacoAbility(Location loc, Player player) {
        super(loc, player);
    }

    @Override
    public void run() {
        ItemStack taco = ItemStackBuilder.buildItemStack(PoseidonTools.getInstance().getConfig(),
                "itemsFormat.spicyTaco");
        taco = PoseidonTools.getInstance().getNMS().addStringTag(taco, "Consumable", "SpicyTaco");
        getLocation().getWorld().dropItemNaturally(getLocation(), taco);
    }
}
