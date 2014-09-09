package com.worldcretornica.plotme_core.api.v0_14b;

import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface IPlotMe_GeneratorManager {

    public String getPlotId(Location loc);

    public String getPlotId(Player player);

    public List<Player> getPlayersInPlot(World w, String id);

    public void fillroad(String id1, String id2, World w);

    public void fillmiddleroad(String id1, String id2, World w);

    public void setOwnerDisplay(World world, String id, String Line1, String Line2, String Line3, String Line4);

    public void setSellerDisplay(World world, String id, String Line1, String Line2, String Line3, String Line4);

    public void setAuctionDisplay(World world, String id, String Line1, String Line2, String Line3, String Line4);

    public void removeOwnerDisplay(World world, String id);

    public void removeSellerDisplay(World world, String id);

    public void removeAuctionDisplay(World world, String id);

    public int getIdX(String id);

    public int getIdZ(String id);

    public Location getPlotBottomLoc(World world, String id);

    public Location getPlotTopLoc(World world, String id);

    public void setBiome(World w, String id, Biome b);

    public void refreshPlotChunks(World w, String id);

    public Location getTop(World w, String id);

    public Location getBottom(World w, String id);

    public void clear(World w, String id);

    public void clear(Location bottom, Location top);

    public Long[] clear(Location bottom, Location top, long maxBlocks, boolean clearEntities, Long[] start);

    public Long[] clear(World w, String id, long maxBlocks, boolean clearEntities, Long[] start);

    public void adjustPlotFor(World w, String id, boolean Claimed, boolean Protect, boolean Auctionned, boolean ForSale);

    public boolean isBlockInPlot(String id, Location blocklocation);

    public boolean movePlot(World wFrom, World wTo, String idFrom, String idTo);

    public int bottomX(String id, World w);

    public int bottomZ(String id, World w);

    public int topX(String id, World w);

    public int topZ(String id, World w);

    public void regen(World w, String id, CommandSender sender);

    public Location getPlotHome(World w, String id);

    public boolean isValidId(String id);

    public boolean createConfig(String worldname, Map<String, String> args, CommandSender cs);

    public Map<String, String> getDefaultGenerationConfig();

    public int getPlotSize(String worldname);

    public int getRoadHeight(String worldname);
}
