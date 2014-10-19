package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IConfigSection;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlotMapInfo {

    private final PlotMe_Core plugin;

    private final ConcurrentHashMap<String, Plot> plots;
    private final List<String> freedplots;
    private final String world;
    private final IConfigSection config;

    public PlotMapInfo(PlotMe_Core instance, String world) {
        this.plugin = instance;
        this.world = world;
        config = plugin.getServerBridge().loadDefaultConfig("worlds." + world);
        plots = new ConcurrentHashMap<>(1000, 0.75f, 5);
        freedplots = plugin.getSqlManager().getFreed(world);
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
        return config.getIntegerList("ProtectedBlocks");
    }

    public boolean isProtectedBlock(int blockId) {
        return getProtectedBlocks().contains(blockId);
    }

    private List<String> getPreventedItems() {
        return config.getStringList("PreventedItems");
    }

    public boolean isPreventedItem(String itemId) {
        return getPreventedItems().contains(itemId);
    }

    public String getNextFreed() {
        if (freedplots.isEmpty()) {
            return config.getString("NextFreed");
        } else {
            return freedplots.get(0);
        }
    }

    public void setNextFreed(String id) {
        config.set("NextFreed", id);
        config.saveConfig();
    }

    public int getPlotAutoLimit() {
        return config.getInt("PlotAutoLimit");
    }

    public void setPlotAutoLimit(int plotAutoLimit) {
        config.set("PlotAutoLimit", plotAutoLimit);
        config.saveConfig();
    }

    public int getDaysToExpiration() {
        return config.getInt("DaysToExpiration");
    }

    public void setDaysToExpiration(int daysToExpiration) {
        config.set("DaysToExpiration", daysToExpiration);
        config.saveConfig();
    }

    private IConfigSection getEconomySection() {
        return config.getConfigurationSection("economy");
    }

    public boolean isUseEconomy() {
        return getEconomySection().getBoolean("UseEconomy");
    }

    public void setUseEconomy(boolean useEconomy) {
        getEconomySection().set("UseEconomy", useEconomy);
        config.saveConfig();
    }

    public boolean isCanPutOnSale() {
        return getEconomySection().getBoolean("CanPutOnSale");
    }

    public void setCanPutOnSale(boolean canPutOnSale) {
        getEconomySection().set("CanPutOnSale", canPutOnSale);
        config.saveConfig();
    }

    public boolean isCanSellToBank() {
        return getEconomySection().getBoolean("CanSellToBank");
    }

    public void setCanSellToBank(boolean canSellToBank) {
        getEconomySection().set("CanSellToBank", canSellToBank);
        config.saveConfig();
    }

    public boolean isRefundClaimPriceOnReset() {
        return getEconomySection().getBoolean("RefundClaimPriceOnReset");
    }

    public void setRefundClaimPriceOnReset(boolean refundClaimPriceOnReset) {
        getEconomySection().set("RefundClaimPriceOnReset", refundClaimPriceOnReset);
        config.saveConfig();
    }

    public boolean isRefundClaimPriceOnSetOwner() {
        return getEconomySection().getBoolean("RefundClaimPriceOnSetOwner");
    }

    public void setRefundClaimPriceOnSetOwner(boolean refundClaimPriceOnSetOwner) {
        getEconomySection().set("RefundClaimPriceOnSetOwner", refundClaimPriceOnSetOwner);
        config.saveConfig();
    }

    public double getClaimPrice() {
        return getEconomySection().getDouble("ClaimPrice");
    }

    public void setClaimPrice(double claimPrice) {
        getEconomySection().set("ClaimPrice", claimPrice);
        config.saveConfig();
    }

    public double getClearPrice() {
        return getEconomySection().getDouble("ClearPrice");
    }

    public void setClearPrice(double clearPrice) {
        getEconomySection().set("ClearPrice", clearPrice);
        config.saveConfig();
    }

    public double getAddPlayerPrice() {
        return getEconomySection().getDouble("AddPlayerPrice");
    }

    public void setAddPlayerPrice(double addPlayerPrice) {
        getEconomySection().set("AddPlayerPrice", addPlayerPrice);
        config.saveConfig();
    }

    public double getDenyPlayerPrice() {
        return getEconomySection().getDouble("DenyPlayerPrice");
    }

    public void setDenyPlayerPrice(double denyPlayerPrice) {
        getEconomySection().set("DenyPlayerPrice", denyPlayerPrice);
        config.saveConfig();
    }

    public double getRemovePlayerPrice() {
        return getEconomySection().getDouble("RemovePlayerPrice");
    }

    public void setRemovePlayerPrice(double removePlayerPrice) {
        getEconomySection().set("RemovePlayerPrice", removePlayerPrice);
        config.saveConfig();
    }

    public double getUndenyPlayerPrice() {
        return getEconomySection().getDouble("UndenyPlayerPrice");
    }

    public void setUndenyPlayerPrice(double undenyPlayerPrice) {
        getEconomySection().set("UndenyPlayerPrice", undenyPlayerPrice);
        config.saveConfig();
    }

    public double getPlotHomePrice() {
        return getEconomySection().getDouble("PlotHomePrice");
    }

    public void setPlotHomePrice(double plotHomePrice) {
        getEconomySection().set("PlotHomePrice", plotHomePrice);
        config.saveConfig();
    }

    public boolean isCanCustomizeSellPrice() {
        return getEconomySection().getBoolean("CanCustomizeSellPrice");
    }

    public void setCanCustomizeSellPrice(boolean canCustomizeSellPrice) {
        getEconomySection().set("CanCustomizeSellPrice", canCustomizeSellPrice);
        config.saveConfig();
    }

    public double getSellToPlayerPrice() {
        return getEconomySection().getDouble("SellToPlayerPrice");
    }

    public void setSellToPlayerPrice(double sellToPlayerPrice) {
        getEconomySection().set("SellToPlayerPrice", sellToPlayerPrice);
        config.saveConfig();
    }

    public double getSellToBankPrice() {
        return getEconomySection().getDouble("SellToBankPrice");
    }

    public void setSellToBankPrice(double sellToBankPrice) {
        getEconomySection().set("SellToBankPrice", sellToBankPrice);
        config.saveConfig();
    }

    public double getBuyFromBankPrice() {
        return getEconomySection().getDouble("BuyFromBankPrice");
    }

    public void setBuyFromBankPrice(double buyFromBankPrice) {
        getEconomySection().set("BuyFromBankPrice", buyFromBankPrice);
        config.saveConfig();
    }

    public double getAddCommentPrice() {
        return getEconomySection().getDouble("AddCommentPrice");
    }

    public void setAddCommentPrice(double addCommentPrice) {
        getEconomySection().set("AddCommentPrice", addCommentPrice);
        config.saveConfig();
    }

    public double getBiomeChangePrice() {
        return getEconomySection().getDouble("BiomeChangePrice");
    }

    public void setBiomeChangePrice(double biomeChangePrice) {
        getEconomySection().set("BiomeChangePrice", biomeChangePrice);
        config.saveConfig();
    }

    public double getProtectPrice() {
        return getEconomySection().getDouble("ProtectPrice");
    }

    public void setProtectPrice(double protectPrice) {
        getEconomySection().set("ProtectPrice", protectPrice);
        config.saveConfig();
    }

    public double getDisposePrice() {
        return getEconomySection().getDouble("DisposePrice");
    }

    public void setDisposePrice(double disposePrice) {
        getEconomySection().set("DisposePrice", disposePrice);
        config.saveConfig();
    }

    public boolean isAutoLinkPlots() {
        return config.getBoolean("AutoLinkPlots");
    }

    public void setAutoLinkPlots(boolean autoLinkPlots) {
        config.set("AutoLinkPlots", autoLinkPlots);
        config.saveConfig();
    }

    public boolean isDisableExplosion() {
        return config.getBoolean("DisableExplosion");
    }

    public void setDisableExplosion(boolean disableExplosion) {
        config.set("DisableExplosion", disableExplosion);
        config.saveConfig();
    }

    public boolean isDisableIgnition() {
        return config.getBoolean("DisableIgnition");
    }

    public void setDisableIgnition(boolean disableIgnition) {
        config.set("DisableIgnition", disableIgnition);
        config.saveConfig();
    }

    public boolean isUseProgressiveClear() {
        return config.getBoolean("UseProgressiveClear");
    }

    public void setUseProgressiveClear(boolean useProgressiveClear) {
        config.set("UseProgressiveClear", useProgressiveClear);
        config.saveConfig();
    }
}
