package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnector extends Database {

    public SQLiteConnector(PlotMe_Core plugin) {
        super(plugin);
    }

    /**
     * Establish a connection to the plotme database
     * @return true if the connection was established, false otherwise
     */
    @Override
    public boolean startConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder() + "/plotmecore.db");
            connection.setAutoCommit(false);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().severe("Could not establish a connection to the PlotMe SQLite database:");
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
    }

    @Override
    public void createTables() {
        Connection connection = getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String PLOT_TABLES = "CREATE TABLE plotmecore_plots ("
                    + "    id INTEGER PRIMARY KEY UNIQUE,"
                    + "    plotX INTEGER NOT NULL,"
                    + "    plotZ INTEGER NOT NULL,"
                    + "    world VARCHAR(32) NOT NULL,"
                    + "    ownerID BLOB(16) NOT NULL,"
                    + "    owner VARCHAR(32) NOT NULL,"
                    + "    biome VARCHAR(32) DEFAULT 'PLAINS' NOT NULL,"
                    + "    finished BOOLEAN DEFAULT '0' NOT NULL,"
                    + "    finishedDate VARCHAR(16),"
                    + "    forSale BOOLEAN DEFAULT '0' NOT NULL,"
                    + "    price DOUBLE DEFAULT '0' NOT NULL,"
                    + "    protected BOOLEAN DEFAULT '0' NOT NULL,"
                    + "    expiredDate VARCHAR(16),"
                    + "    topX INTEGER NOT NULL DEFAULT '0',"
                    + "    topZ INTEGER NOT NULL DEFAULT '0',"
                    + "    bottomX INTEGER NOT NULL DEFAULT '0',"
                    + "    bottomZ INTEGER NOT NULL DEFAULT '0',"
                    + "    plotName VARCHAR(32) UNIQUE,"
                    + "    plotLikes INTEGER NOT NULL DEFAULT '0',"
                    + "    homeX INTEGER NOT NULL,"
                    + "    homeY INTEGER NOT NULL,"
                    + "    homeZ INTEGER NOT NULL,"
                    + "    homeName VARCHAR(32)"
                    + ");";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Could not create the table (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }

        }
    }

}
