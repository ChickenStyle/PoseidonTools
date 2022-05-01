package me.chickenstyle.poseidontools.events;

import me.chickenstyle.poseidontools.PoseidonTools;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class ConsumableEvents implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        if (item == null || item.getType() == Material.AIR) return;
        if (!PoseidonTools.getInstance().getNMS().hasTag(item, "Consumable")) return;

        String type = PoseidonTools.getInstance().getNMS().getStringData(item, "Consumable");

        if (type.equals("RageBerry")) {
            System.out.println("Worked");
            PoseidonTools.getInstance().getDamageManager().setDamageBoost(
                    player.getUniqueId(), 0.2, 15
            );

            PoseidonTools.getInstance().getDamageManager().setGettingDamageBoost(
                    player.getUniqueId(), 0.4, 15
            );
        }

        if (type.equals("SpiritOfTheWoods")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20, 2));
        }

        if (type.equals("SpicyTaco")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 45 * 20, 3));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 45 * 20, 2));
        }



        item.setAmount(item.getAmount() - 1);
        player.setItemInHand(item);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f,1.0f);
        e.setCancelled(true);
    }
}
