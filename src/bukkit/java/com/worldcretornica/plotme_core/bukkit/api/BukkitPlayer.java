package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.TeleportRunnable;
import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.BukkitUtil;
import org.bukkit.entity.Player;

public class BukkitPlayer extends BukkitOfflinePlayer implements IPlayer {

    private final Player player;

    public BukkitPlayer(Player player) {
        super(player);
        this.player = player;
    }

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public IWorld getWorld() {
        return new BukkitWorld(player.getWorld());
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), getPosition());
    }

    @Override
    public void setLocation(Location location) {
        player.teleport(BukkitUtil.adapt(location));
    }

    /**
     * Uses the code that allows a delay while
     * "Teleporting" or moving the entity
     *
     * @param location new location
     * @param plugin
     */
    @Override public void teleport(Location location, PlotMe_Core plugin) {
        if (plugin.getConfig().getInt("tp-delay") != 0) {
            player.sendMessage(String.format("You will be teleported in %d seconds.", plugin.getConfig().getInt("tp-delay")));
            plugin.getServerBridge().runTaskLater(new TeleportRunnable(this, location), plugin.getConfig().getInt("tp-delay"));
        } else {
            setLocation(location);
        }
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public IItemStack getItemInHand() {
        return new BukkitItemStack(player.getItemInHand());
    }

    @Override
    public void remove() {
        player.remove();
    }

    @Override
    public String toString() {
        return "Bukkit Player{ name= " + getName() + " uuid = " + getUniqueId().toString() + " }";
    }

    public Vector getPosition() {
        return BukkitUtil.locationToVector(player.getLocation());
    }
}
