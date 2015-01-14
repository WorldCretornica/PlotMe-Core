package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class BukkitPlayer extends BukkitOfflinePlayer implements Player {

    private final org.bukkit.entity.Player player;

    public BukkitPlayer(org.bukkit.entity.Player player) {
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
    public boolean hasPermission(String node) {
        return player.hasPermission(node);
    }

    @Override
    public World getWorld() {
        return new BukkitWorld(player.getWorld());
    }

    @Override
    public Location getLocation() {
        return new BukkitLocation(player.getLocation());
    }

    @Override
    public void setLocation(Location location) {
        player.teleport(((BukkitLocation) location).getLocation());
    }

    public org.bukkit.entity.Player getPlayer() {
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

}
