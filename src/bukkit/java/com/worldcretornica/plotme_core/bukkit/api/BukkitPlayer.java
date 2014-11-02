package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayer extends BukkitOfflinePlayer implements IPlayer {

    private Player player;

    public BukkitPlayer(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public void sendMessage(String msg) {
        player.sendMessage(msg);
    }

    @Override
    public boolean hasPermission(String node) {
        return player.hasPermission(node);
    }

    @Override
    public boolean isOp() {
        return player.isOp();
    }

    @Override
    public IWorld getWorld() {
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
