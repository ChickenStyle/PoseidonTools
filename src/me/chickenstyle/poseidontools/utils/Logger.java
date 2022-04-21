package me.chickenstyle.poseidontools.utils;

import org.bukkit.Bukkit;

public class Logger {

    private static String prefix = "&7[&bPoseidon&dTools&7] >>>&f ";

    public static void log(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.color(prefix + msg));
    }
}
