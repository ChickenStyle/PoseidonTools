package me.chickenstyle.poseidontools.ymls;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.utils.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    private File file;
    private FileConfiguration config;

    public Config(String configName) {
        PoseidonTools.getInstance().saveResource(configName, false);
        this.file = new File(PoseidonTools.getInstance().getDataFolder()+"/" + configName);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void set(String path, Object data) {
        this.config.set(path, data);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public String getString(String path) {
        return (String) get(path);
    }

    public List<String> getStringList(String path) {
        return (List<String>) get(path);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        Message.loadMessages();
    }
}
