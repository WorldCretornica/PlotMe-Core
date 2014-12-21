package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.Biomes;
import com.worldcretornica.plotme_core.api.IBiome;
import org.bukkit.block.Biome;

public class BukkitBiome implements IBiome {

    private final Biome biome;

    public BukkitBiome(Biome biome) {
        this.biome = biome;
    }

    public BukkitBiome(String name) {
        biome = Biome.valueOf(name);
    }

    public BukkitBiome(Biomes biome) {
        this.biome = Biome.valueOf(biome.toString());
    }

    public Biome getBiome() {
        return biome;
    }

}
