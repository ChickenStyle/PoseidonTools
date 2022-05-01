package me.chickenstyle.poseidontools.events;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.ToolType;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import me.chickenstyle.poseidontools.utils.Message;
import me.chickenstyle.poseidontools.utils.PlaceHolder;
import me.chickenstyle.poseidontools.utils.PoseidonTool;
import me.chickenstyle.poseidontools.utils.ToolBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Random;

public class AxeEnchantEvents implements Listener {

    private static HashMap<BlockData, TokenDrop> tokenDropData;
    private static Random rnd = new Random();

    public AxeEnchantEvents() {
        loadTokenDropChances();
    }

    @EventHandler
    public void onLogBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;

        PoseidonTool tool = ToolBuilder.fromItemStack(player.getItemInHand());

        if (tool.getToolType() != ToolType.POSEIDONS_AXE) return;
        Block block = e.getBlock();
        if (PoseidonTools.getInstance().hasWorldGuard()) {
            if (!PoseidonTools.getInstance().getWorldGuard().canBuild(player,block)) return;
        }

        if (block.getType() != Material.LOG && block.getType() != Material.LOG_2) return;

        TokenDrop tokenDrop = getTokenDrop(block);
        boolean canDropTokens = tokenDrop != null;
        int tokenDropChance = canDropTokens ? tokenDrop.getChance() : 0;
        int tokenDropAmount = canDropTokens ? tokenDrop.generateDropAmount() : 0;
        FileConfiguration config = PoseidonTools.getInstance().getEnchantConfig().getConfig();


        double efficiencyChance = config.getDouble("POSEIDONS_AXE.Efficiency.startingChance");
        efficiencyChance += tool.getEnchantmentLevel("Efficiency") *
                config.getDouble("POSEIDONS_AXE.Efficiency.chanceIncreasePerLevel");

        if (tool.hasEnchantment("Efficiency") &&
            rnd.nextDouble() < (efficiencyChance / 100.0)) {
            int effectPower = config.getInt("POSEIDONS_AXE.Efficiency.effectPower");
            int duration = config.getInt("POSEIDONS_AXE.Efficiency.duration") * 20;
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, effectPower));
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Efficiency")));
        }

        if (tool.hasEnchantment("Token_Booster") && canDropTokens &&
                rnd.nextDouble() < (config.getDouble("POSEIDONS_AXE.Token_Booster.startingChance")/100.0)) {
            double dropRangeIncreasePerLevel = config
                    .getDouble("POSEIDONS_AXE.Token_Booster.dropRangeIncreasePerLevel");
            tokenDropAmount += tokenDrop.generateDropAmount(
                    (int)(dropRangeIncreasePerLevel * tool.getEnchantmentLevel("Token_Booster")));
            tokenDropChance = 100;
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Token Booster")));
        }

        if (tool.hasEnchantment("Drop_Booster") &&
                rnd.nextDouble() < (config.getDouble("POSEIDONS_AXE.Drop_Booster.startingChance")/100.0)) {
            double amountIncrease = config.getDouble("POSEIDONS_AXE.Drop_Booster.logAmountIncreasePerLevel");
            for (ItemStack item : block.getDrops(player.getItemInHand())) {
                item.setAmount((int) (item.getAmount() + amountIncrease * tool.getEnchantmentLevel("Drop_Booster")));
                player.getWorld().dropItemNaturally(block.getLocation(),item);
            }
            block.setType(Material.AIR);
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Drop Booster")));
        }


        double keyFinderChance = config.getDouble("POSEIDONS_AXE.Key_Finder.startingChance");
        keyFinderChance += tool.getEnchantmentLevel("Key_Finder") *
                config.getDouble("POSEIDONS_AXE.Key_Finder.chanceIncreasePerLevel");

        if (tool.hasEnchantment("Key_Finder") &&
                rnd.nextDouble() < (keyFinderChance/100.0)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    config.getString("POSEIDONS_AXE.Key_Finder.command")
                            .replace("%player%", player.getName()));
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Key Finder")));
        }


        double spawnerFinderChance = config.getDouble("POSEIDONS_AXE.Spawner_Finder.startingChance");
        spawnerFinderChance += tool.getEnchantmentLevel("Spawner_Finder") *
                config.getDouble("POSEIDONS_AXE.Spawner_Finder.chanceIncreasePerLevel");

        if (tool.hasEnchantment("Spawner_Finder") &&
                rnd.nextDouble() < (spawnerFinderChance/100.0)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    config.getString("POSEIDONS_AXE.Spawner_Finder.command")
                            .replace("%player%", player.getName()));
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Spawner Finder")));
        }


        double speedChance = config.getDouble("POSEIDONS_AXE.Speed.startingChance");
        speedChance += tool.getEnchantmentLevel("Speed") *
                config.getDouble("POSEIDONS_AXE.Speed.chanceIncreasePerLevel");

        if (tool.hasEnchantment("Speed") &&
                rnd.nextDouble() < (speedChance / 100.0)) {
            int effectPower = config.getInt("POSEIDONS_AXE.Speed.effectPower") - 1;
            int duration = config.getInt("POSEIDONS_AXE.Speed.duration") * 20;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, effectPower));
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Speed")));
        }

        if (canDropTokens && rnd.nextInt(100) < tokenDropChance) {
            ItemStack token = ItemStackBuilder.buildToken();
            token.setAmount(tokenDropAmount);
            player.getWorld().dropItemNaturally(block.getLocation(), token);
        }

    }

    private static TokenDrop getTokenDrop(Block block) {
        for (BlockData data : tokenDropData.keySet()) {
            if (data.isTheSame(block)) {
                return tokenDropData.get(data);
            }
        }
        return null;
    }


    private void loadTokenDropChances() {
        tokenDropData = new HashMap<>();
        for (String data : PoseidonTools.getInstance().getConfig().getStringList("tokenData.logDrop")) {

            String[] info = data.split(":");

            if (info[0].contains(",")) {
                tokenDropData.put(new BlockData(Material.valueOf(info[0].split(",")[0]),
                                Short.parseShort(info[0].split(",")[1])),
                        new TokenDrop(Integer.parseInt(data.split(":")[1]),Integer.parseInt(data.split(":")[2])));
            } else {
                tokenDropData.put(new BlockData(Material.valueOf(info[0].split(",")[0]),
                                Short.parseShort("0")),
                        new TokenDrop(Integer.parseInt(data.split(":")[1]),Integer.parseInt(data.split(":")[2])));
            }

        }
    }

    private static class BlockData {
        private Material block;
        private short damage;

        public BlockData(Material block, short damage) {
            this.block = block;
            this.damage = damage;
        }

        public boolean isTheSame(Block block) {
            return this.block == block.getType() && this.damage == block.getState().getData().getData();
        }
    }
}
