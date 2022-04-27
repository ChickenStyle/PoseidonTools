package me.chickenstyle.poseidontools.events;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.ToolType;
import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import me.chickenstyle.poseidontools.utils.Message;
import me.chickenstyle.poseidontools.utils.PlaceHolder;
import me.chickenstyle.poseidontools.utils.PoseidonTool;
import me.chickenstyle.poseidontools.utils.ToolBuilder;
import me.chickenstyle.poseidontools.ymls.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EnchantmentEvents implements Listener {

    public EnchantmentEvents() {
        loadTokenDropChances();
    }

    private static HashMap<EntityType, TokenDrop> tokenDropData;
    private static Random rnd = new Random();

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player player = e.getEntity().getKiller();
        if (player == null) return;
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;

        PoseidonTool tool = ToolBuilder.fromItemStack(player.getItemInHand());
        if (tool.getToolType() != ToolType.MOBSWORD) return;
        boolean canDropTokens = tokenDropData.containsKey(entity.getType());
        int tokenDropChance = canDropTokens ? tokenDropData.get(entity.getType()).getChance() : 0;
        int tokenDropAmount = canDropTokens ? tokenDropData.get(entity.getType()).generateDropAmount() : 0;
        FileConfiguration enchantConfig = PoseidonTools.getInstance().getEnchantConfig().getConfig();

        if (tool.hasEnchantment("Token_Booster") && canDropTokens &&
                rnd.nextDouble() < (enchantConfig.getInt("MOBSWORD.Token_Booster.startingChance")/100.0)) {
            double dropRangeIncreasePerLevel = enchantConfig
                    .getDouble("MOBSWORD.Token_Booster.dropRangeIncreasePerLevel");
            tokenDropAmount += tokenDropData.get(entity.getType()).generateDropAmount(
                    (int)(dropRangeIncreasePerLevel * tool.getEnchantmentLevel("Token_Booster")));
            tokenDropChance = 100;
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Token Booster")));
        }

        if (tool.hasEnchantment("Drop_Booster") &&
                rnd.nextDouble() < (enchantConfig.getInt("MOBSWORD.Drop_Booster.startingChance")/100.0)) {
            double rangeIncrease = enchantConfig.getDouble("MOBSWORD.Drop_Booster.dropRangeIncreasePerLevel");
            int amountIncrease = (int) (rangeIncrease * tool.getEnchantmentLevel("Drop_Booster"));

            for (ItemStack item : e.getDrops()) {
                item.setAmount(item.getAmount() + amountIncrease);
            }
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Drop Booster")));
        }

        double keyFinderChance = enchantConfig.getDouble("MOBSWORD.Key_Finder.startingChance");
        keyFinderChance += tool.getEnchantmentLevel("Key_Finder") *
                enchantConfig.getDouble("MOBSWORD.Key_Finder.chanceIncreasePerLevel");

        if (tool.hasEnchantment("Key_Finder") &&
                rnd.nextDouble() < (keyFinderChance/100.0)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    enchantConfig.getString("MOBSWORD.Key_Finder.command").replace("%player%", player.getName()));
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Key Finder")));
        }


        double spawnerFinderChance = enchantConfig.getDouble("MOBSWORD.Spawner_Finder.startingChance");
        spawnerFinderChance += tool.getEnchantmentLevel("Spawner_Finder") *
                enchantConfig.getDouble("MOBSWORD.Spawner_Finder.chanceIncreasePerLevel");

        if (tool.hasEnchantment("Spawner_Finder") &&
                rnd.nextDouble() < (spawnerFinderChance/100.0)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    enchantConfig.getString("MOBSWORD.Spawner_Finder.command").replace("%player%", player.getName()));
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Spawner Finder")));
        }


        double xpFinderChance = enchantConfig.getDouble("MOBSWORD.XP_Finder.startingChance");
        xpFinderChance += tool.getEnchantmentLevel("XP_Finder") *
                enchantConfig.getDouble("MOBSWORD.XP_Finder.chanceIncreasePerLevel");

        if (tool.hasEnchantment("XP_Finder") &&
                rnd.nextDouble() < (xpFinderChance/100.0)) {
            e.setDroppedExp(e.getDroppedExp() +
                    rnd.nextInt(enchantConfig.getInt("MOBSWORD.XP_Finder.maxXPIncrease") +
                            enchantConfig.getInt("MOBSWORD.XP_Finder.minXPIncrease")));
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "XP Finder")));
        }


        double immunityChance = enchantConfig.getDouble("MOBSWORD.Immunity.startingChance");
        immunityChance += tool.getEnchantmentLevel("Immunity") *
                enchantConfig.getDouble("MOBSWORD.Immunity.chanceIncreasePerLevel");

        if (tool.hasEnchantment("Immunity") &&
                rnd.nextDouble() < (immunityChance/100.0)) {
            player.setNoDamageTicks(enchantConfig.getInt("MOBSWORD.Immunity.immunityTime") * 20);
            player.sendMessage(Message.ACTIVATED_ENCHANTMENT.toMSG(new PlaceHolder("enchantment",
                    "Immunity")));
        }

        if (canDropTokens && rnd.nextInt(100) < tokenDropChance) {
            ItemStack token = ItemStackBuilder.buildToken();
            token.setAmount(tokenDropAmount);
            e.getDrops().add(token);
        }



    }


    public static void loadTokenDropChances() {
        tokenDropData = new HashMap<>();
        for (String data : PoseidonTools.getInstance().getConfig().getStringList("tokenData.mobDrop")) {
            tokenDropData.put(EntityType.valueOf(data.split(":")[0]),
                    new TokenDrop(Integer.parseInt(data.split(":")[1]),Integer.parseInt(data.split(":")[2])));
        }
    }

    private static class TokenDrop {

        private int chance;
        private int dropRange;

        public TokenDrop(int chance, int dropRange) {
            this.chance = chance;
            this.dropRange = dropRange;
        }

        public int generateDropAmount() {
            return generateDropAmount(0);
        }

        public int generateDropAmount(int rangeIncrease) {
            return ThreadLocalRandom.current().nextInt(1, dropRange + rangeIncrease + 1);
        }

        public int getChance() {
            return chance;
        }
    }
}
