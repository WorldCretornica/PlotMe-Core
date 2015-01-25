package com.worldcretornica.plotme_core.sponge.api;

import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

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
        return player.hasPermission(node);
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
        player.setLocation(((SpongeLocation) location).getLocation());
    }
    
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public IItemStack getItemInHand() {
        Optional<ItemStack> itemInHand = player.getItemInHand();
        if (itemInHand.isPresent()) {
            return new SpongeItemStack(itemInHand.get());
        } else
            return null;
    }
    
    @Override
    public void remove() {
        player.remove();
    }
}
