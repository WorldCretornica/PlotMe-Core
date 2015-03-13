package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;

import java.io.File;
import java.sql.Connection;
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
import java.util.regex.Pattern;

public class SqlManager {

    private static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    private static final Pattern COMPILE = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");
    private final PlotMe_Core plugin;
    private Connection conn;

    public SqlManager(PlotMe_Core plugin) {
        this.plugin = plugin;
    }

    public Connection initialize() {
        try {
            if (isUsingMySQL()) {
                Class.forName("com.mysql.jdbc.Driver");
                String url = plugin.getConfig().getString("mySQLconn");
                String user = plugin.getConfig().getString("mySQLuname");
                String pass = plugin.getConfig().getString("mySQLpass");
                conn = DriverManager.getConnection(url, user, pass);
                conn.setAutoCommit(false);
            } else {
                Class.forName(SQLITE_DRIVER);
                conn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder().getAbsolutePath() + "/plotmecore.db");
                conn.setAutoCommit(false);
            }
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("SQL exception on initialize: ");
            plugin.getLogger().severe(e.getMessage());
        }
        return conn;
    }

    public void updateTables() {
        Statement statement = null;
        ResultSet set = null;

        try {
            Connection conn = getConnection();

            statement = conn.createStatement();

            if (isUsingMySQL()) {

            } else {

            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Update table exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
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

    public void createTables() {
        Statement st = null;
        Connection conn = getConnection();
        try {

            String METADATA_TABLE = "CREATE TABLE IF NOT EXISTS `plotmeMetadata` ("
                    + "`idX` INTEGER NOT NULL,"
                    + "`idZ` INTEGER NOT NULL,"
                    + "`world` varchar(32) NOT NULL,"
                    + "`pluginname` nvarchar(100) NOT NULL,"
                    + "`propertyname` nvarchar(100) NOT NULL,"
                    + "`propertyvalue` nvarchar(255),"
                    + "PRIMARY KEY (idX, idZ, world, pluginname, propertyname) "
                    + ");";
            st.executeUpdate(METADATA_TABLE);
            conn.commit();

            updateTables();

            if (isUsingMySQL()) {

                String sqlitedb = "plots.db";
                File sqlitefile = new File(plugin.getServerBridge().getDataFolder().getAbsolutePath(), sqlitedb);
                if (sqlitefile.exists()) {
                    plugin.getLogger().info("Modifying database for MySQL support");
                    plugin.getLogger().info("Trying to import plots from plots.db");
                    Class.forName(SQLITE_DRIVER);
                    Connection sqliteconn =
                            DriverManager.getConnection("jdbc:sqlite:" + plugin.getServerBridge().getDataFolder().getAbsolutePath() + "/plotmecore"
                                    + ".db");

                    sqliteconn.setAutoCommit(false);
                    Statement slstatement = sqliteconn.createStatement();
                    ResultSet setPlots = slstatement.executeQuery("SELECT * FROM plotmePlots");
                    Statement slAllowed = sqliteconn.createStatement();
                    ResultSet setAllowed = null;
                    Statement slDenied = sqliteconn.createStatement();
                    ResultSet setDenied = null;
                    Statement slMetadata = sqliteconn.createStatement();
                    ResultSet setMetadata = null;

                    int size = 0;
                    while (setPlots.next()) {
                        PlotId id = new PlotId(setPlots.getInt("idX"), setPlots.getInt("idZ"));
                        String owner = setPlots.getString("owner");
                        String world = setPlots.getString("world");
                        int topX = setPlots.getInt("topX");
                        int bottomX = setPlots.getInt("bottomX");
                        int topZ = setPlots.getInt("topZ");
                        int bottomZ = setPlots.getInt("bottomZ");
                        String biome = setPlots.getString("biome");
                        Date expireddate = setPlots.getDate("expireddate");
                        boolean finished = setPlots.getBoolean("finished");
                        PlayerList allowed = new PlayerList();
                        PlayerList denied = new PlayerList();
                        double customprice = setPlots.getDouble("customprice");
                        boolean forsale = setPlots.getBoolean("forsale");
                        String finisheddate = setPlots.getString("finisheddate");
                        boolean protect = setPlots.getBoolean("protected");
                        Map<String, Map<String, String>> metadata = new HashMap<>();

                        byte[] byOwner = setPlots.getBytes("ownerId");

                        UUID ownerId = UUIDFetcher.fromBytes(byOwner);
                        setAllowed = slAllowed.executeQuery("SELECT * FROM plotmeAllowed WHERE idX = '" + id.getX() +
                                "' AND idZ = '" + id.getZ() + "' AND world = '" + world + "'");

                        while (setAllowed.next()) {
                            byte[] byPlayerId = setAllowed.getBytes("playerid");
                            if (byPlayerId == null) {
                                allowed.put(setAllowed.getString("player"));
                            } else {
                                allowed.put(setAllowed.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                            }
                        }

                        setAllowed.close();
                        setDenied = slDenied.executeQuery(
                                "SELECT * FROM plotmeDenied WHERE idX = '" + id.getX() + "' AND idZ = '" + id.getZ() + "' AND world = '" + world
                                        + "'");

                        while (setDenied.next()) {
                            byte[] byPlayerId = setDenied.getBytes("playerid");
                            if (byPlayerId == null) {
                                denied.put(setDenied.getString("player"));
                            } else {
                                denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                            }
                        }

                        setDenied.close();

                        setMetadata = slMetadata
                                .executeQuery("SELECT pluginname, propertyname, propertyvalue FROM plotmeMetadata WHERE idX = '" + id.getX() +
                                        "' AND idZ = '" + id.getZ() + "' AND world = '" + world + "'");

                        while (setMetadata.next()) {
                            String pluginname = setMetadata.getString("pluginname");
                            String propertyname = setMetadata.getString("propertyname");
                            String propertyvalue = setMetadata.getString("propertyvalue");
                            if (!metadata.containsKey(pluginname)) {
                                metadata.put(pluginname, new HashMap<String, String>());
                            }
                            metadata.get(pluginname).put(propertyname, propertyvalue);
                        }

                        setMetadata.close();


                        Plot plot = new Plot(plugin, owner, ownerId, world, biome, null, finished, allowed, id, customprice,
                                forsale, finisheddate, protect, denied, metadata, 0, null);
                        addPlot(plot, id, topX, bottomX, topZ, bottomZ);

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
                    if (slMetadata != null) {
                        slMetadata.close();
                    }
                    setPlots.close();
                    if (setDenied != null) {
                        setDenied.close();
                    }
                    if (setAllowed != null) {
                        setAllowed.close();
                    }
                    if (setMetadata != null) {
                        setMetadata.close();
                    }
                    sqliteconn.close();

                    plugin.getLogger().info("Renaming " + sqlitedb + " to " + sqlitedb + ".old");
                    if (!sqlitefile.renameTo(new File(plugin.getServerBridge().getDataFolder().getAbsolutePath(), sqlitedb + ".old"))) {
                        plugin.getLogger().severe("Failed to rename " + sqlitedb + "! Please rename this manually!");
                    }
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Create table exception :");
            plugin.getLogger().severe(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().severe("Class not found exception :");
            plugin.getLogger().severe(ex.getMessage());
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

    public void addPlot(Plot plot, PlotId id, IWorld world) {
        PlotMeCoreManager manager = PlotMeCoreManager.getInstance();

        addPlot(plot, id, manager.topX(plot.getId(), world), manager.bottomX(plot.getId(), world), manager.topZ(plot.getId(), world),
                manager.bottomZ(plot.getId(), world));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void addPlot(Plot plot, PlotId id, int topX, int bottomX, int topZ, int bottomZ) {
        PreparedStatement ps = null;

        // Plots
        try {
            Connection conn = getConnection();

            String strSql = "INSERT INTO plotmePlots (idX, idZ, owner, world, topX, bottomX, topZ, bottomZ, "
                    + "biome, expireddate, finished, customprice, forsale, finisheddate, protected, ownerId) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(strSql);
            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, plot.getOwner());
            ps.setString(4, plot.getWorld().toLowerCase());

            ps.setInt(5, topX);
            ps.setInt(6, bottomX);
            ps.setInt(7, topZ);
            ps.setInt(8, bottomZ);
            ps.setString(9, ((BukkitBiome) plot.getBiome()).getBiome().name());
            //ps.setDate(10, plot.getExpiredDate());
            ps.setBoolean(11, plot.isFinished());
            ps.setDouble(12, plot.getPrice());
            ps.setBoolean(13, plot.isForSale());
            ps.setString(14, plot.getFinishedDate());
            ps.setBoolean(15, plot.isProtect());
            //ps.setBytes(20, UUIDFetcher.toBytes(plot.getOwnerId()));

            ps.executeUpdate();
            conn.commit();

            PlayerList allowedlist = plot.allowed();
            if (allowedlist != null && allowedlist.getAllPlayers() != null) {
                HashMap<String, UUID> allowed = allowedlist.getAllPlayers();
                for (String key : allowed.keySet()) {
                    addPlotAllowed(key, allowed.get(key), id, plot.getWorld());
                }
            }

            PlayerList deniedlist = plot.denied();
            if (deniedlist != null && deniedlist.getAllPlayers() != null) {
                HashMap<String, UUID> denied = deniedlist.getAllPlayers();
                for (String key : denied.keySet()) {
                    addPlotDenied(key, denied.get(key), id, plot.getWorld());
                }
            }

            Map<String, Map<String, String>> metadata = plot.getAllPlotProperties();
            if (metadata != null && !metadata.isEmpty()) {
                for (String pluginname : metadata.keySet()) {
                    Map<String, String> pluginproperties = metadata.get(pluginname);
                    for (String propertyname : pluginproperties.keySet()) {
                        savePlotProperty(id, plot.getWorld().toLowerCase(), pluginname, propertyname, pluginproperties.get(propertyname));
                    }
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
                plugin.getLogger().severe("Insert exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void updatePlot(PlotId id, String world, String field, Object value) {
        PreparedStatement ps = null;

        //Plots
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("UPDATE plotmePlots SET " + field + " = ? " + "WHERE idX = ? AND idZ = ? AND world = ?");

            ps.setObject(1, value);
            ps.setInt(2, id.getX());
            ps.setInt(3, id.getZ());
            ps.setString(4, world.toLowerCase());

            ps.executeUpdate();
            conn.commit();

            if ("owner".equalsIgnoreCase(field)) {
                fetchUUIDAsync(id, world, "owner", value.toString());
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void addPlotAllowed(String player, UUID uuid, PlotId id, String world) {
        PreparedStatement ps = null;

        //Allowed
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO plotmeAllowed (idX, idZ, player, world, playerid) VALUES (?,?,?,?,?)");

            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, player);
            ps.setString(4, world.toLowerCase());
            if (uuid != null) {
                ps.setBytes(5, UUIDFetcher.toBytes(uuid));
            } else {
                ps.setBytes(5, null);
                fetchUUIDAsync(id, world, "allowed", player);
            }

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Insert exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    public void addPlotDenied(String player, UUID playerid, PlotId id, String world) {
        PreparedStatement ps = null;

        //Denied
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO plotmeDenied (idX, idZ, player, world, playerid) VALUES (?,?,?,?,?)");

            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, player);
            ps.setString(4, world.toLowerCase());
            if (playerid == null) {
                ps.setBytes(5, null);
                fetchUUIDAsync(id, world, "denied", player);
            } else {
                ps.setBytes(5, UUIDFetcher.toBytes(playerid));
            }

            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Insert exception :");
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

    public void deletePlot(PlotId id, String world) {
        PreparedStatement ps = null;
        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, world.toLowerCase());
            ps.executeUpdate();
            ps.close();
            conn.commit();

            ps = conn.prepareStatement("DELETE FROM plotmeDenied WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, world.toLowerCase());
            ps.executeUpdate();
            ps.close();
            conn.commit();

            ps = conn.prepareStatement("DELETE FROM plotmeMetadata WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, world.toLowerCase());
            ps.executeUpdate();
            ps.close();
            conn.commit();

            ps = conn.prepareStatement("DELETE FROM plotmePlots WHERE idX = ? and idZ = ? and LOWER(world) = ?");
            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
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
                plugin.getLogger().severe("Delete exception (on close) :");
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
            ps.setString(4, world.toLowerCase());
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
                plugin.getLogger().severe("Delete exception (on close) :");
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
            ps.setString(4, world.toLowerCase());
            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            plugin.getLogger().severe("Delete exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Delete exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
    }

    /**
     * @param world must be lowercase
     * @param id plot id
     * @return the plot
     */
    public Plot getPlot(String world, PlotId id) {
        Plot plot = null;
        PreparedStatement statementPlot = null;
        PreparedStatement statementAllowed = null;
        PreparedStatement statementDenied = null;
        PreparedStatement statementMetadata = null;
        ResultSet setPlots = null;
        ResultSet setAllowed = null;
        ResultSet setDenied = null;
        ResultSet setMetadata = null;

        int idX = id.getX();
        int idZ = id.getZ();

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
                Date expiredDate = setPlots.getDate("expireddate");
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                Map<String, Map<String, String>> metadata = new HashMap<>();
                double customPrice = setPlots.getDouble("customprice");
                boolean forSale = setPlots.getBoolean("forsale");
                String finishedDate;
                if (finished) {
                    finishedDate = setPlots.getString("finisheddate");
                } else {
                    finishedDate = null;
                }
                boolean protect = setPlots.getBoolean("protected");

                byte[] byOwner = setPlots.getBytes("ownerId");

                UUID ownerId = UUIDFetcher.fromBytes(byOwner);

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

                statementMetadata = conn.prepareStatement(
                        "SELECT pluginname, propertyname, propertyvalue FROM plotmeMetadata WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementMetadata.setString(1, world);
                statementMetadata.setInt(2, idX);
                statementMetadata.setInt(3, idZ);

                setMetadata = statementMetadata.executeQuery();

                while (setMetadata.next()) {
                    String pluginname = setMetadata.getString("pluginname");
                    String propertyname = setMetadata.getString("propertyname");
                    String propertyvalue = setMetadata.getString("propertyvalue");
                    if (!metadata.containsKey(pluginname)) {
                        metadata.put(pluginname, new HashMap<String, String>());
                    }
                    metadata.get(pluginname).put(propertyname, propertyvalue);
                }

                setMetadata.close();

                plot = new Plot(plugin, owner, ownerId, world, biome, null, finished, allowed,
                        id, customPrice, forSale, finishedDate, protect, denied, metadata, 0, null);
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
                if (statementMetadata != null) {
                    statementMetadata.close();
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
                if (setMetadata != null) {
                    setMetadata.close();
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

                HashMap<PlotId, Plot> plots = getPlots(worldName);

                PlotMapInfo pmi = PlotMeCoreManager.getInstance().getMap(worldName);

                for (PlotId id : plots.keySet()) {
                    pmi.addPlot(id, plots.get(id));
                    plugin.getServerBridge().getEventFactory()
                            .callPlotLoadedEvent(plugin.getServerBridge().getWorld(worldName), plots.get(id));
                }
                plugin.getServerBridge().getEventFactory().callPlotWorldLoadEvent(worldName, pmi.getNbPlots());
            }
        });
    }

    //Do NOT call from the main thread
    public HashMap<PlotId, Plot> getPlots(String world) {
        HashMap<PlotId, Plot> ret = new HashMap<>();
        Statement statementPlot = null;
        Statement statementAllowed = null;
        Statement statementDenied = null;
        Statement statementMetadata = null;
        ResultSet setPlots = null;
        ResultSet setAllowed = null;
        ResultSet setDenied = null;
        ResultSet setMetadata = null;

        try {
            Connection conn = getConnection();

            statementPlot = conn.createStatement();
            setPlots = statementPlot.executeQuery("SELECT * FROM plotmePlots WHERE LOWER(world) = '" + world + "'");

            while (setPlots.next()) {
                PlotId id = new PlotId(setPlots.getInt("idX"), setPlots.getInt("idZ"));
                String owner = setPlots.getString("owner");
                String biome = setPlots.getString("biome");
                Date expiredDate = setPlots.getDate("expireddate");
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                double price = setPlots.getDouble("customprice");
                boolean forSale = setPlots.getBoolean("forsale");
                String finishedDate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                Map<String, Map<String, String>> metadata = new HashMap<>();

                byte[] byOwner = setPlots.getBytes("ownerId");

                UUID ownerId = UUIDFetcher.fromBytes(byOwner);

                statementAllowed = conn.createStatement();
                setAllowed = statementAllowed.executeQuery("SELECT * FROM plotmeAllowed WHERE idX = '" + id.getX() +
                        "' AND idZ = '" + id.getZ() + "' AND LOWER(world) = '" + world + "'");

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
                setDenied = statementDenied.executeQuery("SELECT * FROM plotmeDenied WHERE idX = '" + id.getX() +
                        "' AND idZ = '" + id.getZ() + "' AND LOWER(world) = '" + world + "'");

                while (setDenied.next()) {
                    byte[] byPlayerId = setDenied.getBytes("playerid");
                    if (byPlayerId == null) {
                        denied.put(setDenied.getString("player"));
                    } else {
                        denied.put(setDenied.getString("player"), UUIDFetcher.fromBytes(byPlayerId));
                    }
                }

                setDenied.close();

                statementMetadata = conn.createStatement();
                setMetadata = statementMetadata.executeQuery("SELECT * FROM plotmeMetadata WHERE idX = '" + id.getX() +
                        "' AND idZ = '" + id.getZ() + "' AND LOWER(world) = '" + world + "'");

                while (setMetadata.next()) {
                    String pluginname = setMetadata.getString("pluginname");
                    String propertyname = setMetadata.getString("propertyname");
                    String propertyvalue = setMetadata.getString("propertyvalue");
                    if (!metadata.containsKey(pluginname)) {
                        metadata.put(pluginname, new HashMap<String, String>());
                    }
                    metadata.get(pluginname).put(propertyname, propertyvalue);
                }

                setMetadata.close();

                Plot plot = new Plot(plugin, owner, ownerId, world, biome, null, finished, allowed, id,
                        price, forSale, finishedDate, protect, denied, metadata, 0, null);
                ret.put(id, plot);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Load exception :");
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
                if (statementMetadata != null) {
                    statementMetadata.close();
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
                if (setMetadata != null) {
                    setMetadata.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Load Exception (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return ret;
    }

    public int getFinishedPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        int nbplots = 0;

        try {
            Connection conn = getConnection();

            ps = conn.prepareStatement("SELECT Count(*) AS NbPlot FROM plotmePlots WHERE LOWER(world) = ? AND finished <> 0");
            ps.setString(1, world);

            setNbPlots = ps.executeQuery();

            if (setNbPlots.next()) {
                nbplots = setNbPlots.getInt(1);
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

    public int getExpiredPlotCount(String world) {
        PreparedStatement ps = null;
        ResultSet setNbPlots = null;
        int nbplots = 0;

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
                nbplots = setNbPlots.getInt(1);
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
                            "SELECT idX, idZ, owner, finisheddate FROM plotmePlots WHERE LOWER(world) = ? AND finished <> 0 ORDER BY finisheddate "
                                    + "LIMIT ?, ?");
            statementPlot.setString(1, world);
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(2, nbPerPage * (page - 1));
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(3, nbPerPage);

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                PlotId id = new PlotId(setPlots.getInt("idX"), setPlots.getInt("idZ"));
                String owner = setPlots.getString("owner");

                String finisheddate = setPlots.getString("finisheddate");

                Plot plot = new Plot(plugin);
                plot.setOwner(owner);
                plot.setId(id);
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
        Connection conn = getConnection();

        try {
            Calendar cal = Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            Date sqlDate = new Date(utilDate.getTime());

            statementPlot =
                    conn.prepareStatement(
                            "SELECT idX, idZ, owner, expireddate FROM plotmePlots WHERE LOWER(world) = ? AND protected = 0 AND expireddate < ? "
                                    + "ORDER BY expireddate LIMIT ?, ?");
            statementPlot.setString(1, world);
            statementPlot.setDate(2, sqlDate);
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(3, nbPerPage * (page - 1));
            //noinspection JpaQueryApiInspection
            statementPlot.setInt(4, nbPerPage);

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                PlotId id = new PlotId(setPlots.getInt("idX"), setPlots.getInt("idZ"));
                String owner = setPlots.getString("owner");
                //Date expireddate = setPlots.getDate("expireddate");
                Plot plot = new Plot(plugin);
                plot.setOwner(owner);
                plot.setId(id);
                //plot.setExpiredDate(expireddate);

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
                            "SELECT idX, idZ, owner, expireddate FROM plotmePlots WHERE LOWER(world) = ? AND protected = 0 AND expireddate < ? "
                                    + "ORDER BY expireddate LIMIT 1");
            statementPlot.setString(1, world);
            statementPlot.setDate(2, sqlDate);

            setPlots = statementPlot.executeQuery();

            if (setPlots.next()) {
                PlotId id = new PlotId(setPlots.getInt("idX"), setPlots.getInt("idZ"));
                String owner = setPlots.getString("owner");
                Date expireddate = setPlots.getDate("expireddate");
                Plot plot = new Plot(plugin);
                plot.setOwner(owner);
                plot.setId(id);
                //plot.setExpiredDate(expireddate);

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
                PlotId id = new PlotId(rs.getInt("idX"), rs.getInt("idZ"));

                ret.add(getPlot(world, id));
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Error getting all plots:");
            plugin.getLogger().severe(ex.toString());
        }
        return ret;
    }

    /**
     * Get plots the player owns or is allowed
     *
     * @param playerId
     * @return plot list of owned or allowed plots
     */
    public List<Plot> getPlayerPlots(UUID playerId) {
        return getPlayerPlots(playerId, "", false);
    }

    /**
     * Get plots the player owns
     *
     * @param playerId
     * @param world
     *
     * @return plot list of owned plots
     */
    public List<Plot> getOwnedPlots(String world, UUID playerId) {
        return getPlayerPlots(playerId, world, true);
    }

    /**
     * Get plots where the player is allowed or owns.
     *
     * @param playerId
     * @param world
     * @param ownedonly Only get the plots the player owns
     *
     * @return plot list of owned or allowed plots
     */
    private List<Plot> getPlayerPlots(UUID playerId, String world, boolean ownedonly) {
        List<Plot> ret = new ArrayList<>();
        PreparedStatement statementPlot = null;
        PreparedStatement statementAllowed = null;
        PreparedStatement statementDenied = null;
        PreparedStatement statementMetadata = null;
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

            if (ownedonly) {
                query += "A.ownerId = ? ";
            } else {
                query += "(A.ownerId = ? OR B.playerId = ?) ";
            }

            if (!world.isEmpty()) {
                query += "AND world = ?";
            }

            statementPlot = conn.prepareStatement(query);

            statementPlot.setBytes(1, UUIDFetcher.toBytes(playerId));
            if (ownedonly && !world.isEmpty()) {
                statementPlot.setString(3, world.toLowerCase());
            }

            setPlots = statementPlot.executeQuery();

            while (setPlots.next()) {
                PlotId id = new PlotId(setPlots.getInt("idX"), setPlots.getInt("idZ"));
                String biome = setPlots.getString("biome");
                Date expireddate = setPlots.getDate("expireddate");
                boolean finished = setPlots.getBoolean("finished");
                PlayerList allowed = new PlayerList();
                PlayerList denied = new PlayerList();
                double customprice = setPlots.getDouble("customprice");
                boolean forsale = setPlots.getBoolean("forsale");
                String finisheddate = setPlots.getString("finisheddate");
                boolean protect = setPlots.getBoolean("protected");
                String currworld = setPlots.getString("world");
                String owner = setPlots.getString("owner");
                Map<String, Map<String, String>> metadata = new HashMap<>();

                byte[] byOwner = setPlots.getBytes("ownerid");

                UUID ownerId = UUIDFetcher.fromBytes(byOwner);

                statementAllowed = conn.prepareStatement("SELECT * FROM plotmeAllowed WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementAllowed.setString(1, currworld.toLowerCase());
                statementAllowed.setInt(2, id.getX());
                statementAllowed.setInt(3, id.getZ());

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
                statementDenied.setString(1, currworld.toLowerCase());
                statementDenied.setInt(2, id.getX());
                statementDenied.setInt(3, id.getZ());

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

                statementMetadata = conn.prepareStatement("SELECT * FROM plotmeMetadata WHERE LOWER(world) = ? AND idX = ? AND idZ = ?");
                statementMetadata.setString(1, currworld.toLowerCase());
                statementMetadata.setInt(2, id.getX());
                statementMetadata.setInt(3, id.getZ());

                ResultSet setMetadata = statementMetadata.executeQuery();

                while (setMetadata.next()) {
                    String pluginname = setMetadata.getString("pluginname");
                    String propertyname = setMetadata.getString("propertyname");
                    String propertyvalue = setMetadata.getString("propertyvalue");
                    if (!metadata.containsKey(pluginname)) {
                        metadata.put(pluginname, new HashMap<String, String>());
                    }
                    metadata.get(pluginname).put(propertyname, propertyvalue);
                }

                setMetadata.close();

                Plot plot = new Plot(plugin, owner, ownerId, currworld, biome, null, finished, allowed,
                        id, customprice, forsale, finisheddate, protect, denied, metadata, 0, null);

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
                if (statementMetadata != null) {
                    statementMetadata.close();
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

    private boolean executesql(String sql) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            boolean result = statement.execute(sql);
            statement.close();
            conn.commit();
            return result;
        } catch (SQLException ex) {
            return false;
        }
    }

    public void plotConvertToUUIDAsynchronously() {
        plugin.getServerBridge().runTaskLaterAsynchronously(new Runnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Checking if conversion to UUID needed...");
                Statement statementPlayers = null;
                PreparedStatement psOwnerId = null;
                PreparedStatement psAllowedPlayerId0 = null;
                PreparedStatement psAllowedPlayerId1 = null;
                PreparedStatement psAllowedPlayerId2 = null;
                PreparedStatement psAllowedPlayerId3 = null;
                PreparedStatement psAllowedPlayerId4 = null;
                PreparedStatement psDeniedPlayerId0 = null;
                PreparedStatement psDeniedPlayerId1 = null;
                PreparedStatement psDeniedPlayerId2 = null;
                PreparedStatement psDeniedPlayerId3 = null;
                PreparedStatement psDeniedPlayerId4 = null;

                PreparedStatement psDeleteOwner = null;
                PreparedStatement psDeleteAllowed = null;
                PreparedStatement psDeleteDenied = null;

                ResultSet setPlayers = null;

                try {
                    Connection conn = getConnection();

                    //Remove duplicated names
                    executesql("UPDATE plotmeAllowed SET player = lower(player)");
                    executesql("UPDATE plotmeDenied SET player = lower(player)");

                    // Get all the players
                    statementPlayers = conn.createStatement();
                    // Exclude groups and names with * or missing
                    String sql =
                            "SELECT LOWER(owner) AS Name FROM plotmePlots WHERE NOT owner IS NULL AND NOT owner = '' AND ownerid IS NULL GROUP BY "
                                    + "LOWER(owner) ";
                    sql += "UNION SELECT LOWER(player) AS Name FROM plotmeAllowed WHERE NOT player IS NULL AND Not player = '' AND Not player LIKE"
                            + " 'group:%' AND Not player LIKE '%*%' AND playerid IS NULL GROUP BY LOWER(player) ";
                    sql += "UNION SELECT LOWER(player) AS Name FROM plotmeDenied WHERE NOT player IS NULL AND Not player = '' AND Not player LIKE "
                            + "'group:%' AND Not player LIKE '%*%' AND playerid IS NULL GROUP BY LOWER(player) ";
                    sql += "LIMIT " + ((int) UUIDFetcher.PROFILES_PER_REQUEST);

                    setPlayers = statementPlayers.executeQuery(sql);
                    boolean boConversion = false;

                    if (setPlayers.next()) {
                        do {
                            List<String> names = new ArrayList<>();

                            //Prepare delete statements
                            psDeleteOwner = conn.prepareStatement("UPDATE plotmePlots SET owner = '' WHERE LOWER(owner) = ? ");
                            psDeleteAllowed = conn.prepareStatement("DELETE FROM plotmeAllowed WHERE LOWER(player) = ? ");
                            psDeleteDenied = conn.prepareStatement("DELETE FROM plotmeDenied WHERE LOWER(player) = ? ");

                            do {
                                String name = setPlayers.getString("Name");
                                if (!name.isEmpty()) {
                                    if (COMPILE.matcher(name).matches()) {
                                        names.add(name);
                                    } else {
                                        plugin.getLogger().warning("Invalid name found : " + name + ", removing.");
                                        psDeleteOwner.setString(1, name);
                                        psDeleteOwner.executeUpdate();
                                        psDeleteAllowed.setString(1, name);
                                        psDeleteAllowed.executeUpdate();
                                        psDeleteDenied.setString(1, name);
                                        psDeleteDenied.executeUpdate();
                                        conn.commit();
                                    }
                                }
                            } while (setPlayers.next());

                            if (!names.isEmpty()) {
                                UUIDFetcher fetcher = new UUIDFetcher(names);

                                plugin.getLogger().info("Fetching " + names.size() + " UUIDs from Mojang servers...");
                                Map<String, UUID> response = fetcher.call();

                                if (!response.isEmpty()) {

                                    String sqlUpdate = "UPDATE plotmePlots SET ownerid = ?, owner = ? WHERE LOWER(owner) = ? AND ownerid IS NULL";
                                    psOwnerId = conn.prepareStatement(sqlUpdate);

                                    sqlUpdate = "CREATE TABLE IF NOT EXISTS `TEMPPLOTMEALLOWED` (`idX` INTEGER, `idZ` INTEGER, `world` varchar(32));";
                                    psAllowedPlayerId0 = conn.prepareStatement(sqlUpdate);
                                    psAllowedPlayerId0.execute();
                                    psAllowedPlayerId0.close();

                                    sqlUpdate = "DELETE FROM TEMPPLOTMEALLOWED;";
                                    psAllowedPlayerId1 = conn.prepareStatement(sqlUpdate);
                                    sqlUpdate =
                                            "INSERT INTO TEMPPLOTMEALLOWED SELECT idX, idZ, world FROM plotmeAllowed WHERE LOWER(player) = ? OR "
                                                    + "LOWER(player) = ? GROUP BY idX, idZ, world HAVING Count(*) = 2;";
                                    psAllowedPlayerId2 = conn.prepareStatement(sqlUpdate);

                                    if (isUsingMySQL()) {
                                        sqlUpdate =
                                                "DELETE A1.* FROM plotmeAllowed A1 INNER JOIN TEMPPLOTMEALLOWED as A2 ON A1.idX = A2.idX AND A1.idZ"
                                                        + " = A2.idZ AND A1.world = A2.world WHERE LOWER(A1.player) = ?;";
                                    } else {
                                        sqlUpdate =
                                                "DELETE FROM plotmeAllowed WHERE idX || ';' || idZ || ';' || world IN( SELECT A1.idX || ';' || A1"
                                                        + ".idZ || ';' || A1.world "
                                                        +
                                                        "FROM plotmeAllowed A1 INNER JOIN TEMPPLOTMEALLOWED as A2 ON A1.idX = A2.idX AND A1.idZ = "
                                                        + "A2.idZ AND A1.world = A2.world WHERE LOWER(A1.player) = ?)";
                                    }

                                    psAllowedPlayerId3 = conn.prepareStatement(sqlUpdate);
                                    sqlUpdate = "UPDATE plotmeAllowed SET playerid = ?, player = ? WHERE LOWER(player) = ? AND playerid IS NULL";
                                    psAllowedPlayerId4 = conn.prepareStatement(sqlUpdate);


                                    sqlUpdate = "CREATE TABLE IF NOT EXISTS `TEMPPLOTMEDENIED` (`idX` INTEGER, `idZ` INTEGER, `world` varchar(32));";
                                    psDeniedPlayerId0 = conn.prepareStatement(sqlUpdate);
                                    psDeniedPlayerId0.execute();
                                    psDeniedPlayerId0.close();

                                    sqlUpdate = "DELETE FROM TEMPPLOTMEDENIED;";
                                    psDeniedPlayerId1 = conn.prepareStatement(sqlUpdate);
                                    sqlUpdate = "INSERT INTO TEMPPLOTMEDENIED SELECT idX, idZ, world FROM plotmeDenied " +
                                            "WHERE LOWER(player) = ? OR LOWER(player) = ? GROUP BY idX, idZ, world HAVING Count(*) = 2;";
                                    psDeniedPlayerId2 = conn.prepareStatement(sqlUpdate);

                                    if (isUsingMySQL()) {
                                        sqlUpdate =
                                                "DELETE D1.* FROM plotmeDenied D1 INNER JOIN TEMPPLOTMEDENIED as D2 ON D1.idX = D2.idX AND D1.idZ ="
                                                        + " D2.idZ AND D1.world = D2.world WHERE LOWER(D1.player) = ?;";
                                    } else {
                                        sqlUpdate =
                                                "DELETE FROM plotmeDenied WHERE idX || ';' || idZ || ';' || world IN( SELECT A1.idX || ';' || A1"
                                                        + ".idZ || ';' || A1.world "
                                                        +
                                                        "FROM plotmeDenied A1 INNER JOIN TEMPPLOTMEDENIED as A2 ON A1.idX = A2.idX AND A1.idZ = A2"
                                                        + ".idZ AND A1.world = A2.world WHERE LOWER(A1.player) = ?)";
                                    }

                                    psDeniedPlayerId3 = conn.prepareStatement(sqlUpdate);
                                    sqlUpdate = "UPDATE plotmeDenied SET playerid = ?, player = ? WHERE LOWER(player) = ? AND playerid IS NULL";
                                    psDeniedPlayerId4 = conn.prepareStatement(sqlUpdate);

                                    //int nbConverted = 0;
                                    for (String keyname : response.keySet()) {

                                        String oldname;
                                        String newname;

                                        if (keyname.contains(";")) {
                                            oldname = keyname.substring(0, keyname.indexOf(";"));
                                            newname = keyname.substring(keyname.indexOf(";") + 1);
                                        } else {
                                            oldname = keyname;
                                            newname = keyname;
                                        }

                                        UUID uuid = response.get(keyname);

                                        if (uuid != null) {
                                            byte[] byteuuid = UUIDFetcher.toBytes(uuid);
                                            // Owner
                                            psOwnerId.setBytes(1, byteuuid);
                                            psOwnerId.setString(2, newname);
                                            psOwnerId.setString(3, oldname.toLowerCase());
                                            int count = 0;
                                            count += psOwnerId.executeUpdate();
                                            // Allowed
                                            psAllowedPlayerId1.execute();
                                            psAllowedPlayerId2.setString(1, oldname.toLowerCase());
                                            psAllowedPlayerId2.setString(2, newname.toLowerCase());
                                            psAllowedPlayerId2.executeUpdate();
                                            psAllowedPlayerId3.setString(1, newname.toLowerCase());
                                            psAllowedPlayerId3.executeUpdate();
                                            psAllowedPlayerId4.setBytes(1, byteuuid);
                                            psAllowedPlayerId4.setString(2, newname);
                                            psAllowedPlayerId4.setString(3, oldname.toLowerCase());
                                            count += psAllowedPlayerId4.executeUpdate();
                                            // Denied
                                            psDeniedPlayerId1.execute();
                                            psDeniedPlayerId2.setString(1, oldname.toLowerCase());
                                            psDeniedPlayerId2.setString(2, newname.toLowerCase());
                                            psDeniedPlayerId2.executeUpdate();
                                            psDeniedPlayerId3.setString(1, newname.toLowerCase());
                                            psDeniedPlayerId3.executeUpdate();
                                            psDeniedPlayerId4.setBytes(1, byteuuid);
                                            psDeniedPlayerId4.setString(2, newname);
                                            psDeniedPlayerId4.setString(3, oldname.toLowerCase());
                                            count += psDeniedPlayerId4.executeUpdate();
                                            conn.commit();
                                            if (count == 0) {
                                                plugin.getLogger().warning("Unable to update player '" + keyname + "'");
                                            }
                                        } else {
                                            //Couldn't find player at mojang

                                            plugin.getLogger().warning("Name not found at mojang : " + oldname + ", removing.");
                                            psDeleteOwner.setString(1, oldname);
                                            psDeleteOwner.executeUpdate();
                                            psDeleteAllowed.setString(1, oldname);
                                            psDeleteAllowed.executeUpdate();
                                            psDeleteDenied.setString(1, oldname);
                                            psDeleteDenied.executeUpdate();
                                            conn.commit();
                                        }
                                    }


                                    psOwnerId.close();
                                    psAllowedPlayerId1.close();
                                    psDeniedPlayerId1.close();
                                    psAllowedPlayerId2.close();
                                    psDeniedPlayerId2.close();
                                    psAllowedPlayerId3.close();
                                    psDeniedPlayerId3.close();
                                    psAllowedPlayerId4.close();
                                    psDeniedPlayerId4.close();

                                    //Update plot information
                                    for (PlotMapInfo pmi : PlotMeCoreManager.getInstance().getPlotMaps().values()) {
                                        for (Plot plot : pmi.getLoadedPlots().values()) {
                                            for (Entry<String, UUID> player : response.entrySet()) {

                                                String newname = player.getKey();
                                                String oldname = newname;
                                                UUID uuid = player.getValue();

                                                if (newname.contains(";")) {
                                                    oldname = newname.substring(0, newname.indexOf(";"));
                                                    newname = newname.substring(newname.indexOf(";") + 1);
                                                }

                                                //Owner
                                                if (plot.getOwnerId() == null && plot.getOwner().equalsIgnoreCase(oldname)) {
                                                    plot.setOwner(newname);
                                                    plot.setOwnerId(uuid);
                                                }

                                                //Allowed
                                                plot.allowed().replace(oldname, newname, uuid);

                                                //Denied
                                                plot.denied().replace(oldname, newname, uuid);

                                            }
                                        }
                                    }

                                    boConversion = true;
                                }
                            }

                            psDeleteOwner.close();
                            psDeleteAllowed.close();
                            psDeleteDenied.close();

                            setPlayers.close();
                            setPlayers = statementPlayers.executeQuery(sql);

                        } while (setPlayers.next());
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
                } finally {
                    try {
                        if (statementPlayers != null) {
                            statementPlayers.close();
                        }
                        if (psOwnerId != null) {
                            psOwnerId.close();
                        }
                        if (psAllowedPlayerId0 != null) {
                            psAllowedPlayerId0.close();
                        }
                        if (psAllowedPlayerId1 != null) {
                            psAllowedPlayerId1.close();
                        }
                        if (psAllowedPlayerId2 != null) {
                            psAllowedPlayerId2.close();
                        }
                        if (psAllowedPlayerId3 != null) {
                            psAllowedPlayerId3.close();
                        }
                        if (psAllowedPlayerId4 != null) {
                            psAllowedPlayerId4.close();
                        }
                        if (psDeniedPlayerId0 != null) {
                            psDeniedPlayerId0.close();
                        }
                        if (psDeniedPlayerId1 != null) {
                            psDeniedPlayerId1.close();
                        }
                        if (psDeniedPlayerId2 != null) {
                            psDeniedPlayerId2.close();
                        }
                        if (psDeniedPlayerId3 != null) {
                            psDeniedPlayerId3.close();
                        }
                        if (psDeniedPlayerId4 != null) {
                            psDeniedPlayerId4.close();
                        }
                        if (setPlayers != null) {
                            setPlayers.close();
                        }
                        if (psDeleteOwner != null) {
                            psDeleteOwner.close();
                        }
                        if (psDeleteAllowed != null) {
                            psDeleteAllowed.close();
                        }
                        if (psDeleteDenied != null) {
                            psDeleteDenied.close();
                        }

                        PreparedStatement psAllowedPlayerId5 = null;
                        PreparedStatement psDeniedPlayerId5 = null;
                        try {
                            psAllowedPlayerId5 = conn.prepareStatement("DROP TABLE IF EXISTS TEMPPLOTMEALLOWED;");
                            psAllowedPlayerId5.execute();
                            psDeniedPlayerId5 = conn.prepareStatement("DROP TABLE IF EXISTS TEMPPLOTMEDENIED;");
                            psDeniedPlayerId5.execute();
                        } catch (SQLException ee) {
                            plugin.getLogger().severe(ee.getMessage());
                        }

                        if (psAllowedPlayerId5 != null) {
                            psAllowedPlayerId5.close();
                        }

                        if (psDeniedPlayerId5 != null) {
                            psDeniedPlayerId5.close();
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

    private void fetchUUIDAsync(final PlotId id, final String world, final String property, final String name) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {

                PreparedStatement ps = null;

                try {
                    Connection conn = getConnection();

                    IPlayer player = plugin.getServerBridge().getPlayerExact(name);
                    UUID uuid = null;
                    String newname = name;

                    if (player == null) {
                        uuid = UUIDFetcher.getUUIDOf(name);
                    } else {
                        uuid = player.getUniqueId();
                        newname = player.getName();
                    }

                    switch (property) {
                        case "owner":
                            ps = conn.prepareStatement(
                                    "UPDATE plotmePlots SET ownerid = ?, owner = ? WHERE LOWER(owner) = ? AND idX = '" + id.getX() + "' AND idZ = '"
                                            + id.getZ() + "' AND LOWER(world) = '" + world + "'");
                            break;
                        case "allowed":
                            ps = conn.prepareStatement(
                                    "UPDATE plotmeAllowed SET playerid = ?, player = ? WHERE LOWER(player) = ? AND idX = '" + id.getX()
                                            + "' AND idZ = '" + id.getZ() + "' AND LOWER(world) = '" + world + "'");
                            break;
                        case "denied":
                            ps = conn.prepareStatement(
                                    "UPDATE plotmeDenied SET playerid = ?, player = ? WHERE LOWER(player) = ? AND idX = '" + id.getX()
                                            + "' AND idZ = '" + id.getZ() + "' AND LOWER(world) = '" + world + "'");
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
                        Plot plot = PlotMeCoreManager.getInstance().getPlotById(id, world);

                        if (plot != null) {
                            switch (property) {
                                case "owner":
                                    plot.setOwner(newname);
                                    plot.setOwnerId(uuid);
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
        return plugin.getConfig().getBoolean("usemySQL", false);
    }

    public void updatePlotsNewUUID(final UUID uuid, final String newname) {
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                PreparedStatement[] pss = new PreparedStatement[4];
                byte[] uuidInBytes = UUIDFetcher.toBytes(uuid);

                try {
                    Connection conn = getConnection();

                    pss[0] = conn.prepareStatement("UPDATE plotmePlots SET owner = ? WHERE ownerid = ?");
                    pss[2] = conn.prepareStatement("UPDATE plotmeAllowed SET player = ? WHERE playerid = ?");
                    pss[3] = conn.prepareStatement("UPDATE plotmeDenied SET player = ? WHERE playerid = ?");

                    for (PreparedStatement ps : pss) {
                        ps.setString(1, newname);
                        ps.setBytes(2, uuidInBytes);
                        ps.executeUpdate();
                    }

                    conn.commit();

                    for (PreparedStatement ps : pss) {
                        ps.close();
                    }

                } catch (SQLException ex) {
                    plugin.getLogger().severe("Update player in database from uuid failed :");
                    plugin.getLogger().severe(ex.getMessage());
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
                    }
                }
            }
        });
    }

    public boolean savePlotProperty(PlotId id, String world, String pluginname, String property, String value) {
        PreparedStatement ps = null;

        //Plots
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(
                    "SELECT idX FROM plotmeMetadata WHERE idX = ? AND idZ = ? AND world = ? AND pluginname = ? AND propertyname = ?");
            ps.setInt(1, id.getX());
            ps.setInt(2, id.getZ());
            ps.setString(3, world.toLowerCase());
            ps.setString(4, pluginname);
            ps.setString(5, property);

            ResultSet rsProperty = ps.executeQuery();

            if (rsProperty.next()) {
                rsProperty.close();
                ps.close();

                ps = conn.prepareStatement(
                        "UPDATE plotmeMetadata SET propertyvalue = ? WHERE idX = ? AND idZ = ? AND world = ? AND pluginname = ? AND propertyname = "
                                + "?");
                ps.setString(1, value);
                ps.setInt(1, id.getX());
                ps.setInt(2, id.getZ());
                ps.setString(4, world.toLowerCase());
                ps.setString(5, pluginname);
                ps.setString(6, property);

                ps.executeUpdate();
            } else {
                rsProperty.close();
                ps.close();

                ps = conn.prepareStatement(
                        "INSERT INTO plotmeMetadata(idX, idZ, world, pluginname, propertyname, propertyvalue) VALUES(?,?,?,?,?,?)");
                ps.setInt(1, id.getX());
                ps.setInt(2, id.getZ());
                ps.setString(3, world.toLowerCase());
                ps.setString(4, pluginname);
                ps.setString(5, property);
                ps.setString(6, value);

                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            plugin.getLogger().severe("Saving property Exception :");
            plugin.getLogger().severe(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Saving property (on close) :");
                plugin.getLogger().severe(ex.getMessage());
            }
        }
        return false;
    }
}
