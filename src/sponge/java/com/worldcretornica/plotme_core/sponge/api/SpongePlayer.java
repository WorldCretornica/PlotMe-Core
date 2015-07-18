package com.worldcretornica.plotme_core.sponge.api;

import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.TeleportRunnable;
import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.chat.ChatTypes;

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
        return player.hasPermission(permission);
    }

    @Override
    public IWorld getWorld() {
        return new SpongeWorld(player.getWorld());
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), getPosition());
    }

    @Override
    public void setLocation(Location location) {
        //player.setLocation(location);
    }

    /**
     * Uses the code that allows a delay while
     * "Teleporting" or moving the entity
     *
     * @param location new location
     * @param plugin
     */
    @Override public void teleport(Location location, PlotMe_Core plugin) {
        int anInt = PlotMe_CorePlugin.getInstance()
                .getAPI().getConfig().getInt("tp-delay");
        if (anInt == 0) {
            setLocation(location);
        } else {
            PlotMe_CorePlugin.getInstance().getServerObjectBuilder().runTaskLater(new TeleportRunnable(this, location), anInt);
        }
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
