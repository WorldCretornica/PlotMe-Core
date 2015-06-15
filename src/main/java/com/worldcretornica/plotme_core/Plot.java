package com.worldcretornica.plotme_core;

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

    private final HashMap<String, Plot.AccessLevel> allowed = new HashMap<>();
    private final HashSet<String> denied = new HashSet<>();
    private final HashMap<String, Map<String, String>> metadata = new HashMap<>();
    private final Vector plotTopLoc;
    private final Vector plotBottomLoc;
    private final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
    private final String createdDate;
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
    private HashSet<UUID> likers = new HashSet<>();

    public Plot(String owner, UUID uuid, IWorld world, PlotId plotId, Vector plotTopLoc, Vector plotBottomLoc) {
        setOwner(owner);
        setOwnerId(uuid);
        setWorld(world);
        setId(plotId);
        this.plotTopLoc = plotTopLoc;
        this.plotBottomLoc = plotBottomLoc;
        createdDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public Plot(long internalID, String owner, UUID ownerId, IWorld world, String biome, Date expiredDate,
            HashMap<String, AccessLevel> allowed,
            HashSet<String>
                    denied,
            HashSet<UUID> likers, PlotId id, double price, boolean forSale, boolean finished, String finishedDate, boolean protect,
            Map<String, Map<String, String>> metadata, int plotLikes, String plotName, Vector topLoc, Vector bottomLoc, String createdDate) {
        this.internalID = internalID;
        this.owner = owner;
        this.ownerId = ownerId;
        this.world = world;
        this.biome = biome;
        this.expiredDate = expiredDate;
        this.finished = finished;
        this.finishedDate = finishedDate;
        this.allowed.putAll(allowed);
        this.id = id;
        this.price = price;
        this.forSale = forSale;
        this.finishedDate = finishedDate;
        this.protect = protect;
        this.likers.addAll(likers);
        this.plotName = plotName;
        this.likes = plotLikes;
        this.denied.addAll(denied);
        this.metadata.putAll(metadata);
        this.plotTopLoc = topLoc;
        this.plotBottomLoc = bottomLoc;
        this.createdDate = createdDate;
    }

    public void resetExpire(int days) {
        if (days == 0) {
            if (getExpiredDate() != null) {
                setExpiredDate(null);
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            java.util.Date utlDate = cal.getTime();
            java.sql.Date temp = new java.sql.Date(utlDate.getTime());
            if (expiredDate == null || temp.after(expiredDate)) {
                expiredDate = temp;
            }
        }
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
            if ("*".equals(name)) {
                this.getMembers().clear();
            }
            getMembers().put(name, level);
        }
    }

    public void addDenied(String name) {
        if (!isDeniedConsulting(name)) {
            getDenied().add(name);
        }
    }

    public void removeAllowed(String name) {
        if (getMembers().containsKey(name)) {
            getMembers().remove(name, AccessLevel.ALLOWED);
        }
    }

    public void removeMember(String name) {
        if (getMembers().containsKey(name)) {
            getMembers().remove(name);
        }
    }

    public void removeDenied(String name) {
        if (getDenied().contains(name)) {
            getDenied().remove(name);
        }
    }

    public void removeAllAllowed() {
        getMembers().clear();
    }

    public void removeAllDenied() {
        getDenied().clear();
    }

    public boolean isAllowedConsulting(String name) {
        if ("*".equals(name)) {
            return isAllowedInternal(name);
        }
        UUID player = manager.getPlayer(name).getUniqueId();
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
            }
            if (accessLevel == AccessLevel.TRUSTED) {
                return manager.getPlayer(name) != null;
            }
        } else {
            return getMembers().containsKey("*");
        }
        return false;
    }

    public boolean isDeniedConsulting(String name) {
        return isDeniedInternal(name);
    }

    public boolean isDenied(UUID uuid) {
        return isDeniedInternal(uuid.toString());
    }

    private boolean isDeniedInternal(String name) {
        return getDenied().contains("*") || getDenied().contains(name);
    }

    /**
     * A map of allowed and trusted players
     * @return allowed and trusted player map
     */
    public HashMap<String, Plot.AccessLevel> getMembers() {
        return allowed;
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
        if (finished) {
            setFinishedDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        } else {
            setFinishedDate(null);
        }
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

    private void setFinishedDate(String finishedDate) {
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
        return true;
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

    public void addLike(int likes, UUID player) {
        this.getLikers().add(player);
        this.likes = likes;
    }

    public String getPlotName() {
        return plotName;
    }

    public void setPlotName(String plotName) {
        this.plotName = plotName;
    }

    /**
     * Do not use for teleporting players. It will suffocate or kill them.
     * @return
     */
    public Vector getMiddle() {
        Vector bottom = plotBottomLoc;
        Vector top = plotTopLoc;

        double x = (top.getX() + bottom.getX() + 1) / 2;
        double z = (top.getZ() + bottom.getZ() + 1) / 2;


        return new Vector(x, 0, z);
    }

    public int getTopX() {
        return plotTopLoc.getBlockX();
    }

    public int getTopZ() {
        return plotTopLoc.getBlockZ();
    }

    public Vector getPlotTopLoc() {
        return plotTopLoc;
    }

    public Vector getPlotBottomLoc() {
        return plotBottomLoc;
    }

    public int getBottomX() {
        return plotBottomLoc.getBlockX();
    }

    public int getBottomZ() {
        return plotBottomLoc.getBlockZ();
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
    public HashSet<UUID> getLikers() {
        return likers;
    }

    public void setLikers(HashSet<UUID> likers) {
        this.likers = likers;
    }

    //todo test equals to make sure it is reliable.
    @Override public boolean equals(Object obj) {
        if (obj instanceof Plot) {
            Plot obj1 = (Plot) obj;
            if (obj1.getInternalID() == this.internalID && obj1.getId().equals(this.id) && obj1.getOwnerId().equals(this.ownerId) && obj1.getWorld()
                    .equals(this.world)) {
                return true;
            }

        }
        return false;
    }

    public boolean canPlayerLike(UUID uniqueId) {
        return !likers.contains(uniqueId);
    }

    public void removeLike(int i, UUID uniqueId) {
        likes -= i;
        likers.remove(uniqueId);
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