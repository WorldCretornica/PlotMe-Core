package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BukkitConfigSection implements IConfigSection {

    private final FileConfiguration master;
    private final ConfigurationSection section;
    private final PlotMe_CorePlugin plugin;

    public BukkitConfigSection(PlotMe_CorePlugin instance) {
        plugin = instance;
        master = plugin.getConfig();
        section = master;
    }

    public BukkitConfigSection(PlotMe_CorePlugin instance, FileConfiguration master) {
        plugin = instance;
        section = master;
        this.master = master;
    }

    public BukkitConfigSection(PlotMe_CorePlugin instance, FileConfiguration master, ConfigurationSection configSection) {
        plugin = instance;
        this.master = master;
        section = configSection;
    }


    @Override
    public List<Integer> getIntegerList(String configpath) {
        return section.getIntegerList(configpath);
    }

    @Override
    public void saveConfig() {
        plugin.saveConfig();
    }

    /**
     * Sets the specified path to the given value.
     * <p/>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     * <p/>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store {@link Configuration}s or {@link ConfigurationSection}s,
     * please use {@link #createSection(String)} for that.
     *
     * @param path  Path of the object to set.
     * @param value New value to set the path to.
     */
    @Override
    public void set(String path, Object value) {
        section.set(path, value);
    }

    @Override
    public List<String> getStringList(String configPath) {
        return section.getStringList(configPath);
    }

    @Override
    public String getString(String string) {
        return section.getString(string);
    }

    @Override
    public String getString(String string, String defaultValue) {
        return section.getString(string, defaultValue);
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
    public boolean contains(String string) {
        return section.contains(string);
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return section.getKeys(deep);
    }

    @Override
    public IConfigSection createSection(String string) {
        return new BukkitConfigSection(plugin, master, section.createSection(string));
    }

    @Override
    public void copyDefaults(boolean b) {
        master.options().copyDefaults(b);
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
