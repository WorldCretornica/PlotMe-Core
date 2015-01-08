package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.*;
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
    public boolean hasPermission(String node) {
        return player.hasPermission(node);
    }

    @Override
    public World getWorld() {
        return new BukkitWorld(player.getWorld());
    }

    @Override
    public ILocation getLocation() {
        return new BukkitLocation(player.getLocation());
    }

    @Override
    public void teleport(ILocation location) {
        player.teleport(((BukkitLocation) location).getLocation());
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
    public IEntityType getType() {
        return new BukkitEntityType(player.getType());
    }
}
