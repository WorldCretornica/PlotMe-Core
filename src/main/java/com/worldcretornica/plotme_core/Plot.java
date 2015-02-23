package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Plot implements Cloneable {

    //TODO look into removing reference to plugin

    private final PlotMe_Core plugin;
    private final PlayerList allowed;
    private final PlayerList denied;
    private String owner;
    private UUID ownerId;
    private String world;
    private IBiome biome;
    private String expiredDate;
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
        setBiome(this.plugin.getServerBridge().getBiome("PLAINS"));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        String temp = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        setExpiredDate(temp);

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
        setBiome(this.plugin.getServerBridge().getBiome("PLAINS"));
        setId(plotId);

        if (days == 0) {
            setExpiredDate(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            String temp = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            setExpiredDate(temp);
        }

        setPrice(0.0);
        setForSale(false);
        setFinishedDate(null);
        setProtected(false);
        metadata = new HashMap<>();
    }

    public Plot(PlotMe_Core plugin, String owner, UUID ownerId, String world, String biome, String expiredDate, boolean finished,
            PlayerList allowed, PlotId id, double price, boolean forSale, String finishedDate, boolean protect, PlayerList denied,
            Map<String, Map<String, String>> metadata, int plotLikes, String plotName) {
        this.plugin = plugin;
        setOwner(owner);
        setOwnerId(ownerId);
        setWorld(world);
        setBiome(this.plugin.getServerBridge().getBiome(biome));
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
            String temp = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            if (getExpiredDate() == null || !temp.equalsIgnoreCase(getExpiredDate())) {
                setExpiredDate(temp);
                updateField("expiredDate", getExpiredDate());
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

    public IBiome getBiome() {
        return biome;
    }

    public final void setBiome(IBiome biome) {
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

    public void addAllowed(String name, UUID uuid) {
        if (!isAllowedConsulting(name)) {
            allowed().put(name, uuid);
            plugin.getSqlManager().addPlotAllowed(name, uuid, getInternalID());
        }
    }

    public void addAllowed(String name) {
        if (!isAllowedConsulting(name)) {
            allowed().put(name);
            plugin.getSqlManager().addPlotAllowed(name, null, getInternalID());
        }
    }

    public void addAllowed(UUID uuid) {
        if (!isAllowed(uuid)) {
            String name = allowed().put(uuid);
            plugin.getSqlManager().addPlotAllowed(name, uuid, getInternalID());
        }
    }

    public void addDenied(String name) {
        if (!isDeniedConsulting(name)) {
            denied().put(name);
            plugin.getSqlManager().addPlotDenied(name, null, getInternalID());
        }
    }

    public void addDenied(UUID uuid) {
        if (!isDenied(uuid)) {
            String name = denied().put(uuid);
            plugin.getSqlManager().addPlotDenied(name, uuid, getInternalID());
        }
    }

    public void removeAllowed(String name) {
        if (allowed().contains(name)) {
            UUID uuid = allowed().remove(name);
            plugin.getSqlManager().deletePlotAllowed(getId().getX(), getId().getZ(), name, uuid, getWorld());

            if (plugin.getServerBridge().getPlotWorldEdit() != null) {
                IPlayer player = plugin.getServerBridge().getPlayer(uuid);

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

    public void removeAllowed(UUID uuid) {
        if (allowed().contains(uuid)) {
            String name = allowed().remove(uuid);
            plugin.getSqlManager().deletePlotAllowed(getId().getX(), getId().getZ(), name, uuid, getWorld());

            if (plugin.getServerBridge().getPlotWorldEdit() != null) {
                IPlayer player = plugin.getServerBridge().getPlayer(uuid);

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
            UUID uuid = denied().remove(name);
            plugin.getSqlManager().deletePlotDenied(getId().getX(), getId().getZ(), name, uuid, getWorld());
        }
    }

    public void removeDenied(UUID uuid) {
        if (denied().contains(uuid)) {
            String name = denied().remove(uuid);
            plugin.getSqlManager().deletePlotDenied(getId().getX(), getId().getZ(), name, uuid, getWorld());
        }
    }

    public void removeAllAllowed() {
        HashMap<String, UUID> list = allowed().getAllPlayers();
        for (String n : list.keySet()) {
            UUID uuid = list.get(n);
            plugin.getSqlManager().deletePlotAllowed(getId().getX(), getId().getZ(), n, uuid, getWorld());
        }
        allowed().clear();
    }

    public void removeAllDenied() {
        HashMap<String, UUID> list = denied().getAllPlayers();
        for (String n : list.keySet()) {
            UUID uuid = list.get(n);
            plugin.getSqlManager().deletePlotDenied(getId().getX(), getId().getZ(), n, uuid, getWorld());
        }
        denied().clear();
    }

    public boolean isAllowedConsulting(String name) {
        IPlayer player = plugin.getServerBridge().getPlayerExact(name);
        return player != null && isAllowedInternal(name, player.getUniqueId(), true);
    }

    public boolean isAllowed(String name, UUID uuid) {
        return isAllowedInternal(name, uuid, true);
    }

    public boolean isAllowed(UUID uuid) {
        return isAllowedInternal("", uuid, true);
    }

    private boolean isAllowedInternal(String name, UUID uuid, boolean checkStar) {

        if (getOwnerId().equals(uuid)) {
            return true;
        }

        HashMap<String, UUID> list = allowed().getAllPlayers();
        for (String str : list.keySet()) {
            if (checkStar && "*".equals(str)) {
                return true;
            }

            UUID u = list.get(str);
            if (u != null && u.equals(uuid) || uuid == null && str.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeniedConsulting(String name) {
        IPlayer player = plugin.getServerBridge().getPlayerExact(name);
        return player != null && isDeniedInternal(name, player.getUniqueId());
    }

    public boolean isDenied(UUID uuid) {
        return isDeniedInternal("", uuid);
    }

    public boolean isDeniedInternal(String name, UUID uuid) {

        if (isAllowedInternal(name, uuid, false)) {
            return false;
        }

        HashMap<String, UUID> list = denied().getAllPlayers();
        for (String str : list.keySet()) {
            if ("*".equals(str)) {
                return true;
            }

            UUID u = list.get(str);
            if (str.equalsIgnoreCase(name) || uuid != null && u != null && u.equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public PlayerList allowed() {
        return allowed;
    }

    public PlayerList denied() {
        return denied;
    }

    public int allowedcount() {
        return allowed().size();
    }

    public int deniedcount() {
        return denied().size();
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

    public final String getExpiredDate() {
        return expiredDate;
    }

    public final void setExpiredDate(String expiredDate) {
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

    public final boolean isProtect() {
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

    @Override
    protected Plot clone() throws CloneNotSupportedException {
        return (Plot) super.clone();
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