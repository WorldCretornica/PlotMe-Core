package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

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

    public void createTables() {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            //Main Plot Storage Table
            String PLOT_TABLE = "CREATE TABLE IF NOT EXISTS `plotmecore_plots` ("
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
                    + "`homeName` VARCHAR(32)"
                    + ");";
            statement.executeUpdate(PLOT_TABLE);
            connection.commit();
            //Plot Allowed table
            String PLOT_ALLOWED = "CREATE TABLE IF NOT EXISTS `plotmecore_allowed` ("
                    //Not used yet but included if needed in the future
                    + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
                    /*`plot_id` is the internal plot id that is in the first collumn of the main plot table.
                    This is not to be confused with the plot id that the user is used to seeing.
                    */
                    + "`plot_id` VARCHAR(32) NOT NULL,"
                    // `allowedID` will be null if we add or deny a group to the plot.
                    + "`allowedID` BLOB(16),"
                    //The name of the user or group that is allowed
                    + "`allowed` VARCHAR(32) NOT NULL"
                    + ");";
            statement.executeUpdate(PLOT_ALLOWED);
            connection.commit();
            //Plot Denied table
            String PLOT_DENIED = "CREATE TABLE IF NOT EXISTS `plotmecore_denied` ("
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
            statement.executeUpdate(PLOT_DENIED);
            connection.commit();
            String PLOT_LIKES = "CREATE TABLE IF NOT EXISTS `plotmecore_likes` ("
                    + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
                    + "`plot_id` VARCHAR(32) NOT NULL,"
                    + "`playerID` BLOB(16),"
                    + "`player` VARCHAR(32) NOT NULL"
                    + ");";
            statement.executeUpdate(PLOT_LIKES);
            connection.commit();
            String METADATA_TABLE = "CREATE TABLE IF NOT EXISTS `plotmecore_metadata` ("
                    + "`id` INTEGER PRIMARY KEY UNIQUE NOT NULL,"
                    + "`plot_id` VARCHAR(32) NOT NULL,"
                    + "`pluginName` NVARCHAR(100) NOT NULL,"
                    + "`propertyBame` NVARCHAR(100) NOT NULL,"
                    + "`propertyValue` NVARCHAR(255)"
                    + ");";
            statement.executeUpdate(METADATA_TABLE);
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void legacyConverter() {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                Connection connection = getConnection();
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("INSERT INTO `plotmecore_plots` (plotX, plotZ, world, ownerID, owner, biome, finished, finishedDate, "
                            + "forSale, price, protected, expiredDate, topX,topZ, bottomX, bottomZ) SELECT idX,idZ,world,ownerid,owner,biome,"
                            + "finished,finisheddate,forsale,customprice,protected,expireddate,topX,topZ,bottomX,bottomZ FROM `plotmePlots` WHERE "
                            + "ownerid IS NOT NULL");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
