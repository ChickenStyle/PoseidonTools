package me.chickenstyle.poseidontools.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Ability {

    private final Location loc;
    private final Player player;

    public Ability(Location loc, Player player) {
        this.loc = loc;
        this.player = player;
    }

    public Location getLocation() {
        return loc;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void run();
}
