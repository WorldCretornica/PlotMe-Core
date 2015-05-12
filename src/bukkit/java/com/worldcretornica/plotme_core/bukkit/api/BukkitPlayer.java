package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import org.bukkit.Location;
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
    public ILocation getLocation() {
        return new ILocation(getWorld(), getPosition());
    }

    @Override
    public void setLocation(ILocation location) {
        player.teleport(new Location(((BukkitWorld) location.getWorld()).getWorld(), location.getX(), location.getY(), location.getZ()));
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
        return BukkitConverter.locationToVector(player.getLocation());
    }
}
