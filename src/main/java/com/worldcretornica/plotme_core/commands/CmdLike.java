package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdLike extends PlotCommand {

    public CmdLike(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        PlotId id1 = new PlotId(1, 1);
        PlotId id2 = new PlotId(2, 1);
        manager.getGenManager(player.getWorld()).fillMiddleRoad(id1, id2, player.getWorld());
        return true;
    }

}
