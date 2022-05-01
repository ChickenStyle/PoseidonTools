package me.chickenstyle.poseidontools;

import me.chickenstyle.poseidontools.gui.ItemStackBuilder;
import me.chickenstyle.poseidontools.utils.ToolBuilder;
import me.chickenstyle.poseidontools.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PoseidonToolsCommand implements CommandExecutor {

    // /pg give %player% tool max
    // /pg give %player% token {amount}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("PoseidonTools.give")) return false;

        try {
            if (!args[0].equalsIgnoreCase("give")) throw new IndexOutOfBoundsException();
            Player target = Bukkit.getPlayer(args[1]);
            if (!target.isOnline()) {
                sender.sendMessage(Utils.color("&7Player isn't online ;("));
                return false;
            }
            if (args[2].equalsIgnoreCase("token")) {
                int amount = Integer.parseInt(args[3]);
                ItemStack tokens = ItemStackBuilder.buildToken();
                tokens.setAmount(amount);
                HashMap<Integer, ItemStack> drops = target.getInventory().addItem(tokens);

                if (!drops.isEmpty()) {
                    for (ItemStack item : drops.values()) {
                        target.getWorld().dropItemNaturally(target.getLocation(), item);
                    }
                }

                sender.sendMessage(Utils.color("&aYou gave &6" + target.getName() + " " + amount + " &7tokens!"));
            } else {
                ToolType type = ToolType.valueOf(args[2].toUpperCase());

                boolean maxLevel = args.length == 4 && args[3].equalsIgnoreCase("max");

                ItemStack item = maxLevel ? ToolBuilder.toItemStack(ToolBuilder.generateMaxTool(type)) :
                                            ToolBuilder.toItemStack(ToolBuilder.generateDefaultTool(type));
                target.getInventory().addItem(item);
            }

        } catch (Exception ex) {
            sender.sendMessage(Utils.color("&7Invalid Command usage"));
            sender.sendMessage(Utils.color("&7/pg give %player% %tool_type% MAX <- (Optional)"));
            sender.sendMessage(Utils.color("&7/pg give %player% TOKEN {amount}"));
        }


        return true;
    }
}
