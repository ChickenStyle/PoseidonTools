package me.chickenstyle.poseidontools;

import me.chickenstyle.poseidontools.utils.ToolBuilder;
import me.chickenstyle.poseidontools.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PoseidonToolsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("PoseidonTools.give")) return false;
        if (args.length != 2) {
            sender.sendMessage(Utils.color("&7Invalid command usage"));
            sender.sendMessage(Utils.color("&7/pd %player% %tool_type%"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        try {
            ToolType type = ToolType.valueOf(args[1].toUpperCase());
            if (target.isOnline()) {
                target.getInventory().addItem(ToolBuilder.toItemStack(ToolBuilder.generateDefaultTool(type)));
                sender.sendMessage(Utils.color("&aYou gave &6" + target.getName() + " &aa &6" + args[1] + "&a!"));
            } else {
                sender.sendMessage(Utils.color("&7Player " + target.getName() + " isn't online :("));
            }
        } catch (Exception ex) {
            sender.sendMessage(Utils.color("&cInvalid ToolType ;("));
            return false;
        }


        return true;
    }
}
