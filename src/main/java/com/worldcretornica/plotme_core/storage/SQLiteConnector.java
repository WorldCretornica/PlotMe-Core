package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnector extends Database {

    public SQLiteConnector(PlotMe_Core plugin) {
        super(plugin);
    }

    /**
     * Establish a connection to the plotme database
     * @return connection established
     */
    @Override
    public Connection startConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder().getAbsolutePath() + "/plotmecore.db");
            connection.setAutoCommit(false);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().severe("Could not establish a connection to the PlotMe SQLite database:");
            plugin.getLogger().severe(e.getMessage());
            return connection;
        }
    }

    @Override
    public void createTables() {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            //MySQL specific plot table creation.
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_plots` ("
                    + "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "`plotX` INTEGER NOT NULL,"
                    + "`plotZ` INTEGER NOT NULL,"
                    + "`world` VARCHAR(32) NOT NULL,"
                    + "`ownerID` VARCHAR(50) NOT NULL,"
                    + "`owner` VARCHAR(32) NOT NULL,"
                    + "`biome` VARCHAR(50) NOT NULL DEFAULT 'PLAINS',"
                    + "`finished` BOOLEAN NOT NULL DEFAULT '0',"
                    + "`finishedDate` VARCHAR(20) DEFAULT NULL,"
                    + "`forSale` BOOLEAN NOT NULL DEFAULT '0',"
                    + "`price` DOUBLE NOT NULL DEFAULT '0',"
                    + "`protected` BOOLEAN NOT NULL DEFAULT '0',"
                    + "`expiredDate` DATETIME DEFAULT NULL,"
                    + "`topX` INTEGER NOT NULL DEFAULT '0',"
                    + "`topZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`bottomX` INTEGER NOT NULL DEFAULT '0',"
                    + "`bottomZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`plotName` VARCHAR(32) DEFAULT NULL UNIQUE,"
                    + "`plotLikes` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeX` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeY` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeName` VARCHAR(32) DEFAULT NULL"
                    + ");");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS `plotLocation` ON plotmecore_plots(plotx,plotz,world);");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS `playerHome` ON plotmecore_plots(ownerid,homename);");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_denied` ("
                    + "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`player` VARCHAR(50) NOT NULL"
                    + ");");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS `denied` ON plotmecore_denied(plot_id,player)");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_allowed` ("
                    + "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`player` VARCHAR(50) NOT NULL,"
                    + "`access` INTEGER NOT NULL DEFAULT '1'"
                    + ");");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS `allowed` ON plotmecore_allowed(plot_id,player)");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS plotmecore_likes ("
                    + "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`player` VARCHAR(50) NOT NULL"
                    + ");");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS `likes` ON plotmecore_likes(plot_id,player)");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_metadata` ("
                    + "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`pluginName` VARCHAR(100) NOT NULL,"
                    + "`propertyName` VARCHAR(100) NOT NULL,"
                    + "`propertyValue` VARCHAR(255) DEFAULT NULL"
                    + ");");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @deprecated Legacy Code for 0.16.3 to 0.17 Update. To be removed in 0.18 or 0.19
     */
    @Deprecated
    boolean tableExists(String name) {
        try (Connection conn = legacyConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            try (ResultSet rs = dbm.getTables(null, null, name, null)) {
                return rs.next();
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Table Check Exception :");
            plugin.getLogger().severe(ex.getMessage());
            return false;
        }
    }

    Connection legacyConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder().getAbsolutePath() + "/plots.db");
            connection.setAutoCommit(false);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().severe("Could not establish a connection to the PlotMe SQLite database:");
            plugin.getLogger().severe(e.getMessage());
            return connection;
        }
    }
}
