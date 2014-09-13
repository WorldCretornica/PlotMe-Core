package com.worldcretornica.plotme_core;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class Plot implements Comparable<Plot> {

    private PlotMe_Core plugin = null;

    private String owner;
    private String world;
    private HashSet<String> allowed;
    private HashSet<String> denied;
    private Biome biome;
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
    private String auctionneddate;

    public Plot(PlotMe_Core instance) {
        this.plugin = instance;
        this.owner = "";
        this.setWorld("");
        this.setId("");
        this.allowed = new HashSet<>();
        this.denied = new HashSet<>();
        this.setBiome(Biome.PLAINS);

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
        this.setCurrentBid(0);
    }

    public Plot(PlotMe_Core instance, String own, World wor, String tid, int days) {
        this.plugin = instance;
        this.owner = own;
        this.setWorld(wor.getName());
        this.allowed = new HashSet<>();
        this.denied = new HashSet<>();
        this.setBiome(Biome.PLAINS);
        this.setId(tid);

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
        this.setCurrentBid(0);
    }

    public Plot(PlotMe_Core instance, String o, String w, String bio, Date exp, boolean fini, HashSet<String> al,
                List<String[]> comm, String tid, double custprice, boolean sale, String finishdt, boolean prot, String bidder,
                Double bid, boolean isauctionned, HashSet<String> den, String auctdate) {
        this.plugin = instance;
        this.owner = o;
        this.setWorld(w);
        this.setBiome(Biome.valueOf(bio));
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

    public Biome getBiome() {
        return this.biome;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAllowed() {
        String list = "";

        for (String s : this.allowed) {
            list = list + s + ", ";
        }
        if (list.length() > 1) {
            list = list.substring(0, list.length() - 2);
        }
        return list;
    }

    public String getDenied() {
        String list = "";

        for (String s : this.denied) {
            list = list + s + ", ";
        }
        if (list.length() > 1) {
            list = list.substring(0, list.length() - 2);
        }
        return list;
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
        if (!isAllowed(name)) {
            this.allowed.add(name);
            this.plugin.getSqlManager().addPlotAllowed(name, this.plugin.getPlotMeCoreManager().getIdX(this.getId()), this.plugin.getPlotMeCoreManager().getIdZ(this.getId()), this.getWorld());
        }
    }

    public void addDenied(String name) {
        if (!isDenied(name)) {
            this.denied.add(name);
            this.plugin.getSqlManager().addPlotDenied(name, this.plugin.getPlotMeCoreManager().getIdX(this.getId()), this.plugin.getPlotMeCoreManager().getIdZ(this.getId()), this.getWorld());
        }
    }

    public void removeAllowed(String name) {
        String found = "";

        for (String n : this.allowed) {
            if (n.equalsIgnoreCase(name)) {
                found = n;
                break;
            }
        }

        if (!found.equals("")) {
            this.allowed.remove(found);
            this.plugin.getSqlManager().deletePlotAllowed(this.plugin.getPlotMeCoreManager().getIdX(this.getId()), this.plugin.getPlotMeCoreManager().getIdZ(this.getId()), found, this.getWorld());
        }
    }

    public void removeDenied(String name) {
        String found = "";

        for (String n : this.denied) {
            if (n.equalsIgnoreCase(name)) {
                found = n;
                break;
            }
        }

        if (!found.equals("")) {
            this.denied.remove(found);
            this.plugin.getSqlManager().deletePlotDenied(this.plugin.getPlotMeCoreManager().getIdX(this.getId()), this.plugin.getPlotMeCoreManager().getIdZ(this.getId()), found, this.getWorld());
        }
    }

    public void removeAllAllowed() {
        for (String n : this.allowed) {
            this.plugin.getSqlManager().deletePlotAllowed(this.plugin.getPlotMeCoreManager().getIdX(this.getId()), this.plugin.getPlotMeCoreManager().getIdZ(this.getId()), n, this.getWorld());
        }
        this.allowed = new HashSet<>();
    }

    public void removeAllDenied() {
        for (String n : this.denied) {
            this.plugin.getSqlManager().deletePlotDenied(this.plugin.getPlotMeCoreManager().getIdX(this.getId()), this.plugin.getPlotMeCoreManager().getIdZ(this.getId()), n, this.getWorld());
        }
        this.denied = new HashSet<>();
    }

    public boolean isAllowed(String name) {
        return isAllowed(name, true, true);
    }

    public boolean isAllowed(String name, boolean IncludeStar, boolean IncludeGroup) {

        if (this.owner.equalsIgnoreCase(name) || (IncludeStar && this.owner.equals("*"))) {
            return true;
        }

        if (IncludeGroup && this.owner.toLowerCase().startsWith("group:") && Bukkit.getServer().getPlayerExact(name) != null) {
            if (Bukkit.getServer().getPlayerExact(name).hasPermission("plotme.group." + this.owner.replace("Group:", ""))) {
                return true;
            }
        }

        for (String str : this.allowed) {
            if (str.equalsIgnoreCase(name) || (IncludeStar && str.equals("*"))) {
                return true;
            }

            if (IncludeGroup && str.toLowerCase().startsWith("group:") && Bukkit.getServer().getPlayerExact(name) != null) {
                if (Bukkit.getServer().getPlayerExact(name).hasPermission("plotme.group." + str.replace("Group:", ""))) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isDenied(String name) {
        if (isAllowed(name, false, false)) {
            return false;
        }

        for (String str : this.denied) {
            if (str.equalsIgnoreCase(name) || str.equals("*")) {
                return true;
            }

            if (str.toLowerCase().startsWith("group:") && Bukkit.getServer().getPlayerExact(name) != null) {
                if (Bukkit.getServer().getPlayerExact(name).hasPermission("plotme.group." + str.replace("Group:", ""))) {
                    return true;
                }
            }
        }

        return false;
    }

    public HashSet<String> allowed() {
        return this.allowed;
    }

    public HashSet<String> denied() {
        return this.denied;
    }

    public int allowedcount() {
        return this.allowed.size();
    }

    public int deniedcount() {
        return this.denied.size();
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

    public final void setBiome(Biome biome) {
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

    public final void setCurrentBidder(String currentbidder) {
        this.currentbidder = currentbidder;
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
