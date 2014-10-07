package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.sql.Date;
import java.text.DateFormat;
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
    private boolean auctionned;
    private String currentbidder;
    private double currentbid;
    private UUID currentbidderId;
    private String auctionneddate;

    public Plot(PlotMe_Core instance) {
        this.plugin = instance;
        this.setOwner("");
        this.setOwnerId(null);
        this.setWorld("");
        this.setId("");
        this.allowed = new PlayerList();
        this.denied = new PlayerList();
        this.setBiome(plugin.getServerObjectBuilder().getBiome("PLAINS"));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        java.util.Date utlDate = cal.getTime();
        this.setExpiredDate(new java.sql.Date(utlDate.getTime()));

        this.comments = new ArrayList<>();
        this.setCustomPrice(0);
        this.setForSale(false);
        this.setFinishedDate("");
        this.setProtect(false);
        this.setAuctionned(false);
        this.setCurrentBidder("");
        this.setCurrentBidderId(null);
        this.setCurrentBid(0);
    }

    public Plot(PlotMe_Core instance, String owner, UUID uuid, IWorld w, String plotid, int days) {
        this.plugin = instance;
        this.setOwner(owner);
        this.setOwnerId(uuid);
        this.setWorld(w.getName());
        this.allowed = new PlayerList();
        this.denied = new PlayerList();
        this.setBiome(plugin.getServerObjectBuilder().getBiome("PLAINS"));
        this.setId(plotid);

        if (days == 0) {
            this.setExpiredDate(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            java.util.Date utlDate = cal.getTime();
            this.setExpiredDate(new java.sql.Date(utlDate.getTime()));
        }

        this.comments = new ArrayList<>();
        this.setCustomPrice(0);
        this.setForSale(false);
        this.setFinishedDate("");
        this.setProtect(false);
        this.setAuctionned(false);
        this.setCurrentBidder("");
        this.setCurrentBidderId(null);
        this.setCurrentBid(0);
    }

    public Plot(PlotMe_Core instance, UUID uuid, IWorld world, String plotid, int days) {
        this.plugin = instance;
        this.setOwnerId(uuid);
        
        IPlayer p = plugin.getServerObjectBuilder().getPlayer(uuid);
        if (p != null) {
            this.setOwner(p.getName());
        } else {
            this.setOwner("");
        }
        this.setWorld(world.getName());
        this.allowed = new PlayerList();
        this.denied = new PlayerList();
        this.setBiome(plugin.getServerObjectBuilder().getBiome("PLAINS"));
        this.setId(plotid);

        if (days == 0) {
            this.setExpiredDate(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            java.util.Date utlDate = cal.getTime();
            this.setExpiredDate(new java.sql.Date(utlDate.getTime()));
        }

        this.comments = new ArrayList<>();
        this.setCustomPrice(0);
        this.setForSale(false);
        this.setFinishedDate("");
        this.setProtect(false);
        this.setAuctionned(false);
        this.setCurrentBidder("");
        this.setCurrentBidderId(null);
        this.setCurrentBid(0);
    }

    public Plot(PlotMe_Core instance, String owner, UUID ownerId, String world, String bio, Date exp, boolean fini, 
            PlayerList al, List<String[]> comm, String tid, double custprice, boolean sale, String finishdt, 
            boolean prot, String bidder, UUID bidderId, Double bid, boolean isauctionned, PlayerList den, String auctdate) {
        this.plugin = instance;
        this.setOwner(owner);
        this.setOwnerId(ownerId);
        this.setWorld(world);
        this.setBiome(plugin.getServerObjectBuilder().getBiome(bio));
        this.setExpiredDate(exp);
        this.setFinished(fini);
        this.allowed = al;
        this.comments = comm;
        this.setId(tid);
        this.setCustomPrice(custprice);
        this.setForSale(sale);
        this.setFinishedDate(finishdt);
        this.setProtect(prot);
        this.setAuctionned(isauctionned);
        this.setCurrentBidder(bidder);
        this.setCurrentBidderId(bidderId);
        this.setCurrentBid(bid);
        this.denied = den;
        this.setAuctionnedDate(auctdate);
    }

    public void setExpire(Date date) {
        if (!this.getExpiredDate().equals(date)) {
            this.setExpiredDate(date);
            updateField("expireddate", this.getExpiredDate());
        }
    }

    public void resetExpire(int days) {
        if (days == 0) {
            if (this.getExpiredDate() != null) {
                this.setExpiredDate(null);
                updateField("expireddate", this.getExpiredDate());
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            java.util.Date utlDate = cal.getTime();
            java.sql.Date temp = new java.sql.Date(utlDate.getTime());
            if (this.getExpiredDate() == null || !temp.toString().equalsIgnoreCase(this.getExpiredDate().toString())) {
                this.setExpiredDate(temp);
                updateField("expireddate", this.getExpiredDate());
            }
        }
    }

    public String getExpire() {
        return DateFormat.getDateInstance().format(this.getExpiredDate());
    }

    public void setFinished() {
        this.setFinishedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
        this.finished = true;

        updateFinished(this.getFinishedDate(), true);
    }

    public void setUnfinished() {
        this.setFinishedDate("");
        this.setFinished(false);

        updateFinished(this.getFinishedDate(), this.isFinished());
    }

    public IBiome getBiome() {
        return this.biome;
    }

    public final String getOwner() {
        return this.owner;
    }
    
    public final UUID getOwnerId() {
        return this.ownerId;
    }

    public final void setOwner(String owner) {
        this.owner = owner;
    }
    
    public final void setOwnerId(UUID uuid) {
        this.ownerId = uuid;
    }

    public String getAllowed() {
        return allowed.getPlayerList();
    }

    public String getDenied() {
        return denied.getPlayerList();
    }

    public int getCommentsCount() {
        return this.comments.size();
    }

    public String[] getComment(int i) {
        return this.comments.get(i);
    }

    public List<String[]> getComments() {
        return this.comments;
    }

    public void addComment(String[] comment) {
        this.comments.add(comment);
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
            
            if(plugin.getServerObjectBuilder().getPlotWorldEdit() != null) {
                IPlayer p = plugin.getServerObjectBuilder().getPlayer(uuid);
                
                if(p != null) {
                    if(plugin.getPlotMeCoreManager().isPlotWorld(p.getWorld())) {
                        if(!plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId()))
                            plugin.getServerObjectBuilder().getPlotWorldEdit().setMask(p);
                        else
                            plugin.getServerObjectBuilder().getPlotWorldEdit().removeMask(p);
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
            
            if(plugin.getServerObjectBuilder().getPlotWorldEdit() != null) {
                IPlayer p = plugin.getServerObjectBuilder().getPlayer(uuid);
                
                if(p != null) {
                    if(plugin.getPlotMeCoreManager().isPlotWorld(p.getWorld())) {
                        if(!plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId()))
                            plugin.getServerObjectBuilder().getPlotWorldEdit().setMask(p);
                        else
                            plugin.getServerObjectBuilder().getPlotWorldEdit().removeMask(p);
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
        IPlayer p = plugin.getServerObjectBuilder().getPlayerExact(name);
        if(p == null) {
            return false;
        } else {
            return isAllowedInternal(p.getName(), p.getUniqueId(), true, true);
        }
    }
    
    public boolean isAllowedConsulting(String name) {
        IPlayer p = plugin.getServerObjectBuilder().getPlayerExact(name);
        if(p != null) {
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

    @Deprecated
    public boolean isAllowed(String name, boolean IncludeStar, boolean IncludeGroup) {
        IPlayer p = plugin.getServerObjectBuilder().getPlayerExact(name);
        if(p == null) {
            return false;
        } else {
            return isAllowedInternal(p.getName(), p.getUniqueId(), IncludeStar, IncludeGroup);
        }
    }
    
    private boolean isAllowedInternal(String name, UUID uuid, boolean IncludeStar, boolean IncludeGroup) {
                
        if(IncludeStar && owner.equals("*")) {
            return true;
        }
        
        IPlayer p = null;

        if (uuid != null) {
            p = plugin.getServerObjectBuilder().getPlayer(uuid);
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
            if(IncludeStar && str.equals("*")) {
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

    @Deprecated
    public boolean isDenied(String name) {
        IPlayer p = plugin.getServerObjectBuilder().getPlayerExact(name);
        if(p == null) {
            return false;
        } else {
            return isDeniedInternal(name, null);
        }
    }
    
    public boolean isDeniedConsulting(String name) {
        IPlayer p = plugin.getServerObjectBuilder().getPlayerExact(name);
        if(p != null) {
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
            p = plugin.getServerObjectBuilder().getPlayer(uuid);
        }
        
        HashMap<String, UUID> list = denied.getAllPlayers();
        for (String str : list.keySet()) {
            if(str.equals("*")) {
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
        if (this.getExpiredDate().compareTo(plot.getExpiredDate()) == 0) {
            return this.owner.compareTo(plot.owner);
        } else {
            return this.getExpiredDate().compareTo(plot.getExpiredDate());
        }
    }

    private void updateFinished(String finishtime, boolean isfinished) {
        updateField("finisheddate", finishtime);
        updateField("finished", isfinished);
    }

    public void updateField(String field, Object value) {
        this.plugin.getSqlManager().updatePlot(this.plugin.getPlotMeCoreManager().getIdX(this.getId()), this.plugin.getPlotMeCoreManager().getIdZ(this.getId()), this.getWorld(), field, value);
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

    public final boolean isAuctionned() {
        return auctionned;
    }

    public final void setAuctionned(boolean auctionned) {
        this.auctionned = auctionned;
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
        this.currentbidderId = uuid;
    }

    public final double getCurrentBid() {
        return currentbid;
    }

    public final void setCurrentBid(double currentbid) {
        this.currentbid = currentbid;
    }

    public final String getAuctionnedDate() {
        return auctionneddate;
    }

    public final void setAuctionnedDate(String auctionneddate) {
        this.auctionneddate = auctionneddate;
    }
}
