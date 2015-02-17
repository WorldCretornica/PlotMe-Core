package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    }

    public void mySQLConversion () {

    }
}
