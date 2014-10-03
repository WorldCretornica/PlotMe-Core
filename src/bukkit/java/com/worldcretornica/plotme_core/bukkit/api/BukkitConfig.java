package com.worldcretornica.plotme_core.bukkit.api;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;

public class BukkitConfig implements IConfigSection {

    private PlotMe_CorePlugin plugin;
    
    public BukkitConfig(PlotMe_CorePlugin instance) {
        this.plugin = instance;
    }

    public void saveConfig() {
        plugin.saveConfig();
    }

    @Override
    public void loadConfig(String worldPath) {
        ConfigurationSection defaultCS = getDefaultWorld();
        ConfigurationSection configCS;
        if (defaultCS.contains(worldPath)) {
            configCS = plugin.getConfig().getConfigurationSection(worldPath);
        } else {
            plugin.getConfig().set(worldPath, defaultCS);
            saveConfig();
            configCS = plugin.getConfig().getConfigurationSection(worldPath);
        }
        for (String path : defaultCS.getKeys(true)) {
            configCS.addDefault(path, defaultCS.get(path));
        }
    }
    
    private ConfigurationSection getDefaultWorld() {
        InputStream defConfigStream = plugin.getResource("default-world.yml");
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(defConfigStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            isr = new InputStreamReader(defConfigStream);
        }
        return YamlConfiguration.loadConfiguration(isr);
    }

    @Override
    public List<Integer> getIntegerList(String configpath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void set(String string, Object value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getStringList(String configpath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString(String string, String defaultvalue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getInt(String string) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getInt(String string, int defaultvalue) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IConfigSection getConfigurationSection(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getBoolean(String string) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getBoolean(String string, boolean defaultvalue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public double getDouble(String string) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDouble(String string, double defaultvalue) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean contains(String string) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<String> getKeys(boolean b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IConfigSection createSection(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void copyDefaults(boolean b) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object get(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reloadConfig() {
        // TODO Auto-generated method stub
        
    }

}
