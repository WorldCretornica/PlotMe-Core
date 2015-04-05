package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnector extends Database {

    private final String url;
    private final String userName;
    private final String password;

    public MySQLConnector(PlotMe_Core plugin, String url, String userName, String password) {
        super(plugin);
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Connection startConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("PlotMe could not establish a connection to the MySQL database:");
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
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,"
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
                    + "`expiredDate` DATETIME NULL DEFAULT NULL,"
                    + "`topX` INTEGER NOT NULL DEFAULT '0',"
                    + "`topZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`bottomX` INTEGER NOT NULL DEFAULT '0',"
                    + "`bottomZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`plotName` VARCHAR(32) DEFAULT NULL UNIQUE,"
                    + "`plotLikes` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeX` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeY` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeName` VARCHAR(32) DEFAULT NULL,"
                    + "UNIQUE KEY `plotLocation` (`plotX`,`plotZ`,`world`),"
                    + "UNIQUE KEY `playerHome` (`ownerID`(16),`homeName`)"
                    + ");");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_denied` ("
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`player` VARCHAR(50) NOT NULL,"
                    + "UNIQUE INDEX `allowed` (plot_id,player)"
                    + ");");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_allowed` ("
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`player` VARCHAR(50) NOT NULL,"
                    + "`access` INTEGER NOT NULL DEFAULT '1',"
                    + "UNIQUE INDEX `allowed` (plot_id,player)"
                    + ");");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS plotmecore_likes ("
                    + "`id` INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`player` VARCHAR(50) NOT NULL,"
                    + "UNIQUE INDEX `likes` (plot_id, player)"
                    + ");");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_metadata` ("
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`pluginname` VARCHAR(100) NOT NULL,"
                    + "`propertyname` VARCHAR(100) NOT NULL,"
                    + "`propertyvalue` VARCHAR(255) DEFAULT NULL"
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
        Connection conn = getConnection();
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            try (ResultSet rs = dbm.getTables(null, null, name, null)) {
                return rs.next();
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Table Check Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
            return false;
        }
    }

    @Override
    Connection legacyConnection() {
        return getConnection();
    }

}
