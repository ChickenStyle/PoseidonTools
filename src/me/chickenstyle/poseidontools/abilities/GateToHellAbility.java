package me.chickenstyle.poseidontools.abilities;

import me.chickenstyle.poseidontools.PoseidonTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GateToHellAbility extends Ability {

    private String command;
    public GateToHellAbility(Location loc, Player player) {
        super(loc, player);
        this.command = PoseidonTools.getInstance().getAbilitiesConfig()
                .getConfig().getString("MOBSWORD.Gateway_to_Hell.commandToRun");
    }

    @Override
    public void run() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", getPlayer().getName()));
    }
}
