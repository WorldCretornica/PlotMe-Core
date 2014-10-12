package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

public class SqlManager {

    private PlotMe_Core plugin;

    private final Boolean usemySQL;
    private final String mySQLuname;
    private final String mySQLpass;
    private final String mySQLconn;

    private static Connection conn;

    public SqlManager(PlotMe_Core instance, boolean usemysql, String sqlusername, String sqlpassword, String sqlconnection) {
        plugin = instance;
        this.mySQLconn = sqlconnection;
        this.mySQLpass = sqlpassword;
        this.mySQLuname = sqlusername;
        this.usemySQL = usemysql;
    }

    public Connection initialize() {
        try {
            if (usemySQL) {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(mySQLconn, mySQLuname, mySQLpass);
                conn.setAutoCommit(false);
            } else {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder() + "/plots.db");
                conn.setAutoCommit(false);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("SQL exception on initialize :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().severe("You need the SQLite/MySQL library. :");
            plugin.getLogger().severe("  " + ex.getMessage());
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

            String schema = getSchema();

            if (usemySQL) {
                /**
                 * * START Version 0.8 changes **
                 */
                //CustomPrice
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='customprice'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD customprice double NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();

                //ForSale
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='forsale'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD forsale boolean NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();

                //finisheddate
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='finisheddate'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD finisheddate varchar(16) NULL;");
                    conn.commit();
                }
                set.close();

                //Protected
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='protected'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD protected boolean NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();

                //Auctionned
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='auctionned'");
                if (!set.next()) {
                    statement.executeUpdate("ALTER TABLE plotmePlots ADD auctionned boolean NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();

                //Auctionenddate
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND (column_name='auctionenddate' OR column_name='auctionneddate')");
                if (!set.next()) {
                    statement.executeUpdate("ALTER TABLE plotmePlots ADD auctionenddate varchar(16) NULL;");
                    conn.commit();
                }
                set.close();

                //Currentbidder
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='currentbidder'");
                if (!set.next()) {
                    statement.executeUpdate("ALTER TABLE plotmePlots ADD currentbidder varchar(32) NULL;");
                    conn.commit();
                }
                set.close();

                //Currentbid
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='currentbid'");
                if (!set.next()) {
                    statement.executeUpdate("ALTER TABLE plotmePlots ADD currentbid double NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();

                /**
                 * * END Version 0.8 changes **
                 */
                
                /*** START Version 0.13d changes ***/

                // OwnerId
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND " + "TABLE_NAME='plotmePlots' AND column_name='ownerid'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD ownerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                // Allowed playerid
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND " + "TABLE_NAME='plotmeAllowed' AND column_name='playerid'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmeAllowed ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                // Denied playerid
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND " + "TABLE_NAME='plotmeDenied' AND column_name='playerid'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmeDenied ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                // Commenter playerid
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND " + "TABLE_NAME='plotmeComments' AND column_name='playerid'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmeComments ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                // CurrentBidderId
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND " + "TABLE_NAME='plotmePlots' AND column_name='currentbidderId'");
                if (!set.next()) {
                    statement.execute("ALTER TABLE plotmePlots ADD currentbidderId blob(16) NULL;");
                    conn.commit();
                }
                set.close();

                /*** END Version 0.13d changes ***/
                
                /**
                 * * START Version 0.14 changes **
                 */
                //Rename auctionneddate correctly
                set = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' AND "
                                                     + "TABLE_NAME='plotmePlots' AND column_name='auctionenddate'");
                if (set.next()) {
                    statement.execute("ALTER TABLE plotmePlots CHANGE auctionenddate auctionneddate varchar(16) NULL;");
                    conn.commit();
                }
                set.close();
                /**
                 * * END Version 0.14 changes **
                 */

            } else {

                /**
                 * * START Version 0.8 changes **
                 */
                //CustomPrice
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");
                String column;
                boolean found = false;
                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("customprice")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD customprice double NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();
                found = false;

                //ForSale
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("forsale")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD forsale boolean NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();
                found = false;

                //FinishedDate
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("finisheddate")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD finisheddate varchar(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                //Protected
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("protected")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD protected boolean NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();
                found = false;

                //Auctionned
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("auctionned")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD auctionned boolean NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();
                found = false;

                //Auctionenddate
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("auctionenddate") || column.equalsIgnoreCase("auctionneddate")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD auctionenddate varchar(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                //Currentbidder
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("currentbidder")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD currentbidder varchar(32) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                //Currentbid
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("currentbid")) {
                        found = true;
                    }
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD currentbid double NOT NULL DEFAULT '0';");
                    conn.commit();
                }
                set.close();
                found = false;
                /**
                 * * END Version 0.8 changes **
                 */

                /*** START Version 0.13d changes ***/

                // OwnerId
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("ownerid"))
                        found = true;
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
                    if (column.equalsIgnoreCase("playerid"))
                        found = true;
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
                    if (column.equalsIgnoreCase("playerid"))
                        found = true;
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmeDenied ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                // Commenter id
                set = statement.executeQuery("PRAGMA table_info(`plotmeComments`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("playerid"))
                        found = true;
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmeComments ADD playerid blob(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                // CurrentBidderId
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("currentbidderId"))
                        found = true;
                }

                if (!found) {
                    statement.execute("ALTER TABLE plotmePlots ADD currentbidderId blob(16) NULL;");
                    conn.commit();
                }
                set.close();
                found = false;

                /*** END Version 0.13d changes ***/
                
                /**
                 * * START Version 0.14 changes **
                 */
                //Rename auctionneddate correctly
                set = statement.executeQuery("PRAGMA table_info(`plotmePlots`)");

                while (set.next() && !found) {
                    column = set.getString(2);
                    if (column.equalsIgnoreCase("auctionenddate")) {
                        found = true;
                    }
                }

                if (found) {
                    //statement.execute("ALTER TABLE plotmePlots CHANGE auctionenddate auctionneddate varchar(16) NULL;"); <- doesn't work

                    Statement plotstatement = conn.createStatement();

                    ResultSet plotset = plotstatement.executeQuery("SELECT SQL FROM SQLITE_MASTER WHERE NAME = 'plotmePlots';");

                    if (plotset.next()) {
                        String createstring = plotset.getString(1);
                        createstring = createstring.replace("auctionenddate", "auctionneddate").replace("'", "''");

                        Statement changestatement = conn.createStatement();
                        changestatement.execute("PRAGMA writable_schema = 1");
                        changestatement.execute("UPDATE SQLITE_MASTER SET SQL = '" + createstring + "' WHERE NAME = 'plotmePlots'");
                        changestatement.execute("PRAGMA writable_schema = 0");
                        conn.commit();
                        changestatement.close();
                    }
                    plotstatement.close();
                    plotset.close();

                    conn.commit();
                }
                set.close();
                set = null;

                /**
                 * * END Version 0.14 changes **
                 */
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Update table exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public Connection getConnection() {
        if (conn == null) {
            conn = initialize();
        }
        if (usemySQL) {
            try {
                if (!conn.isValid(10)) {
                    conn = initialize();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Failed to check SQL status :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return conn;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                if (usemySQL) {
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
                plugin.getLogger().severe("  " + ex.getMessage());
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
            plugin.getLogger().severe("  " + ex.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Table Check SQL Exception (on closing) :");
                plugin.getLogger().severe("  " + ex.getMessage());
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
                                            + "`idX` INTEGER,"
                                            + "`idZ` INTEGER,"
                                            + "`owner` varchar(32) NOT NULL,"
                                            + "`world` varchar(32) NOT NULL DEFAULT '0',"
                                            + "`topX` INTEGER NOT NULL DEFAULT '0',"
                                            + "`bottomX` INTEGER NOT NULL DEFAULT '0',"
                                            + "`topZ` INTEGER NOT NULL DEFAULT '0',"
                                            + "`bottomZ` INTEGER NOT NULL DEFAULT '0',"
                                            + "`biome` varchar(32) NOT NULL DEFAULT '0',"
                                            + "`expireddate` DATETIME NULL,"
                                            + "`finished` boolean NOT NULL DEFAULT '0',"
                                            + "`customprice` double NOT NULL DEFAULT '0',"
                                            + "`forsale` boolean NOT NULL DEFAULT '0',"
                                            + "`finisheddate` varchar(16) NULL,"
                                            + "`protected` boolean NOT NULL DEFAULT '0',"
                                            + "`auctionned` boolean NOT NULL DEFAULT '0',"
                                            + "`auctionneddate` varchar(16) NULL,"
                                            + "`currentbid` double NOT NULL DEFAULT '0',"
                                            + "`currentbidder` varchar(32) NULL,"
                                            + "`currentbidderId` blob(16),"
                                            + "`ownerId` blob(16)," 
                                            + "PRIMARY KEY (idX, idZ, world) "
                                            + ");";
                st.executeUpdate(PLOT_TABLE);
                conn.commit();
            }

            if (!tableExists("plotmeComments")) {
                String COMMENT_TABLE = "CREATE TABLE `plotmeComments` ("
                                               + "`idX` INTEGER,"
                                               + "`idZ` INTEGER,"
                                               + "`world` varchar(32) NOT NULL,"
                                               + "`commentid` INTEGER,"
                                               + "`player` varchar(32) NOT NULL,"
                                               + "`comment` text,"
                                               + "`playerid` blob(16),"
                                               + "PRIMARY KEY (idX, idZ, world, commentid) "
                                               + ");";
                st.executeUpdate(COMMENT_TABLE);
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

            if (!tableExists("plotmeFreed")) {
                String FREED_TABLE = "CREATE TABLE `plotmeFreed` ("
                                             + "`idX` INTEGER,"
                                             + "`idZ` INTEGER,"
                                             + "`world` varchar(32) NOT NULL,"
                                             + "PRIMARY KEY (idX, idZ, world) "
                                             + ");";
                st.executeUpdate(FREED_TABLE);
                conn.commit();
            }

            UpdateTables();

            if (usemySQL) {
                plugin.getLogger().info("Modifying database for MySQL support");

                String sqlitedb = "plots.db";
                File sqlitefile = new File(plugin.getServerBridge().getDataFolder(), sqlitedb);
                if (!sqlitefile.exists()) {
                    //plotmecore.getLogger().info("Could not find old " + sqlitedb);
                } else {
                    plugin.getLogger().info("Trying to import plots from plots.db");
                    Class.forName("org.sqlite.JDBC");
                    Connection sqliteconn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder() + "\\" + sqlitedb);

                    sqliteconn.setAutoCommit(false);
                    Statement slstatement = sqliteconn.createStatement();
                    ResultSet setPlots = slstatement.executeQuery("SELECT * FROM plotmePlots");
                    Statement slAllowed = sqliteconn.createStatement();
                    ResultSet setAllowed = null;
                    Statement slDenied = sqliteconn.createStatement();
                    ResultSet setDenied = null;
                    Statement slComments = sqliteconn.createStatement();
                    ResultSet setComments = null;
                    Statement slFreed = sqliteconn.createStatement();

                    int size = 0;
                    while (setPlots.next()) {
                        int idX = setPlots.getInt("idX");
                        int idZ = setPlots.getInt("idZ");
                        String owner = setPlots.getString("owner");
                        String world = setPlots.getString("world").toLowerCase();
                        int topX = setPlots.getInt("topX");
                        int bottomX = setPlots.getInt("bottomX");
                        int topZ = setPlots.getInt("topZ");
                        int bottomZ = setPlots.getInt("bottomZ");
                        String biome = setPlots.getString("biome");
                        java.sql.Date expireddate = null;
                        try {
                            expireddate = setPlots.getDate("expireddate");
                        } catch (SQLException ignored) {
                        }
                        boolean finished = setPlots.getBoolean("finished");
                        PlayerList allowed = new PlayerList();
                        PlayerList denied = new PlayerList();
                        List<String[]> comments = new ArrayList<>();
                        double customprice = setPlots.getDouble("customprice");
                        boolean forsale = setPlots.getBoolean("forsale");
                        String finisheddate = setPlots.getString("finisheddate");
                        boolean protect = setPlots.getBoolean("protected");
                        boolean auctionned = setPlots.getBoolean("auctionned");
                        String currentbidder = setPlots.getString("currentbidder");
                        double currentbid = setPlots.getDouble("currentbid");
                        String auctionneddate;
                        try {
                            auctionneddate = setPlots.getString("auctionneddate");
                        } catch (SQLException e) {
                            auctionneddate = setPlots.getString("auctionenddate");
                        }
                        
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

                        setAllowed = slAllowed.executeQuery("SELECT * FROM plotmeAllowed WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND world = '" + world + "'");

                        while (setAllowed.next()) {
                            byte[] byPlayerId = setAllowed.getBytes("playerid");
                            if (byPlayerId == null) {
                                allowed.put(setAllowed.getString("player"));
                            } else {
                                allowed.put(setAllowed.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                            }
                        }

                        setAllowed.close();

                        setDenied = slDenied.executeQuery("SELECT * FROM plotmeDenied WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND world = '" + world + "'");

                        while (setDenied.next()) {
                            byte[] byPlayerId = setDenied.getBytes("playerid");
                            if (byPlayerId == null) {
                                denied.put(setDenied.getString("player"));
                            } else {
                                denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                            }
                        }

                        setDenied.close();

                        setComments = slComments.executeQuery("SELECT * FROM plotmeComments WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND world = '" + world + "'");

                        while (setComments.next()) {
                            String[] comment = new String[3];
                            
                            byte[] byPlayerId = setComments.getBytes("playerid");
                            if (byPlayerId != null) {
                                comment[2] = UUIDFetcher.fromBytes(byPlayerId).toString();
                            } else {
                                comment[2] = null;
                            }
                            
                            comment[0] = setComments.getString("player");
                            comment[1] = setComments.getString("comment");
                            comments.add(comment);
                        }

                        Plot plot = new Plot(plugin, owner, ownerId, world, biome, 
                                expireddate, finished, allowed, comments, "" + idX + ";" + idZ, customprice, 
                                forsale, finisheddate, protect, currentbidder, currentbidderid, currentbid, 
                                auctionned, denied, auctionneddate);
                        addPlot(plot, idX, idZ, topX, bottomX, topZ, bottomZ);

                        size++;
                    }

                    ResultSet setFreed = slstatement.executeQuery("SELECT * FROM plotmeFreed");
                    while (setFreed.next()) {
                        int idX = setPlots.getInt("idX");
                        int idZ = setPlots.getInt("idZ");
                        String world = setPlots.getString("world").toLowerCase();

                        addFreed(idX, idZ, world);
                    }

                    plugin.getLogger().info("Imported " + size + " plots from " + sqlitedb);
                    slstatement.close();
                    if (slAllowed != null) {
                        slAllowed.close();
                    }
                    if (slComments != null) {
                        slComments.close();
                    }
                    if (slDenied != null) {
                        slDenied.close();
                    }
                    if (slFreed != null) {
                        slFreed.close();
                    }
                    setPlots.close();
                    if (setComments != null) {
                        setComments.close();
                    }
                    if (setDenied != null) {
                        setDenied.close();
                    }
                    if (setAllowed != null) {
                        setAllowed.close();
                    }
                    setFreed.close();
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
            plugin.getLogger().severe("You need the SQLite library :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Could not create the table (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void addFreed(int idX, int idZ, String world) {
        PreparedStatement ps = null;

        //Freed
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO plotmeFreed (idX, idZ, world) VALUES (?,?,?)");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world.toLowerCase());

            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void addPlot(Plot plot, int idX, int idZ, IWorld w) {
        addPlot(plot, idX, idZ, 
                plugin.getPlotMeCoreManager().topX(plot.getId(), w), 
                plugin.getPlotMeCoreManager().bottomX(plot.getId(), w), 
                plugin.getPlotMeCoreManager().topZ(plot.getId(), w), 
                plugin.getPlotMeCoreManager().bottomZ(plot.getId(), w));
    }

    public void addPlot(Plot plot, int idX, int idZ, int topX, int bottomX, int topZ, int bottomZ) {
        PreparedStatement ps = null;
        StringBuilder strSql = new StringBuilder();

        // Plots
        try {
            Connection conn = getConnection();

            strSql.append("INSERT INTO plotmePlots (idX, idZ, owner, world, topX, bottomX, topZ, bottomZ, ");
            strSql.append("biome, expireddate, finished, customprice, forsale, finisheddate, protected,");
            strSql.append("auctionned, auctionneddate, currentbid, currentbidder, currentbidderId, ownerId) ");
            strSql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps = conn.prepareStatement(strSql.toString());
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, plot.getOwner());
            ps.setString(4, plot.getWorld().toLowerCase());
            ps.setInt(5, topX);
            ps.setInt(6, bottomX);
            ps.setInt(7, topZ);
            ps.setInt(8, bottomZ);
            ps.setString(9, plot.getBiome().name());
            ps.setDate(10, plot.getExpiredDate());
            ps.setBoolean(11, plot.isFinished());
            ps.setDouble(12, plot.getCustomPrice());
            ps.setBoolean(13, plot.isForSale());
            ps.setString(14, plot.getFinishedDate());
            ps.setBoolean(15, plot.isProtect());
            ps.setBoolean(16, plot.isAuctionned());
            ps.setDate(17, null); //not implemented
            ps.setDouble(18, plot.getCurrentBid());
            ps.setString(19, plot.getCurrentBidder());
            if (plot.getCurrentBidderId() != null) {
                ps.setBytes(20, UUIDFetcher.toBytes(plot.getCurrentBidderId()));
            } else {
                ps.setBytes(20, null);
            }
            if (plot.getOwnerId() != null) {
                ps.setBytes(21, UUIDFetcher.toBytes(plot.getOwnerId()));
            } else {
                ps.setBytes(21, null);
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

            if (plot.getComments() != null && !plot.getComments().isEmpty()) {
                int commentid = 1;
                for (String[] comments : plot.getComments()) {
                    UUID uuid = null;

                    if (comments.length >= 3) {
                        String strUUID = comments[2];
                        try {
                            uuid = UUID.fromString(strUUID);
                        } catch (Exception ignored) {
                        }
                    }

                    addPlotComment(comments, commentid, idX, idZ, plot.getWorld(), uuid);
                    commentid++;
                }
            }

            if (plot.getOwner() != null && !plot.getOwner().isEmpty() && plot.getOwnerId() == null) {
                fetchOwnerUUIDAsync(idX, idZ, plot.getWorld().toLowerCase(), plot.getOwner());
            }

            if (plot.getCurrentBidder() != null && !plot.getCurrentBidder().isEmpty() && plot.getCurrentBidderId() == null) {
                fetchBidderUUIDAsync(idX, idZ, plot.getWorld().toLowerCase(), plot.getCurrentBidder());
            }

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void updatePlot(int idX, int idZ, String world, String field, Object value) {
        PreparedStatement ps = null;

        //Plots
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("UPDATE plotmePlots SET " + field + " = ? "
                                               + "WHERE idX = ? AND idZ = ? AND world = ?");

            ps.setObject(1, value);
            ps.setInt(2, idX);
            ps.setInt(3, idZ);
            ps.setString(4, world.toLowerCase());

            ps.executeUpdate();
            conn.commit();
            
            if (field.equalsIgnoreCase("owner")) {
                fetchOwnerUUIDAsync(idX, idZ, world, value.toString());
            } else if (field.equalsIgnoreCase("currentbidder")) {
                fetchBidderUUIDAsync(idX, idZ, world, value.toString());
            }

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void addPlotAllowed(String player, UUID playerid, int idX, int idZ, String world) {
        PreparedStatement ps = null;

        //Allowed
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO plotmeAllowed (idX, idZ, player, world, playerid) "
                                               + "VALUES (?,?,?,?,?)");

            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, player);
            ps.setString(4, world.toLowerCase());
            if (playerid != null) {
                ps.setBytes(5, UUIDFetcher.toBytes(playerid));
            } else {
                ps.setBytes(5, null);
                fetchAllowedUUIDAsync(idX, idZ, world, player);
            }

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void addPlotDenied(String player, UUID playerid, int idX, int idZ, String world) {
        PreparedStatement ps = null;

        //Denied
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO plotmeDenied (idX, idZ, player, world, playerid) "
                                               + "VALUES (?,?,?,?,?)");

            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, player);
            ps.setString(4, world.toLowerCase());
            if (playerid != null) {
                ps.setBytes(5, UUIDFetcher.toBytes(playerid));
            } else {
                ps.setBytes(5, null);
                fetchDeniedUUIDAsync(idX, idZ, world, player);
            }

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void addPlotComment(String[] comment, int commentid, int idX, int idZ, String world, UUID uuid) {
        PreparedStatement ps = null;

        // Comments
        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("INSERT INTO plotmeComments (idX, idZ, commentid, player, comment, world, playerid) " + "VALUES (?,?,?,?,?,?,?)");
            
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setInt(3, commentid);
            ps.setString(4, comment[0]);
            ps.setString(5, comment[1]);
            ps.setString(6, world.toLowerCase());
            if (uuid != null) {
                ps.setBytes(7, UUIDFetcher.toBytes(uuid));
            } else {
                ps.setBytes(7, null);
            }

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void deletePlot(int idX, int idZ, String world) {
        PreparedStatement ps = null;
        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("DELETE FROM plotmeComments WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world);
            ps.executeUpdate();
            ps.close();
            conn.commit();

            ps = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world);
            ps.executeUpdate();
            ps.close();
            conn.commit();

            ps = conn.prepareStatement("DELETE FROM plotmePlots WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world);
            ps.executeUpdate();
            ps.close();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Delete Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void deleteFreed(int idX, int idZ, String world) {
        PreparedStatement ps = null;
        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("DELETE FROM plotmeFreed WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setString(3, world);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Delete Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public void deletePlotComment(int idX, int idZ, int commentid, String world) {
        PreparedStatement ps = null;
        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("DELETE FROM plotmeComments WHERE idX = ? and idZ = ? and commentid = ? and LOWER(world) = ?");
            ps.setInt(1, idX);
            ps.setInt(2, idZ);
            ps.setInt(3, commentid);
            ps.setString(4, world);
            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Delete Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
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
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
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
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
    }

    public Plot getPlot(String world, String id) {
        Plot plot = null;
        PreparedStatement statementPlot = null;
        PreparedStatement statementAllowed = null;
        PreparedStatement statementDenied = null;
        PreparedStatement statementComment = null;
        ResultSet setPlots = null;
        ResultSet setAllowed = null;
        ResultSet setDenied = null;
        ResultSet setComments = null;

        int idX = plugin.getPlotMeCoreManager().getIdX(id);
        int idZ = plugin.getPlotMeCoreManager().getIdZ(id);

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
                java.sql.Date expireddate = null;
                try {
                    expireddate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                List<String[]> comments = new ArrayList<>();
                double customprice = setPlots.getDouble("customprice");
                boolean forsale = setPlots.getBoolean("forsale");
                String finisheddate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                String currentbidder = setPlots.getString("currentbidder");
                double currentbid = setPlots.getDouble("currentbid");
                boolean auctionned = setPlots.getBoolean("auctionned");
                String auctionneddate = setPlots.getString("auctionneddate");
                
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

                if (setAllowed != null)
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

                if (setDenied != null)
                    setDenied.close();

                statementComment = conn.prepareStatement("SELECT * FROM plotmeComments WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementComment.setString(1, world);
                statementComment.setInt(2, idX);
                statementComment.setInt(3, idZ);
                setComments = statementComment.executeQuery();

                while (setComments.next()) {
                    String[] comment = new String[3];
                    comment[0] = setComments.getString("player");
                    comment[1] = setComments.getString("comment");
                    
                    byte[] byPlayerId = setComments.getBytes("playerid");
                    if (byPlayerId != null) {
                        comment[2] = UUIDFetcher.fromBytes(byPlayerId).toString();
                    }
                    
                    comments.add(comment);
                }

                plot = new Plot(plugin, owner, ownerId, world, biome, expireddate, finished, allowed,
                                       comments, "" + idX + ";" + idZ, customprice, forsale, finisheddate, protect,
                                       currentbidder, currentbidderid, currentbid, auctionned, denied, auctionneddate);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Plot load Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (statementAllowed != null) {
                    statementAllowed.close();
                }
                if (statementComment != null) {
                    statementComment.close();
                }
                if (statementDenied != null) {
                    statementDenied.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
                if (setComments != null) {
                    setComments.close();
                }
                if (setDenied != null) {
                    setDenied.close();
                }
                if (setAllowed != null) {
                    setAllowed.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Plot load Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return plot;
    }

    public void loadPlotsAsynchronously(String world) {
        final String worldname = world;
        
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Starting to load plots for world " + worldname);

                HashMap<String, Plot> plots = getPlots(worldname);

                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(worldname);

                for (String id : plots.keySet()) {
                    pmi.addPlot(id, plots.get(id));
                    plugin.getServerBridge().getEventFactory().callPlotLoadedEvent(plugin, plugin.getServerBridge().getWorld(worldname), plots.get(id));
                }

                // plugin.getLogger().info("Done loading " + pmi.getNbPlots() +
                // " plots for world " + worldname);
                plugin.getServerBridge().getEventFactory().callPlotWorldLoadEvent(plugin, worldname, pmi.getNbPlots());
            }
        });
    }

    //Do NOT call from the main thread
    private HashMap<String, Plot> getPlots(String world) {
        HashMap<String, Plot> ret = new HashMap<>();
        Statement statementPlot = null;
        Statement statementAllowed = null;
        Statement statementDenied = null;
        Statement statementComment = null;
        ResultSet setPlots = null;
        ResultSet setAllowed = null;
        ResultSet setDenied = null;
        ResultSet setComments = null;

        try {
            Connection conn = getConnection();

            statementPlot = conn.createStatement();
            setPlots = statementPlot.executeQuery("SELECT * FROM plotmePlots WHERE LOWER(world) = '" + world + "'");

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String owner = setPlots.getString("owner");
                String biome = setPlots.getString("biome");
                java.sql.Date expireddate = null;
                try {
                    expireddate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                List<String[]> comments = new ArrayList<>();
                double customprice = setPlots.getDouble("customprice");
                boolean forsale = setPlots.getBoolean("forsale");
                String finisheddate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                String currentbidder = setPlots.getString("currentbidder");
                double currentbid = setPlots.getDouble("currentbid");
                boolean auctionned = setPlots.getBoolean("auctionned");
                String auctionneddate = setPlots.getString("auctionneddate");

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
                
                statementAllowed = conn.createStatement();
                setAllowed = statementAllowed.executeQuery("SELECT * FROM plotmeAllowed WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");

                while (setAllowed.next()) {
                    byte[] byPlayerId = setAllowed.getBytes("playerid");
                    if (byPlayerId == null) {
                        allowed.put(setAllowed.getString("player"));
                    } else {
                        allowed.put(setAllowed.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                if (setAllowed != null)
                    setAllowed.close();

                statementDenied = conn.createStatement();
                setDenied = statementDenied.executeQuery("SELECT * FROM plotmeDenied WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");

                while (setDenied.next()) {
                    byte[] byPlayerId = setDenied.getBytes("playerid");
                    if (byPlayerId == null) {
                        denied.put(setDenied.getString("player"));
                    } else {
                        denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                if (setDenied != null)
                    setDenied.close();

                statementComment = conn.createStatement();
                setComments = statementComment.executeQuery("SELECT * FROM plotmeComments WHERE idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");

                while (setComments.next()) {
                    String[] comment = new String[3];
                    comment[0] = setComments.getString("player");
                    comment[1] = setComments.getString("comment");
                    
                    byte[] byPlayerId = setComments.getBytes("playerid");
                    if (byPlayerId != null) {
                        comment[2] = UUIDFetcher.fromBytes(byPlayerId).toString();
                    }
                    
                    comments.add(comment);
                }

                Plot plot = new Plot(plugin, owner, ownerId, world, biome, expireddate, finished, allowed, comments, "" + idX + ";" + idZ, 
                        customprice, forsale, finisheddate, protect, currentbidder, currentbidderid, currentbid, auctionned, denied, auctionneddate);
                ret.put("" + idX + ";" + idZ, plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Load Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (statementAllowed != null) {
                    statementAllowed.close();
                }
                if (statementComment != null) {
                    statementComment.close();
                }
                if (statementDenied != null) {
                    statementDenied.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
                if (setComments != null) {
                    setComments.close();
                }
                if (setDenied != null) {
                    setDenied.close();
                }
                if (setAllowed != null) {
                    setAllowed.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Load Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return ret;
    }

    public List<String> getFreed(String world) {
        List<String> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            statementPlot = conn.prepareStatement("SELECT idX, idZ FROM plotmeFreed WHERE LOWER(world) = ?");
            statementPlot.setString(1, world);

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");

                String id = idX + ";" + idZ;

                ret.add(id);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("GetFreed Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
        } finally {
            try {
                if (statementPlot != null) {
                    statementPlot.close();
                }
                if (setPlots != null) {
                    setPlots.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("GetFreed Exception (on close) :");
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return ret;
    }

    public long getPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        long nbplots = 0;

        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ?");
            ps.setString(1, world);

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getLong(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("PlotCount Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return nbplots;
    }

    public int getPlotCount(String world, UUID ownerId, String owner) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        int nbplots = 0;

        try {
            Connection conn = getConnection();

            if (ownerId == null) {
                ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND owner = ?");
                ps.setString(1, world);
                ps.setString(2, owner);
            } else {
                ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND ownerId = ?");
                ps.setString(1, world);
                ps.setBytes(2, UUIDFetcher.toBytes(ownerId));
            }

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getInt(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("PlotCount Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return nbplots;
    }

    public long getFinishedPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        long nbplots = 0;

        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND finished <> 0");
            ps.setString(1, world);

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getLong(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("FinishedPlotCount Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return nbplots;
    }

    public long getExpiredPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        long nbplots = 0;

        try {
            Connection conn = getConnection();

            Calendar cal = Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new Date(utilDate.getTime());

            ps = conn.prepareStatement("SELECT Count(*) as NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND expireddate < ?");
            ps.setString(1, world);
            ps.setDate(2, sqlDate);

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getLong(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("ExpiredPlotCount Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return nbplots;
    }

    public List<Plot> getDonePlots(String world, int Page, int NbPerPage) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            statementPlot = conn.prepareStatement("SELECT idX, idZ, owner, finisheddate FROM plotmePlots WHERE LOWER(world) = ? AND finished <> 0 ORDER BY finisheddate LIMIT ?, ?");
            statementPlot.setString(1, world);
            statementPlot.setInt(2, NbPerPage * (Page - 1));
            statementPlot.setInt(3, NbPerPage);

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
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return ret;
    }

    public List<Plot> getExpiredPlots(String world, int Page, int NbPerPage) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            Calendar cal = Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            java.sql.Date sqlDate = new Date(utilDate.getTime());

            statementPlot = conn.prepareStatement("SELECT idX, idZ, owner, expireddate FROM plotmePlots WHERE LOWER(world) = ? AND protected = 0 AND expireddate < ? ORDER BY expireddate LIMIT ?, ?");
            statementPlot.setString(1, world);
            statementPlot.setDate(2, sqlDate);
            statementPlot.setInt(3, NbPerPage * (Page - 1));
            statementPlot.setInt(4, NbPerPage);

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String owner = setPlots.getString("owner");

                java.sql.Date expireddate = null;
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
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
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
            java.sql.Date sqlDate = new Date(utilDate.getTime());

            statementPlot = conn.prepareStatement("SELECT idX, idZ, owner, expireddate FROM plotmePlots WHERE LOWER(world) = ? AND protected = 0 AND expireddate < ? ORDER BY expireddate LIMIT 1");
            statementPlot.setString(1, world);
            statementPlot.setDate(2, sqlDate);

            setPlots = statementPlot.executeQuery();

            if (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String owner = setPlots.getString("owner");

                java.sql.Date expireddate = null;
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
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
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

    public List<Plot> getPlayerPlots(UUID playerId, String playername) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            if (playerId == null) {
                statementPlot = conn.prepareStatement("SELECT DISTINCT A.* FROM plotmePlots A LEFT JOIN plotmeAllowed B ON A.idX = B.idX AND A.idZ = B.idZ AND A.world = B.world "
                        + " WHERE owner = ? OR B.player = ? ORDER BY A.world");
                statementPlot.setString(1, playername);
                statementPlot.setString(2, playername);
            } else {
                statementPlot = conn.prepareStatement("SELECT DISTINCT A.* FROM plotmePlots A LEFT JOIN plotmeAllowed B ON A.idX = B.idX AND A.idZ = B.idZ AND A.world = B.world "
                                                              + " WHERE ownerId = ? OR B.playerId = ? ORDER BY A.world");
                statementPlot.setBytes(1, UUIDFetcher.toBytes(playerId));
                statementPlot.setBytes(2, UUIDFetcher.toBytes(playerId));
            }

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String biome = setPlots.getString("biome");
                java.sql.Date expireddate = null;
                try {
                    expireddate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                List<String[]> comments = new ArrayList<>();
                double customprice = setPlots.getDouble("customprice");
                boolean forsale = setPlots.getBoolean("forsale");
                String finisheddate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                String currentbidder = setPlots.getString("currentbidder");
                double currentbid = setPlots.getDouble("currentbid");
                boolean auctionned = setPlots.getBoolean("auctionned");
                String auctionneddate = setPlots.getString("auctionneddate");
                String world = setPlots.getString("world");
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

                PreparedStatement statementAllowed = conn.prepareStatement("SELECT * FROM plotmeAllowed WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementAllowed.setString(1, world);
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

                PreparedStatement statementDenied = conn.prepareStatement("SELECT * FROM plotmeDenied WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementDenied.setString(1, world);
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

                PreparedStatement statementComment = conn.prepareStatement("SELECT * FROM plotmeComments WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementComment.setString(1, world);
                statementComment.setInt(2, idX);
                statementComment.setInt(3, idZ);
                ResultSet setComments = statementComment.executeQuery();

                while (setComments.next()) {
                    String[] comment = new String[3];
                    comment[0] = setComments.getString("player");
                    comment[1] = setComments.getString("comment");
                    
                    byte[] byPlayerId = setComments.getBytes("playerid");
                    if (byPlayerId != null) {
                        comment[2] = UUIDFetcher.fromBytes(byPlayerId).toString();
                    }
                    
                    comments.add(comment);
                }

                Plot plot = new Plot(plugin, owner, ownerId, world, biome, expireddate, finished, allowed,
                                            comments, "" + idX + ";" + idZ, customprice, forsale, finisheddate, protect,
                                            currentbidder, currentbidderid, currentbid, auctionned, denied, auctionneddate);

                ret.add(plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("DonePlots Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return ret;
    }

    public List<Plot> getOwnedPlots(String w, UUID ownerId, String owner) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            if (ownerId == null) {
                statementPlot = conn.prepareStatement("SELECT * FROM plotmePlots WHERE world LIKE ? AND owner LIKE ? ORDER BY world");
                statementPlot.setString(1, w);
                statementPlot.setString(2, owner);
            } else {
                statementPlot = conn.prepareStatement("SELECT * FROM plotmePlots WHERE world LIKE ? AND ownerId = ? ORDER BY world");
                statementPlot.setString(1, w);
                statementPlot.setBytes(2, UUIDFetcher.toBytes(ownerId));
            }

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                int idX = setPlots.getInt("idX");
                int idZ = setPlots.getInt("idZ");
                String biome = setPlots.getString("biome");
                java.sql.Date expireddate = null;
                try {
                    expireddate = setPlots.getDate("expireddate");
                } catch (SQLException ignored) {
                }
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                List<String[]> comments = new ArrayList<>();
                double customprice = setPlots.getDouble("customprice");
                boolean forsale = setPlots.getBoolean("forsale");
                String finisheddate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                String currentbidder = setPlots.getString("currentbidder");
                double currentbid = setPlots.getDouble("currentbid");
                boolean auctionned = setPlots.getBoolean("auctionned");
                String auctionneddate = setPlots.getString("auctionneddate");
                String world = setPlots.getString("world");

                byte[] byBidder = setPlots.getBytes("currentbidderid");
                byte[] byOwner = setPlots.getBytes("ownerid");

                UUID currentbidderid = null;

                if (byBidder != null) {
                    currentbidderid = UUIDFetcher.fromBytes(byBidder);
                }
                
                if (byOwner != null) {
                    ownerId = UUIDFetcher.fromBytes(byOwner);
                }

                PreparedStatement statementAllowed = conn.prepareStatement("SELECT * FROM plotmeAllowed WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementAllowed.setString(1, world);
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

                PreparedStatement statementDenied = conn.prepareStatement("SELECT * FROM plotmeDenied WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementDenied.setString(1, world);
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

                PreparedStatement statementComment = conn.prepareStatement("SELECT * FROM plotmeComments WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementComment.setString(1, world);
                statementComment.setInt(2, idX);
                statementComment.setInt(3, idZ);
                ResultSet setComments = statementComment.executeQuery();

                while (setComments.next()) {
                    String[] comment = new String[3];
                    comment[0] = setComments.getString("player");
                    comment[1] = setComments.getString("comment");

                    byte[] byPlayerId = setComments.getBytes("playerid");
                    if (byPlayerId != null) {
                        comment[2] = UUIDFetcher.fromBytes(byPlayerId).toString();
                    }
                    
                    comments.add(comment);
                }

                Plot plot = new Plot(plugin, owner, ownerId, world, biome, expireddate, finished, allowed,
                                            comments, "" + idX + ";" + idZ, customprice, forsale, finisheddate, protect,
                                            currentbidder, currentbidderid, currentbid, auctionned, denied, auctionneddate);

                ret.add(plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("DonePlots Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return ret;
    }

    public String getFirstWorld(String owner) {
        PreparedStatement statementPlot = null;
        ResultSet setPlots = null;

        try {
            Connection conn = getConnection();

            statementPlot = conn.prepareStatement("SELECT world FROM plotmePlots WHERE owner = ? LIMIT 1");
            statementPlot.setString(1, owner);

            setPlots = statementPlot.executeQuery();

            if (setPlots.next()) {
                return setPlots.getString("world");
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("DonePlots Exception :");
            plugin.getLogger().severe("  " + ex.getMessage());
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
                plugin.getLogger().severe("  " + ex.getMessage());
            }
        }
        return "";
    }

    
    public void plotConvertToUUIDAsynchronously() {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Checking if conversion to UUID needed...");

                Statement statementPlayers = null;
                PreparedStatement psOwnerId = null;
                PreparedStatement psCurrentBidderId = null;
                PreparedStatement psAllowedPlayerId = null;
                PreparedStatement psDeniedPlayerId = null;
                PreparedStatement psCommentsPlayerId = null;
                
                PreparedStatement psDeleteOwner = null;
                PreparedStatement psDeleteCurrentBidder = null;
                PreparedStatement psDeleteAllowed = null;
                PreparedStatement psDeleteDenied = null;
                PreparedStatement psDeleteComments = null;
                
                ResultSet setPlayers = null;

                try {
                    Connection conn = getConnection();

                    // Get all the players
                    statementPlayers = conn.createStatement();
                    // Exclude groups and names with * or missing
                    String sql = "SELECT LOWER(owner) as Name FROM plotmePlots WHERE NOT owner IS NULL AND Not owner LIKE 'group:%' AND Not owner LIKE '%*%' AND ownerid IS NULL GROUP BY LOWER(owner) ";
                    sql = sql + "UNION SELECT LOWER(currentbidder) as Name FROM plotmePlots WHERE NOT currentbidder IS NULL AND currentbidderid IS NULL GROUP BY LOWER(currentbidder) ";
                    sql = sql + "UNION SELECT LOWER(player) as Name FROM plotmeAllowed WHERE NOT player IS NULL AND Not player LIKE 'group:%' AND Not player LIKE '%*%' AND playerid IS NULL GROUP BY LOWER(player) ";
                    sql = sql + "UNION SELECT LOWER(player) as Name FROM plotmeDenied WHERE NOT player IS NULL AND Not player LIKE 'group:%' AND Not player LIKE '%*%' AND playerid IS NULL GROUP BY LOWER(player) ";
                    sql = sql + "UNION SELECT LOWER(player) as Name FROM plotmeComments WHERE NOT player IS NULL AND Not player LIKE 'group:%' AND Not player LIKE '%*%' AND playerid IS NULL GROUP BY LOWER(player)";

                    setPlayers = statementPlayers.executeQuery(sql);

                    boolean boConversion = false;
                    if (setPlayers.next()) {
                        List<String> names = new ArrayList<>();

                        //Prepare delete statements
                        psDeleteOwner = conn.prepareStatement("UPDATE plotmePlots SET owner = '' WHERE owner = ? ");
                        psDeleteCurrentBidder = conn.prepareStatement("UPDATE plotmePlots SET currentbidder = null WHERE currentbidder = ? ");
                        psDeleteAllowed = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE player = ? ");
                        psDeleteDenied = conn.prepareStatement("DELETE FROM plotmeDenied WHERE player = ? ");
                        psDeleteComments = conn.prepareStatement("DELETE FROM plotmeComments WHERE player = ? ");

                        do {
                            String name = setPlayers.getString("Name");
                            if (!name.isEmpty()) {
                                if (name.matches("^[a-zA-Z0-9_]{1,16}$")) {
                                    names.add(name);
                                } else {
                                    plugin.getLogger().warning("Invalid name found : " + name + ". Removing from database.");
                                    psDeleteOwner.setString(1, name);
                                    psDeleteOwner.executeUpdate();
                                    psDeleteCurrentBidder.setString(1, name);
                                    psDeleteCurrentBidder.executeUpdate();
                                    psDeleteAllowed.setString(1, name);
                                    psDeleteAllowed.executeUpdate();
                                    psDeleteDenied.setString(1, name);
                                    psDeleteDenied.executeUpdate();
                                    psDeleteComments.setString(1, name);
                                    psDeleteComments.executeUpdate();
                                    conn.commit();
                                }
                            }
                        } while (setPlayers.next());

                        psDeleteOwner.close();
                        psDeleteCurrentBidder.close();
                        psDeleteAllowed.close();
                        psDeleteDenied.close();
                        psDeleteComments.close();
                        
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
                                psCurrentBidderId = conn.prepareStatement("UPDATE plotmePlots SET currentbidderid = ? WHERE LOWER(currentbidder) = ? AND currentbidderid IS NULL");
                                psAllowedPlayerId = conn.prepareStatement("UPDATE plotmeAllowed SET playerid = ? WHERE LOWER(player) = ? AND playerid IS NULL");
                                psDeniedPlayerId = conn.prepareStatement("UPDATE plotmeDenied SET playerid = ? WHERE LOWER(player) = ? AND playerid IS NULL");
                                psCommentsPlayerId = conn.prepareStatement("UPDATE plotmeComments SET playerid = ? WHERE LOWER(player) = ? AND playerid IS NULL");

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
                                    // Commenter
                                    psCommentsPlayerId.setBytes(1, UUIDFetcher.toBytes(response.get(key)));
                                    psCommentsPlayerId.setString(2, key.toLowerCase());
                                    psCommentsPlayerId.executeUpdate();
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
                                psCommentsPlayerId.close();
                                
                                
                                //Update plot information
                                for (PlotMapInfo pmi : plugin.getPlotMeCoreManager().getPlotMaps().values()) {
                                    for(Plot plot : pmi.getLoadedPlots().values()) {
                                        for(Entry<String, UUID> player : response.entrySet()) {
                                            //Owner
                                            if (plot.getOwnerId() == null && plot.getOwner() != null && plot.getOwner().equalsIgnoreCase(player.getKey())) {
                                                plot.setOwner(player.getKey());
                                                plot.setOwnerId(player.getValue());
                                            }
                                            
                                            //Bidder
                                            if (plot.getCurrentBidderId() == null && plot.getCurrentBidder() != null && plot.getCurrentBidder().equalsIgnoreCase(player.getKey())) {
                                                plot.setCurrentBidder(player.getKey());
                                                plot.setCurrentBidderId(player.getValue());
                                            }
                                            
                                            //Allowed
                                            plot.allowed().replace(player.getKey(), player.getValue());
                                            
                                            //Denied
                                            plot.denied().replace(player.getKey(), player.getValue());
                                            
                                            //Comments
                                            for(String[] comment : plot.getComments()) {
                                                if (comment.length > 2) {
                                                    if (comment[2] == null) {
                                                        if (null != null) {
                                                            if (comment[0].equalsIgnoreCase(player.getKey())) {
                                                                comment[0] = player.getKey();
                                                                comment[2] = player.getValue().toString();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
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
                    plugin.getLogger().severe("  " + ex.getMessage());
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
                        if (psCommentsPlayerId != null) {
                            psCommentsPlayerId.close();
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
                        if (psDeleteComments != null) {
                            psDeleteComments.close();
                        }
                    } catch (SQLException ex) {
                        plugin.getLogger().severe("Conversion to UUID failed (on close) :");
                        plugin.getLogger().severe("  " + ex.getMessage());
                        for (StackTraceElement e : ex.getStackTrace()) {
                            plugin.getLogger().severe("  " + e);
                        }
                    }
                }
            }
        });
    }
    
    public void fetchOwnerUUIDAsync(int idX, int idZ, String world, String owner) {
        _fetchUUIDAsync(idX, idZ, world, "owner", owner);
    }

    public void fetchBidderUUIDAsync(int idX, int idZ, String world, String bidder) {
        _fetchUUIDAsync(idX, idZ, world, "bidder", bidder);
    }

    public void fetchAllowedUUIDAsync(int idX, int idZ, String world, String allowed) {
        _fetchUUIDAsync(idX, idZ, world, "allowed", allowed);
    }

    public void fetchDeniedUUIDAsync(int idX, int idZ, String world, String denied) {
        _fetchUUIDAsync(idX, idZ, world, "denied", denied);
    }

    private void _fetchUUIDAsync(final int idX, final int idZ, final String world, final String Property, final String name) {
        if (plugin.getInitialized()) {
            plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
                @Override
                public void run() {
    
                    PreparedStatement ps = null;
    
                    try {
                        Connection conn = getConnection();
    
                        IPlayer p = plugin.getServerBridge().getPlayerExact(name);
                        UUID uuid = null;
                        String newname = name;
    
                        if (p != null) {
                            uuid = p.getUniqueId();
                            newname = p.getName();
                        } else if (name.isEmpty()) {
                            uuid = null;
                            newname = "";
                        } else {
                            List<String> names = new ArrayList<>();
    
                            names.add(name);
    
                            UUIDFetcher fetcher = new UUIDFetcher(names);

                            try {
                                //plugin.getLogger().info("Fetching " + names.size() + " UUIDs from Mojang servers...");
                                Map<String, UUID> response = fetcher.call();
                                //plugin.getLogger().info("Received " + response.size() + " UUIDs. Starting database update...");

                                if (!response.isEmpty()) {
                                    uuid = response.values().toArray(new UUID[0])[0];
                                    newname = response.keySet().toArray(new String[0])[0];
                                }
                            } catch (IOException e) {
                                plugin.getLogger().warning("Unable to connect to Mojang server!");
                            } catch (Exception e) {
                                plugin.getLogger().warning("Exception while running UUIDFetcher");
                                e.printStackTrace();
                            }
                        }
    
                        switch (Property) {
                            case "owner":
                                ps = conn.prepareStatement("UPDATE plotmePlots SET ownerid = ?, owner = ? WHERE LOWER(owner) = ? AND idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");
                                break;
                            case "bidder":
                                ps = conn.prepareStatement("UPDATE plotmePlots SET currentbidderid = ?, currentbidder = ? WHERE LOWER(currentbidder) = ? AND idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");
                                break;
                            case "allowed":
                                ps = conn.prepareStatement("UPDATE plotmeAllowed SET playerid = ?, player = ? WHERE LOWER(player) = ? AND idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");
                                break;
                            case "denied":
                                ps = conn.prepareStatement("UPDATE plotmeDenied SET playerid = ?, player = ? WHERE LOWER(player) = ? AND idX = '" + idX + "' AND idZ = '" + idZ + "' AND LOWER(world) = '" + world + "'");
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
                        ps.setString(3, name.toLowerCase());
                        ps.executeUpdate();
                        conn.commit();
    
                        ps.close();
                        
                        if (uuid != null) {
                            Plot plot = plugin.getPlotMeCoreManager().getPlotById(world, "" + idX + ";" + idZ);
                            
                            if(plot != null) {
                                switch (Property) {
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
                        plugin.getLogger().severe("  " + ex.getMessage());
                        for (StackTraceElement e : ex.getStackTrace()) {
                            plugin.getLogger().severe("  " + e);
                        }
                    } finally {
                        try {
                            if (ps != null) {
                                ps.close();
                            }
                        } catch (SQLException ex) {
                            plugin.getLogger().severe("Conversion to UUID failed (on close) :");
                            plugin.getLogger().severe("  " + ex.getMessage());
                            for (StackTraceElement e : ex.getStackTrace()) {
                                plugin.getLogger().severe("  " + e);
                            }
                        }
                    }
                }
            });
        }
    }
    
    public void updatePlotsNewUUID(final UUID uuid, final String newname) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                PreparedStatement[] pss = new PreparedStatement[5];

                try {
                    Connection conn = getConnection();

                    pss[0] = conn.prepareStatement("UPDATE plotmePlots SET owner = ? WHERE ownerid = ?");
                    pss[1] = conn.prepareStatement("UPDATE plotmePlots SET currentbidder = ? WHERE currentbidderid = ?");
                    pss[2] = conn.prepareStatement("UPDATE plotmeAllowed SET player = ? WHERE playerid = ?");
                    pss[3] = conn.prepareStatement("UPDATE plotmeDenied SET player = ? WHERE playerid = ?");
                    pss[4] = conn.prepareStatement("UPDATE plotmeComments SET player = ? WHERE playerid = ?");

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
                    plugin.getLogger().severe("  " + ex.getMessage());
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
                        plugin.getLogger().severe("  " + ex.getMessage());
                        for (StackTraceElement e : ex.getStackTrace()) {
                            plugin.getLogger().severe("  " + e);
                        }
                    }
                }
            }
        });
    }
}
