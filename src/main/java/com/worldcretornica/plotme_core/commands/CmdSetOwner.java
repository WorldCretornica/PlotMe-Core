package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotOwnerChangeEvent;

import java.util.UUID;

public class CmdSetOwner extends PlotCommand {

    public CmdSetOwner(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "setowner";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length < 2 && args.length >= 3) {
            throw new BadUsageException(getUsage());
        }
        if (args[1].length() > 16 || !validUserPattern.matcher(args[1]).matches()) {
            throw new IllegalArgumentException(C("InvalidCommandInput"));
        }
        IPlayer player = (IPlayer) sender;
        IWorld world = player.getWorld();
        if (player.hasPermission(PermissionNames.ADMIN_SETOWNER) && manager.isPlotWorld(world)) {
            PlotMapInfo pmi = manager.getMap(world);
            Plot plot = manager.getPlot(player);
            if (plot == null) {
                player.sendMessage("Set Owner only works on claimed plots");
                return true;
            }
            String newOwner = null;
            //If the player by the name given is not online, stop the command from executing.
            UUID newOwnerId = null;
            for (IPlayer online : serverBridge.getOnlinePlayers()) {
                if (online.getName().equalsIgnoreCase(args[1])) {
                    newOwner = online.getName();
                    newOwnerId = online.getUniqueId();
                    break;
                }
            }
            if (newOwnerId == null || newOwner == null) {
                player.sendMessage(C("MsgNoPlayerFound"));
                return true;
            }

            UUID oldowner = plot.getOwnerId();


            if (!oldowner.equals(newOwnerId)) {
                PlotOwnerChangeEvent event = new PlotOwnerChangeEvent(world, plot, player, newOwner);
                serverBridge.getEventBus().post(event);

                if (!event.isCancelled()) {
                    plot.setForSale(false);
                    manager.removeSellSign(plot, world);
                    plot.resetExpire(pmi.getDaysToExpiration());
                    plot.updateField("forsale", false);
                    plot.setOwner(newOwner);
                    plot.setOwnerId(newOwnerId);
                    manager.setOwnerSign(world, plot);
                    //todo new function to change the plot owner in database or just modify the plot class to do this.
                    player.sendMessage(C("MsgOwnerChangedTo") + " " + newOwner);
                }
            } else {
                player.sendMessage("This person already owns this plot!"); //TODO add caption for this
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme setowner <" + C("WordPlayer") + ">";
    }
}
