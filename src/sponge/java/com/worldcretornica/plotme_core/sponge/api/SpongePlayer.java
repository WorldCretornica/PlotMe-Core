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
import org.spongepowered.api.util.Tristate;

public class SpongePlayer extends SpongeUser implements IPlayer {

    private final Player player;

    public SpongePlayer(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(ChatTypes.CHAT, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.getData().setPermission(player.getActiveContexts(), permission, Tristate.UNDEFINED);
    }

    @Override
    public IWorld getWorld() {
        return new SpongeWorld(player.getWorld());
    }

    @Override
    public ILocation getLocation() {
        return new ILocation(getWorld(), getPosition());
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
        return new Vector(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
    }
}
