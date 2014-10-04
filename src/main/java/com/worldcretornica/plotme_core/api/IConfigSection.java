package com.worldcretornica.plotme_core.api;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface IConfigSection {

    public List<Integer> getIntegerList(String configpath);
    
    public void loadConfig(String worldPath);
    
    public void saveConfig();

    public void set(String string, Object value);

    public List<String> getStringList(String configpath);

    public String getString(String string);
    public String getString(String string, String defaultvalue);

    public int getInt(String string);
    public int getInt(String string, int defaultvalue);
    
    public IConfigSection getConfigurationSection(String path);

    public boolean getBoolean(String string);
    public boolean getBoolean(String string, boolean defaultvalue);

    public double getDouble(String string);
    public double getDouble(String string, double defaultvalue);

    public boolean contains(String string);

    public Set<String> getKeys(boolean b);

    public IConfigSection createSection(String string);

    public void copyDefaults(boolean b);

    public Object get(String path);

    public void reloadConfig();

    public void setDefaults(IConfigSection defConfig);

    public void save(File configFile);
}
