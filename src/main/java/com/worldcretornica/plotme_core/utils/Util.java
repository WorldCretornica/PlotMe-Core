package com.worldcretornica.plotme_core.utils;

import com.worldcretornica.plotme_core.PlotMe_Core;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Util {

    private final PlotMe_Core plugin;

    private final String LOG = "[PlotMe-Core Event] ";

    public Util(PlotMe_Core instance) {
        plugin = instance;
    }

    public String C(String s) {
        if (plugin.getCaptionConfig().contains(s)) {
            return addColor(plugin.getCaptionConfig().getString(s));
        } else {
            plugin.getLogger().log(Level.WARNING, "Missing caption: {0}", s);
            return "ERROR:Missing caption '" + s + "'";
        }
    }

    public static String addColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static StringBuilder whitespace(int length) {
        int spaceWidth = MinecraftFontWidthCalculator.getStringWidth(" ");

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i + spaceWidth < length; i += spaceWidth) {
            ret.append(" ");
        }

        return ret;
    }

    public static String round(double money) {
        return money % 1 == 0 ? "" + Math.round(money) : "" + money;
    }

    public void warn(String msg) {
        plugin.getLogger().warning(LOG + msg);
    }

    public String moneyFormat(double price) {
        return moneyFormat(price, true);
    }

    public String moneyFormat(double price, boolean showsign) {
        if (price == 0) {
            return "";
        }

        String format = round(Math.abs(price));

        Economy econ = plugin.getServerBridge().getEconomy();
        
        if (econ != null) {
            format = price <= 1 && price >= -1 ? format + " " + econ.currencyNameSingular() : format + " " + econ.currencyNamePlural();
        }

        if (showsign) {
            return "§a" + (price > 0 ? "+" + format : "-" + format);
        } else {
            return "§a" + format;
        }
    }

    public static String FormatBiome(String biome) {
        biome = biome.toLowerCase();

        String[] tokens = biome.split("_");

        biome = "";

        for (String token : tokens) {
            token = token.substring(0, 1).toUpperCase() + token.substring(1);

            if (biome.isEmpty()) {
                biome = token;
            } else {
                biome = biome + "_" + token;
            }
        }

        return biome;
    }
}
