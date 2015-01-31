package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.*;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;

import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class SqlManager {

    public static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    private static final Pattern COMPILE = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");
    private final PlotMe_Core plugin;
    private final String mySQLuname;
    private final String mySQLpass;
    private final String mySQLconn;
    private Connection conn;

    public SqlManager(PlotMe_Core plugin, String sqlusername, String sqlpassword, String sqlconnection) {
        this.plugin = plugin;
        mySQLconn = sqlconnection;
        mySQLpass = sqlpassword;
        mySQLuname = sqlusername;
    }

    public Connection initialize() {
        try {
            if (isUsingMySQL()) {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(mySQLconn, mySQLuname, mySQLpass);
                conn.setAutoCommit(false);
            } else {
                Class.forName(SQLITE_DRIVER);
                conn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder() + "/plots.db");
                conn.setAutoCommit(false);
            }
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("SQL exception on initialize :");
            plugin.getLogger().severe(e.getMessage());
        }

        createTable();

        return conn;
    }

    public String getSchema() {
        String conn = mySQLconn;

        if (conn.lastIndexOf("/") > 0) {
            return conn.substring(conn.lastIndexOf("/") + 1);
        } else {
            return "";
        }
    }

    public void UpdateTables() {
        Statement statement = null;
        ResultSet set = null;

        try {
            Connection conn = getConnection();

            statement = conn.createStatement();

            if (isUsingMySQL()) {
                String schema = getSchema();
                /*** START Version 0.13d changes ***/

                // OwnerId
                set =
                        statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                               + "TABLE_NAME='plotmePlots' AND column_name='ownerid'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD ownerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                // Allowed playerid
                set =
                        statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                               + "TABLE_NAME='plotmeAllowed' AND column_name='playerid'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmeAllowed ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                // Denied playerid
                set =
                        statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                               + "TABLE_NAME='plotmeDenied' AND column_name='playerid'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmeDenied ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                // CurrentBidderId
                set =
                        statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                               + "TABLE_NAME='plotmePlots' AND column_name='currentbidderId'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD currentbidderId blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                /*** END Version 0.13d changes ***/

            } else {

                /*** START Version 0.13d changes ***/

                // OwnerId
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                String column;
                boolean found = false;
                while (set.next() && !found) {
                    column = set.getString(2);
                    if ("ownerid".equalsIgnoreCase(column)) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD ownerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                // Allowed id
                set = statement.executeQuery("PRAGMA table_info(`plotmeAllowed`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if ("playerid".equalsIgnoreCase(column)) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmeAllowed ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                // Denied id
                set = statement.executeQuery("PRAGMA table_info(`plotmeDenied`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if ("playerid".equalsIgnoreCase(column)) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmeDenied ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                // CurrentBidderId
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if ("currentbidderId".equalsIgnoreCase(column)) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD currentbidderId blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                /*** END Version 0.13d changes ***/

            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Update table exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (set != null) {
                    set.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Update table exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public Connection getConnection() {
        if (conn == null) {
            conn = initialize();
        }
        if (isUsingMySQL()) {
            try {
                if (!conn.isValid(10)) {
                    conn = initialize();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Failed to check SQL status :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return conn;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                if (isUsingMySQL()) {
                    if (conn.isValid(10)) {
                        conn.close();
                    }
                    conn = null;
                } else {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Error on Connection close :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    private boolean tableExists(String name) {
        ResultSet rs = null;
        try {
            Connection conn = getConnection();

            DatabaseMetaData dbm = conn.getMetaData();
            rs = dbm.getTables(null, null, name, null);
            return rs.next();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Table Check Exception :");
            plugin.getLogger().severe(ex.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Table Check SQL Exception (on closing) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    private void createTable() {
        Statement st = null;
        try {
            //PlotMe.logger.info(PlotMe.PREFIX + " Creating Database...");
            Connection conn = getConnection();
            st = conn.createStatement();

            if (!tableExists("plotmePlots")) {
                String PLOT_TABLE = "CREATE TABLE `plotmePlots` ("
                                    + "`idX` INTEGER," //1
                                    + "`idZ` INTEGER," //2
                                    + "`owner` VARCHAR(32) NOT NULL," //3
                                    + "`world` VARCHAR(32) NOT NULL DEFAULT '0'," //4
                                    + "`topX` INTEGER NOT NULL DEFAULT '0'," //5
                                    + "`bottomX` INTEGER NOT NULL DEFAULT '0'," //6
                                    + "`topZ` INTEGER NOT NULL DEFAULT '0'," //7
                                    + "`bottomZ` INTEGER NOT NULL DEFAULT '0'," //8
                                    + "`biome` VARCHAR(32) NOT NULL DEFAULT '0'," //9
                                    + "`expireddate` DATE,"  //10
                                    + "`finished` BOOLEAN NOT NULL DEFAULT '0'," //11
                                    + "`customprice` DOUBLE NOT NULL DEFAULT '0'," //12
                                    + "`forsale` BOOLEAN NOT NULL DEFAULT '0'," //13
                                    + "`finisheddate` VARCHAR(16)," //14
                                    + "`protected` BOOLEAN NOT NULL DEFAULT '0'," //15
                                    + "`auctionned` BOOLEAN NOT NULL DEFAULT '0'," //16
                                    + "`currentbid` DOUBLE NOT NULL DEFAULT '0'," //17
                                    + "`currentbidder` VARCHAR(32)," //18
                                    + "`currentbidderId` BLOB(16)," //19
                                    + "`ownerId` BLOB(16)," //20
                                    + "PRIMARY KEY (idX, idZ, world) "
                                    + ");";
                st.executeUpdate(PLOT_TABLE);
                conn.commit();
            }

            if (!tableExists("plotmeAllowed")) {
                String ALLOWED_TABLE = "CREATE TABLE `plotmeAllowed` ("
                                       + "`idX` INTEGER,"
                                       + "`idZ` INTEGER,"
                                       + "`world` varchar(32) NOT NULL,"
                                       + "`player` varchar(32) NOT NULL,"
                                       + "`playerid` blob(16),"
                                       + "PRIMARY KEY (idX, idZ, world, player) "
                                       + ");";
                st.executeUpdate(ALLOWED_TABLE);
                conn.commit();
            }

            if (!tableExists("plotmeDenied")) {
                String DENIED_TABLE = "CREATE TABLE `plotmeDenied` ("
                                      + "`idX` INTEGER,"
                                      + "`idZ` INTEGER,"
                                      + "`world` varchar(32) NOT NULL,"
                                      + "`player` varchar(32) NOT NULL,"
                                      + "`playerid` blob(16),"
                                      + "PRIMARY KEY (idX, idZ, world, player) "
                                      + ");";
                st.executeUpdate(DENIED_TABLE);
                conn.commit();
            }

            UpdateTables();

            if (isUsingMySQL()) {
                plugin.getLogger().info("Modifying database for MySQL support");

                String sqlitedb = "plots.db";
                File sqlitefile = new File(plugin.getServerBridge().getDataFolder(), sqlitedb);
                if (sqlitefile.exists()) {
                    plugin.getLogger().info("Trying to import plots from plots.db");
                    Class.forName(SQLITE_DRIVER);
                    Connection sqliteconn = DriverManager.getConnection("jdbc:sqlite:" + sqlitefile.getPath());

                    sqliteconn.setAutoCommit(false);
                    Statement slstatement = sqliteconn.createStatement();
                    ResultSet setPlots = slstatement.executeQuery("SELECT * FROM plotmePlots");
                    Statement slAllowed = sqliteconn.createStatement();
                    ResultSet setAllowed = null;
                    Statement slDenied = sqliteconn.createStatement();
                    ResultSet setDenied = null;

                    short size = 0;
                    while (setPlots.next()) {
                        int idX = setPlots.getInt("idX");
                        int idZ = setPlots.getInt("idZ");
                        String owner = setPlots.getString("owner");
                        String world = setPlots.getString("world");
                        int topX = setPlots.getInt("topX");
                        int bottomX = setPlots.getInt("bottomX");
                        int topZ = setPlots.getInt("topZ");
                        int bottomZ = setPlots.getInt("bottomZ");
                        String biome = setPlots.getString("biome");
                        Date expireddate = null;
                        try {
                            expireddate = setPlots.getDate("expireddate");
                        } catch (SQLException ignored) {
                        }
                        boolean finished = setPlots.getBoolean("finished");
                        PlayerList allowed = new PlayerList();
                        PlayerList denied = new PlayerList();
                        double customprice = setPlots.getDouble("customprice");
                        boolean forsale = setPlots.getBoolean("forsale");
                        String finisheddate = setPlots.getString("finisheddate");
                        boolean protect = setPlots.getBoolean("protected");
                        boolean auctionned = setPlots.getBoolean("auctionned");
                        String currentbidder = setPlots.getString("currentbidder");
                        double currentbid = setPlots.getDouble("currentbid");

                        byte[] byOwner = setPlots.getBytes("ownerId");
                        byte[] byBidder = setPlots.getBytes("currentbidderid");
                        UUID ownerId = null;

                        if (byOwner != null) {
                            ownerId = UUIDFetcher.fromBytes(byOwner);
                        }
                        UUID currentbidderid = null;
                        if (byBidder != null) {
                            currentbidderid = UUIDFetcher.fromBytes(byBidder);
                        }

                        setAllowed =
                                slAllowed.executeQuery(
                                        "SELECT * FROM plotmeAllowed WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND world = '" + world + "'");

                        while (setAllowed.next()) {
                            byte[] byPlayerId = setAllowed.getBytes("playerid");
                            if (byPlayerId == null) {
                                allowed.put(setAllowed.getString("player"));
                            } else {
                                allowed.put(setAllowed.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                            }
                        }

                        setAllowed.close();
                        setDenied =
                                slDenied.executeQuery(
                                        "SELECT * FROM plotmeDenied WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND world = '" + world + "'");

                        while (setDenied.next()) {
                            byte[] byPlayerId = setDenied.getBytes("playerid");
                            if (byPlayerId == null) {
                                denied.put(setDenied.getString("player"));
                            } else {
                                denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                            }
                        }

                        setDenied.close();

                        Plot plot = new Plot(plugin, owner, ownerId, world, biome,
                                             expireddate, finished, allowed, idX + ";" + idZ, customprice,
                                             forsale, finisheddate, protect, currentbidder, currentbidderid, currentbid,
                                             auctionned, denied);
                        addPlot(plot, idX, idZ, topX, bottomX, topZ, bottomZ);

                        size++;
                    }

                    plugin.getLogger().info("Imported " + size + " plots from " + sqlitedb);
                    slstatement.close();
                    if (slAllowed != null) {
                        slAllowed.close();
                    }
                    if (slDenied != null) {
                        slDenied.close();
                    }
                    setPlots.close();
                    if (setDenied != null) {
                        setDenied.close();
                    }
                    if (setAllowed != null) {
                        setAllowed.close();
                    }
                    sqliteconn.close();

                    plugin.getLogger().info("Renaming " + sqlitedb + " to " + sqlitedb + ".old");
                    if (!sqlitefile.renameTo(new File(plugin.getServerBridge().getDataFolder(), sqlitedb + ".old"))) {
                        plugin.getLogger().severe("Failed to rename " + sqlitedb + "! Please rename this manually!");
                    }
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Create Table Exception :");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().severe("Class Not Found Exception :");
            ex.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Could not create the table (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void addPlot(Plot plot, int idX, int idZ, IWorld world) {
        PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
        
        addPlot(plot, idX, idZ,
                manager.topX(plot.getId(), world),
                manager.bottomX(plot.getId(), world),
                manager.topZ(plot.getId(), world),
                manager.bottomZ(plot.getId(), world));
    }

    public void addPlot(Plot plot, int idX, int idZ, int topX, int bottomX, int topZ, int bottomZ) {
        PreparedStatement ps = null;
        StringBuilder strSql = new StringBuilder();

        // Plots
        try {
            Connection conn = getConnection();

            strSql.append("INSERT INTO plotmePlots (idX, idZ, owner, world, topX, bottomX, topZ, bottomZ, ");
            strSql.append("biome, expireddate, finished, customprice, forsale, finisheddate, protected,");
            strSql.append("auctionned, currentbid, currentbidder, currentbidderId, ownerId) ");
            strSql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps = conn.prepareStatement(strSql.toString());
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, plot.getOwner());
            ps.setString(4, plot.getWorld().toLowerCase());

            ps.setInt(5, topX);
            ps.setInt(6, bottomX);
            //noinspection SuspiciousNameCombination
            ps.setInt(7, topZ);
            //noinspection SuspiciousNameCombination
            ps.setInt(8, bottomZ);
            ps.setString(9, ((BukkitBiome) plot.getBiome()).getBiome().name());
            ps.setDate(10, plot.getExpiredDate());
            ps.setBoolean(11, plot.isFinished());
            ps.setDouble(12, plot.getCustomPrice());
            ps.setBoolean(13, plot.isForSale());
            ps.setString(14, plot.getFinishedDate());
            ps.setBoolean(15, plot.isProtect());
            ps.setBoolean(16, plot.isAuctioned());
            ps.setDouble(17, plot.getCurrentBid());
            ps.setString(18, plot.getCurrentBidder());
            if (plot.getCurrentBidderId() != null) {
                ps.setBytes(19, UUIDFetcher.toBytes(plot.getCurrentBidderId()));
            } else {
                ps.setBytes(19, null);
            }
            if (plot.getOwnerId() != null) {
                ps.setBytes(20, UUIDFetcher.toBytes(plot.getOwnerId()));
            } else {
                ps.setBytes(20, null);
            }

            ps.executeUpdate();
            conn.commit();

            if (plot.allowed() != null && plot.allowed().getAllPlayers() != null) {
                HashMap<String, UUID> allowed = plot.allowed().getAllPlayers();
                for (String key : allowed.keySet()) {
                    addPlotAllowed(key, allowed.get(key), idX, idZ, plot.getWorld());
                }
            }

            if (plot.denied() != null && plot.denied().getAllPlayers() != null) {
                HashMap<String, UUID> denied = plot.denied().getAllPlayers();
                for (String key : denied.keySet()) {
                    addPlotDenied(key, denied.get(key), idX, idZ, plot.getWorld());
                }
            }

            if (plot.getOwner() != null && !plot.getOwner().isEmpty() && plot.getOwnerId() == null) {
                fetchUUIDAsync(idX, idZ, plot.getWorld().toLowerCase(), "owner", plot.getOwner());
            }

            if (plot.getCurrentBidder() != null && plot.getCurrentBidderId() == null) {
                fetchUUIDAsync(idX, idZ, plot.getWorld().toLowerCase(), "bidder", plot.getCurrentBidder());
            }

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void updatePlot(int idX, int idZ, String world, String field, Object value) {
        PreparedStatement ps = null;

        //Plots
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("UPDATE plotmePlots SET " + field + " = ? " + "WHERE idX = ? AND idZ = ? AND world = ?");

            ps.setObject(1, value);
            ps.setInt(2, idX);
            ps.setInt(3, idZ);
            ps.setString(4, world.toLowerCase());

            ps.executeUpdate();
            conn.commit();

            if ("owner".equalsIgnoreCase(field)) {
                fetchUUIDAsync(idX, idZ, world, "owner", value.toString());
            } else if ("currentbidder".equalsIgnoreCase(field)) {
                if (value != null) {
                    fetchUUIDAsync(idX, idZ, world, "bidder", value.toString());
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void addPlotAllowed(String player, UUID uuid, int idX, int idZ, String world) {
        PreparedStatement ps = null;

        //Allowed
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO plotmeAllowed (idX, idZ, player, world, playerid) VALUES (?,?,?,?,?)");

            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, player);
            ps.setString(4, world.toLowerCase());
            if (uuid != null) {
                ps.setBytes(5, UUIDFetcher.toBytes(uuid));
            } else {
                ps.setBytes(5, null);
                fetchUUIDAsync(idX, idZ, world, "allowed", player);
            }

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void addPlotDenied(String player, UUID playerid, int idX, int idZ, String world) {
        PreparedStatement ps = null;

        //Denied
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO plotmeDenied (idX, idZ, player, world, playerid) VALUES (?,?,?,?,?)");

            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, player);
            ps.setString(4, world.toLowerCase());
            if (playerid != null) {
                ps.setBytes(5, UUIDFetcher.toBytes(playerid));
            } else {
                ps.setBytes(5, null);
                fetchUUIDAsync(idX, idZ, world, "denied", player);
            }

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void deletePlot(int idX, int idZ, String world) {
        PreparedStatement ps = null;
        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world.toLowerCase());
            ps.executeUpdate();
            ps.close();
            conn.commit();

            ps = conn.prepareStatement("DELETE FROM plotmeDenied WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world.toLowerCase());
            ps.executeUpdate();
            ps.close();
            conn.commit();

            ps = conn.prepareStatement("DELETE FROM plotmePlots WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world.toLowerCase());
            ps.executeUpdate();
            ps.close();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Delete Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void deletePlotAllowed(int idX, int idZ, String player, UUID playerid, String world) {
        PreparedStatement ps = null;

        try {
            Connection conn = getConnection();

            if (playerid == null) {
                ps = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE idX = ? and idZ = ? and player = ? and LOWER(world) = ?");
                ps.setString(3, player);
            } else {
                ps = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE idX = ? and idZ = ? and playerid = ? and LOWER(world) = ?");
                ps.setBytes(3, UUIDFetcher.toBytes(playerid));
            }
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(4, world);
            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Delete Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void deletePlotDenied(int idX, int idZ, String player, UUID playerid, String world) {
        PreparedStatement ps = null;

        try {
            Connection conn = getConnection();

            if (playerid == null) {
                ps = conn.prepareStatement("DELETE FROM plotmeDenied WHERE idX = ? and idZ = ? and player = ? and LOWER(world) = ?");
                ps.setString(3, player);
            } else {
                ps = conn.prepareStatement("DELETE FROM plotmeDenied WHERE idX = ? and idZ = ? and playerid = ? and LOWER(world) = ?");
                ps.setBytes(3, UUIDFetcher.toBytes(playerid));
            }
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(4, world);
            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Delete Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public Plot getPlot(String world, String id) {
        Plot plot = null;
        PreparedStatement statementPlot = null;
        PreparedStatement statementAllowed = null;
        PreparedStatement statementDenied = null;
        ResultSet setPlots = null;
        ResultSet setAllowed = null;
        ResultSet setDenied = null;

        int idX = PlotMeCoreManager.getInstance().getIdX(id);
        int idZ = PlotMeCoreManager.getInstance().getIdZ(id);

        try {
            Connection conn = getConnection();

            statementPlot = conn.prepareStatement("SELECT * FROM plotmePlots WHERE LOWER(world) = ? AND idX = ? and idZ = ?");
            statementPlot.setString(1, world);
            statementPlot.setInt(2, idX);
            statementPlot.setInt(3, idZ);

            setPlots = statementPlot.executeQuery();

            if (setPlots.next()) {
                String owner = setPlots.getString("owner");
                String biome = setPlots.getString("biome");
                Date expiredDate = null;
                try {
                    expiredDate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                double customPrice = setPlots.getDouble("customprice");
                boolean forSale = setPlots.getBoolean("forsale");
                String finishedDate;
                if (finished) {
                    finishedDate = setPlots.getString("finisheddate");
                } else {
                    finishedDate = null;
                }
                boolean protect = setPlots.getBoolean("protected");
                String currentBidder = setPlots.getString("currentbidder");
                double currentBid = setPlots.getDouble("currentbid");
                boolean auctioned = setPlots.getBoolean("auctionned");

                byte[] byOwner = setPlots.getBytes("ownerId");
                byte[] byBidder = setPlots.getBytes("currentbidderid");

                UUID ownerId = null;

                if (byOwner != null) {
                    ownerId = UUIDFetcher.fromBytes(byOwner);
                }
                UUID currentBidderId = null;
                if (byBidder != null) {
                    currentBidderId = UUIDFetcher.fromBytes(byBidder);
                }

                statementAllowed = conn.prepareStatement("SELECT * FROM plotmeAllowed WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementAllowed.setString(1, world);
                statementAllowed.setInt(2, idX);
                statementAllowed.setInt(3, idZ);

                setAllowed = statementAllowed.executeQuery();

                while (setAllowed.next()) {
                    byte[] byPlayerId = setAllowed.getBytes("playerid");
                    if (byPlayerId == null) {
                        allowed.put(setAllowed.getString("player"));
                    } else {
                        allowed.put(setAllowed.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                setAllowed.close();

                statementDenied = conn.prepareStatement("SELECT * FROM plotmeDenied WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementDenied.setString(1, world);
                statementDenied.setInt(2, idX);
                statementDenied.setInt(3, idZ);

                setDenied = statementDenied.executeQuery();

                while (setDenied.next()) {
                    byte[] byPlayerId = setDenied.getBytes("playerid");
                    if (byPlayerId == null) {
                        denied.put(setDenied.getString("player"));
                    } else {
                        denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }
                setDenied.close();

                plot = new Plot(plugin, owner, ownerId, world, biome, expiredDate, finished, allowed,
                                idX + ";" + idZ, customPrice, forSale, finishedDate, protect,
                                currentBidder, currentBidderId, currentBid, auctioned, denied);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Plot load Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (statementAllowed != null) {
                    statementAllowed.close();
                }
                if (statementDenied != null) {
                    statementDenied.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
                if (setDenied != null) {
                    setDenied.close();
                }
                if (setAllowed != null) {
                    setAllowed.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Plot load Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return plot;
    }

    public void loadPlotsAsynchronously(String world) {
        final String worldName = world;

        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Starting to load plots for world " + worldName);

                HashMap<String, Plot> plots = getPlots(worldName);

                PlotMapInfo pmi = PlotMeCoreManager.getInstance().getMap(worldName);

                for (String id : plots.keySet()) {
                    pmi.addPlot(id, plots.get(id));
                    plugin.getServerBridge().getEventFactory()
                            .callPlotLoadedEvent(plugin, plugin.getServerBridge().getWorld(worldName), plots.get(id));
                }

                // plugin.getLogger().info("Done loading " + pmi.getNbPlots() +
                // " plots for world " + worldname);
                plugin.getServerBridge().getEventFactory().callPlotWorldLoadEvent(worldName, pmi.getNbPlots());
            }
        });
    }

    //Do NOT call from the main thread
    public HashMap<String, Plot> getPlots(String world) {
        HashMap<String, Plot> ret = new HashMap<>();
        Statement statementPlot = null;
        Statement statementAllowed = null;
        Statement statementDenied = null;
        ResultSet setPlots = null;
        ResultSet setAllowed = null;
        ResultSet setDenied = null;

        try {
            Connection conn = getConnection();

            statementPlot = conn.createStatement();
            setPlots = statementPlot.executeQuery("SELECT * FROM plotmePlots WHERE LOWER(world) = '" + world + "'");

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String owner = setPlots.getString("owner");
                String biome = setPlots.getString("biome");
                Date expiredDate = null;
                try {
                    expiredDate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                double customPrice = setPlots.getDouble("customprice");
                boolean forSale = setPlots.getBoolean("forsale");
                String finishedDate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                String currentBidder = setPlots.getString("currentbidder");
                double currentBid = setPlots.getDouble("currentbid");
                boolean auctioned = setPlots.getBoolean("auctionned");

                byte[] byOwner = setPlots.getBytes("ownerId");
                byte[] byBidder = setPlots.getBytes("currentbidderid");

                UUID ownerId = null;

                if (byOwner != null) {
                    ownerId = UUIDFetcher.fromBytes(byOwner);
                }
                UUID currentBidderId = null;
                if (byBidder != null) {
                    currentBidderId = UUIDFetcher.fromBytes(byBidder);
                }

                statementAllowed = conn.createStatement();
                setAllowed =
                        statementAllowed.executeQuery(
                                "SELECT * FROM plotmeAllowed WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");

                while (setAllowed.next()) {
                    byte[] byPlayerId = setAllowed.getBytes("playerid");
                    if (byPlayerId == null) {
                        allowed.put(setAllowed.getString("player"));
                    } else {
                        allowed.put(setAllowed.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                setAllowed.close();

                statementDenied = conn.createStatement();
                setDenied =
                        statementDenied.executeQuery(
                                "SELECT * FROM plotmeDenied WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");

                while (setDenied.next()) {
                    byte[] byPlayerId = setDenied.getBytes("playerid");
                    if (byPlayerId == null) {
                        denied.put(setDenied.getString("player"));
                    } else {
                        denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                setDenied.close();

                Plot plot = new Plot(plugin, owner, ownerId, world, biome, expiredDate, finished, allowed, idX + ";" + idZ,
                                     customPrice, forSale, finishedDate, protect, currentBidder, currentBidderId, currentBid, auctioned, denied);
                ret.put(idX + ";" + idZ, plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Load Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (statementAllowed != null) {
                    statementAllowed.close();
                }
                if (statementDenied != null) {
                    statementDenied.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
                if (setDenied != null) {
                    setDenied.close();
                }
                if (setAllowed != null) {
                    setAllowed.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Load Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return ret;
    }

    /**
     * Get the number of plots in the world
     * @param world plotworld to check
     * @return number of plots in the world
     */
    public short getPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        short nbplots = 0;

        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ?");
            ps.setString(1, world);

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getShort(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("PlotCount Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (setNbPlots != null) {
                    setNbPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("PlotCount Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return nbplots;
    }

    public short getPlotCount(String world, UUID ownerId, String owner) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        short nbplots = 0;

        try {
            Connection conn = getConnection();

            if (ownerId == null) {
                ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND owner = ?");
                ps.setString(1, world.toLowerCase());
                ps.setString(2, owner);
            } else {
                ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND ownerId = ?");
                ps.setString(1, world.toLowerCase());
                ps.setBytes(2, UUIDFetcher.toBytes(ownerId));
            }

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getShort(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("PlotCount Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (setNbPlots != null) {
                    setNbPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("PlotCount Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return nbplots;
    }

    public short getFinishedPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        short nbplots = 0;

        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND finished <> 0");
            ps.setString(1, world);

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getShort(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("FinishedPlotCount Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (setNbPlots != null) {
                    setNbPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("FinishedPlotCount Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return nbplots;
    }

    public short getExpiredPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        short nbplots = 0;

        try {
            Connection conn = getConnection();

            Calendar cal = Calendar.getInstance();
            java.util.Date time = cal.getTime();
            Date date = new Date(time.getTime());

            ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND expireddate < ?");
            ps.setString(1, world);
            ps.setDate(2, date);

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getShort(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("ExpiredPlotCount Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (setNbPlots != null) {
                    setNbPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("ExpiredPlotCount Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return nbplots;
    }

    public List<Plot> getDonePlots(String world, int page, int nbPerPage) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            statementPlot =
                    conn.prepareStatement(
                            "SELECT idX, idZ, owner, finisheddate FROM plotmePlots WHERE LOWER(world) = ? AND finished <> 0 ORDER BY finisheddate LIMIT ?, ?");
            statementPlot.setString(1, world);
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(2, nbPerPage * (page - 1));
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(3, nbPerPage);

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String owner = setPlots.getString("owner");

                String finisheddate = setPlots.getString("finisheddate");

                Plot plot = new Plot(plugin);
                plot.setOwner(owner);
                plot.setId(idX + ";" + idZ);
                plot.setFinishedDate(finisheddate);

                ret.add(plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("DonePlots Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("DonePlots Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return ret;
    }

    public List<Plot> getExpiredPlots(String world, int page, int nbPerPage) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            Calendar cal = Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            Date sqlDate = new Date(utilDate.getTime());

            statementPlot =
                    conn.prepareStatement(
                            "SELECT idX, idZ, owner, expireddate FROM plotmePlots WHERE LOWER(world) = ? AND protected = 0 AND expireddate < ? ORDER BY expireddate LIMIT ?, ?");
            statementPlot.setString(1, world);
            statementPlot.setDate(2, sqlDate);
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(3, nbPerPage * (page - 1));
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(4, nbPerPage);

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String owner = setPlots.getString("owner");

                Date expireddate = null;
                try {
                    expireddate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }

                Plot plot = new Plot(plugin);
                plot.setOwner(owner);
                plot.setId(idX + ";" + idZ);
                plot.setExpiredDate(expireddate);

                ret.add(plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("ExpiredPlots Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("ExpiredPlots Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return ret;
    }


    public Plot getExpiredPlot(String world) {
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            Calendar cal = Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            Date sqlDate = new Date(utilDate.getTime());

            statementPlot =
                    conn.prepareStatement(
                            "SELECT idX, idZ, owner, expireddate FROM plotmePlots WHERE LOWER(world) = ? AND protected = 0 AND expireddate < ? ORDER BY expireddate LIMIT 1");
            statementPlot.setString(1, world);
            statementPlot.setDate(2, sqlDate);

            setPlots = statementPlot.executeQuery();

            if (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String owner = setPlots.getString("owner");

                Date expireddate = null;
                try {
                    expireddate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }

                Plot plot = new Plot(plugin);
                plot.setOwner(owner);
                plot.setId(idX + ";" + idZ);
                plot.setExpiredDate(expireddate);

                return plot;
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("ExpiredPlots Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("ExpiredPlots Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return null;
    }

    public List<Plot> getAllPlots() {
        List<Plot> ret = new ArrayList<>();
        Connection connection = getConnection();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT world, idX, idZ FROM plotmePlots");

            while (rs.next()) {
                String world = rs.getString("world");
                int idX = rs.getInt("idX");
                int idZ = rs.getInt("idZ");

                ret.add(getPlot(world, idX + ";" + idZ));
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /**
     * Get plots the player owns or is allowed
     *
     * @param playername
     * @param playerId
     * @return
     */
    public List<Plot> getPlayerPlots(String playername, UUID playerId) {
        return getPlayerPlots(playername, playerId, "", false);
    }
    
    /**
     * Get plots the player owns
     *
     * @param playername
     * @param playerId
     * @param world
     *
     * @return
     */
    public List<Plot> getOwnedPlots(String world, UUID playerId, String playername) {
        return getPlayerPlots(playername, playerId, world, true);
    }
    
    /**
     * Get plots where the player is allowed or owns.
     *
     * @param playername
     * @param playerId
     * @param world
     * @param ownedonly Only get the plots the player owns
     *
     * @return
     */
    private List<Plot> getPlayerPlots(String playername, UUID playerId, String world, boolean ownedonly) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        PreparedStatement statementAllowed = null;
        PreparedStatement statementDenied = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            String query = "SELECT DISTINCT A.*";
            if (ownedonly) {
                query += "FROM plotmePlots A ";
            } else {
                query += "FROM plotmePlots A LEFT JOIN plotmeAllowed B ON A.idX = B.idX AND A.idZ = B.idZ AND A.world = B.world ";
            }
            
            query += "WHERE ";
            
            if (playerId == null) {
                if (ownedonly) {
                    query += "A.owner = ? ";
                } else {
                    query += "(A.owner = ? OR B.player = ?) ";
                }
            } else {
                if (ownedonly) {
                    query += "A.ownerId = ? ";
                } else {
                    query += "(A.ownerId = ? OR B.playerId = ?) ";
                }
            }
            
            if (!world.isEmpty()) {
                query += "AND world = ?";
            }
            
            statementPlot = conn.prepareStatement(query);
            
            if (playerId == null) {
                statementPlot.setString(1, playername);
                if (!ownedonly) {
                    statementPlot.setString(2, playername);
                    if (!world.isEmpty()) {
                        statementPlot.setString(3, world);
                    }
                } else if (!world.isEmpty()) {
                    statementPlot.setString(2, world);
                }
            } else {
                statementPlot.setBytes(1, UUIDFetcher.toBytes(playerId));
                if (!ownedonly) {
                    statementPlot.setBytes(2, UUIDFetcher.toBytes(playerId));
                    if (!world.isEmpty()) {
                        statementPlot.setString(3, world);
                    }
                } else if (!world.isEmpty()) {
                    statementPlot.setString(2, world);
                }
            }

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String biome = setPlots.getString("biome");
                Date expireddate = null;
                try {
                    expireddate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                double customprice = setPlots.getDouble("customprice");
                boolean forsale = setPlots.getBoolean("forsale");
                String finisheddate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                String currentbidder = setPlots.getString("currentbidder");
                double currentbid = setPlots.getDouble("currentbid");
                boolean auctionned = setPlots.getBoolean("auctionned");
                String currworld = setPlots.getString("world");
                String owner = setPlots.getString("owner");

                byte[] byBidder = setPlots.getBytes("currentbidderid");
                byte[] byOwner = setPlots.getBytes("ownerid");

                UUID currentbidderid = null;

                if (byBidder != null) {
                    currentbidderid = UUIDFetcher.fromBytes(byBidder);
                }

                UUID ownerId = null;
                if (byOwner != null) {
                    ownerId = UUIDFetcher.fromBytes(byOwner);
                }

                statementAllowed = conn.prepareStatement("SELECT * FROM plotmeAllowed WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementAllowed.setString(1, currworld);
                statementAllowed.setInt(2, idX);
                statementAllowed.setInt(3, idZ);

                ResultSet setAllowed = statementAllowed.executeQuery();

                while (setAllowed.next()) {
                    byte[] byPlayerId = setAllowed.getBytes("playerid");
                    if (byPlayerId == null) {
                        allowed.put(setAllowed.getString("player"));
                    } else {
                        allowed.put(setAllowed.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                setAllowed.close();

                statementDenied = conn.prepareStatement("SELECT * FROM plotmeDenied WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementDenied.setString(1, currworld);
                statementDenied.setInt(2, idX);
                statementDenied.setInt(3, idZ);

                ResultSet setDenied = statementDenied.executeQuery();

                while (setDenied.next()) {
                    byte[] byPlayerId = setDenied.getBytes("playerid");
                    if (byPlayerId == null) {
                        denied.put(setDenied.getString("player"));
                    } else {
                        denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                setDenied.close();
                
                Plot plot = new Plot(plugin, owner, ownerId, currworld, biome, expireddate, finished, allowed,
                                     idX + ";" + idZ, customprice, forsale, finisheddate, protect,
                                     currentbidder, currentbidderid, currentbid, auctionned, denied);

                ret.add(plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("getPlayerPlots Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (statementAllowed != null) {
                    statementAllowed.close();
                }
                if (statementDenied != null) {
                    statementDenied.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("getPlayerPlots Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return ret;
    }

    public void plotConvertToUUIDAsynchronously() {
        plugin.getServerBridge().runTaskLaterAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Checking if conversion to UUID needed...");
                Statement statementPlayers = null;
                PreparedStatement psOwnerId = null;
                PreparedStatement psCurrentBidderId = null;
                PreparedStatement psAllowedPlayerId = null;
                PreparedStatement psDeniedPlayerId = null;

                PreparedStatement psDeleteOwner = null;
                PreparedStatement psDeleteCurrentBidder = null;
                PreparedStatement psDeleteAllowed = null;
                PreparedStatement psDeleteDenied = null;

                ResultSet setPlayers = null;

                try {
                    Connection conn = getConnection();

                    // Get all the players
                    statementPlayers = conn.createStatement();
                    // Exclude groups and names with * or missing
                    String
                            sql =
                            "SELECT LOWER(owner) as Name FROM plotmePlots WHERE NOT owner IS NULL AND Not owner LIKE 'group:%' AND Not owner LIKE '%*%' AND ownerid IS NULL GROUP BY LOWER(owner) ";
                    sql +=
                            "UNION SELECT LOWER(currentbidder) as Name FROM plotmePlots WHERE NOT currentbidder IS NULL AND currentbidderid IS NULL GROUP BY LOWER(currentbidder) ";
                    sql +=
                            "UNION SELECT LOWER(player) as Name FROM plotmeAllowed WHERE NOT player IS NULL AND Not player LIKE 'group:%' AND Not player LIKE '%*%' AND playerid IS NULL GROUP BY LOWER(player) ";
                    sql +=
                            "UNION SELECT LOWER(player) as Name FROM plotmeDenied WHERE NOT player IS NULL AND Not player LIKE 'group:%' AND Not player LIKE '%*%' AND playerid IS NULL GROUP BY LOWER(player) ";

                    setPlayers = statementPlayers.executeQuery(sql);
                    boolean boConversion = false;
                    if (setPlayers.next()) {
                        List<String> names = new ArrayList<>();

                        //Prepare delete statements
                        psDeleteOwner = conn.prepareStatement("UPDATE plotmePlots SET owner = '' WHERE owner = ? ");
                        psDeleteCurrentBidder = conn.prepareStatement("UPDATE plotmePlots SET currentbidder = null WHERE currentbidder = ? ");
                        psDeleteAllowed = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE player = ? ");
                        psDeleteDenied = conn.prepareStatement("DELETE FROM plotmeDenied WHERE player = ? ");

                        do {
                            String name = setPlayers.getString("Name");
                            if (!name.isEmpty()) {
                                if (COMPILE.matcher(name).matches()) {
                                    names.add(name);
                                } else {
                                    plugin.getLogger().warning("Invalid name found : " + name);
                                    plugin.getLogger().warning("Removing from database.");
                                    psDeleteOwner.setString(1, name);
                                    psDeleteOwner.executeUpdate();
                                    psDeleteCurrentBidder.setString(1, name);
                                    psDeleteCurrentBidder.executeUpdate();
                                    psDeleteAllowed.setString(1, name);
                                    psDeleteAllowed.executeUpdate();
                                    psDeleteDenied.setString(1, name);
                                    psDeleteDenied.executeUpdate();
                                    conn.commit();
                                }
                            }
                        } while (setPlayers.next());

                        psDeleteOwner.close();
                        psDeleteCurrentBidder.close();
                        psDeleteAllowed.close();
                        psDeleteDenied.close();

                        if (!names.isEmpty()) {
                            UUIDFetcher fetcher = new UUIDFetcher(names);

                            Map<String, UUID> response = null;

                            try {
                                plugin.getLogger().info("Fetching " + names.size() + " UUIDs from Mojang servers...");
                                response = fetcher.call();
                            } catch (Exception e) {
                                plugin.getLogger().warning("Exception while running UUIDFetcher");
                                e.printStackTrace();
                            }

                            if (!response.isEmpty()) {
                                plugin.getLogger().info("Finished fetching " + response.size() + " UUIDs. Starting database update.");
                                psOwnerId = conn.prepareStatement("UPDATE plotmePlots SET ownerid = ? WHERE LOWER(owner) = ? AND ownerid IS NULL");
                                psCurrentBidderId = conn.prepareStatement(
                                        "UPDATE plotmePlots SET currentbidderid = ? WHERE LOWER(currentbidder) = ? AND currentbidderid IS NULL");
                                psAllowedPlayerId =
                                        conn.prepareStatement("UPDATE plotmeAllowed SET playerid = ? WHERE LOWER(player) = ? AND playerid IS NULL");
                                psDeniedPlayerId =
                                        conn.prepareStatement("UPDATE plotmeDenied SET playerid = ? WHERE LOWER(player) = ? AND playerid IS NULL");

                                int nbConverted = 0;
                                for (String key : response.keySet()) {
                                    // Owner
                                    psOwnerId.setBytes(1, UUIDFetcher.toBytes(response.get(key)));
                                    psOwnerId.setString(2, key.toLowerCase());
                                    int count = 0;
                                    count += psOwnerId.executeUpdate();
                                    // Bidder
                                    psCurrentBidderId.setBytes(1, UUIDFetcher.toBytes(response.get(key)));
                                    psCurrentBidderId.setString(2, key.toLowerCase());
                                    count += psCurrentBidderId.executeUpdate();
                                    // Allowed
                                    psAllowedPlayerId.setBytes(1, UUIDFetcher.toBytes(response.get(key)));
                                    psAllowedPlayerId.setString(2, key.toLowerCase());
                                    count += psAllowedPlayerId.executeUpdate();
                                    // Denied
                                    psDeniedPlayerId.setBytes(1, UUIDFetcher.toBytes(response.get(key)));
                                    psDeniedPlayerId.setString(2, key.toLowerCase());
                                    count += psDeniedPlayerId.executeUpdate();
                                    conn.commit();
                                    if (count > 0) {
                                        nbConverted++;
                                    } else {
                                        plugin.getLogger().warning("Unable to update player '" + key + "'");
                                    }
                                }

                                psOwnerId.close();
                                psCurrentBidderId.close();
                                psAllowedPlayerId.close();
                                psDeniedPlayerId.close();

                                //Update plot information
                                for (PlotMapInfo pmi : PlotMeCoreManager.getInstance().getPlotMaps().values()) {
                                    for (Plot plot : pmi.getLoadedPlots().values()) {
                                        for (Entry<String, UUID> player : response.entrySet()) {
                                            //Owner
                                            if (plot.getOwnerId() == null && plot.getOwner() != null && plot.getOwner()
                                                    .equalsIgnoreCase(player.getKey())) {
                                                plot.setOwner(player.getKey());
                                                plot.setOwnerId(player.getValue());
                                            }

                                            //Bidder
                                            if (plot.getCurrentBidderId() == null && plot.getCurrentBidder() != null && plot.getCurrentBidder()
                                                    .equalsIgnoreCase(player.getKey())) {
                                                plot.setCurrentBidder(player.getKey());
                                                plot.setCurrentBidderId(player.getValue());
                                            }

                                            //Allowed
                                            plot.allowed().replace(player.getKey(), player.getValue());

                                            //Denied
                                            plot.denied().replace(player.getKey(), player.getValue());
                                        }
                                    }
                                }

                                boConversion = true;
                                plugin.getLogger().info(nbConverted + " players converted");
                            }
                        }
                    }
                    setPlayers.close();
                    statementPlayers.close();

                    if (boConversion) {
                        plugin.getLogger().info("Plot conversion finished");
                    } else {
                        plugin.getLogger().info("No plot conversion needed");
                    }
                } catch (SQLException ex) {
                    plugin.getLogger().severe("Conversion to UUID failed :");
                    plugin.getLogger().severe(ex.getMessage());
                    for (StackTraceElement e : ex.getStackTrace()) {
                        plugin.getLogger().severe("  " + e);
                    }
                } finally {
                    try {
                        if (statementPlayers != null) {
                            statementPlayers.close();
                        }
                        if (psOwnerId != null) {
                            psOwnerId.close();
                        }
                        if (psCurrentBidderId != null) {
                            psCurrentBidderId.close();
                        }
                        if (psAllowedPlayerId != null) {
                            psAllowedPlayerId.close();
                        }
                        if (psDeniedPlayerId != null) {
                            psDeniedPlayerId.close();
                        }
                        if (setPlayers != null) {
                            setPlayers.close();
                        }
                        if (psDeleteOwner != null) {
                            psDeleteOwner.close();
                        }
                        if (psDeleteCurrentBidder != null) {
                            psDeleteCurrentBidder.close();
                        }
                        if (psDeleteAllowed != null) {
                            psDeleteAllowed.close();
                        }
                        if (psDeleteDenied != null) {
                            psDeleteDenied.close();
                        }
                    } catch (SQLException ex) {
                        plugin.getLogger().severe("Conversion to UUID failed (on close) :");
                        plugin.getLogger().severe(ex.getMessage());
                        for (StackTraceElement e : ex.getStackTrace()) {
                            plugin.getLogger().severe("  " + e);
                        }
                    }
                }
            }
        }, 20L);
    }

    private void fetchUUIDAsync(final int idX, final int idZ, final String world, final String property, final String name) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {

                PreparedStatement ps = null;

                try {
                    Connection conn = getConnection();

                    IPlayer player = null;
                    if (name != null) {
                        player = plugin.getServerBridge().getPlayerExact(name);
                    }
                    UUID uuid = null;
                    String newname = name;

                    if (player != null) {
                        uuid = player.getUniqueId();
                        newname = player.getName();
                    } else if (name != null) {
                        try {
                            uuid = UUIDFetcher.getUUIDOf(name);
                        } catch (Exception e) {
                            plugin.getLogger().severe("Failed to get UUID for the following name: " + name);
                            plugin.getLogger().severe("Either unable to connect to Mojang servers or a serious error occurred.");
                        }
                    }

                    switch (property) {
                        case "owner":
                            ps = conn.prepareStatement(
                                            "UPDATE plotmePlots SET ownerid = ?, owner = ? WHERE LOWER(owner) = ? AND idX = '" + idX + "' AND idZ = '"
                                            + idZ + "' AND LOWER(world) = '" + world + "'");
                            break;
                        case "bidder":
                            ps = conn.prepareStatement(
                                            "UPDATE plotmePlots SET currentbidderid = ?, currentbidder = ? WHERE LOWER(currentbidder) = ? AND idX = '"
                                            + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");
                            break;
                        case "allowed":
                            ps = conn.prepareStatement(
                                            "UPDATE plotmeAllowed SET playerid = ?, player = ? WHERE LOWER(player) = ? AND idX = '" + idX
                                            + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");
                            break;
                        case "denied":
                            ps = conn.prepareStatement("UPDATE plotmeDenied SET playerid = ?, player = ? WHERE LOWER(player) = ? AND idX = '" + idX
                                                          + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");
                            break;
                        default:
                            return;
                    }

                    if (uuid != null) {
                        ps.setBytes(1, UUIDFetcher.toBytes(uuid));
                    } else {
                        ps.setBytes(1, null);
                    }
                    ps.setString(2, newname);
                    if (name != null) {
                        ps.setString(3, name.toLowerCase());
                    } else {
                        ps.setString(3, null);
                    }
                    ps.executeUpdate();
                    conn.commit();

                    ps.close();

                    if (uuid != null) {
                        Plot plot = PlotMeCoreManager.getInstance().getPlotById(idX + ";" + idZ, world);

                        if (plot != null) {
                            switch (property) {
                                case "owner":
                                    plot.setOwner(newname);
                                    plot.setOwnerId(uuid);
                                    break;
                                case "bidder":
                                    plot.setCurrentBidder(newname);
                                    plot.setCurrentBidderId(uuid);
                                    break;
                                case "allowed":
                                    plot.allowed().remove(name);
                                    plot.allowed().put(newname, uuid);
                                    break;
                                case "denied":
                                    plot.denied().remove(name);
                                    plot.denied().put(newname, uuid);
                                    break;
                                default:
                            }
                        }
                    }
                } catch (SQLException ex) {
                    plugin.getLogger().severe("Conversion to UUID failed :");
                    plugin.getLogger().severe(ex.getMessage());
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException ex) {
                        plugin.getLogger().severe("Conversion to UUID failed (on close) :");
                        plugin.getLogger().severe(ex.getMessage());
                    }
                }
            }
        });
    }

    public boolean isUsingMySQL() {
        return plugin.getServerBridge().getConfig().getBoolean("usemySQL", false);
    }

    public void updatePlotsNewUUID(final UUID uuid, final String newname) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                PreparedStatement[] pss = new PreparedStatement[4];

                try {
                    Connection conn = getConnection();

                    pss[0] = conn.prepareStatement("UPDATE plotmePlots SET owner = ? WHERE ownerid = ?");
                    pss[1] = conn.prepareStatement("UPDATE plotmePlots SET currentbidder = ? WHERE currentbidderid = ?");
                    pss[2] = conn.prepareStatement("UPDATE plotmeAllowed SET player = ? WHERE playerid = ?");
                    pss[3] = conn.prepareStatement("UPDATE plotmeDenied SET player = ? WHERE playerid = ?");

                    for (PreparedStatement ps : pss) {
                        ps.setString(1, newname);
                        ps.setBytes(2, UUIDFetcher.toBytes(uuid));
                        ps.executeUpdate();
                    }

                    conn.commit();

                    for (PreparedStatement ps : pss) {
                        ps.close();
                    }

                } catch (SQLException ex) {
                    plugin.getLogger().severe("Update player in database from uuid failed :");
                    plugin.getLogger().severe(ex.getMessage());
                    for (StackTraceElement e : ex.getStackTrace()) {
                        plugin.getLogger().severe("  " + e);
                    }
                } finally {
                    try {
                        for (PreparedStatement ps : pss) {
                            if (ps != null) {
                                ps.close();
                            }
                        }
                    } catch (SQLException ex) {
                        plugin.getLogger().severe("Update player in database from uuid failed (on close) :");
                        plugin.getLogger().severe(ex.getMessage());
                        for (StackTraceElement e : ex.getStackTrace()) {
                            plugin.getLogger().severe("  " + e);
                        }
                    }
                }
            }
        });
    }
}
