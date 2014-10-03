package com.worldcretornica.plotme_core.bukkit.api;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;

public class BukkitConfigSection implements IConfigSection {

    private FileConfiguration section;
    private PlotMe_CorePlugin plugin;
    
    public BukkitConfigSection(PlotMe_CorePlugin instance) {
        plugin = instance;
        section = plugin.getConfig();
    }
    
    
    @Override
    public List<Integer> getIntegerList(String configpath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void loadConfig(String worldPath) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveConfig() {
        // TODO Auto-generated method stub
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
