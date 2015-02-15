package com.worldcretornica.plotme_core.storage;

import com.worldcretornica.plotme_core.PlotMe_Core;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector extends Database {


    public MySQLConnector(PlotMe_Core plugin) {

        super(plugin);
    }

    @Override
    public boolean startConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = plugin.getServerBridge().getConfig().getString("mySQLconn");
            String user = plugin.getServerBridge().getConfig().getString("mySQLuname");
            String pass = plugin.getServerBridge().getConfig().getString("mySQLpass");
            connection = DriverManager.getConnection(url, user, pass);
            connection.setAutoCommit(false);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("PlotMe could not establish a connection to the MySQL database:");
            plugin.getLogger().severe(e.getMessage());
            return false;
        }

    }

    @Override
    public void createTables() {

    }
}
