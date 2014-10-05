package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.entity.Player;

import com.worldcretornica.plotme_core.api.IEntityType;
import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class BukkitPlayer extends BukkitOfflinePlayer implements IPlayer {

    Player player;
    
    public BukkitPlayer(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public void sendMessage(String c) {
        player.sendMessage(c);
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
        player.teleport(((BukkitLocation) location).location);
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
