package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlayerList;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                try (Statement statement = connection.createStatement();
                        PreparedStatement statement2 = connection.prepareStatement(SELECT_INTERNAL_ID
                                + " AND LOWER(world) = ?"); PreparedStatement statement3 = connection.prepareStatement(
                        "INSERT INTO plotmecore_allowed (plot_id, allowedID, allowed) VALUES (?,?,?)");
                        PreparedStatement statement4 = connection
                                .prepareStatement("INSERT INTO plotmecore_denied (plot_id, deniedID, denied) VALUES (?,?,?)")) {
                    statement.executeUpdate("INSERT INTO plotmecore_plots (plotX, plotZ, world, ownerID, owner, biome, finished, finishedDate, "
                            + "forSale, price, protected, expiredDate, topX,topZ, bottomX, bottomZ) SELECT idX,idZ,world,ownerid,owner,biome,"
                            + "finished,finisheddate,forsale,customprice,protected,expireddate,topX,topZ,bottomX,bottomZ FROM `plotmePlots` WHERE "
                            + "ownerid IS NOT NULL");
                    statement.executeUpdate("");
                    connection.commit();
                    ResultSet allowedResult = statement.executeQuery("SELECT * FROM plotmeallowed");
                    while (allowedResult.next()) {
                        int idX = allowedResult.getInt("idX");
                        int idZ = allowedResult.getInt("idZ");
                        String world = allowedResult.getString("world").toLowerCase();
                        statement2.setInt(1, idX);
                        statement2.setInt(2, idZ);
                        statement2.setString(3, world);
                        ResultSet resultSet = statement2.executeQuery();
                        while (resultSet.next()) {
                            statement3.executeQuery("INSERT INTO plotmecore_allowed (plot_id, allowedID, allowed) VALUES (" + resultSet.getInt(1)
                                    + "," + allowedResult.getBytes("playerid") + "," + allowedResult.getString("player") + ")");
                        }
                        resultSet.close();
                    }
                    connection.commit();
                    allowedResult.close();
                    ResultSet deniedResult = statement.executeQuery("SELECT * FROM plotmedenied");
                    while (deniedResult.next()) {
                        int idX = deniedResult.getInt("idX");
                        int idZ = deniedResult.getInt("idZ");
                        String world = deniedResult.getString("world").toLowerCase();
                        statement2.setInt(1, idX);
                        statement2.setInt(2, idZ);
                        statement2.setString(3, world);
                        ResultSet resultSet = statement2.executeQuery();
                        while (resultSet.next()) {
                            statement4.executeQuery("INSERT INTO plotmecore_denied (plot_id, allowedID, allowed) VALUES (" + resultSet.getInt(1)
                                    + "," + deniedResult.getString("playerid") + "," + deniedResult.getString("player") + ")");
                        }
                        resultSet.close();
                    }
                    deniedResult.close();
                    connection.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

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
                    plugin.getServerBridge().getEventFactory()
                            .callPlotLoadedEvent(plugin.getServerBridge().getWorld(world), plots.get(id));
                }
                plugin.getServerBridge().getEventFactory().callPlotWorldLoadEvent(world, pmi.getNbPlots());
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
                            PlayerList allowed = new PlayerList();
                            PlayerList denied = new PlayerList();
                            PlayerList likers = new PlayerList();
                            statementAllowed.setInt(1, internalID);
                            try (ResultSet setAllowed = statementAllowed.executeQuery()) {
                                while (setAllowed.next()) {
                                    allowed.put(setAllowed.getString("player"));
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
            ResultSet setPlots = statementPlot.executeQuery();
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

                PlayerList allowed = new PlayerList();
                PlayerList trusted = new PlayerList();
                PlayerList denied = new PlayerList();
                Map<String, Map<String, String>> metadata = new HashMap<>();

                //Get Allowed Players
                statementAllowed.setInt(1, internalID);

                ResultSet setAllowed = statementAllowed.executeQuery();

                while (setAllowed.next()) {
                    int accessLevel = setAllowed.getInt("access");
                    //This is for adding players
                    if (accessLevel == 1) {
                        allowed.add(setAllowed.getString("allowed"));
                    } else if (accessLevel == 2) { //If the player is trusted only
                        trusted.add(setAllowed.getString("allowed"));
                    }
                }
                setAllowed.close();

                //Get Denied Players
                statementDenied.setInt(1, internalID);

                ResultSet setDenied = statementDenied.executeQuery();

                while (setDenied.next()) {
                    byte[] byPlayerId = setDenied.getBytes("deniedID");
                    if (byPlayerId == null) {
                        denied.put(setDenied.getString("denied"));
                    } else {
                        denied.add(UUIDFetcher.fromBytes(byPlayerId).toString());
                    }
                }
                setDenied.close();

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

    public List<Plot> getExpiredPlots(String name, int i, int i1) {
        return null;
    }
}
