package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IBiome;
import org.spongepowered.api.world.biome.BiomeType;

public class SpongeBiomeType implements IBiome {

    private final BiomeType biomeType;

    public SpongeBiomeType(BiomeType biomeType) {
        this.biomeType = biomeType;
    }

    public BiomeType getBiomeType() {
        return biomeType;
    }
}
