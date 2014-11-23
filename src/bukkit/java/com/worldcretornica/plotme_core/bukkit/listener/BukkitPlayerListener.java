/*
 * Copyright (C) 2013
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

public class BukkitPlayerListener implements Listener {

    private final PlotMe_CorePlugin plugin;

    public BukkitPlayerListener(PlotMe_CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        if (!player.isOp()) {
            return;
        }
        Set<String> badWorlds = plugin.getAPI().badWorlds;
        for (String world : badWorlds) {
            if (plugin.getAPI().getGenManager(world) == null) {
                // TODO: Add as multilingual caption
                player.sendMessage("The world " + ChatColor.GOLD + world + ChatColor.RESET + " is defined as a plotworld but does not exist or is not using a PlotMe generator.");
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        BukkitWorld toWorld = new BukkitWorld(event.getTo().getWorld());
        World worlds = toWorld.getWorld();
        if (worlds.equals(event.getFrom().getWorld())) {
            return;
        }
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        if (player.isOp() && plugin.getAPI().badWorlds.contains(toWorld.getName())) {
            if (PlotMe_Core.getGenManager(toWorld) == null) {
                // TODO: Add as multilingual caption
                player.sendMessage("This world is defined as a plotworld but is not using a PlotMe generator.");
            }
        }
    }
}