package com.worldcretornica.configuration.file;

/**
 * Various settings for controlling the input and output of a {@link
 * YamlConfiguration}
 */
public class YamlConfigurationOptions extends FileConfigurationOptions {

    protected YamlConfigurationOptions(YamlConfiguration configuration) {
        super(configuration);
    }


    @Override
    public YamlConfiguration configuration() {
        return (YamlConfiguration) super.configuration();
    }


    @Override
    public YamlConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }


    @Override
    public YamlConfigurationOptions header(String value) {
        super.header(value);
        return this;
    }


    @Override
    public YamlConfigurationOptions copyHeader(boolean value) {
        super.copyHeader(value);
        return this;
    }

}
