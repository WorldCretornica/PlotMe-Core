package com.worldcretornica.configuration;

import com.worldcretornica.configuration.file.FileConfiguration;
import com.worldcretornica.configuration.file.YamlConfiguration;
import com.worldcretornica.plotme_core.PlotMe_Core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConfigAccessor {

    private final String fileName;
    private final PlotMe_Core plugin;

    private File configFile;
    private FileConfiguration fileConfiguration;
    private File pluginFolder;

    public ConfigAccessor(PlotMe_Core plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getServerBridge().getDataFolder();
        this.configFile = new File(dataFolder, fileName);
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            fileConfiguration.setDefaults(defConfig);
        }
    }

    private InputStream getResource(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration != null && configFile != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().severe("Could not save config to " + configFile);
            }
        }
    }

    /**
     * Create the file if it does not exist.
     *
     * @return true if the file was created, false if it exists or was never created
     */
    public boolean createFile() {
        if (!configFile.exists()) {
            saveFile(false);
            return true;
        }
        return false;
    }

    /**
     * This will save the file contained in the plugin jar.
     *
     * @param overwrite if the configuration is already created, should it be overwritten
     */
    private void saveFile(boolean overwrite) {
        if (overwrite) {
            try (InputStream in = getResource(fileName); OutputStream out = new FileOutputStream(configFile)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException ignored) {
            }
        }

    }

}
