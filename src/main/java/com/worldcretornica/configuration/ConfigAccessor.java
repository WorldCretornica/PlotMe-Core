package com.worldcretornica.configuration;

import com.worldcretornica.configuration.file.FileConfiguration;
import com.worldcretornica.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConfigAccessor {

    private final String fileName;
    private final File configFile;
    private final Object plugin;
    private FileConfiguration fileConfiguration;

    public ConfigAccessor(Object plugin, File pluginFolder, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(pluginFolder, fileName);
    }

    public void reloadFile() {
        fileConfiguration = YamlConfiguration.loadConfig(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = getResource(fileName);
        YamlConfiguration defConfig = YamlConfiguration.loadConfig(new InputStreamReader(defConfigStream));
        fileConfiguration.setDefaults(defConfig);
    }

    private InputStream getResource(String fileName) {
        return plugin.getClass().getResourceAsStream(fileName);
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadFile();
        }

        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
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
            saveFile(true);
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
