package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class Plot implements Comparable<Plot> {

    //TODO look into removing reference to plugin
    private PlotMe_Core plugin;

    private String owner;
    private UUID ownerId;
    private String world;
    private PlayerList allowed;
    private PlayerList denied;
    private IBiome biome;
    private Date expireddate;
    private boolean finished;
    private List<String[]> comments;
    private String id;
    private double customprice;
    private boolean forsale;
    private String finisheddate;
    private boolean protect;
    private boolean auctioned;
    private String currentbidder;
    private double currentbid;
    private UUID currentbidderId;
    private String auctioneddate;

    public Plot(PlotMe_Core instance) {
        this.plugin = instance;
        setOwner("");
        setOwnerId(null);
        setWorld("");
        setId("");
        allowed = new PlayerList();
        denied = new PlayerList();
        setBiome(plugin.getServerBridge().getBiome("PLAINS"));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        java.util.Date utlDate = cal.getTime();
        setExpiredDate(new java.sql.Date(utlDate.getTime()));

        comments = new ArrayList<>();
        setCustomPrice(0);
        setForSale(false);
        setFinishedDate("");
        setProtect(false);
        setAuctioned(false);
        setCurrentBidder("");
        setCurrentBidderId(null);
        setCurrentBid(0);
    }

    public Plot(PlotMe_Core instance, String owner, UUID uuid, IWorld world, String plotid, int days) {
        this.plugin = instance;
        setOwner(owner);
        setOwnerId(uuid);
        setWorld(world.getName().toLowerCase());
        allowed = new PlayerList();
        denied = new PlayerList();
        setBiome(plugin.getServerBridge().getBiome("PLAINS"));
        setId(plotid);

        if (days == 0) {
            setExpiredDate(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            java.util.Date utlDate = cal.getTime();
            setExpiredDate(new java.sql.Date(utlDate.getTime()));
        }

        comments = new ArrayList<>();
        setCustomPrice(0);
        setForSale(false);
        setFinishedDate("");
        setProtect(false);
        setAuctioned(false);
        setCurrentBidder("");
        setCurrentBidderId(null);
        setCurrentBid(0);
    }

    public Plot(PlotMe_Core instance, String owner, UUID ownerId, String world, String biome, Date expiredDate, boolean finished,
                PlayerList al, List<String[]> comm, String id, double customPrice, boolean sale, String finishedDate,
                boolean protect, String bidder, UUID bidderId, double bid, boolean isAuctioned, PlayerList denied, String auctionedDate) {
        this.plugin = instance;
        setOwner(owner);
        setOwnerId(ownerId);
        setWorld(world);
        setBiome(plugin.getServerBridge().getBiome(biome));
        setExpiredDate(expiredDate);
        setFinished(finished);
        allowed = al;
        comments = comm;
        setId(id);
        setCustomPrice(customPrice);
        setForSale(sale);
        setFinishedDate(finishedDate);
        setProtect(protect);
        setAuctioned(isAuctioned);
        setCurrentBidder(bidder);
        setCurrentBidderId(bidderId);
        setCurrentBid(bid);
        this.denied = denied;
        setAuctionedDate(auctionedDate);
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
            if (getExpiredDate() == null || !temp.toString().equalsIgnoreCase(getExpiredDate().toString())) {
                setExpiredDate(temp);
                updateField("expireddate", getExpiredDate());
            }
        }
    }

    public void setFinished() {
        setFinishedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
        finished = true;

        updateFinished(getFinishedDate(), true);
    }

    public void setUnfinished() {
        setFinishedDate("");
        setFinished(false);

        updateFinished(getFinishedDate(), isFinished());
    }

    public IBiome getBiome() {
        return biome;
    }

    public final String getOwner() {
        return owner;
    }

    public final UUID getOwnerId() {
        return ownerId;
    }

    public final void setOwner(String owner) {
        this.owner = owner;
    }

    public final void setOwnerId(UUID uuid) {
        ownerId = uuid;
    }

    public String getAllowed() {
        return allowed.getPlayerList();
    }

    public String getDenied() {
        return denied.getPlayerList();
    }

    public int getCommentsCount() {
        return comments.size();
    }

    public String[] getComment(int i) {
        return comments.get(i);
    }

    public List<String[]> getComments() {
        return comments;
    }

    public void addComment(String[] comment) {
        comments.add(comment);
    }

    public void addAllowed(String name) {
        if (!isAllowedConsulting(name)) {
            allowed.put(name);
            plugin.getSqlManager().addPlotAllowed(name, null, plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), world);
        }
    }

    public void addAllowed(UUID uuid) {
        if (!isAllowed(uuid)) {
            String name = allowed.put(uuid);
            plugin.getSqlManager().addPlotAllowed(name, uuid, plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), world);
        }
    }

    public void addDenied(String name) {
        if (!isDeniedConsulting(name)) {
            denied.put(name);
            plugin.getSqlManager().addPlotDenied(name, null, plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), world);
        }
    }

    public void addDenied(UUID uuid) {
        if (!isDenied(uuid)) {
            String name = denied.put(uuid);
            plugin.getSqlManager().addPlotDenied(name, uuid, plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), world);
        }
    }

    public void removeAllowed(String name) {
        if (allowed.contains(name)) {
            UUID uuid = allowed.remove(name);
            plugin.getSqlManager().deletePlotAllowed(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), name, uuid, world);

            if (plugin.getServerBridge().getPlotWorldEdit() != null) {
                IPlayer p = plugin.getServerBridge().getPlayer(uuid);

                if (p != null) {
                    if (plugin.getPlotMeCoreManager().isPlotWorld(p.getWorld())) {
                        if (!plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId()))
                            plugin.getServerBridge().getPlotWorldEdit().setMask(p);
                        else
                            plugin.getServerBridge().getPlotWorldEdit().removeMask(p);
                    }
                }
            }
        }
    }

    public void removeAllowedGroup(String name) {
        if (allowed.contains(name)) {
            allowed.remove(name);
            plugin.getSqlManager().deletePlotAllowed(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), name, null, world);
        }
    }

    public void removeAllowed(UUID uuid) {
        if (allowed.contains(uuid)) {
            String name = allowed.remove(uuid);
            plugin.getSqlManager().deletePlotAllowed(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), name, uuid, world);

            if (plugin.getServerBridge().getPlotWorldEdit() != null) {
                IPlayer p = plugin.getServerBridge().getPlayer(uuid);

                if (p != null) {
                    if (plugin.getPlotMeCoreManager().isPlotWorld(p.getWorld())) {
                        if (!plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId()))
                            plugin.getServerBridge().getPlotWorldEdit().setMask(p);
                        else
                            plugin.getServerBridge().getPlotWorldEdit().removeMask(p);
                    }
                }
            }
        }
    }

    public void removeDenied(String name) {
        if (denied.contains(name)) {
            UUID uuid = denied.remove(name);
            plugin.getSqlManager().deletePlotDenied(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), name, uuid, world);
        }
    }

    public void removeDeniedGroup(String name) {
        if (denied.contains(name)) {
            denied.remove(name);
            plugin.getSqlManager().deletePlotDenied(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), name, null, world);
        }
    }

    public void removeDenied(UUID uuid) {
        if (denied.contains(uuid)) {
            String name = denied.remove(uuid);
            plugin.getSqlManager().deletePlotDenied(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), name, uuid, world);
        }
    }

    public void removeAllAllowed() {
        HashMap<String, UUID> list = allowed.getAllPlayers();
        for (String n : list.keySet()) {
            UUID uuid = list.get(n);
            plugin.getSqlManager().deletePlotAllowed(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), n, uuid, world);
        }
        allowed.clear();
    }

    public void removeAllDenied() {
        HashMap<String, UUID> list = denied.getAllPlayers();
        for (String n : list.keySet()) {
            UUID uuid = list.get(n);
            plugin.getSqlManager().deletePlotDenied(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), n, uuid, world);
        }
        denied.clear();
    }

    @Deprecated
    public boolean isAllowed(String name) {
        IPlayer p = plugin.getServerBridge().getPlayerExact(name);
        return p != null && isAllowedInternal(p.getName(), p.getUniqueId(), true, true);
    }

    public boolean isAllowedConsulting(String name) {
        IPlayer p = plugin.getServerBridge().getPlayerExact(name);
        if (p != null) {
            return isAllowedInternal(name, p.getUniqueId(), true, true);
        } else {
            return isAllowedInternal(name, null, true, true);
        }
    }

    public boolean isGroupAllowed(String name) {
        return isAllowedInternal(name, null, true, true);
    }

    public boolean isAllowed(String name, UUID uuid) {
        return isAllowedInternal(name, uuid, true, true);
    }

    public boolean isAllowed(UUID uuid) {
        return isAllowedInternal("", uuid, true, true);
    }

    private boolean isAllowedInternal(String name, UUID uuid, boolean IncludeStar, boolean IncludeGroup) {

        if (IncludeStar && owner.equals("*")) {
            return true;
        }

        IPlayer p = null;

        if (uuid != null) {
            p = plugin.getServerBridge().getPlayer(uuid);
        }

        if (uuid != null && ownerId != null && ownerId.equals(uuid) || uuid == null && owner.equalsIgnoreCase(name)) {
            return true;
        }

        if (IncludeGroup && owner.toLowerCase().startsWith("group:") && p != null) {
            if (p.hasPermission("plotme.group." + owner.replace("Group:", ""))) {
                return true;
            }
        }

        HashMap<String, UUID> list = allowed.getAllPlayers();
        for (String str : list.keySet()) {
            if (IncludeStar && str.equals("*")) {
                return true;
            }

            UUID u = list.get(str);
            if (u != null && u.equals(uuid) || uuid == null && str.equalsIgnoreCase(name)) {
                return true;
            }
            if (IncludeGroup && str.toLowerCase().startsWith("group:") && p != null)
                if (p.hasPermission("plotme.group." + str.replace("Group:", "")))
                    return true;
        }
        return false;
    }

    public boolean isDeniedConsulting(String name) {
        IPlayer p = plugin.getServerBridge().getPlayerExact(name);
        if (p != null) {
            return isDeniedInternal(name, p.getUniqueId());
        } else {
            return isDeniedInternal(name, null);
        }
    }

    public boolean isGroupDenied(String name) {
        return isDeniedInternal(name, null);
    }

    public boolean isDenied(UUID uuid) {
        return isDeniedInternal("", uuid);
    }

    private boolean isDeniedInternal(String name, UUID uuid) {

        if (isAllowedInternal(name, uuid, false, false))
            return false;

        IPlayer p = null;
        if (uuid != null) {
            p = plugin.getServerBridge().getPlayer(uuid);
        }

        HashMap<String, UUID> list = denied.getAllPlayers();
        for (String str : list.keySet()) {
            if (str.equals("*")) {
                return true;
            }

            UUID u = list.get(str);
            if (str.equalsIgnoreCase(name) || uuid != null && (u != null && u.equals(uuid) || str.toLowerCase().startsWith("group:") && p != null && p.hasPermission("plotme.group." + str.replace("Group:", "")))) {
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
        return allowed.size();
    }

    public int deniedcount() {
        return denied.size();
    }

    @Override
    public int compareTo(Plot plot) {
        if (getExpiredDate().compareTo(plot.getExpiredDate()) == 0) {
            return owner.compareTo(plot.owner);
        } else {
            return getExpiredDate().compareTo(plot.getExpiredDate());
        }
    }

    private void updateFinished(String finishtime, boolean isfinished) {
        updateField("finisheddate", finishtime);
        updateField("finished", isfinished);
    }

    public void updateField(String field, Object value) {
        plugin.getSqlManager().updatePlot(plugin.getPlotMeCoreManager().getIdX(getId()), plugin.getPlotMeCoreManager().getIdZ(getId()), getWorld(), field, value);
    }

    public final String getWorld() {
        return world;
    }

    public final void setWorld(String world) {
        this.world = world;
    }

    public final void setBiome(IBiome biome) {
        this.biome = biome;
    }

    public final Date getExpiredDate() {
        return expireddate;
    }

    public final void setExpiredDate(Date expireddate) {
        this.expireddate = expireddate;
    }

    public final boolean isFinished() {
        return finished;
    }

    public final void setFinished(boolean finished) {
        this.finished = finished;
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final double getCustomPrice() {
        return customprice;
    }

    public final void setCustomPrice(double customprice) {
        this.customprice = customprice;
    }

    public final boolean isForSale() {
        return forsale;
    }

    public final void setForSale(boolean forsale) {
        this.forsale = forsale;
    }

    public final String getFinishedDate() {
        return finisheddate;
    }

    public final void setFinishedDate(String finisheddate) {
        this.finisheddate = finisheddate;
    }

    public final boolean isProtect() {
        return protect;
    }

    public final void setProtect(boolean protect) {
        this.protect = protect;
    }

    public final boolean isAuctioned() {
        return auctioned;
    }

    public final void setAuctioned(boolean auctioned) {
        this.auctioned = auctioned;
    }

    public final String getCurrentBidder() {
        return currentbidder;
    }

    public final UUID getCurrentBidderId() {
        return currentbidderId;
    }

    public final void setCurrentBidder(String currentbidder) {
        this.currentbidder = currentbidder;
    }

    public final void setCurrentBidderId(UUID uuid) {
        currentbidderId = uuid;
    }

    public final double getCurrentBid() {
        return currentbid;
    }

    public final void setCurrentBid(double currentbid) {
        this.currentbid = currentbid;
    }

    public final String getAuctionedDate() {
        return auctioneddate;
    }

    public final void setAuctionedDate(String auctioneddate) {
        this.auctioneddate = auctioneddate;
    }
}
