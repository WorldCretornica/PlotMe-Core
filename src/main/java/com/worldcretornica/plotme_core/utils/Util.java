package com.worldcretornica.plotme_core.utils;

import com.worldcretornica.plotme_core.PlotMe_Core;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Util {

    private final PlotMe_Core plugin;

    public Util(PlotMe_Core instance) {
        plugin = instance;
    }

    private static String addColor(String string) {
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

    public String C(String caption) {
        if (plugin.getCaptionConfig().contains(caption)) {
            return addColor(plugin.getCaptionConfig().getString(caption));
        } else {
            plugin.getLogger().log(Level.WARNING, "Missing caption: {0}", caption);
            return "ERROR:Missing caption '" + caption + "'";
        }
    }

    public String moneyFormat(double price, boolean showSign) {
        if (price == 0) {
            return "";
        }

        String format = String.valueOf(Math.round(Math.abs(price)));

        Economy economy = plugin.getServerBridge().getEconomy();

        if (economy != null) {
            if (price <= 1.0 && price >= -1.0) {
                format = format + " " + economy.currencyNameSingular();
            } else {
                format = format + " " + economy.currencyNamePlural();
            }
        }

        if (showSign) {
            if (price > 0.0) {
                return "§a" + ("+" + format);
            } else {
                return "§a" + ("-" + format);
            }
        } else {
            return "§a" + format;
        }
    }

}
