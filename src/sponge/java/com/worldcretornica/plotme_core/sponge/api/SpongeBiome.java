package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IBiome;
import org.spongepowered.api.world.biome.BiomeType;

public class SpongeBiome implements IBiome {

    private final BiomeType biome;

    public SpongeBiome(BiomeType biome) {
        this.biome = biome;
    }

    String getName() {
        return biome.getName();
    }
}
