package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class Plot {

    //TODO look into removing reference to plugin

    private final PlotMe_Core plugin;
    private final HashSet<String> denied;
    private HashMap<String, Integer> allowed;
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
        this.denied = new HashSet<>();
    }

    public Plot(PlotMe_Core plugin, String owner, UUID uuid, IWorld world, PlotId plotId, int days) {
        this.plugin = plugin;
        setOwner(owner);
        setOwnerId(uuid);
        setWorld(world.getName().toLowerCase());
        allowed = new HashMap<>();
        denied = new HashSet<>();
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

    public Plot(PlotMe_Core plugin, int internalID, String owner, UUID ownerId, String world, String biome, Date expiredDate,
            HashMap<String, Integer> allowed,
            HashSet<String>
                    denied,
            PlotId id, double price, boolean forSale, boolean finished,
            String finishedDate, boolean protect,
            Map<String, Map<String, String>> metadata, int plotLikes, String plotName) {
        this.plugin = plugin;
        setInternalID(internalID);
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

    public Plot(PlotMe_Core plugin, int internalID, String owner, UUID ownerId, String world, String biome, Date expiredDate,
            HashMap<String, Integer> allowed,
            HashSet<String>
                    denied,
            HashSet<String> likers, PlotId id, double price, boolean forSale, boolean finished, String finishedDate, boolean protect,
            Map<String, Map<String, String>> metadata, int plotLikes, String plotName) {
        this.plugin = plugin;
        setInternalID(internalID);
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

    public HashSet<String> getDenied() {
        return denied;
    }

    public void addAllowed(String name) {
        if (!isAllowedConsulting(name)) {
            getAllowed().put(name, 1);
            plugin.getSqlManager().addPlotAllowed(name, getInternalID());
        }
    }

    public void addDenied(String name) {
        if (!isDeniedConsulting(name)) {
            getDenied().add(name);
            plugin.getSqlManager().addPlotDenied(name, getInternalID());
        }
    }

    public void removeAllowed(String name) {
        if (getAllowed().containsKey(name)) {
            plugin.getSqlManager().deletePlotAllowed(getInternalID(), name);
        }
    }

    public void removeDenied(String name) {
        if (getDenied().contains(name)) {
            plugin.getSqlManager().deletePlotDenied(getInternalID(), name);
        }
    }

    public void removeAllAllowed() {
        plugin.getSqlManager().deleteAllAllowed(getInternalID());
        getAllowed().clear();
    }

    public void removeAllDenied() {
        plugin.getSqlManager().deleteAllDenied(getInternalID());
        getDenied().clear();
    }

    public boolean isAllowedConsulting(String name) {
        if ("*".equalsIgnoreCase(name)) {
            return isAllowedInternal(name);
        }
        UUID player = plugin.getServerBridge().getOfflinePlayer(name).getUniqueId();
        return player != null && isAllowedInternal(name);
    }

    public boolean isAllowed(UUID uuid) {
        return isAllowedInternal(uuid.toString());
    }

    private boolean isAllowedInternal(String name) {
        if (getAllowed().containsKey(name)) {
            Integer accessLevel = getAllowed().get(name);
            if (accessLevel != null) {
                if (accessLevel == AccessLevel.ALLOWED.getLevel()) {
                    return true;
                } else if (!"*".equalsIgnoreCase(name) && accessLevel == AccessLevel.TRUSTED.getLevel()) {
                    return plugin.getServerBridge().getPlayer(UUIDFetcher.getUUIDOf(name)).isOnline();
                }
            }
        } else if (getAllowed().containsKey("*")) {
            Integer accessLevel = getAllowed().get("*");
            if (accessLevel != null) {
                if (accessLevel == AccessLevel.ALLOWED.getLevel()) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public boolean isDeniedConsulting(String name) {
        IPlayer player = plugin.getServerBridge().getPlayerExact(name);
        return player != null && isDeniedInternal(name);
    }

    public boolean isDenied(UUID uuid) {
        return isDeniedInternal(uuid.toString());
    }

    public boolean isDeniedInternal(String name) {
        return getDenied().contains("*") || getDenied().contains(name);
    }

    public HashMap<String, Integer> getAllowed() {
        return allowed;
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

    enum AccessLevel {
        ALLOWED(0),
        TRUSTED(1);

        private final int level;

        AccessLevel(int accessLevel) {
            level = accessLevel;
        }

        public int getLevel() {
            return level;
        }
    }
}