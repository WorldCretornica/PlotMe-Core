package com.worldcretornica.plotme_core.utils;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.inventory.InventoryHolder;

public class ChunkEntry {

    private final ChunkCoords chunk;
    private final IBlock[] materials;
    private final IWorld world;
    private final Vector min;
    private final Vector bottom;
    private final Vector top;

    public ChunkEntry(ChunkCoords chunk, IBlock[] materials, ClearEntry entry, Vector min) {
        this.chunk = chunk;
        this.materials = materials;
        this.world = entry.getPlot().getWorld();
        this.min = min;
        this.bottom = entry.getPlot().getPlotBottomLoc();
        this.top = entry.getPlot().getPlotTopLoc();

    }

    public void run() {
        ((BukkitWorld) world).getWorld().regenerateChunk(chunk.getX(), chunk.getZ());
        for (int x = 0; x < 16; ++x) {
            for (int y = 0; y < 256; ++y) {
                for (int z = 0; z < 16; ++z) {
                    Vector pt = min.add(x, y, z);
                    int index = y * 256 + z * 16 + x;
                    int lowestX = Math.min(bottom.getBlockX() + 1, top.getBlockX() - 1);
                    int highestX = Math.max(bottom.getBlockX() + 1, top.getBlockX() - 1);
                    int lowestZ = Math.min(bottom.getBlockZ() - 1, top.getBlockZ() + 1);
                    int highestZ = Math.max(bottom.getBlockZ() - 1, top.getBlockZ() + 1);

                    boolean contains =
                            pt.getBlockX() >= lowestX && pt.getBlockX() <= highestX && pt.getBlockZ() >= lowestZ && pt.getBlockZ() <= highestZ;
                    if (!contains) {
                        BukkitBlock block = ((BukkitBlock) materials[index]);
                        BukkitBlock blockAt = (BukkitBlock) world.getBlockAt(pt);
                        blockAt.setTypeIdAndData((short) block.getTypeId(), block.getData(), false);
                        if (block.getState() instanceof InventoryHolder) {
                            if (blockAt.getState() instanceof InventoryHolder) {
                                ((InventoryHolder) blockAt.getState()).getInventory()
                                        .setContents(((InventoryHolder) block.getState()).getInventory().getContents());
                            }
                        }
                    }
                }
            }
        }
        world.refreshChunk(chunk.getX(), chunk.getZ());

    }
}
