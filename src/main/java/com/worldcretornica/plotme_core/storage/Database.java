package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Database {

    public PlotMe_Core plugin;

    public Connection connection;

    public Database(PlotMe_Core plugin) {
        this.plugin = plugin;
    }

    /**
     * Closes the connecection to the database
     * This will return true if the connection is null.
     * @return true if the connection closed successfully
     */
    public boolean closeConnection() {
        if (connection == null) {
            return true;
        }
        try {
            connection.close();
            connection = null;
            return true;
        } catch (SQLException e) {
            plugin.getLogger().severe("Error disconnecting from the database:");
            e.printStackTrace();
            return false;
        }
    }

    public abstract boolean startConnection();

    /**
     * The database connection
     * @return the connection to the database
     */
    public Connection getConnection() {
        if (connection == null) {
            if (!startConnection()) {
                plugin.getLogger().severe("Error getting connection.");
                return null;
            }
        }
        return connection;
    }

    public abstract void createTables();
}
