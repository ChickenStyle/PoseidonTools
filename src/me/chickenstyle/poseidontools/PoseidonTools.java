package me.chickenstyle.poseidontools;

import com.mysql.jdbc.log.Log;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.chickenstyle.poseidontools.events.ToolXPEvents;
import me.chickenstyle.poseidontools.gui.MenuHandler;
import me.chickenstyle.poseidontools.nms.NMSHandler;
import me.chickenstyle.poseidontools.utils.Logger;
import me.chickenstyle.poseidontools.utils.Message;
import me.chickenstyle.poseidontools.ymls.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PoseidonTools extends JavaPlugin {

    private NMSHandler nmsHandler;
    private Config msgConfig;
    private Config menuConfig;
    private MenuHandler menuHandler;
    private Economy eco;
    private WorldGuardPlugin worldGuard;

    private static PoseidonTools instance;

    @Override
    public void onEnable() {
        if (!detectNMSVersion()) {
            Logger.log("&4Not supported Minecraft version!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        menuHandler = new MenuHandler();
        registerEvents();
        setupEconomy();
        loadHooks();

        saveDefaultConfig();
        msgConfig = new Config("messages.yml");
        menuConfig = new Config("gui.yml");

        Message.loadMessages();
        Logger.log("&aPlugin has been loaded!");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new ToolXPEvents(), this);
        getServer().getPluginManager().registerEvents(menuHandler.getListeners(), this);
    }

    private boolean detectNMSVersion() {

        String version;

        try {

            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }
        version = version.substring(1);

        try {
            nmsHandler = (NMSHandler) Class.forName("me.chickenstyle.poseidontools.nms.Handler_" +version).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return false;
        }

        return true;
    }

    public Config getMSGConfig() {
        return msgConfig;
    }

    public NMSHandler getNMS() {
        return nmsHandler;
    }

    public static PoseidonTools getInstance() {
        return instance;
    }

    public MenuHandler getMenuHandler() {
        return menuHandler;
    }

    public Config getMenuConfig() {
        return menuConfig;
    }

    private void loadHooks() {
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            this.worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            Logger.log("&aWorldGuard has been detected!");
        } else {
            Logger.log("&4Couldn't find WorldGuard");
        }

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        this.eco = rsp.getProvider();
        return (eco != null);
    }

    public boolean hasEconomy() {
        return eco != null;
    }

    public boolean hasWorldGuard() {
        return worldGuard != null;
    }

    public WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }

}
