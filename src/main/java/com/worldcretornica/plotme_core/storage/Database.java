package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.api.event.PlotLoadEvent;
import com.worldcretornica.plotme_core.api.event.PlotWorldLoadEvent;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Database {

    final PlotMe_Core plugin;
    private final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
    public long nextPlotId;
    public ConcurrentHashMap<IWorld, ArrayList<Plot>> worldToPlotMap = new ConcurrentHashMap<>();
    public ArrayList<Plot> plots = new ArrayList<>();
    Connection connection;

    public Database(PlotMe_Core plugin) {
        this.plugin = plugin;
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
        return worldToPlotMap.get(world).size();
    }

    /**
     * Get the number of plots in the database
     * @return number of plots in the world
     */
    public int getTotalPlotCount() {
        return plots.size();
    }

    public int getPlotCount(IWorld worldIC, UUID uuid) {
        ArrayList<Plot> plots = worldToPlotMap.get(worldIC);
        int count = 0;
        for (Plot plot : plots) {
            if (plot.getOwnerId().equals(uuid)) {
                count++;
            }
        }
        return count;
    }

    public void addPlot(Plot plot) {
        plots.add(plot);
        worldToPlotMap.get(plot.getWorld()).add(plot);
        addPlotInternal(plot);
    }

    private void addPlotInternal(Plot plot) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "INSERT INTO plotmecore_plots(id,plotX, plotZ, world, ownerID, owner, biome, finished, finishedDate, forSale, price, protected, "
                        + "expiredDate, topX, topZ, bottomX, bottomZ, plotLikes, createdDate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            if (plot.getInternalID() == 0) {
                ps.setLong(1, nextPlotId);
                plot.setInternalID(nextPlotId);
                incrementNextInternalPlotId();
            }
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
    }

    private void savePlotProperty(int internalID, String pluginname, String propertyname, String s) {

    }

    public void addPlotDenied(String player, long plotInternalID) {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT INTO plotmecore_denied (plot_id, player) VALUES (?,?)")) {
            ps.setLong(1, plotInternalID);
            ps.setString(2, player);
            ps.execute();
            getConnection().commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error adding denied data for plot with internal id " + plotInternalID);
            e.printStackTrace();
        }


    }

    public void addPlotMember(String player, long plotInternalID, Plot.AccessLevel access) {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT INTO plotmecore_allowed (plot_id, player, access) VALUES(?,?,?)")) {
            ps.setLong(1, plotInternalID);
            ps.setString(2, player);
            ps.setInt(3, access.getLevel());
            ps.execute();
            getConnection().commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error adding allowed data for plot with internal id " + plotInternalID);
            e.printStackTrace();
        }
    }

    public void deletePlot(Plot plot) {
        worldToPlotMap.get(plot.getWorld()).remove(plot);
        plots.remove(plot);
        deletePlotInternal(plot.getInternalID(), "plotmecore_allowed");
        deletePlotInternal(plot.getInternalID(), "plotmecore_denied");
        deletePlotInternal(plot.getInternalID(), "plotmecore_metadata");
        deletePlotInternal(plot.getInternalID(), "plotmecore_likes");
        deletePlotInternal(plot.getInternalID(), "plotmecore_plots");
    }

    private void deletePlotInternal(long internalID, String table) {
        try (PreparedStatement ps = getConnection().prepareStatement("DELETE FROM " + table + " WHERE plot_id = ?")) {
            ps.setLong(1, internalID);
            ps.executeUpdate();
            getConnection().commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting data from table " + table + " with the internal id " + internalID);
            e.printStackTrace();
        }
    }

    /**
     * Placeholder.
     *
     * @param uuid
     * @return plots. unmodifiable.
     */

    public Set<Plot> getPlayerPlots(UUID uuid) {
        HashSet<Plot> playerPlots = new HashSet<>();
        for (Plot plot : plots) {
            if (plot.getOwnerId().equals(uuid)) {
                playerPlots.add(plot);
            }
        }
        return Collections.unmodifiableSet(playerPlots);
    }

    public void updatePlotsNewUUID(UUID uuid, String name) {

    }

    /**
     * Placeholder.
     *
     * @param world
     * @param uuid
     * @return owned plots. unmodifiable.
     */
    public Set<Plot> getOwnedPlots(IWorld world, UUID uuid) {
        HashSet<Plot> plots = new HashSet<>();
        for (Plot plot : worldToPlotMap.get(world)) {
            if (plot.getOwnerId().equals(uuid)) {
                plots.add(plot);
            }
        }
        return Collections.unmodifiableSet(plots);
    }

    public void loadPlotsAsynchronously(final IWorld world) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Loading plots for world " + world);
                ArrayList<Plot> plots = getPlots(world);
                worldToPlotMap.put(world, plots);
                PlotWorldLoadEvent eventWorld = new PlotWorldLoadEvent(world, plots.size());
                plugin.getEventBus().post(eventWorld);
                for (Plot plot : plots) {
                    PlotLoadEvent event = new PlotLoadEvent(world, plot);
                    plugin.getEventBus().post(event);

                }

            }

            private ArrayList<Plot> getPlots(IWorld world) {
                ArrayList<Plot> ret = new ArrayList<>();
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
                            long internalID = setPlots.getLong("id");
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
                            Vector topLoc = new Vector(setPlots.getInt("topX"), 255, setPlots.getInt("topZ"));
                            Vector bottomLoc = new Vector(setPlots.getInt("bottomX"), 0, setPlots.getInt("bottomZ"));
                            HashMap<String, Map<String, String>> metadata = new HashMap<>();
                            HashMap<String, Plot.AccessLevel> allowed = new HashMap<>();
                            HashSet<String> denied = new HashSet<>();
                            HashSet<String> likers = new HashSet<>();
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
                                    likers.add(setLikes.getString("player"));
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
        for (Plot plot : worldToPlotMap.get(world)) {
            if (plot.getId().equals(id)) {
                return plot;
            }
        }
        return null;
    }

    public void updatePlot(PlotId id, IWorld name, String biome, Object name1) {
        //TODO Get rid of this ugly method
    }

    public void updatePlot(Plot plot) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE plotmecore_plots SET plotX = ?, plotZ = ?, world = ?, ownerID = ?, "
                + "owner = ?, biome = ?, finished = ?, finishedDate = ?, forSale = ?, price = ?, protected = ?, expiredDate = ?, topX = ?, topZ = "
                + "?, bottomX = ?, bottomZ = ?, plotName = ?, plotLikes = ?, homeX = ?, homeY = ?, homeZ = ?, homeName = ?, createdDate = ? WHERE "
                + "id = ?")) {
            statement.setInt(1, plot.getId().getX());
            statement.setInt(2, plot.getId().getZ());
            statement.setString(3, plot.getWorld().getName().toLowerCase());
            statement.setString(4, plot.getOwnerId().toString());
            statement.setString(5, plot.getOwner());
            statement.setString(6, plot.getBiome());
            statement.setBoolean(7, plot.isFinished());
            statement.setBoolean(7, plot.isFinished());
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deletePlotMember(long internalID, String player) {
        internalDeletePlotPlayer("plotmecore_allowed", internalID, player);
    }

    public void deletePlotDenied(long internalID, String name) {
        internalDeletePlotPlayer("plotmecore_denied", internalID, name);
    }

    public void internalDeletePlotPlayer(String table, long internalID, String player) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table + " WHERE plot_id = ? AND player = ?")) {
            statement.setLong(1, internalID);
            statement.setString(2, player);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This deletes all the players added to this plot. Including trusted players.
     * @param internalID
     */
    public void deleteAllAllowed(long internalID) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM plotmecore_allowed WHERE plot_id = ?")) {
            statement.setLong(1, internalID);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteAllDenied(long internalID) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM plotmecore_denied WHERE deniedID = ?")) {
            statement.setLong(1, internalID);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public boolean savePlotProperty(PlotId id, IWorld world, String pluginname, String property, String value) {
        return false;
    }

    public TreeSet<Plot> getExpiredPlots(IWorld world) {
        TreeSet<Plot> expiredPlots = new TreeSet<>(new Comparator<Plot>() {
            @Override public int compare(Plot o1, Plot o2) {
                return o1.getExpiredDate().compareTo(o2.getExpiredDate());
            }
        });
        for (Plot plot : worldToPlotMap.get(world)) {
            if (plot.getExpiredDate() != null) {
                expiredPlots.add(plot);
            }
        }
        return expiredPlots;
    }

    public HashSet<Plot> getFinishedPlots(String name, int page, int i) {
        return null;
    }

    public void incrementNextInternalPlotId() {
        this.setNextInternalPlotId(this.nextPlotId + 1);
    }

    public void setNextInternalPlotId(long id) {
        this.nextPlotId = id;

        try (Statement statement = getConnection().createStatement()) {
            statement.execute("DELETE FROM plotmecore_nextplotid;");
            statement.execute("INSERT INTO plotmecore_nextplotid VALUES (" + id + ");");
        } catch (SQLException e) {
        }
    }
}
