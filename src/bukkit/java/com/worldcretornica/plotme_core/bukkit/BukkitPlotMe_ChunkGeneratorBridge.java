package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.api.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_ChunkGenerator;

public class BukkitPlotMe_ChunkGeneratorBridge implements IPlotMe_ChunkGenerator {

    private final IBukkitPlotMe_ChunkGenerator chunkGenerator;

    public BukkitPlotMe_ChunkGeneratorBridge(IBukkitPlotMe_ChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }
    
    @Override
    public IPlotMe_GeneratorManager getManager() {
        return new BukkitPlotMe_GeneratorManagerBridge(chunkGenerator.getManager());
    }

}
