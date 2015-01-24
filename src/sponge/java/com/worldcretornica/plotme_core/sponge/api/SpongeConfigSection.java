package com.worldcretornica.plotme_core.sponge.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.sponge.PlotMe_Sponge;

public class SpongeConfigSection implements IConfigSection {

    private final PlotMe_Sponge plugin;
    
    public SpongeConfigSection(PlotMe_Sponge instance) {
        this.plugin = instance;
    }

    @Override
    public List<Integer> getIntegerList(String configpath) {
        // TODO Auto-generated method stub
        return null;
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
    public boolean contains(String string) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
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

    @Override
    public void setDefaults(IConfigSection defConfig) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void save(File configFile) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
}
