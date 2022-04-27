package me.chickenstyle.poseidontools.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Ability {

    private Location loc;
    private Player player;

    public Ability(Location loc, Player player) {
        this.loc = loc;
        this.player = player;
    }

    public Location getLocation() {
        return loc;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public abstract void run();
}
