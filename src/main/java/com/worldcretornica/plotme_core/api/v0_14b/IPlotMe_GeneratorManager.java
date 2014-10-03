package com.worldcretornica.plotme_core.api.v0_14b;

import java.util.List;
import java.util.Map;

import com.worldcretornica.plotme_core.api.*;

public interface IPlotMe_GeneratorManager {

    public String getPlotId(ILocation loc);

    public String getPlotId(IPlayer player);

    public List<IPlayer> getPlayersInPlot(IWorld w, String id);

    public void fillroad(String id1, String id2, IWorld w);

    public void fillmiddleroad(String id1, String id2, IWorld w);

    public void setOwnerDisplay(IWorld world, String id, String Line1, String Line2, String Line3, String Line4);

    public void setSellerDisplay(IWorld world, String id, String Line1, String Line2, String Line3, String Line4);

    public void setAuctionDisplay(IWorld world, String id, String Line1, String Line2, String Line3, String Line4);

    public void removeOwnerDisplay(IWorld world, String id);

    public void removeSellerDisplay(IWorld world, String id);

    public void removeAuctionDisplay(IWorld world, String id);

    public int getIdX(String id);

    public int getIdZ(String id);

    public ILocation getPlotBottomLoc(IWorld world, String id);

    public ILocation getPlotTopLoc(IWorld world, String id);

    public void setBiome(IWorld w, String id, IBiome biome);

    public void refreshPlotChunks(IWorld w, String id);

    public ILocation getTop(IWorld w, String id);

    public ILocation getBottom(IWorld w, String id);

    public void clear(IWorld w, String id);

    public void clear(ILocation bottom, ILocation top);

    public Long[] clear(ILocation bottom, ILocation top, long maxBlocks, boolean clearEntities, Long[] start);

    public Long[] clear(IWorld w, String id, long maxBlocks, boolean clearEntities, Long[] start);

    public void adjustPlotFor(IWorld w, String id, boolean Claimed, boolean Protect, boolean Auctionned, boolean ForSale);

    public boolean isBlockInPlot(String id, ILocation blocklocation);

    public boolean movePlot(IWorld wFrom, IWorld wTo, String idFrom, String idTo);

    public int bottomX(String id, IWorld w);

    public int bottomZ(String id, IWorld w);

    public int topX(String id, IWorld w);

    public int topZ(String id, IWorld w);

    public void regen(IWorld w, String id, ICommandSender sender);

    public ILocation getPlotHome(IWorld w, String id);

    public boolean isValidId(String id);

    public boolean createConfig(String worldname, Map<String, String> args, ICommandSender cs);

    public Map<String, String> getDefaultGenerationConfig();

    public int getPlotSize(String worldname);

    public int getRoadHeight(String worldname);
}
