package com.worldcretornica.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConfigAccessor {

    private final String fileName;
    private final File configFile;
    private YamlConfiguration fileConfiguration;

    public ConfigAccessor(File pluginFolder, String fileName) {
        this.fileName = fileName;
        this.configFile = new File(pluginFolder, fileName);
    }

    public void reloadFile() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        try (InputStream defConfigStream = getResource(fileName)) {

            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
            fileConfiguration.setDefaults(defConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getResource(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    public YamlConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadFile();
        }

        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ignored) {
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
