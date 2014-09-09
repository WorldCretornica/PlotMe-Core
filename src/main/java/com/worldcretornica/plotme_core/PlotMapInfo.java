package com.worldcretornica.plotme_core;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlotMapInfo {

    private final PlotMe_Core plugin;

    private final ConcurrentHashMap<String, Plot> plots;
    private final List<String> freedplots;
    private final String world;
    private final String worldPath;

    /*public PlotMapInfo(PlotMe_Core instance) {
     plugin = instance;
     _plots = new ConcurrentHashMap<String, Plot>();
     _freedplots = new ArrayList<String>();
     }*/
    public PlotMapInfo(PlotMe_Core instance, String world) {
        this.plugin = instance;
        this.world = world;
        this.worldPath = "worlds." + world;
        loadConfig();
        this.plots = new ConcurrentHashMap<>(1000, 0.75f, 5);
        this.freedplots = plugin.getSqlManager().getFreed(world);
    }

    private ConfigurationSection loadConfig() {
        ConfigurationSection defaultCS = getDefaultWorld();
        ConfigurationSection configCS;
        if (plugin.getConfig().contains(worldPath)) {
            configCS = plugin.getConfig().getConfigurationSection(worldPath);
        } else {
            plugin.getConfig().set(worldPath, defaultCS);
            saveConfig();
            configCS = plugin.getConfig().getConfigurationSection(worldPath);
        }
        for (String path : defaultCS.getKeys(true)) {
            configCS.addDefault(path, defaultCS.get(path));
        }
        return configCS;
    }

    private ConfigurationSection getConfig() {
	    return plugin.getConfig().getConfigurationSection(worldPath);
    }

    private ConfigurationSection getDefaultWorld() {
        InputStream defConfigStream = plugin.getResource("default-world.yml");

        return YamlConfiguration.loadConfiguration(defConfigStream);
    }

    public void saveConfig() {
        plugin.saveConfig();
    }

    public int getNbPlots() {
        return plots.size();
    }

    public Plot getPlot(String id) {
        if (id.isEmpty()) {
            return null;
        }
        if (!plots.containsKey(id)) {
            Plot plot = plugin.getSqlManager().getPlot(world, id);
            if (plot == null) {
                return null;
            }

            plots.put(id, plot);
        }

        return plots.get(id);
    }

    public ConcurrentHashMap<String, Plot> getLoadedPlots() {
        return plots;
    }

    public void addPlot(String id, Plot plot) {
        plots.putIfAbsent(id, plot);
    }

    public void removePlot(String id) {
        if (plots.containsKey(id)) {
            plots.remove(id);
        }
    }

    public void addFreed(String id) {
        if (!freedplots.contains(id)) {
            freedplots.add(id);
            int x = plugin.getPlotMeCoreManager().getIdX(id);
            int z = plugin.getPlotMeCoreManager().getIdZ(id);
            plugin.getSqlManager().addFreed(x, z, world);
        }
    }

    public void removeFreed(String id) {
        if (freedplots.contains(id)) {
            freedplots.remove(id);
            int x = plugin.getPlotMeCoreManager().getIdX(id);
            int z = plugin.getPlotMeCoreManager().getIdZ(id);
            plugin.getSqlManager().deleteFreed(x, z, world);
        }
    }

    private List<Integer> getProtectedBlocks() {
        return getConfig().getIntegerList("ProtectedBlocks");
    }

    private void setProtectedBlocks(List<Integer> protectedBlocks) {
        getConfig().set("ProtectedBlocks", protectedBlocks);
        saveConfig();
    }

    public void addProtectedBlock(Integer blockId) {
        if (!isProtectedBlock(blockId)) {
            final List<Integer> protectedBlocks = getProtectedBlocks();
            protectedBlocks.add(blockId);
            setProtectedBlocks(protectedBlocks);
        }
    }

    public void removeProtectedBlock(Integer blockId) {
        if (isProtectedBlock(blockId)) {
            final List<Integer> protectedBlocks = getProtectedBlocks();
            protectedBlocks.remove(blockId);
            setProtectedBlocks(protectedBlocks);
        }
    }

    public boolean isProtectedBlock(Integer blockId) {
        return getProtectedBlocks().contains(blockId);
    }

    private List<String> getPreventedItems() {
        return getConfig().getStringList("PreventedItems");
    }

    private void setPreventedItems(List<String> preventedItems) {
        getConfig().set("PreventedItems", preventedItems);
        saveConfig();
    }

    public void addPreventedItem(String itemId) {
        if (!isPreventedItem(itemId)) {
            final List<String> preventedItems = getPreventedItems();
            preventedItems.add(itemId);
            setPreventedItems(preventedItems);
        }
    }

    public void removePreventedItems(String itemId) {
        if (isPreventedItem(itemId)) {
            final List<String> preventedItems = getPreventedItems();
            preventedItems.remove(itemId);
            setPreventedItems(preventedItems);
        }
    }

    public boolean isPreventedItem(String itemId) {
        return getPreventedItems().contains(itemId);
    }

    public String getNextFreed() {
        if (!freedplots.isEmpty()) {
            return freedplots.get(0);
        } else {
            return getConfig().getString("NextFreed");
        }
    }

    public void setNextFreed(String id) {
        getConfig().set("NextFreed", id);
        saveConfig();
    }

    public int getPlotAutoLimit() {
        return getConfig().getInt("PlotAutoLimit");
    }

    public void setPlotAutoLimit(int plotAutoLimit) {
        getConfig().set("PlotAutoLimit", plotAutoLimit);
        saveConfig();
    }

    public int getDaysToExpiration() {
        return getConfig().getInt("DaysToExpiration");
    }

    public void setDaysToExpiration(int daysToExpiration) {
        getConfig().set("DaysToExpiration", daysToExpiration);
        saveConfig();
    }

    private ConfigurationSection getEconomySection() {
        return getConfig().getConfigurationSection("economy");
    }

    public boolean isUseEconomy() {
        return getEconomySection().getBoolean("UseEconomy");
    }

    public void setUseEconomy(boolean useEconomy) {
        getEconomySection().set("UseEconomy", useEconomy);
        saveConfig();
    }

    public boolean isCanPutOnSale() {
        return getEconomySection().getBoolean("CanPutOnSale");
    }

    public void setCanPutOnSale(boolean canPutOnSale) {
        getEconomySection().set("CanPutOnSale", canPutOnSale);
        saveConfig();
    }

    public boolean isCanSellToBank() {
        return getEconomySection().getBoolean("CanSellToBank");
    }

    public void setCanSellToBank(boolean canSellToBank) {
        getEconomySection().set("CanSellToBank", canSellToBank);
        saveConfig();
    }

    public boolean isRefundClaimPriceOnReset() {
        return getEconomySection().getBoolean("RefundClaimPriceOnReset");
    }

    public void setRefundClaimPriceOnReset(boolean refundClaimPriceOnReset) {
        getEconomySection().set("RefundClaimPriceOnReset", refundClaimPriceOnReset);
        saveConfig();
    }

    public boolean isRefundClaimPriceOnSetOwner() {
        return getEconomySection().getBoolean("RefundClaimPriceOnSetOwner");
    }

    public void setRefundClaimPriceOnSetOwner(boolean refundClaimPriceOnSetOwner) {
        getEconomySection().set("RefundClaimPriceOnSetOwner", refundClaimPriceOnSetOwner);
        saveConfig();
    }

    public double getClaimPrice() {
        return getEconomySection().getDouble("ClaimPrice");
    }

    public void setClaimPrice(double claimPrice) {
        getEconomySection().set("ClaimPrice", claimPrice);
        saveConfig();
    }

    public double getClearPrice() {
        return getEconomySection().getDouble("ClearPrice");
    }

    public void setClearPrice(double clearPrice) {
        getEconomySection().set("ClearPrice", clearPrice);
        saveConfig();
    }

    public double getAddPlayerPrice() {
        return getEconomySection().getDouble("AddPlayerPrice");
    }

    public void setAddPlayerPrice(double addPlayerPrice) {
        getEconomySection().set("AddPlayerPrice", addPlayerPrice);
        saveConfig();
    }

    public double getDenyPlayerPrice() {
        return getEconomySection().getDouble("DenyPlayerPrice");
    }

    public void setDenyPlayerPrice(double denyPlayerPrice) {
        getEconomySection().set("DenyPlayerPrice", denyPlayerPrice);
        saveConfig();
    }

    public double getRemovePlayerPrice() {
        return getEconomySection().getDouble("RemovePlayerPrice");
    }

    public void setRemovePlayerPrice(double removePlayerPrice) {
        getEconomySection().set("RemovePlayerPrice", removePlayerPrice);
        saveConfig();
    }

    public double getUndenyPlayerPrice() {
        return getEconomySection().getDouble("UndenyPlayerPrice");
    }

    public void setUndenyPlayerPrice(double undenyPlayerPrice) {
        getEconomySection().set("UndenyPlayerPrice", undenyPlayerPrice);
        saveConfig();
    }

    public double getPlotHomePrice() {
        return getEconomySection().getDouble("PlotHomePrice");
    }

    public void setPlotHomePrice(double plotHomePrice) {
        getEconomySection().set("PlotHomePrice", plotHomePrice);
        saveConfig();
    }

    public boolean isCanCustomizeSellPrice() {
        return getEconomySection().getBoolean("CanCustomizeSellPrice");
    }

    public void setCanCustomizeSellPrice(boolean canCustomizeSellPrice) {
        getEconomySection().set("CanCustomizeSellPrice", canCustomizeSellPrice);
        saveConfig();
    }

    public double getSellToPlayerPrice() {
        return getEconomySection().getDouble("SellToPlayerPrice");
    }

    public void setSellToPlayerPrice(double sellToPlayerPrice) {
        getEconomySection().set("SellToPlayerPrice", sellToPlayerPrice);
        saveConfig();
    }

    public double getSellToBankPrice() {
        return getEconomySection().getDouble("SellToBankPrice");
    }

    public void setSellToBankPrice(double sellToBankPrice) {
        getEconomySection().set("SellToBankPrice", sellToBankPrice);
        saveConfig();
    }

    public double getBuyFromBankPrice() {
        return getEconomySection().getDouble("BuyFromBankPrice");
    }

    public void setBuyFromBankPrice(double buyFromBankPrice) {
        getEconomySection().set("BuyFromBankPrice", buyFromBankPrice);
        saveConfig();
    }

    public double getAddCommentPrice() {
        return getEconomySection().getDouble("AddCommentPrice");
    }

    public void setAddCommentPrice(double addCommentPrice) {
        getEconomySection().set("AddCommentPrice", addCommentPrice);
        saveConfig();
    }

    public double getBiomeChangePrice() {
        return getEconomySection().getDouble("BiomeChangePrice");
    }

    public void setBiomeChangePrice(double biomeChangePrice) {
        getEconomySection().set("BiomeChangePrice", biomeChangePrice);
        saveConfig();
    }

    public double getProtectPrice() {
        return getEconomySection().getDouble("ProtectPrice");
    }

    public void setProtectPrice(double protectPrice) {
        getEconomySection().set("ProtectPrice", protectPrice);
        saveConfig();
    }

    public double getDisposePrice() {
        return getEconomySection().getDouble("DisposePrice");
    }

    public void setDisposePrice(double disposePrice) {
        getEconomySection().set("DisposePrice", disposePrice);
        saveConfig();
    }

    public boolean isAutoLinkPlots() {
        return getConfig().getBoolean("AutoLinkPlots");
    }

    public void setAutoLinkPlots(boolean autoLinkPlots) {
        getConfig().set("AutoLinkPlots", autoLinkPlots);
        saveConfig();
    }

    public boolean isDisableExplosion() {
        return getConfig().getBoolean("DisableExplosion");
    }

    public void setDisableExplosion(boolean disableExplosion) {
        getConfig().set("DisableExplosion", disableExplosion);
        saveConfig();
    }

    public boolean isDisableIgnition() {
        return getConfig().getBoolean("DisableIgnition");
    }

    public void setDisableIgnition(boolean disableIgnition) {
        getConfig().set("DisableIgnition", disableIgnition);
        saveConfig();
    }

    public boolean isUseProgressiveClear() {
        return getConfig().getBoolean("UseProgressiveClear");
    }

    public void setUseProgressiveClear(boolean useProgressiveClear) {
        getConfig().set("UseProgressiveClear", useProgressiveClear);
        saveConfig();
    }
}
