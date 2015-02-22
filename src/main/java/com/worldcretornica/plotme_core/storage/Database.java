package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlayerList;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Database {

    public static final String PLOT_TABLE = "CREATE TABLE IF NOT EXISTS plotmecore_plots ("
            + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
            + "`plotX` INTEGER NOT NULL,"
            + "`plotZ` INTEGER NOT NULL,"
            + "`world` VARCHAR(32) NOT NULL,"
            + "`ownerID` BLOB(16) NOT NULL,"
            + "`owner` VARCHAR(32) NOT NULL,"
            + "`biome` VARCHAR(32) DEFAULT 'PLAINS' NOT NULL,"
            + "`finished` BOOLEAN NOT NULL DEFAULT '0',"
            + "`finishedDate` VARCHAR(16),"
            + "`forSale` BOOLEAN NOT NULL DEFAULT '0',"
            + "`price` DOUBLE NOT NULL DEFAULT '0',"
            + "`protected` BOOLEAN NOT NULL DEFAULT '0',"
            + "`expiredDate` VARCHAR(16),"
            + "`topX` INTEGER NOT NULL DEFAULT '0',"
            + "`topZ` INTEGER NOT NULL DEFAULT '0',"
            + "`bottomX` INTEGER NOT NULL DEFAULT '0',"
            + "`bottomZ` INTEGER NOT NULL DEFAULT '0',"
            + "`plotName` VARCHAR(32) UNIQUE,"
            + "`plotLikes` INTEGER NOT NULL DEFAULT '0',"
            + "`homeX` INTEGER NOT NULL,"
            + "`homeY` INTEGER NOT NULL,"
            + "`homeZ` INTEGER NOT NULL,"
            + "`homeName` VARCHAR(32)";
    public static final String ALLOWED_TABLE = "CREATE TABLE IF NOT EXISTS plotmecore_allowed ("
            //Not used yet but included if needed in the future
            + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
            /*`plot_id` is the internal plot id that is in the first collumn of the main plot table.
            This is not to be confused with the plot id that the user is used to seeing.
            */
            + "`plot_id` VARCHAR(32) NOT NULL,"
            // `allowedID` will be null if we add or deny a group to the plot.
            + "`allowedID` BLOB(16),"
            //The name of the user or group that is allowed
            + "`allowed` VARCHAR(32) NOT NULL,"
            //This is true if the player can build even when the plot owner is offline
            + "`trusted` BOOLEAN NOT NULL DEFAULT '1'"
            + ");";
    public static final String DENIED_TABLE = "CREATE TABLE IF NOT EXISTS plotmecore_denied ("
            //Not used yet but included if needed in the future
            + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
            /*`plot_id` is the internal plot id that is in the first collumn of the main plot table.
            This is not to be confused with the plot id that the user is used to seeing.
            */
            + "`plot_id` VARCHAR(32) NOT NULL,"
            // `allowedID` will be null if we add or deny a group to the plot.
            + "`deniedID` BLOB(16),"
            //The name of the user or group that is allowed
            + "`denied` VARCHAR(32) NOT NULL"
            + ");";
    public static final String LIKES_TABLE = "CREATE TABLE IF NOT EXISTS plotmecore_likes ("
            + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
            + "`plot_id` VARCHAR(32) NOT NULL,"
            + "`playerID` BLOB(16),"
            + "`player` VARCHAR(32) NOT NULL"
            + ");";
    public static final String METADATA_TABLE = "CREATE TABLE IF NOT EXISTS plotmecore_metadata ("
            + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
            + "`plot_id` VARCHAR(32) NOT NULL,"
            + "`pluginName` NVARCHAR(100) NOT NULL,"
            + "`propertyBame` NVARCHAR(100) NOT NULL,"
            + "`propertyValue` NVARCHAR(255)"
            + ");";
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
                Connection connection = getConnection();
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
                                    + "," + allowedResult.getString("playerid") + "," + allowedResult.getString("player") + ")");
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
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO plotmecore_plots (plotX, plotZ, owner, ownerID, world, topX, bottomX, "
                        + "topZ, bottomZ, "
                        + "biome, expiredDate, finished, price, forSale, finishedDate, protected, ownerID, plotLikes) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); Statement statement = connection.createStatement()) {

            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, plot.getOwner());
            ps.setBytes(4, UUIDFetcher.toBytes(plot.getOwnerId()));
            ps.setString(5, plot.getWorld().toLowerCase());

            ps.setInt(6, plotTopLoc.getBlockX());
            ps.setInt(7, plotBottomLoc.getBlockX());
            ps.setInt(8, plotTopLoc.getBlockZ());
            ps.setInt(9, plotBottomLoc.getBlockZ());
            ps.setString(10, ((BukkitBiome) plot.getBiome()).getBiome().name());
            ps.setDate(11, plot.getExpiredDate());
            ps.setBoolean(12, plot.isFinished());
            ps.setDouble(13, plot.getPrice());
            ps.setBoolean(14, plot.isForSale());
            ps.setString(15, plot.getFinishedDate());
            ps.setBoolean(16, plot.isProtect());
            ps.setBytes(17, UUIDFetcher.toBytes(plot.getOwnerId()));
            ps.setInt(18, plot.getLikes());
            ps.executeUpdate();
            connection.commit();
            ResultSet getID = statement.executeQuery(SELECT_INTERNAL_ID);
            while (getID.next()) {
                plot.setInternalID(getID.getInt(1));
            }
            PlayerList allowedlist = plot.allowed();
            if (allowedlist.getAllPlayers() != null) {
                HashMap<String, UUID> allowed = allowedlist.getAllPlayers();
                for (String player : allowed.keySet()) {
                    addPlotAllowed(player, allowed.get(player), plot.getInternalID());
                }
            }

            PlayerList deniedlist = plot.denied();
            if (deniedlist.getAllPlayers() != null) {
                HashMap<String, UUID> denied = deniedlist.getAllPlayers();
                for (String player : denied.keySet()) {
                    addPlotDenied(player, denied.get(player), plot.getInternalID());
                }
            }

            Map<String, Map<String, String>> metadata = plot.getAllPlotProperties();
            if (!metadata.isEmpty()) {
                for (String pluginname : metadata.keySet()) {
                    Map<String, String> pluginproperties = metadata.get(pluginname);
                    for (String propertyname : pluginproperties.keySet()) {
                        savePlotProperty(plot.getInternalID(), pluginname, propertyname, pluginproperties.get(propertyname));
                    }
                }
            }

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe(ex.getMessage());
        }
    }

    private void savePlotProperty(int internalID, String pluginname, String propertyname, String s) {

    }

    public void addPlotDenied(String player, UUID uuid, int plotInternalID) {

    }

    public void addPlotAllowed(String key, UUID uuid, int plotInternalID) {

    }

    public void deletePlot(PlotId idTo, String name) {

    }

    public Plot[] getPlayerPlots(String name, UUID uuid) {
        return new Plot[0];
    }

    public void updatePlotsNewUUID(UUID uuid, String name) {

    }

    public List<Plot> getOwnedPlots(String name, UUID uuid, String playerName) {
        return null;
    }
}
