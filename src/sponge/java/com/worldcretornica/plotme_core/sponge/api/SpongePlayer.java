package com.worldcretornica.plotme_core.sponge.api;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class SpongePlayer extends SpongeUser implements IPlayer {

    private final Player player;
    
    public SpongePlayer(Player player) {
        super(player);
        this.player = player;
    }
    
    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }
    
    @Override
    public boolean hasPermission(String node) {
        return false; //TODO
    }
    
    @Override
    public IWorld getWorld() {
        return new SpongeWorld(player.getWorld());
    }
    
    @Override
    public ILocation getLocation() {
        return new SpongeLocation(player.getLocation());
    }
    
    @Override
    public void setLocation(ILocation location) {
        player.teleport(((SpongeLocation) location).getLocation());
    }
    
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public IItemStack getItemInHand() {
        Optional<ItemStack> itemstack = player.getItemInHand();
        if (itemstack.isPresent())
            return new SpongeItemStack(itemstack.get());
        else
            return null;
    }
    
    @Override
    public void remove() {
        player.remove();
    }
}
