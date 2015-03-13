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
            //SQLite Specific Unique Index Additions.
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS plotName ON  plotmecore_plots(plotName)");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS plotName ON  plotmecore_plots(plotName)");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS plotName ON  plotmecore_plots(plotName)");
            connection.commit();
            statement.executeUpdate(ALLOWED_TABLE);
            connection.commit();
            statement.executeUpdate(DENIED_TABLE);
            connection.commit();
            statement.executeUpdate(LIKES_TABLE);
            connection.commit();
            statement.executeUpdate(METADATA_TABLE);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
