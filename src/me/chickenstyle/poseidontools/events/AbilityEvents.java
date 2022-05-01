package me.chickenstyle.poseidontools.events;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.ToolType;
import me.chickenstyle.poseidontools.abilities.*;
import me.chickenstyle.poseidontools.utils.Message;
import me.chickenstyle.poseidontools.utils.PlaceHolder;
import me.chickenstyle.poseidontools.utils.PoseidonTool;

import me.chickenstyle.poseidontools.utils.ToolBuilder;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.material.Gate;

import java.util.Random;

public class AbilityEvents implements Listener {
    private static Random rnd = new Random();

    @EventHandler
    public void onSwordAbilities(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player player = e.getEntity().getKiller();
        if (player == null) return;
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;

        PoseidonTool tool = ToolBuilder.fromItemStack(player.getItemInHand());
        if (tool.getToolType() != ToolType.MOBSWORD) return;
        FileConfiguration config = PoseidonTools.getInstance().getAbilitiesConfig().getConfig();

        double gatewayToHellChance = config.getDouble("MOBSWORD.Gateway_to_Hell.chance");
        gatewayToHellChance += config.getDouble("MOBSWORD.Gateway_to_Hell.chanceIncreasePerLevel") *
                tool.getAbilityLevel("Gateway_to_Hell");


        if (tool.hasAbility("Gateway_to_Hell") && rnd.nextDouble() < gatewayToHellChance / 100) {
            new GateToHellAbility(player.getLocation(), player).run();
            player.sendMessage(Message.ACTIVATED_ABILITY.toMSG(
                    new PlaceHolder("ability", "Gateway to Hell")));
        }

        double rageBeryyChance = config.getDouble("MOBSWORD.Rage_Berry.chance");
        rageBeryyChance += config.getDouble("MOBSWORD.Rage_Berry.chanceIncreasePerLevel") *
                tool.getAbilityLevel("Rage_Berry");

        if (tool.hasAbility("Rage_Berry") && rnd.nextDouble() < rageBeryyChance / 100) {
            new RageBerryAbility(entity.getLocation(),player).run();
            player.sendMessage(Message.ACTIVATED_ABILITY.toMSG(
                    new PlaceHolder("ability", "Rage Berry")));
        }

    }


    @EventHandler
    public void onPickaxeAbilities(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!ToolBuilder.isPoseidonTool(player.getItemInHand())) return;

        PoseidonTool tool = ToolBuilder.fromItemStack(player.getItemInHand());

        if (tool.getToolType() != ToolType.ATLANTIS_PICKAXE) return;
        Block block = e.getBlock();
        if (PoseidonTools.getInstance().hasWorldGuard()) {
            if (!PoseidonTools.getInstance().getWorldGuard().canBuild(player,block)) return;
        }

        FileConfiguration config = PoseidonTools.getInstance().getAbilitiesConfig().getConfig();

        double blackHoleChance = config.getDouble("ATLANTIS_PICKAXE.BlackHole.chance");
        blackHoleChance += config.getDouble("ATLANTIS_PICKAXE.BlackHole.chanceIncreasePerLevel") *
                tool.getAbilityLevel("BlackHole");

        if (tool.hasAbility("BlackHole") && rnd.nextDouble() < blackHoleChance / 100) {
            new BlackHoleAbility(block.getLocation(), player).run();
            player.sendMessage(Message.ACTIVATED_ABILITY.toMSG(
                    new PlaceHolder("ability", "BlackHole")));
        }

        double spicyTacoChance = config.getDouble("ATLANTIS_PICKAXE.Spicy_Taco.chance");
        spicyTacoChance += config.getDouble("ATLANTIS_PICKAXE.Spicy_Taco.chanceIncreasePerLevel") *
                tool.getAbilityLevel("Spicy_Taco");

        if (tool.hasAbility("Spicy_Taco") && rnd.nextDouble() < spicyTacoChance / 100) {
            new SpicyTacoAbility(e.getBlock().getLocation(), player).run();
            player.sendMessage(Message.ACTIVATED_ABILITY.toMSG(
                    new PlaceHolder("ability", "Spicy Taco")));
        }

    }

    @EventHandler
    public void onAxeAbilities(BlockBreakEvent e) {
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

        FileConfiguration config = PoseidonTools.getInstance().getAbilitiesConfig().getConfig();

        double explosiveForagingChance = config.getDouble("POSEIDONS_AXE.Explosive_Foraging.chance");

        if (tool.hasAbility("Explosive_Foraging") && rnd.nextDouble() < explosiveForagingChance / 100) {
            double breakBlocks = config.getDouble("POSEIDONS_AXE.Explosive_Foraging.startingBreakBlocksAmount");
            breakBlocks += config.getDouble("POSEIDONS_AXE.Explosive_Foraging.increaseBreakBlockAmount")
                    * tool.getAbilityLevel("Explosive_Foraging");

            new ExplosiveForagingAbility((int) breakBlocks, block.getLocation(), player,
                    player.getItemInHand()).run();

            player.sendMessage(Message.ACTIVATED_ABILITY.toMSG(
                    new PlaceHolder("ability", "Explosive Foraging")));
        }

        double spiritChance = config.getDouble("POSEIDONS_AXE.Spirit_of_the_Woods.chance");
        spiritChance += config.getDouble("POSEIDONS_AXE.Spirit_of_the_Woods.chanceIncreasePerLevel") *
                tool.getAbilityLevel("Spirit_of_the_Woods");
        System.out.println(spiritChance);
        if (tool.hasAbility("Spirit_of_the_Woods") && rnd.nextDouble() < spiritChance / 100) {
            new SpiritOfTheWoodsAbility(e.getBlock().getLocation(), player).run();
            player.sendMessage(Message.ACTIVATED_ABILITY.toMSG(
                    new PlaceHolder("ability", "Spicy Taco")));
        }

    }

}
