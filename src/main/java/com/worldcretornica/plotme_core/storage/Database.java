package com.worldcretornica.plotme_core.storage;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotLoadEvent;
import com.worldcretornica.plotme_core.api.event.PlotWorldLoadEvent;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

public abstract class Database {

    public final Vector<Plot> plots = new Vector<>();
    final PlotMe_Core plugin;
    public long nextPlotId = 1;
    Connection connection;

    public Database(PlotMe_Core plugin) {
        this.plugin = plugin;
    }

    /**
     * Very demanding task depending on how many plots in each world.
     *
     * @return all plots
     */
    public List<Plot> getPlots() {
        return Collections.unmodifiableList(plots);
    }

    /**
     * Closes the connecection to the database.
     * This will not close the connection if the connection is null.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Could not close database connection: ");
                plugin.getLogger().severe(e.getMessage());
            }
        }
    }

    public abstract Connection startConnection();

    /**
     * The database connection
     * @return the connection to the database
     */
    Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                return startConnection();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Oh no! A connection error occurred:");
            plugin.getLogger().severe(e.getMessage());
        }
        return connection;
    }

    protected abstract void createTables();

    /**
     * Get the number of plots in the world
     * @param world plotworld to check
     * @return number of plots in the world
     */
    public int getWorldPlotCount(IWorld world) {
        int size = 0;
        for (Plot plot : plots) {
            if (plot.getWorld().equals(world)) {
                size++;
            }
        }
        return size;
    }

    /**
     * Get the number of plots in the database
     * @return number of plots in the world
     */
    public int getTotalPlotCount() {
        return plots.size();
    }

    public int getPlotCount(IWorld world, UUID uuid) {
        int count = 0;
        for (Plot plot : plots) {
            if (plot.getOwnerId().equals(uuid) && plot.getWorld().equals(world)) {
                count++;
            }
        }
        return count;
    }

    public void addPlot(Plot plot) {
        addPlotToCache(plot);
        savePlot(plot);
    }

    private void addPlotToCache(Plot plot) {
        plots.add(plot);
    }

    public boolean deletePlot(Plot plot) {
        deletePlotFromStorage(plot);
        return deletePlotFromCache(plot);

    }

    private boolean deletePlotFromCache(Plot plot) {
        if (plots.remove(plot)) {
            plugin.getLogger().info(plots.toString());
            return true;
        } else {
            plugin.getLogger().info(plots.toString());
            return false;
        }
    }

    private void deletePlotFromStorage(Plot plot) {
        deleteAllFrom(plot.getInternalID(), "plotmecore_allowed");
        deleteAllFrom(plot.getInternalID(), "plotmecore_denied");
        deleteAllFrom(plot.getInternalID(), "plotmecore_metadata");
        deleteAllFrom(plot.getInternalID(), "plotmecore_likes");
        deleteAllFrom(plot.getInternalID(), "plotmecore_plots");
    }

    public void deleteAllFrom(long internalID, String table) {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute("DELETE FROM " + table + " WHERE plot_id = " + internalID);
            getConnection().commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting plot " + internalID + "'s data from table: " + table);
            plugin.getLogger().severe("Details: " + e.getMessage());
        }
    }

    /**
     * Placeholder.
     *
     * @param uuid
     * @return plots. unmodifiable.
     */

    public List<Plot> getPlayerPlots(final UUID uuid) {
        ArrayList<Plot> filter = new ArrayList<>();
        filter.addAll(Collections2.filter(plots, new Predicate<Plot>() {
                @Override public boolean apply(Plot plot) {
                    return plot.getOwnerId().equals(uuid);
                }
            }));
        return ImmutableList.copyOf(filter);
    }

    /**
     * Placeholder.
     *
     * @param world
     * @param uuid
     * @return owned plots. unmodifiable.
     */
    public List<Plot> getOwnedPlots(final IWorld world, final UUID uuid) {
        Collection<Plot> filter = Collections2.filter(plots, new Predicate<Plot>() {
            @Override public boolean apply(Plot plot) {
                return plot.getOwnerId().equals(uuid) && plot.getWorld().equals(world);
            }
        });
        return ImmutableList.copyOf(filter);
    }

    public void loadPlotsAsynchronously(final IWorld world) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Loading plots for world " + world.getName());
                Vector<Plot> plots2 = getPlots(world);
                plots.addAll(plots2);
                PlotWorldLoadEvent eventWorld = new PlotWorldLoadEvent(world, plots2.size());
                plugin.getEventBus().post(eventWorld);
                for (Plot plot : plots2) {
                    PlotLoadEvent event = new PlotLoadEvent(plot);
                    plugin.getEventBus().post(event);

                }

            }

            private Vector<Plot> getPlots(IWorld world) {
                Vector<Plot> ret = new Vector<>();
                Connection connection = getConnection();
                try (PreparedStatement statementPlot = connection.prepareStatement("SELECT * FROM plotmecore_plots WHERE LOWER(world) = ?");
                        PreparedStatement statementAllowed = connection.prepareStatement("SELECT * FROM plotmecore_allowed WHERE plot_id = ?");
                        PreparedStatement statementDenied = connection.prepareStatement("SELECT * FROM plotmecore_denied WHERE plot_id = ?");
                        PreparedStatement statementLikes = connection.prepareStatement("SELECT * FROM plotmecore_likes WHERE plot_id = ?");
                        PreparedStatement statementMetadata = connection.prepareStatement("SELECT * FROM plotmecore_metadata WHERE plot_id = ?")
                ) {
                    statementPlot.setString(1, world.getName().toLowerCase());
                    try (ResultSet setPlots = statementPlot.executeQuery()) {
                        while (setPlots.next()) {
                            long internalID = setPlots.getLong("plot_id");
                            PlotId id = new PlotId(setPlots.getInt("plotX"), setPlots.getInt("plotZ"));
                            String owner = setPlots.getString("owner");
                            UUID ownerId = UUID.fromString(setPlots.getString("ownerID"));
                            String biome = setPlots.getString("biome");
                            Date expiredDate = setPlots.getDate("expiredDate");
                            boolean finished = setPlots.getBoolean("finished");
                            String finishedDate = setPlots.getString("finishedDate");
                            String createdDate = setPlots.getString("createdDate");
                            double price = setPlots.getDouble("price");
                            boolean forSale = setPlots.getBoolean("forSale");
                            boolean protect = setPlots.getBoolean("protected");
                            String plotName = setPlots.getString("plotName");
                            int plotLikes = setPlots.getInt("plotLikes");
                            com.worldcretornica.plotme_core.api.Vector
                                    topLoc = new com.worldcretornica.plotme_core.api.Vector(setPlots.getInt("topX"), 255, setPlots.getInt("topZ"));
                            com.worldcretornica.plotme_core.api.Vector bottomLoc =
                                    new com.worldcretornica.plotme_core.api.Vector(setPlots.getInt("bottomX"), 0, setPlots.getInt("bottomZ"));
                            HashMap<String, Map<String, String>> metadata = new HashMap<>();
                            HashMap<String, Plot.AccessLevel> allowed = new HashMap<>();
                            HashSet<String> denied = new HashSet<>();
                            HashSet<UUID> likers = new HashSet<>();
                            statementAllowed.setLong(1, internalID);
                            try (ResultSet setAllowed = statementAllowed.executeQuery()) {
                                while (setAllowed.next()) {
                                    allowed.put(setAllowed.getString("player"), Plot.AccessLevel.getAccessLevel(setAllowed.getInt("access")));
                                }
                            }
                            statementDenied.setLong(1, internalID);
                            try (ResultSet setDenied = statementAllowed.executeQuery()) {
                                while (setDenied.next()) {
                                    denied.add(setDenied.getString("player"));
                                }
                            }
                            statementLikes.setLong(1, internalID);
                            try (ResultSet setLikes = statementLikes.executeQuery()) {
                                while (setLikes.next()) {
                                    likers.add(UUID.fromString(setLikes.getString("player")));
                                }
                            }

                            statementMetadata.setLong(1, internalID);
                            try (ResultSet setMetadata = statementMetadata.executeQuery()) {
                                while (setMetadata.next()) {
                                    String pluginname = setMetadata.getString("pluginname");
                                    String propertyname = setMetadata.getString("propertyname");
                                    String propertyvalue = setMetadata.getString("propertyvalue");
                                    if (!metadata.containsKey(pluginname)) {
                                        metadata.put(pluginname, new HashMap<String, String>());
                                    }
                                    metadata.get(pluginname).put(propertyname, propertyvalue);
                                }
                            }

                            Plot plot =
                                    new Plot(internalID, owner, ownerId, world, biome, expiredDate, allowed, denied,
                                            likers, id, price, forSale, finished, finishedDate, protect, metadata, plotLikes, plotName, topLoc,
                                            bottomLoc, createdDate);
                            ret.add(plot);
                        }
                    }
                } catch (SQLException ex) {
                    plugin.getLogger().severe("Load exception :");
                    plugin.getLogger().severe(ex.getMessage());
                }
                return ret;
            }
        });
    }

    public Plot getPlot(PlotId id, IWorld world) {
        for (Plot plot : plots) {
            if (plot.getId().equals(id) && plot.getWorld().equals(world)) {
                return plot;
            }
        }
        return null;
    }


    public List<Plot> getExpiredPlots(final IWorld world) {
        Collection<Plot> filter = Collections2.filter(plots, new Predicate<Plot>() {
            @Override public boolean apply(Plot plot) {
                return plot.getExpiredDate() != null && Calendar.getInstance().getTime().after(plot.getExpiredDate()) && plot.getWorld().equals
                        (world);
            }
        });
        return ImmutableList.copyOf(filter);
    }

    public List<Plot> getFinishedPlots(final IWorld world) {
        Collection<Plot> filter = Collections2.filter(plots, new Predicate<Plot>() {
            @Override public boolean apply(Plot plot) {
                return plot.isFinished() && plot.getWorld().equals(world);
            }
        });

        return ImmutableList.copyOf(filter);
    }

    public void incrementNextPlotId() {
        this.setNextPlotId(this.nextPlotId + 1);
    }

    public void setNextPlotId(long id) {
        this.nextPlotId = id;

        try (Statement statement = getConnection().createStatement()) {
            statement.execute("DELETE FROM plotmecore_nextid;");
            statement.execute("INSERT INTO plotmecore_nextid VALUES (" + id + ");");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error setting next internal Plot id. Details below: ");
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public void savePlot(Plot plot) {
        if (plot.getInternalID() == 0) {
            plot.setInternalID(nextPlotId);
            incrementNextPlotId();
        }
        writePlotToStorage(plot);
    }

    private void writePlotToStorage(Plot plot) {
        //first delete the plot (if exists) from the database
        deletePlotFromStorage(plot);
        try (PreparedStatement ps = getConnection().prepareStatement(
                "INSERT INTO plotmecore_plots(plot_id,plotX, plotZ, world, ownerID, owner, biome, finished, finishedDate, forSale, price, protected, "
                        + "expiredDate, topX, topZ, bottomX, bottomZ, plotLikes, createdDate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            ps.setLong(1, plot.getInternalID());
            ps.setInt(2, plot.getId().getX());
            ps.setInt(3, plot.getId().getZ());
            ps.setString(4, plot.getWorld().getName().toLowerCase());
            ps.setString(5, plot.getOwnerId().toString());
            ps.setString(6, plot.getOwner());
            ps.setString(7, plot.getBiome());
            ps.setBoolean(8, plot.isFinished());
            ps.setString(9, plot.getFinishedDate());
            ps.setBoolean(10, plot.isForSale());
            ps.setDouble(11, plot.getPrice());
            ps.setBoolean(12, plot.isProtected());
            ps.setDate(13, plot.getExpiredDate());
            ps.setInt(14, plot.getTopX());
            ps.setInt(15, plot.getTopZ());
            ps.setInt(16, plot.getBottomX());
            ps.setInt(17, plot.getBottomZ());
            ps.setInt(18, plot.getLikes());
            ps.setString(19, plot.getCreatedDate());
            ps.executeUpdate();
            getConnection().commit();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe(ex.getMessage());
        }
        for (String denied : plot.getDenied()) {
            try (PreparedStatement ps = getConnection()
                    .prepareStatement("INSERT INTO plotmecore_denied (plot_id, player) VALUES(?,?)")) {
                ps.setLong(1, plot.getInternalID());
                ps.setString(2, denied);
                ps.execute();
                getConnection().commit();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error adding allowed data for plot with internal id " + plot.getInternalID());
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, Plot.AccessLevel> member : plot.getMembers().entrySet()) {
            try (PreparedStatement ps = getConnection()
                    .prepareStatement("INSERT INTO plotmecore_allowed (plot_id, player, access) VALUES(?,?, ?)")) {
                ps.setLong(1, plot.getInternalID());
                ps.setString(2, member.getKey());
                ps.setInt(3, member.getValue().getLevel());
                ps.execute();
                getConnection().commit();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error adding allowed data for plot with internal id " + plot.getInternalID());
                e.printStackTrace();
            }
        }

        for (UUID player : plot.getLikers()) {
            try (PreparedStatement ps = getConnection()
                    .prepareStatement("INSERT INTO plotmecore_likes (plot_id, player) VALUES(?, ?)")) {
                ps.setLong(1, plot.getInternalID());
                ps.setString(2, player.toString());
                ps.execute();
                getConnection().commit();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error adding allowed data for plot with internal id " + plot.getInternalID());
                e.printStackTrace();
            }
        }
        for (Map.Entry<String, Map<String, String>> metadata : plot.getAllPlotProperties().entrySet()) {
            for (Map.Entry<String, String> stringStringEntry : metadata.getValue().entrySet()) {
                try (PreparedStatement ps = getConnection().prepareStatement("INSERT INTO plotmecore_metadata(plot_id, pluginName, propertyName, "
                        + "propertyValue) VALUES (?,?,?,?)")) {
                    ps.setLong(1, plot.getInternalID());
                    ps.setString(2, metadata.getKey());
                    ps.setString(3, stringStringEntry.getKey());
                    ps.setString(4, stringStringEntry.getValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
