package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlayerList;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.event.InternalPlotLoadEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotWorldLoadEvent;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Database {

    public static final String SELECT_INTERNAL_ID = "SELECT id FROM plotmecore_plots WHERE plotX = ? AND plotZ = ?";
    private static final String SELECT_PLOT_COUNT = "SELECT Count(*) as plotCount FROM plotmecore_plots WHERE LOWER(world) = ?";
    public final PlotMe_Core plugin;

    public Connection connection;

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
    public Connection getConnection() {
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

    public abstract void createTables();

    public void legacyConverter() {
        if (true) {
            plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
                @Override
                public void run() {
                    if (tableExists("plotmePlots")) {
                        try (Statement statement = legacyConnection().createStatement()) {
                            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM plotmePlots")) {
                                while (resultSet.next()) {
                                    try (PreparedStatement migrateStatement = getConnection().prepareStatement(
                                            "INSERT INTO plotmecore_plots(plotX, plotZ, world, ownerID, owner, biome, finished, finishedDate, "
                                                    + "forSale, price, protected, expiredDate, topX, topZ, bottomX, bottomZ) VALUES (?,"
                                                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                                        int idX = resultSet.getInt("idX");
                                        migrateStatement.setInt(1, idX);
                                        int idZ = resultSet.getInt("idZ");
                                        migrateStatement.setInt(2, idZ);
                                        String world = resultSet.getString("world");
                                        migrateStatement.setString(3, world);
                                        byte[] ownerBytesId = resultSet.getBytes("ownerId");
                                        migrateStatement.setString(4, UUIDFetcher.fromBytes(ownerBytesId).toString());
                                        migrateStatement.setString(5, resultSet.getString("owner"));
                                        migrateStatement.setString(6, resultSet.getString("biome"));
                                        migrateStatement.setBoolean(7, resultSet.getBoolean("finished"));
                                        migrateStatement.setString(8, resultSet.getString("finisheddate"));
                                        migrateStatement.setBoolean(9, resultSet.getBoolean("forsale"));
                                        migrateStatement.setInt(10, resultSet.getInt("customprice"));
                                        migrateStatement.setBoolean(11, resultSet.getBoolean("protected"));
                                        migrateStatement.setDate(12, resultSet.getDate("expireddate"));
                                        migrateStatement.setInt(13, resultSet.getInt("topX"));
                                        migrateStatement.setInt(14, resultSet.getInt("topZ"));
                                        migrateStatement.setInt(15, resultSet.getInt("bottomX"));

                                        try (ResultSet executePlots = migrateStatement.executeQuery()) {
                                            int internalPlotID = executePlots.getInt("id");
                                            if (tableExists("plotmeAllowed")) {
                                                try (ResultSet resultSet1 = statement.executeQuery("SELECT * FROM plotmeAllowed WHERE idX = " + idX
                                                        + " AND idZ = " + idZ + " AND world = " + world)) {
                                                    UUID pId = UUIDFetcher.getUUIDOf(resultSet1.getString("player"));
                                                    String player = null;
                                                    if (resultSet1.getString("player").equalsIgnoreCase("*")) {
                                                        player = resultSet1.getString("player");
                                                    } else if (pId != null) {
                                                        player = pId.toString();
                                                    }
                                                    if (player != null) {
                                                        try (PreparedStatement migrateStatement2 = getConnection().prepareStatement("INSERT INTO "
                                                                + "plotmecore_allowed (plot_id, player) VALUES (?,?)")) {
                                                            migrateStatement2.setInt(1, internalPlotID);
                                                            migrateStatement2.setString(2, player);
                                                            migrateStatement2.execute();
                                                        }
                                                    }
                                                }

                                            }
                                            if (tableExists("plotmeDenied")) {
                                                try (ResultSet resultSet2 = statement.executeQuery("SELECT * FROM plotmeAllowed WHERE idX = " + idX
                                                        + " AND idZ = " + idZ + " AND world = " + world)) {
                                                    UUID pId = UUIDFetcher.getUUIDOf(resultSet2.getString("player"));
                                                    String player = null;
                                                    if (resultSet2.getString("player").equalsIgnoreCase("*")) {
                                                        player = resultSet2.getString("player");
                                                    } else if (pId != null) {
                                                        player = pId.toString();
                                                    }
                                                    if (player != null) {
                                                        try (PreparedStatement migrateStatement3 = getConnection().prepareStatement("INSERT INTO "
                                                                + "plotmecore_denied (plot_id, player) VALUES (?,?)")) {
                                                            migrateStatement3.setInt(1, internalPlotID);
                                                            migrateStatement3.setString(2, player);
                                                            migrateStatement3.execute();
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            statement.execute("DROP TABLE IF EXISTS plotmeDenied");
                            statement.execute("DROP TABLE IF EXISTS plotmeAllowed");
                            statement.execute("DROP TABLE IF EXISTS plotmePlots");
                            statement.execute("DROP TABLE IF EXISTS plotmeComments");
                            statement.execute("DROP TABLE IF EXISTS plotmeFreed");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                                                           }
            );
        }
    }


    abstract boolean tableExists(String name);

    /**
     * Get the number of plots in the world
     * @param world plotworld to check
     * @return number of plots in the world
     */
    public int getPlotCount(String world) {
        int plotCount = 0;
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_PLOT_COUNT)) {

            ps.setString(1, world);

            ResultSet setNbPlots = ps.executeQuery();
            if (setNbPlots.next()) {
                plotCount = setNbPlots.getInt(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("PlotCount Exception :");
            plugin.getLogger().severe(ex.getMessage());
        }
        return plotCount;
    }

    public int getPlotCount(String world, UUID uuid) {
        int plotCount = 0;
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_PLOT_COUNT + " AND ownerID = ?")) {

            ps.setString(1, world);
            ps.setBytes(2, UUIDFetcher.toBytes(uuid));

            ResultSet setNbPlots = ps.executeQuery();
            if (setNbPlots.next()) {
                plotCount = setNbPlots.getInt(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("PlotCount Exception :");
            plugin.getLogger().severe(ex.getMessage());
        }
        return plotCount;
    }

    public void addPlot(Plot plot, PlotId id, ILocation plotTopLoc, ILocation plotBottomLoc) {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "INSERT INTO plotmecore_plots(plotX, plotZ, world, ownerID, owner, biome, finished, finishedDate, forSale, price, protected, "
                        + "expiredDate, topX, topZ, bottomX, bottomZ, plotLikes) VALUES (?,?,?,?,?,?,?,?,"
                        + "?,?,?,?,?,?,?,?,?)");
                PreparedStatement ps2 = getConnection().prepareStatement(SELECT_INTERNAL_ID)) {

            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, plot.getWorld().toLowerCase());
            ps.setString(4, plot.getOwnerId().toString());
            ps.setString(5, plot.getOwner());
            ps.setString(6, plot.getBiome());
            ps.setBoolean(7, plot.isFinished());
            ps.setString(8, plot.getFinishedDate());
            ps.setBoolean(9, plot.isForSale());
            ps.setDouble(10, plot.getPrice());
            ps.setBoolean(11, plot.isProtected());
            ps.setDate(12, plot.getExpiredDate());
            ps.setInt(13, plotTopLoc.getBlockX());
            ps.setInt(14, plotTopLoc.getBlockZ());
            ps.setInt(15, plotBottomLoc.getBlockX());
            ps.setInt(16, plotBottomLoc.getBlockZ());
            ps.setInt(17, plot.getLikes());
            ps.executeUpdate();
            getConnection().commit();
            ps2.setInt(1, id.getX());
            ps2.setInt(2, id.getZ());
            ResultSet getID = ps2.executeQuery();
            while (getID.next()) {
                plot.setInternalID(getID.getInt(1));
            }

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe(ex.getMessage());
        }
    }

    private void savePlotProperty(int internalID, String pluginname, String propertyname, String s) {

    }

    public void addPlotDenied(String player, int plotInternalID) {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT INTO plotmecore_denied (plot_id, denied) VALUES (?,?)")) {
            ps.setInt(1, plotInternalID);
            ps.setString(2, player);
            ps.execute();
            getConnection().commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error adding denied data for plot with internal id " + plotInternalID);
            e.printStackTrace();
        }


    }

    public void addPlotAllowed(String player, int plotInternalID) {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT INTO plotmecore_allowed (plot_id, allowed, access) VALUES(?,?,?)")) {
            ps.setInt(1, plotInternalID);
            ps.setString(2, player);
            ps.setInt(3, 0);
            ps.execute();
            getConnection().commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error adding allowed data for plot with internal id " + plotInternalID);
            e.printStackTrace();
        }
    }

    public void addPlotTrusted(String player, int plotInternalID) {
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO plotmecore_allowed (plot_id, allowed, access) VALUES(?,?,?)")) {
            ps.setInt(1, plotInternalID);
            ps.setString(2, player);
            ps.setInt(3, 1);
            ps.execute();
            connection.commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error adding allowed data for plot with internal id " + plotInternalID);
            e.printStackTrace();
        }
    }

    public void deletePlot(int internalID) {
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM plotmecore_allowed WHERE plot_id = ?")) {
            ps.setInt(1, internalID);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting allowed data for plot with internal id " + internalID);
            e.printStackTrace();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM plotmecore_metadata WHERE plot_id = ?")) {
            ps.setInt(1, internalID);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting metadata for plot with internal id " + internalID);
            e.printStackTrace();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM plotmecore_likes WHERE plot_id = ?")) {
            ps.setInt(1, internalID);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting likes data for plot with internal id " + internalID);
            e.printStackTrace();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM plotmecore_denied WHERE plot_id = ?")) {
            ps.setInt(1, internalID);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting denied data for plot with internal id " + internalID);
            e.printStackTrace();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM plotmecore_plots WHERE id = ?")) {
            ps.setInt(1, internalID);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting plot with internal id " + internalID);
            e.printStackTrace();
        }
    }

    public List<Plot> getPlayerPlots(String name, UUID uuid) {
        return null;
    }

    public void updatePlotsNewUUID(UUID uuid, String name) {

    }

    public List<Plot> getOwnedPlots(String name, UUID uuid, String playerName) {
        return null;
    }

    public void loadPlotsAsynchronously(final String world) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Starting to load plots for world " + world);

                HashMap<PlotId, Plot> plots = getPlots(world.toLowerCase());

                PlotMapInfo pmi = PlotMeCoreManager.getInstance().getMap(world);

                for (PlotId id : plots.keySet()) {
                    pmi.addPlot(id, plots.get(id));
                    InternalPlotLoadEvent event = new InternalPlotLoadEvent(plugin.getServerBridge().getWorld(world), plots.get(id));
                    plugin.getServerBridge().getEventBus().post(event);

                }
                InternalPlotWorldLoadEvent event = new InternalPlotWorldLoadEvent(world, pmi.getNbPlots());
                plugin.getServerBridge().getEventBus().post(event);

            }

            private HashMap<PlotId, Plot> getPlots(String world) {
                HashMap<PlotId, Plot> ret = new HashMap<>();
                Connection connection = getConnection();
                try (PreparedStatement statementPlot = connection.prepareStatement("SELECT * FROM plotmecore_plots WHERE LOWER(world) = ?");
                        PreparedStatement statementAllowed = connection.prepareStatement("SELECT * FROM plotmecore_allowed WHERE plot_id = ?");
                        PreparedStatement statementDenied = connection.prepareStatement("SELECT * FROM plotmecore_denied WHERE plot_id = ?");
                        PreparedStatement statementLikes = connection.prepareStatement("SELECT * FROM plotmecore_likes WHERE plot_id = ?");
                        PreparedStatement statementMetadata = connection.prepareStatement("SELECT * FROM plotmecore_metadata WHERE plot_id = ?")
                ) {
                    statementPlot.setString(1, world);
                    try (ResultSet setPlots = statementPlot.executeQuery()) {
                        while (setPlots.next()) {
                            int internalID = setPlots.getInt("id");
                            PlotId id = new PlotId(setPlots.getInt("plotX"), setPlots.getInt("plotZ"));
                            String owner = setPlots.getString("owner");
                            UUID ownerId = UUID.fromString(setPlots.getString("ownerID"));
                            String biome = setPlots.getString("biome");
                            Date expiredDate = setPlots.getDate("expiredDate");
                            boolean finished = setPlots.getBoolean("finished");
                            String finishedDate = setPlots.getString("finishedDate");
                            double price = setPlots.getDouble("price");
                            boolean forSale = setPlots.getBoolean("forSale");
                            boolean protect = setPlots.getBoolean("protected");
                            String plotName = setPlots.getString("plotName");
                            int plotLikes = setPlots.getInt("plotLikes");
                            Map<String, Map<String, String>> metadata = new HashMap<>();
                            HashMap<String, Integer> allowed = new HashMap<>();
                            PlayerList denied = new PlayerList();
                            PlayerList likers = new PlayerList();
                            statementAllowed.setInt(1, internalID);
                            try (ResultSet setAllowed = statementAllowed.executeQuery()) {
                                while (setAllowed.next()) {
                                    allowed.put(setAllowed.getString("player"), setAllowed.getInt("access"));
                                }
                            }
                            statementDenied.setInt(1, internalID);
                            try (ResultSet setDenied = statementAllowed.executeQuery()) {
                                while (setDenied.next()) {
                                    denied.put(setDenied.getString("player"));
                                }
                            }
                            statementLikes.setInt(1, internalID);
                            try (ResultSet setLikes = statementLikes.executeQuery()) {
                                while (setLikes.next()) {
                                    likers.put(setLikes.getString("player"));
                                }
                            }

                            statementMetadata.setInt(1, internalID);
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

                            Plot plot = new Plot(plugin, internalID, owner, ownerId, world, biome, expiredDate, allowed, denied, likers, id, price,
                                    forSale,
                                    finished, finishedDate, protect, metadata, plotLikes, plotName);
                            ret.put(id, plot);
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

    public Plot getPlot(String world, PlotId id) {
        int idX = id.getX();
        int idZ = id.getZ();
        Plot plot = null;
        Connection connection = getConnection();
        try (PreparedStatement statementPlot = connection.prepareStatement("SELECT * FROM plotmecore_plots WHERE LOWER(world) = ? AND plotX = ? and "
                + "plotZ = ?");
                PreparedStatement statementAllowed = connection.prepareStatement("SELECT * FROM plotmecore_allowed WHERE plot_id = ?");
                PreparedStatement statementDenied = connection.prepareStatement("SELECT * FROM plotmecore_denied WHERE plot_id = ?");
                PreparedStatement statementMetadata = connection.prepareStatement("SELECT * FROM plotmecore_metadata WHERE plot_id = ?")) {
            statementPlot.setString(1, world.toLowerCase());
            statementPlot.setInt(2, idX);
            statementPlot.setInt(3, idZ);
            try (ResultSet setPlots = statementPlot.executeQuery()) {
                if (setPlots.next()) {
                    int internalID = setPlots.getInt("id");
                    String byOwner = setPlots.getString("ownerID");
                    UUID ownerId = UUID.fromString(byOwner);
                    String owner = setPlots.getString("owner");
                    String biome = setPlots.getString("biome");
                    boolean finished = setPlots.getBoolean("finished");
                    String finishedDate = setPlots.getString("finishedDate");
                    boolean forSale = setPlots.getBoolean("forSale");
                    double price = setPlots.getDouble("price");
                    boolean protect = setPlots.getBoolean("protected");
                    Date expiredDate = setPlots.getDate("expiredDate");
                    String plotName = setPlots.getString("plotName");
                    int plotLikes = setPlots.getInt("plotLikes");

                    HashMap<String, Integer> allowed = new HashMap<>();
                    PlayerList denied = new PlayerList();
                    Map<String, Map<String, String>> metadata = new HashMap<>();

                    statementAllowed.setInt(1, internalID);
                    try (ResultSet setAllowed = statementAllowed.executeQuery()) {
                        while (setAllowed.next()) {
                            allowed.put(setAllowed.getString("player"), setAllowed.getInt("access"));
                        }
                    }
                    statementDenied.setInt(1, internalID);
                    try (ResultSet setDenied = statementDenied.executeQuery()) {
                        while (setDenied.next()) {
                            denied.put(setDenied.getString("denied"));
                        }
                    }

                    statementMetadata.setInt(1, internalID);

                    ResultSet setMetadata = statementMetadata.executeQuery();

                    while (setMetadata.next()) {
                        String pluginname = setMetadata.getString("pluginName");
                        String propertyname = setMetadata.getString("propertyName");
                        String propertyvalue = setMetadata.getString("propertyValue");
                        if (!metadata.containsKey(pluginname)) {
                            metadata.put(pluginname, new HashMap<String, String>());
                        }
                        metadata.get(pluginname).put(propertyname, propertyvalue);
                    }

                    setMetadata.close();

                    plot = new Plot(plugin, internalID, owner, ownerId, world, biome, expiredDate, allowed, denied, id, price, forSale, finished,
                            finishedDate, protect, metadata, plotLikes, plotName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plot;
    }

    public void updatePlot(PlotId id, String name, String biome, Object name1) {
        //TODO Get rid of this ugly method
    }

    public void plotConvertToUUIDAsynchronously() {

    }

    public void coreDatabaseUpdate() {

    }

    public void deletePlotAllowed(int internalID, String name) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM plotmecore_allowed WHERE plot_id = ? AND allowed = ?")) {
            statement.setInt(1, internalID);
            statement.setString(2, name);
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
    public void deleteAllAllowed(int internalID) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM plotmecore_allowed WHERE plot_id = ?")) {
            statement.setInt(1, internalID);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deletePlotDenied(int internalID, String name) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM plotmecore_denied WHERE plot_id = ? AND denied = ?")) {
            statement.setInt(1, internalID);
            statement.setString(2, name);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllDenied(int internalID) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM plotmecore_denied WHERE deniedID = ?")) {
            statement.setInt(1, internalID);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public boolean savePlotProperty(PlotId id, String world, String pluginname, String property, String value) {
        return false;
    }

    public List<Plot> getExpiredPlots(String world) {
        List<Plot> ret = new ArrayList<>();

        try (PreparedStatement statementExpired = getConnection().prepareStatement("SELECT * FROM plotmecore_plots WHERE LOWER(world) = ? AND "
                + "protected = 0 AND finished = 0 AND expireddate < ? ORDER BY expireddate")) {
            Calendar cal = Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            Date sqlDate = new Date(utilDate.getTime());
            statementExpired.setString(1, world.toLowerCase());
            statementExpired.setDate(2, sqlDate);
            try (ResultSet setPlots = statementExpired.executeQuery()) {
                while (setPlots.next()) {
                    PlotId id = new PlotId(setPlots.getInt("idX"), setPlots.getInt("idZ"));
                    String owner = setPlots.getString("owner");
                    Date expireddate = setPlots.getDate("expireddate");
                    Plot plot = null;
                    plot.setOwner(owner);
                    plot.setId(id);
                    plot.setExpiredDate(expireddate);

                    ret.add(plot);
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("ExpiredPlots Exception :");
            plugin.getLogger().severe(ex.getMessage());
        }
        return ret;
    }

    abstract Connection legacyConnection();
}
