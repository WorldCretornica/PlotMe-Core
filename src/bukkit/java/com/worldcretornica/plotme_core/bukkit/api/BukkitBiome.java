package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.block.Biome;

import com.worldcretornica.plotme_core.api.IBiome;

public class BukkitBiome implements IBiome {

    Biome biome;
    
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
