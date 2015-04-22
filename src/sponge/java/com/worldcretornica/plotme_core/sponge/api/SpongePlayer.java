package com.worldcretornica.plotme_core.sponge.api;

import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.chat.ChatTypes;

public class SpongePlayer extends SpongeUser implements IPlayer {

    private final Player player;
    private SpongeWorld world;
    private ILocation iLocation;
    private Vector pos;

    public SpongePlayer(Player player) {
        super(player);
        this.player = player;
        world = new SpongeWorld(player.getWorld());
        pos = new Vector(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        iLocation = new ILocation(world, pos);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(ChatTypes.CHAT, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public IWorld getWorld() {
        return world;
    }

    @Override
    public ILocation getLocation() {
        return iLocation;
    }

    @Override
    public void setLocation(ILocation location) {
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public IItemStack getItemInHand() {
        Optional<ItemStack> itemInHand = player.getItemInHand();
        if (itemInHand.isPresent()) {
            return new SpongeItemStack(itemInHand.get());
        } else {
            return null;
        }
    }

    @Override
    public void remove() {
        player.remove();
    }

    @Override
    public Vector getPosition() {
        return pos;
    }
}
