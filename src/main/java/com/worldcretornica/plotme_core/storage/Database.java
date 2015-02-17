package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

    public PlotMe_Core plugin;

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
        if (connection == null) {
            return startConnection();
        }
        return connection;
    }

    public void createTables() {
        this.connection = getConnection();
        try (Statement statement = connection.createStatement()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
