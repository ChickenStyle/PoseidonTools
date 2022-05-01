package me.chickenstyle.poseidontools.abilities;

import me.chickenstyle.poseidontools.PoseidonTools;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackHoleAbility extends Ability{

    private static Random rnd = new Random();
    private double radius;
    private boolean pullEntities;
    private double pullEntitiesRadius;
    private boolean damageEntitiesOnExplosion;
    private double damagePower;

    public BlackHoleAbility(Location loc, Player player) {
        super(loc, player);
        FileConfiguration config = PoseidonTools.getInstance().getAbilitiesConfig().getConfig();
        radius = config.getDouble("ATLANTIS_PICKAXE.BlackHole.radius");
        pullEntities = config.getBoolean("ATLANTIS_PICKAXE.BlackHole.pullEntities");
        pullEntitiesRadius = config.getDouble("ATLANTIS_PICKAXE.BlackHole.pullEntitiesRadius");
        damageEntitiesOnExplosion = config.getBoolean("ATLANTIS_PICKAXE.BlackHole.damageEntitiesOnExplosion");
        damagePower = config.getDouble("ATLANTIS_PICKAXE.BlackHole.damagePower");
    }

    @Override
    public void run() {
        new BukkitRunnable() {
            int counter = 0;
            final int maxLoops = 50;
            List<Location> blocksAround = getBlocksInSphere(getLocation(), (int) radius);
            List<Entity> nearbyEntities = getNearbyEntities(pullEntitiesRadius);
            @Override
            public void run() {
                if (counter >= maxLoops) {
                    nearbyEntities = getNearbyEntities(pullEntitiesRadius);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof FallingBlock) {
                            FallingBlock fallingBlock = (FallingBlock) entity;
                            if (fallingBlock.getMaterial() != Material.AIR)
                                fallingBlock.getWorld().dropItemNaturally(fallingBlock.getLocation(),
                                        new ItemStack(fallingBlock.getMaterial(),1, fallingBlock.getBlockData()));
                                fallingBlock.remove();
                        }

                        if (entity instanceof LivingEntity && damageEntitiesOnExplosion) {
                            double multiplier = (1 - (getLocation().distance(entity.getLocation())/pullEntitiesRadius));
                            double damage = damagePower * multiplier;
                            ((LivingEntity) entity).damage(damage);
                        }

                    }
                    getLocation().getWorld().playEffect(getLocation(), Effect.EXPLOSION_HUGE, 1);
                    getLocation().getWorld().playSound(getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                    cancel();
                }

                nearbyEntities = getNearbyEntities(pullEntitiesRadius); //Update Nearby entities every second

                for (int i = 0; i < blocksAround.size(); i++) {
                    Location loc = blocksAround.get(i);
                    if (!PoseidonTools.getInstance().getNMS().isItem(loc.getBlock().getType())) continue;

                    FallingBlock fallingBlock = loc.getWorld().spawnFallingBlock(
                            loc,
                            loc.getBlock().getType(),
                            loc.getBlock().getData()
                    );
                    fallingBlock.setDropItem(true);
                    loc.getBlock().setType(Material.AIR);
                }


                for (Entity entity : nearbyEntities) {
                    if (entity instanceof FallingBlock) {
                        moveToward(entity, getLocation(),
                                (Double) PoseidonTools.getInstance()
                                        .getAbilitiesConfig().get("ATLANTIS_PICKAXE.BlackHole.blockPullPower")
                                , true);
                    } else {
                        moveToward(entity, getLocation(),
                                (Double) PoseidonTools.getInstance()
                                .getAbilitiesConfig().get("ATLANTIS_PICKAXE.BlackHole.entitiesPullPower"),
                                false);
                    }

                }

                counter++;
            }
        }.runTaskTimer(PoseidonTools.getInstance(), 0, 2);
    }

    private List<Entity> getNearbyEntities(double radius) {
        List<Entity> entities = new ArrayList<>();

        for (Entity entity : getLocation().getWorld().getEntities()) {
            if ((!pullEntities && !(entity instanceof FallingBlock))) continue;
            if (entity.getLocation().distance(getLocation()) > radius) continue;;
            entities.add(entity);
        }
        return entities;
    }

    private void moveToward(Entity entity, Location to, double speed, boolean setVector){
        Location loc = entity.getLocation();
        Vector direction = to.toVector().subtract(loc.toVector()).normalize();
        entity.setVelocity(setVector ? direction.multiply(speed) : entity.getVelocity().add(direction.multiply(speed)));
    }

    private List<Location> getBlocksInSphere(Location centerBlock, int radius) {
        if (centerBlock == null) {
            return new ArrayList<>();
        }

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {
                    Location loc = new Location(centerBlock.getWorld(), x, y, z);

                    if(centerBlock.distance(loc) < radius) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);


                        if (PoseidonTools.getInstance().hasWorldGuard() &&
                                !PoseidonTools.getInstance().getWorldGuard().canBuild(getPlayer(), l)) {
                            continue;
                        }

                        if (!PoseidonTools.getInstance().getNMS().isItem(l.getBlock().getType())) continue;
                        if (l.getBlock().getType() == Material.BEDROCK) continue;

                        circleBlocks.add(l);

                    }

                }
            }
        }

        return circleBlocks;
    }
}
