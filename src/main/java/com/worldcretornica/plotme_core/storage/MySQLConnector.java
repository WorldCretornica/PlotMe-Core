package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.DriverManager;
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
            statement.executeUpdate("CREATE TABLE `plotmecore_plots` ("
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`plotX` INTEGER NOT NULL,"
                    + "`plotZ` INTEGER NOT NULL,"
                    + "`world` VARCHAR(32) NOT NULL,"
                    + "`ownerID` VARCHAR(50) NOT NULL,"
                    + "`owner` VARCHAR(32) NOT NULL,"
                    + "`biome` VARCHAR(50) NOT NULL DEFAULT 'PLAINS',"
                    + "`finished` BOOLEAN NOT NULL DEFAULT '0',"
                    + "`finishedDate` VARCHAR(16) DEFAULT NULL,"
                    + "`forSale` BOOLEAN NOT NULL DEFAULT '0',"
                    + "`price` DOUBLE NOT NULL DEFAULT '0',"
                    + "`protected` BOOLEAN NOT NULL DEFAULT '0',"
                    + "`expiredDate` VARCHAR(16) DEFAULT NULL,"
                    + "`topX` INTEGER NOT NULL DEFAULT '0',"
                    + "`topZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`bottomX` INTEGER NOT NULL DEFAULT '0',"
                    + "`bottomZ` INTEGER NOT NULL DEFAULT '0',"
                    + "`plotName` VARCHAR(32) DEFAULT NULL UNIQUE,"
                    + "`plotLikes` INTEGER NOT NULL DEFAULT '0',"
                    + "`homeX` INTEGER NOT NULL,"
                    + "`homeY` INTEGER NOT NULL,"
                    + "`homeZ` INTEGER NOT NULL,"
                    + "`homeName` VARCHAR(32) DEFAULT NULL,"
                    + "UNIQUE KEY `plotLocation` (`plotX`,`plotZ`,`world`),"
                    + "UNIQUE KEY `playerHome` (`ownerID`(16),`homeName`)"
                    + ");");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_denied` ("
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`denied` VARCHAR(50) NOT NULL,"
                    + "UNIQUE INDEX `allowed` (plot_id,denied)"
                    + ");");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `plotmecore_allowed` ("
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`allowed` VARCHAR(50) NOT NULL,"
                    + "`access` INTEGER NOT NULL DEFAULT '1',"
                    + "UNIQUE INDEX `allowed` (plot_id,allowed)"
                    + ");");
            connection.commit();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS plotmecore_likes ("
                    + "`id` INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`player` VARCHAR(50) NOT NULL,"
                    + "UNIQUE INDEX `likes` (plot_id, player)"
                    + ");");
            statement.executeUpdate("CREATE TABLE `plotmecore_metadata` ("
                    + "`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`plot_id` INTEGER NOT NULL,"
                    + "`pluginname` VARCHAR(100) NOT NULL,"
                    + "`propertyname` VARCHAR(100) NOT NULL,"
                    + "`propertyvalue` VARCHAR(255)"
                    + ");");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
