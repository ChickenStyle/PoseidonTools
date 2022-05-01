package me.chickenstyle.poseidontools.abilities;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.utils.PoseidonTool;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ExplosiveForagingAbility extends Ability{

    private int maxBreakBlocks;
    private boolean breakLeaves;
    private ItemStack tool;
    private static BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.EAST,
            BlockFace.WEST, BlockFace.SOUTH,BlockFace.NORTH};


    public ExplosiveForagingAbility(int maxBreakBlocks, Location loc, Player player, ItemStack tool) {
        super(loc, player);
        this.maxBreakBlocks = maxBreakBlocks;
        this.breakLeaves = (boolean) PoseidonTools.getInstance().getAbilitiesConfig()
                .getConfig().get("POSEIDONS_AXE.Explosive_Foraging.breakLeaves");
        this.tool = tool;
    }

    @Override
    public void run() {
        getLocation().getWorld().playEffect(getLocation(), Effect.EXPLOSION_LARGE, 1);
        getLocation().getWorld().playSound(getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
        breakTree(getPlayer(), getLocation().getBlock(), maxBreakBlocks);
    }

    private void getBlocks(List<Block> list, Set<Material> validBlocks, Block loc, Counter counter) {
        if (counter.addOne()) {
            list.add(loc);

            for (BlockFace face : faces) {
                Block block = loc.getRelative(face);
                if (validBlocks.contains(block.getType()) && !list.contains(block))
                    getBlocks(list, validBlocks, block, counter);
            }
        }
    }


    public void breakTree(Player player, Block tree, int maxBrokenBlocks) {

        List<Block> bList = new ArrayList<>();
        Set<Material> breakBlocks = new HashSet<Material>(){{
            add(Material.LOG);
            add(Material.LOG_2);
        }};

        if (this.breakLeaves) {
            breakBlocks.add(Material.LEAVES);
            breakBlocks.add(Material.LEAVES_2);
        }
        getBlocks(bList, breakBlocks, tree, new Counter(0, maxBrokenBlocks)); //Loads

        new BukkitRunnable() {

            int counter = 0;
            @Override
            public void run() {
                if (counter >= bList.size()) {
                    cancel();
                    return;
                }

                Block block = bList.get(counter);
                if (!PoseidonTools.getInstance().hasWorldGuard() ||
                        (PoseidonTools.getInstance().hasWorldGuard() &&
                    PoseidonTools.getInstance().getWorldGuard().canBuild(player, block))) {
                    ItemStack[] drops = block.getDrops(tool).toArray(new ItemStack[0]);
                   if (!player.getInventory().addItem(drops).isEmpty()) {
                       for (ItemStack item : drops) {
                           player.getWorld().dropItemNaturally(player.getLocation(), item);
                       }
                   }
                   block.setType(Material.AIR);
                    counter++;
                }
            }
        }.runTaskTimer(PoseidonTools.getInstance(), 0, 1);


    }

    public static class Counter {
        private int counter;
        private final int maxCounter;

        public Counter(int counter, int maxCounter) {
            this.counter = counter;
            this.maxCounter = maxCounter;
        }

        public boolean addOne() {
            if (counter < maxCounter) {
                counter++;
            }
            return counter < maxCounter;
        }
    }
}
