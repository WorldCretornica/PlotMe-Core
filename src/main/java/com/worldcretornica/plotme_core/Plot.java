package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class Plot {

    private final int topX;
    private final int bottomX;
    private final int topZ;
    private final int bottomZ;
    private final HashMap<String, Plot.AccessLevel> allowed = new HashMap<>();
    private final HashSet<String> denied = new HashSet<>();
    private final HashMap<String, Map<String, String>> metadata = new HashMap<>();
    private String owner = "Unknown";
    private UUID ownerId = UUID.randomUUID();
    private IWorld world;
    private String biome = "PLAINS";
    private Date expiredDate = null;
    private boolean finished = false;
    private PlotId id = new PlotId(0, 0);
    private double price = 0.0;
    private boolean forSale = false;
    private String finishedDate = null;
    private boolean protect = false;
    private int likes = 0;
    //defaults to 0 until it is saved to the database
    private long internalID = 0;
    private String plotName;
    private HashSet<String> likers = new HashSet<>();
    private String createdDate;

    public Plot(String owner, UUID uuid, IWorld world, PlotId plotId, Vector plotTopLoc, Vector plotBottomLoc) {
        setOwner(owner);
        setOwnerId(uuid);
        setWorld(world);
        setId(plotId);
        topX = plotTopLoc.getBlockX();
        topZ = plotTopLoc.getBlockZ();
        bottomX = plotBottomLoc.getBlockX();
        bottomZ = plotBottomLoc.getBlockZ();
        createdDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public Plot(long internalID, String owner, UUID ownerId, IWorld world, String biome, Date expiredDate,
            HashMap<String, AccessLevel> allowed,
            HashSet<String>
                    denied,
            HashSet<String> likers, PlotId id, double price, boolean forSale, boolean finished, String finishedDate, boolean protect,
            Map<String, Map<String, String>> metadata, int plotLikes, String plotName, int topX, int bottomX, int topZ, int bottomZ,
            String createdDate) {
        setInternalID(internalID);
        setOwner(owner);
        setOwnerId(ownerId);
        setWorld(world);
        setBiome(biome);
        setExpiredDate(expiredDate);
        setFinished(finished);
        this.allowed.putAll(allowed);
        setId(id);
        setPrice(price);
        setForSale(forSale);
        setFinishedDate(finishedDate);
        setProtected(protect);
        setLikers(likers);
        setLikes(plotLikes);
        setPlotName(plotName);
        this.denied.addAll(denied);
        this.metadata.putAll(metadata);
        this.topX = topX;
        this.bottomX = bottomX;
        this.topZ = topZ;
        this.bottomZ = bottomZ;
        this.createdDate = createdDate;
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

    public void addMember(String name, AccessLevel level) {
        if (!isAllowedConsulting(name)) {
            getMembers().put(name, level);
            PlotMeCoreManager.getInstance().getSQLManager().addPlotMember(name, getInternalID(), level);
        }
    }

    public void addDenied(String name) {
        if (!isDeniedConsulting(name)) {
            getDenied().add(name);
            PlotMeCoreManager.getInstance().getSQLManager().addPlotDenied(name, getInternalID());
        }
    }

    public void removeAllowed(String name) {
        if (getMembers().containsKey(name)) {
            PlotMeCoreManager.getInstance().getSQLManager().deletePlotAllowed(getInternalID(), name);
        }
    }

    public void removeDenied(String name) {
        if (getDenied().contains(name)) {
            PlotMeCoreManager.getInstance().getSQLManager().deletePlotDenied(getInternalID(), name);
        }
    }

    public void removeAllAllowed() {
        PlotMeCoreManager.getInstance().getSQLManager().deleteAllAllowed(getInternalID());
        getMembers().clear();
    }

    public void removeAllDenied() {
        PlotMeCoreManager.getInstance().getSQLManager().deleteAllDenied(getInternalID());
        getDenied().clear();
    }

    public boolean isAllowedConsulting(String name) {
        if ("*".equals(name)) {
            return isAllowedInternal(name);
        }
        UUID player = PlotMeCoreManager.getInstance().getPlayer(name).getUniqueId();
        return player != null && isAllowedInternal(name);
    }

    public boolean isAllowed(UUID uuid) {
        return isAllowedInternal(uuid.toString());
    }
    public boolean isAllowed(String uuid) {
        return isAllowedInternal(uuid);
    }

    private boolean isAllowedInternal(String name) {
        if (getMembers().containsKey(name)) {
            AccessLevel accessLevel = getMembers().get(name);
            if (accessLevel == AccessLevel.ALLOWED) {
                return true;
            } else if ("*".equals(name)) {
                return false;
            }if (accessLevel == AccessLevel.TRUSTED) {
                return PlotMeCoreManager.getInstance().getPlayer(name) != null;
            }
        } else {
            return getMembers().containsKey("*");
        }
        return false;
    }

    public boolean isDeniedConsulting(String name) {
        IOfflinePlayer player = PlotMeCoreManager.getInstance().getPlayer(name);
        return player != null && isDeniedInternal(name);
    }

    public boolean isDenied(UUID uuid) {
        return isDeniedInternal(uuid.toString());
    }

    public boolean isDeniedInternal(String name) {
        return getDenied().contains("*") || getDenied().contains(name);
    }

    /**
     * A map of allowed and trusted players
     * @return allowed and trusted player map
     */
    public HashMap<String, Plot.AccessLevel> getMembers() {
        return allowed;
    }

    private void updateFinished(String finishTime, boolean isFinished) {
        updateField("finisheddate", finishTime);
        updateField("finished", isFinished);
    }

    public void updateField(String field, Object value) {
        PlotMeCoreManager.getInstance().getSQLManager().updatePlot(getId(), getWorld(), field, value);
    }

    public final IWorld getWorld() {
        return world;
    }

    public final void setWorld(IWorld world) {
        this.world = world;
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

    /**
     * Retrieves the price of the plot.
     * If {@link #isForSale()} is false then this should return 0
     * @return the price of the plot
     */
    public final double getPrice() {
        return price;
    }

    public final void setPrice(double price) {
        this.price = price;
    }

    /**
     * Checks if this plot is able to be sold
     * @return true if it is for sale, false otherwise
     */
    public final boolean isForSale() {
        return forSale;
    }

    /**
     * Sets if this plot can be sold or not
     * @param forSale true if it can be sold, false if it cannot be sold
     */
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
        return PlotMeCoreManager.getInstance().getSQLManager().savePlotProperty(getId(), this.world, pluginname, property, value);
    }

    public Map<String, Map<String, String>> getAllPlotProperties() {
        return metadata;
    }

    /**
     * Retrieves the unique internal id for this plot.
     * Commonly used for database lookups and debugging.
     * Normal users should not be concerned about this number nor should they need to see it.
     * @return unique internal id
     */
    public long getInternalID() {
        return internalID;
    }

    /**
     * Sets the unique internal id for this plot.
     * @param internalID unique long value
     */
    public void setInternalID(long internalID) {
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

    public int getTopX() {
        return topX;
    }

    public int getTopZ() {
        return topZ;
    }

    public int getBottomX() {
        return bottomX;
    }

    public int getBottomZ() {
        return bottomZ;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void addDenied(HashSet<String> denied) {
        this.denied.addAll(denied);
    }

    public void addMembers(HashMap<String, AccessLevel> allowed) {
        this.allowed.putAll(allowed);
    }

    /**
     * Gets a set of players who have liked this plot
     * @return
     */
    public HashSet<String> getLikers() {
        return likers;
    }

    public void setLikers(HashSet<String> likers) {
        this.likers = likers;
    }

    public enum AccessLevel {
        ALLOWED(0),
        TRUSTED(1);

        private final int level;

        AccessLevel(int accessLevel) {
            level = accessLevel;
        }

        public static AccessLevel getAccessLevel(int level) {
            switch (level) {
                case 0:
                    return ALLOWED;
                case 1:
                    return TRUSTED;
                default:
                    return ALLOWED;
            }
        }

        public int getLevel() {
            return level;
        }
    }
}