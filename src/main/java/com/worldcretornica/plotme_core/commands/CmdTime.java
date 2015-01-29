package com.worldcretornica.plotme_core.commands;


import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdTime extends PlotCommand {


    public CmdTime(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("plotme.user.time")) {
            if (manager.isPlotWorld(player.getWorld())) {
                return true;
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            return false;
        }
    }
}
