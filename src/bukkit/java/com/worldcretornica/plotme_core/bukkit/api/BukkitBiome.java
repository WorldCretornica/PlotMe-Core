package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBiome;
import org.bukkit.block.Biome;

public class BukkitBiome implements IBiome {

    private Biome biome;
    
    public BukkitBiome(Biome biome) {
        this.biome = biome;
    }
    
    public BukkitBiome(String name) {
        this.biome = Biome.valueOf(name);
    }
    
    @Override
    public String name() {
        return biome.name();
    }
    
    public Biome getBiome() {
        return biome;
    }
}
