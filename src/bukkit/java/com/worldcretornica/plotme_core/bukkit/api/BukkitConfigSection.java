package com.worldcretornica.plotme_core.bukkit.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;

public class BukkitConfigSection implements IConfigSection {

    private FileConfiguration master;
    private ConfigurationSection section;
    private PlotMe_CorePlugin plugin;
    
    public BukkitConfigSection(PlotMe_CorePlugin instance) {
        plugin = instance;
        master = plugin.getConfig();
        section = master;
    }
    
    public BukkitConfigSection(PlotMe_CorePlugin instance, FileConfiguration master) {
        plugin = instance;
        this.section = master;
        this.master = master;
    }
    
    public BukkitConfigSection(PlotMe_CorePlugin instance, FileConfiguration master, ConfigurationSection configurationSection) {
        plugin = instance;
        this.section = configurationSection;
        this.master = master;
    }
    
    
    @Override
    public List<Integer> getIntegerList(String configpath) {
        return section.getIntegerList(configpath);
    }

    @Override
    public void saveConfig() {
        plugin.saveConfig();
    }

    @Override
    public void set(String string, Object value) {
        section.set(string, value);
    }

    @Override
    public List<String> getStringList(String configpath) {
        return section.getStringList(configpath);
    }

    @Override
    public String getString(String string) {
        return section.getString(string);
    }

    @Override
    public String getString(String string, String defaultvalue) {
        return section.getString(string, defaultvalue);
    }

    @Override
    public int getInt(String string) {
        return section.getInt(string);
    }

    @Override
    public int getInt(String string, int defaultvalue) {
        return section.getInt(string, defaultvalue);
    }

    @Override
    public IConfigSection getConfigurationSection(String path) {
        if (section.contains(path)) {
            return new BukkitConfigSection(plugin, master, section.getConfigurationSection(path));
        } else {
            return null;
        }
    }

    @Override
    public boolean getBoolean(String string) {
        return section.getBoolean(string);
    }

    @Override
    public boolean getBoolean(String string, boolean defaultvalue) {
        return section.getBoolean(string, defaultvalue);
    }

    @Override
    public double getDouble(String string) {
        return section.getDouble(string);
    }

    @Override
    public double getDouble(String string, double defaultvalue) {
        return section.getDouble(string, defaultvalue);
    }

    @Override
    public boolean contains(String string) {
        return section.contains(string);
    }

    @Override
    public Set<String> getKeys(boolean b) {
        return section.getKeys(b);
    }

    @Override
    public IConfigSection createSection(String string) {
        return new BukkitConfigSection(plugin, master, section.createSection(string));
    }

    @Override
    public void copyDefaults(boolean b) {
        master.options().copyDefaults();
    }

    @Override
    public Object get(String path) {
        return section.get(path);
    }

    @Override
    public void reloadConfig() {
        plugin.reloadConfig();
    }

    @Override
    public void setDefaults(IConfigSection defConfig) {
        master.setDefaults(((BukkitConfigSection) defConfig).master);
    }

    @Override
    public void save(File configFile) throws IOException {
        master.save(configFile);
    }

}
