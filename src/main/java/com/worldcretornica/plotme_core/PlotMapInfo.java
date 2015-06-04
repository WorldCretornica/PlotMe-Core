package com.worldcretornica.plotme_core;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlotMapInfo {

    private final PlotMe_Core plugin;

    private final ConcurrentHashMap<PlotId, Plot> plots;
    private final ConfigurationSection config;
    private final ConfigAccessor configFile;
    private final IWorld world;

    public PlotMapInfo(PlotMe_Core instance, ConfigAccessor config, IWorld world) {
        plugin = instance;
        this.world = world;
        this.configFile = config;
        this.config = config.getConfig().getConfigurationSection("worlds." + world.getName().toLowerCase());
        plots = new ConcurrentHashMap<>(1000, 0.75f, 5);
    }

    public int getNbPlots() {
        return plots.size();
    }

    private List<Integer> getProtectedBlocks() {
        return config.getIntegerList("ProtectedBlocks");
    }

    public boolean isProtectedBlock(int blockId) {
        return getProtectedBlocks().contains(blockId);
    }

    private List<String> getPreventedItems() {
        return config.getStringList("PreventedItems");
    }

    /**
     * Returns whether or not this world has the provided item prevented
     *
     * @return true if this world has the provided item prevented, false otherwise
     */
    public boolean isPreventedItem(String itemId) {
        return getPreventedItems().contains(itemId);
    }

    public int getPlotAutoLimit() {
        return config.getInt("PlotAutoLimit");
    }

    public void setPlotAutoLimit(int plotAutoLimit) {
        config.set("PlotAutoLimit", plotAutoLimit);
        configFile.saveConfig();
    }

    public int getDaysToExpiration() {
        return config.getInt("DaysToExpiration");
    }

    public void setDaysToExpiration(int daysToExpiration) {
        config.set("DaysToExpiration", daysToExpiration);
        configFile.saveConfig();
    }

    private ConfigurationSection getEconomySection() {
        return config.getConfigurationSection("economy");
    }

    /**
     * Returns whether or not this world can use economy features.
     *
     * @return true if this world can use economy features, false otherwise
     */
    public boolean canUseEconomy() {
        return getEconomySection().getBoolean("UseEconomy");
    }

    /**
     * Sets if this world can use economy features.
     *
     * @param useEconomy true if this world can use economy features, false otherwise
     */
    public void setUseEconomy(boolean useEconomy) {
        getEconomySection().set("UseEconomy", useEconomy);
        configFile.saveConfig();
    }

    public boolean hasPlotEnterAnnouncement() {
        return config.getBoolean("PlotEnterAnnouncement");
    }

    public void setPlotEnterAnnouncement(boolean announce) {
        config.set("PlotEnterAnnouncement", announce);
        configFile.saveConfig();
    }

    public boolean canUseProjectiles() {
        return config.getBoolean("Projectiles");
    }

    public void setUseProjectiles(boolean allowed) {
        config.set("Projectiles", allowed);
        configFile.saveConfig();
    }

    public boolean isCanPutOnSale() {
        return getEconomySection().getBoolean("CanPutOnSale");
    }

    public void setCanPutOnSale(boolean canPutOnSale) {
        getEconomySection().set("CanPutOnSale", canPutOnSale);
        configFile.saveConfig();
    }

    public boolean isRefundClaimPriceOnReset() {
        return getEconomySection().getBoolean("RefundClaimPriceOnReset");
    }

    public double getClaimPrice() {
        return getEconomySection().getDouble("ClaimPrice");
    }

    public void setClaimPrice(double claimPrice) {
        getEconomySection().set("ClaimPrice", claimPrice);
        configFile.saveConfig();
    }

    public double getClearPrice() {
        return getEconomySection().getDouble("ClearPrice");
    }

    public void setClearPrice(double clearPrice) {
        getEconomySection().set("ClearPrice", clearPrice);
        configFile.saveConfig();
    }

    public double getAddPlayerPrice() {
        return getEconomySection().getDouble("AddPlayerPrice");
    }

    public void setAddPlayerPrice(double addPlayerPrice) {
        getEconomySection().set("AddPlayerPrice", addPlayerPrice);
        configFile.saveConfig();
    }

    public double getDenyPlayerPrice() {
        return getEconomySection().getDouble("DenyPlayerPrice");
    }

    public void setDenyPlayerPrice(double denyPlayerPrice) {
        getEconomySection().set("DenyPlayerPrice", denyPlayerPrice);
        configFile.saveConfig();
    }

    public double getRemovePlayerPrice() {
        return getEconomySection().getDouble("RemovePlayerPrice");
    }

    public void setRemovePlayerPrice(double removePlayerPrice) {
        getEconomySection().set("RemovePlayerPrice", removePlayerPrice);
        configFile.saveConfig();
    }

    public double getUndenyPlayerPrice() {
        return getEconomySection().getDouble("UndenyPlayerPrice");
    }

    public void setUndenyPlayerPrice(double undenyPlayerPrice) {
        getEconomySection().set("UndenyPlayerPrice", undenyPlayerPrice);
        configFile.saveConfig();
    }

    public double getPlotHomePrice() {
        return getEconomySection().getDouble("PlotHomePrice");
    }

    public void setPlotHomePrice(double plotHomePrice) {
        getEconomySection().set("PlotHomePrice", plotHomePrice);
        configFile.saveConfig();
    }

    public double getSellToPlayerPrice() {
        return getEconomySection().getDouble("SellToPlayerPrice");
    }

    public void setSellToPlayerPrice(double sellToPlayerPrice) {
        getEconomySection().set("SellToPlayerPrice", sellToPlayerPrice);
        configFile.saveConfig();
    }

    public double getBiomeChangePrice() {
        return getEconomySection().getDouble("BiomeChangePrice");
    }

    public void setBiomeChangePrice(double biomeChangePrice) {
        getEconomySection().set("BiomeChangePrice", biomeChangePrice);
        configFile.saveConfig();
    }

    public double getProtectPrice() {
        return getEconomySection().getDouble("ProtectPrice");
    }

    public void setProtectPrice(double protectPrice) {
        getEconomySection().set("ProtectPrice", protectPrice);
        configFile.saveConfig();
    }

    public double getDisposePrice() {
        return getEconomySection().getDouble("DisposePrice");
    }

    public void setDisposePrice(double disposePrice) {
        getEconomySection().set("DisposePrice", disposePrice);
        configFile.saveConfig();
    }

    public boolean isAutoLinkPlots() {
        return config.getBoolean("AutoLinkPlots");
    }

    public void setAutoLinkPlots(boolean autoLinkPlots) {
        config.set("AutoLinkPlots", autoLinkPlots);
        configFile.saveConfig();
    }

    public boolean isDisableExplosion() {
        return config.getBoolean("DisableExplosion");
    }

    public void setDisableExplosion(boolean disableExplosion) {
        config.set("DisableExplosion", disableExplosion);
        configFile.saveConfig();
    }

    public boolean isDisableIgnition() {
        return config.getBoolean("DisableIgnition");
    }

    public void setDisableIgnition(boolean disableIgnition) {
        config.set("DisableIgnition", disableIgnition);
        configFile.saveConfig();
    }

    public IWorld getWorld() {
        return world;
    }

}
