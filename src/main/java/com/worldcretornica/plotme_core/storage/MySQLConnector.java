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
        startConnection();
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
            statement.executeUpdate(PLOT_TABLE + ", "
                    + "UNIQUE KEY `plotLocation` (`plotX`,`plotZ`,`world`),"
                    + "UNIQUE KEY `playerHome` (`ownerID`(16), `homeName`)"
                    + ");");
            statement.executeQuery("ALTER TABLE plotmecore_plots MODIFY id INTEGER AUTO_INCREMENT");
            connection.commit();
            statement.executeUpdate(ALLOWED_TABLE);
            statement.executeUpdate("ALTER TABLE plotmecore_allowed MODIFY id INTEGER AUTO_INCREMENT");
            connection.commit();
            statement.executeUpdate(DENIED_TABLE);
            statement.executeUpdate("ALTER TABLE plotmecore_denied MODIFY id INTEGER AUTO_INCREMENT");
            connection.commit();
            statement.executeUpdate(LIKES_TABLE);
            statement.executeUpdate("ALTER TABLE plotmecore_likes MODIFY id INTEGER AUTO_INCREMENT");
            connection.commit();
            statement.executeUpdate(METADATA_TABLE);
            statement.executeUpdate("ALTER TABLE plotmecore_metadata MODIFY id INTEGER AUTO_INCREMENT");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
