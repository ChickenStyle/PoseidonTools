package me.chickenstyle.poseidontools.utils;

import me.chickenstyle.poseidontools.PoseidonTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageManager implements Listener{

    private Map<UUID, Double> damageBoost;
    private Map<UUID, Double> gettingDamageBoost;

    public DamageManager() {
        damageBoost = new HashMap<>();
        gettingDamageBoost = new HashMap<>();
    }

    public void setDamageBoost(UUID uuid, double boost, int seconds) {
        damageBoost.put(uuid,boost);

        new BukkitRunnable() {
            @Override
            public void run() {
                damageBoost.remove(uuid,boost);
            }
        }.runTaskLater(PoseidonTools.getInstance(), seconds * 20L);
    }

    public void setGettingDamageBoost(UUID uuid, double boost, int seconds) {
        gettingDamageBoost.put(uuid,boost);

        new BukkitRunnable() {
            @Override
            public void run() {
                gettingDamageBoost.remove(uuid,boost);
            }
        }.runTaskLater(PoseidonTools.getInstance(), seconds * 20L);

    }

    public boolean hasDamageBoost(UUID uuid) {
        return damageBoost.containsKey(uuid);
    }

    public boolean hasGettingDamageBoost(UUID uuid) {
        return gettingDamageBoost.containsKey(uuid);
    }


    public double getDamageBoost(UUID uuid) {
        return damageBoost.get(uuid);
    }

    public double getGettingDamageBoost(UUID uuid) {
        return gettingDamageBoost.get(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        if (hasDamageBoost(player.getUniqueId())) e.setDamage(e.getFinalDamage() +
                getDamageBoost(player.getUniqueId()) * e.getFinalDamage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerGettingDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (hasGettingDamageBoost(player.getUniqueId())) e.setDamage(e.getFinalDamage() +
                getGettingDamageBoost(player.getUniqueId()) * e.getFinalDamage());
    }
}
