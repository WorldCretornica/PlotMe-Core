package com.worldcretornica.plotme_core.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IConfigSection {

    List<Integer> getIntegerList(String configpath);

    void saveConfig();

    void set(String string, Object value);

    List<String> getStringList(String configpath);

    String getString(String string);

    String getString(String string, String defaultvalue);

    int getInt(String string);

    int getInt(String string, int defaultvalue);

    IConfigSection getConfigurationSection(String path);

    boolean getBoolean(String string);

    boolean getBoolean(String string, boolean defaultvalue);

    double getDouble(String string);

    double getDouble(String string, double defaultvalue);

    boolean contains(String string);

    Set<String> getKeys(boolean b);

    IConfigSection createSection(String string);

    void copyDefaults(boolean b);

    Object get(String path);

    void reloadConfig();

    void setDefaults(IConfigSection defConfig);

    void save(File configFile) throws IOException;
}
