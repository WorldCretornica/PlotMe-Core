package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.DriverManager;
import java.sql.SQLException;

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

    }

}
