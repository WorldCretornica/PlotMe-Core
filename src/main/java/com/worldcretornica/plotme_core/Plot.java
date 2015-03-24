package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Plot {

    //TODO look into removing reference to plugin

    private final PlotMe_Core plugin;
    private final PlayerList allowed;
    private final PlayerList denied;
    private String owner;
    private UUID ownerId = UUID.randomUUID();
    private String world;
    private String biome;
    private Date expiredDate;
    private boolean finished;
    private PlotId id;
    private double price;
    private boolean forSale;
    private String finishedDate;
    private boolean protect;
    private Map<String, Map<String, String>> metadata;
    private int likes;
    private int internalID;
    private String plotName;

    public Plot(PlotMe_Core plugin) {
        this.plugin = plugin;
        setBiome("PLAINS");

        setExpiredDate(null);
        setPrice(0.0);
        setForSale(false);
        setFinishedDate(null);
        setProtected(false);
        setPlotName(null);
        setLikes(0);
        allowed = new PlayerList();
        denied = new PlayerList();
    }

    public Plot(PlotMe_Core plugin, String owner, UUID uuid, IWorld world, PlotId plotId, int days) {
        this.plugin = plugin;
        setOwner(owner);
        setOwnerId(uuid);
        setWorld(world.getName().toLowerCase());
        allowed = new PlayerList();
        denied = new PlayerList();
        setBiome("PLAINS");
        setId(plotId);

        if (days == 0) {
            setExpiredDate(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            java.util.Date utlDate = cal.getTime();
            expiredDate = new Date(utlDate.getTime());
        }

        setPrice(0.0);
        setForSale(false);
        setFinishedDate(null);
        setProtected(false);
        metadata = new HashMap<>();
    }

    public Plot(PlotMe_Core plugin, String owner, UUID ownerId, String world, String biome, Date expiredDate, boolean finished,
            PlayerList allowed, PlotId id, double price, boolean forSale, String finishedDate, boolean protect, PlayerList denied,
            Map<String, Map<String, String>> metadata, int plotLikes, String plotName) {
        this.plugin = plugin;
        setOwner(owner);
        setOwnerId(ownerId);
        setWorld(world);
        setBiome(biome);
        setExpiredDate(expiredDate);
        setFinished(finished);
        this.allowed = allowed;
        setId(id);
        setPrice(price);
        setForSale(forSale);
        setFinishedDate(finishedDate);
        setProtected(protect);
        setLikes(plotLikes);
        setPlotName(plotName);
        this.denied = denied;
        this.metadata = metadata;
    }

    public void resetExpire(int days) {
        if (days == 0) {
            if (getExpiredDate() != null) {
                setExpiredDate(null);
                updateField("expireddate", getExpiredDate());
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            java.util.Date utlDate = cal.getTime();
            java.sql.Date temp = new java.sql.Date(utlDate.getTime());
            if (expiredDate == null || !temp.toString().equalsIgnoreCase(expiredDate.toString())) {
                expiredDate = temp;
                updateField("expireddate", expiredDate);
            }
        }
    }

    public void setFinished() {
        setFinishedDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        setFinished(true);

        updateFinished(getFinishedDate(), true);
    }

    public void setUnfinished() {
        setFinishedDate(null);
        setFinished(false);

        updateFinished(null, false);
    }

    public String getBiome() {
        return biome;
    }

    public final void setBiome(String biome) {
        this.biome = biome;
    }

    public final String getOwner() {
        return owner;
    }

    public final void setOwner(String owner) {
        this.owner = owner;
    }

    public final UUID getOwnerId() {
        return ownerId;
    }

    public final void setOwnerId(UUID uuid) {
        ownerId = uuid;
    }

    public String getAllowed() {
        return allowed().getPlayerList();
    }

    public String getDenied() {
        return denied().getPlayerList();
    }

    public void addAllowed(String name) {
        if (!isAllowedConsulting(name)) {
            allowed().put(name);
            plugin.getSqlManager().addPlotAllowed(name, getInternalID());
        }
    }

    public void addDenied(String name) {
        if (!isDeniedConsulting(name)) {
            denied().put(name);
            plugin.getSqlManager().addPlotDenied(name, getInternalID());
        }
    }

    public void removeAllowed(String name) {
        if (allowed().contains(name)) {
            plugin.getSqlManager().deletePlotAllowed(getInternalID(), name);

            if (plugin.getServerBridge().getPlotWorldEdit() != null) {
                IPlayer player = (IPlayer) plugin.getServerBridge().getOfflinePlayer(name);

                if (player != null && PlotMeCoreManager.getInstance().isPlotWorld(player.getWorld())) {
                    if (PlotMeCoreManager.getInstance().isPlayerIgnoringWELimit(player)) {
                        plugin.getServerBridge().getPlotWorldEdit().removeMask(player);
                    } else {
                        plugin.getServerBridge().getPlotWorldEdit().setMask(player);
                    }
                }
            }
        }
    }

    public void removeDenied(String name) {
        if (denied().contains(name)) {
            plugin.getSqlManager().deletePlotDenied(getInternalID(), name);
        }
    }

    public void removeAllAllowed() {
        List<String> list = allowed().getAllPlayers();
        for (String n : list) {
            plugin.getSqlManager().deleteAllAllowed(getInternalID());
        }
        allowed().clear();
    }

    public void removeAllDenied() {
        List<String> list = denied().getAllPlayers();
        for (String n : list) {
            plugin.getSqlManager().deleteAllDenied(getInternalID());
        }
        denied().clear();
    }

    public boolean isAllowedConsulting(String name) {
        if ("*".equals(name)) {
            return isAllowedInternal(name);
        }
        UUID player = plugin.getServerBridge().getOfflinePlayer(name).getUniqueId();
        return player != null && isAllowedInternal(name);
    }

    public boolean isAllowed(UUID uuid) {
        return isAllowedInternal(uuid.toString());
    }

    private boolean isAllowedInternal(String name) {
        List<String> list = allowed().getAllPlayers();
        return this.getOwnerId().toString().equals(name) || list.contains(name) || list.contains("*");
    }

    public boolean isDeniedConsulting(String name) {
        IPlayer player = plugin.getServerBridge().getPlayerExact(name);
        return player != null && isDeniedInternal(name);
    }

    public boolean isDenied(UUID uuid) {
        return isDeniedInternal(uuid.toString());
    }

    public boolean isDeniedInternal(String name) {
        List<String> list = denied().getAllPlayers();
        return !isAllowedInternal(name) && (list.contains("*") || list.contains(name));
    }

    public PlayerList allowed() {
        return allowed;
    }

    public PlayerList denied() {
        return denied;
    }

    private void updateFinished(String finishTime, boolean isFinished) {
        updateField("finisheddate", finishTime);
        updateField("finished", isFinished);
    }

    public void updateField(String field, Object value) {
        plugin.getSqlManager().updatePlot(getId(), getWorld(), field, value);
    }

    public final String getWorld() {
        return world;
    }

    public final void setWorld(String world) {
        this.world = world.toLowerCase();
    }

    public final Date getExpiredDate() {
        return expiredDate;
    }

    public final void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public final boolean isFinished() {
        return finished;
    }

    public final void setFinished(boolean finished) {
        this.finished = finished;
    }

    public final PlotId getId() {
        return id;
    }

    public final void setId(PlotId id) {
        this.id = id;
    }

    public final double getPrice() {
        return price;
    }

    public final void setPrice(double customPrice) {
        this.price = customPrice;
    }

    public final boolean isForSale() {
        return forSale;
    }

    public final void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public final String getFinishedDate() {
        return finishedDate;
    }

    public final void setFinishedDate(String finishedDate) {
        this.finishedDate = finishedDate;
    }

    public final boolean isProtected() {
        return protect;
    }

    public final void setProtected(boolean protect) {
        this.protect = protect;
    }

    public String getPlotProperty(String pluginname, String property) {
        return metadata.get(pluginname).get(property);
    }

    public boolean setPlotProperty(String pluginname, String property, String value) {
        if (!metadata.containsKey(pluginname)) {
            metadata.put(pluginname, new HashMap<String, String>());
        }
        metadata.get(pluginname).put(property, value);
        return plugin.getSqlManager().savePlotProperty(getId(), this.world, pluginname, property, value);
    }

    public Map<String, Map<String, String>> getAllPlotProperties() {
        return metadata;
    }

    public int getInternalID() {
        return internalID;
    }

    public void setInternalID(int internalID) {
        this.internalID = internalID;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPlotName() {
        return plotName;
    }

    public void setPlotName(String plotName) {
        this.plotName = plotName;
    }
}