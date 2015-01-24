package com.worldcretornica.plotme_core.sponge.api;

import org.spongepowered.api.world.biome.BiomeType;

import com.worldcretornica.plotme_core.api.IBiome;

public class SpongeBiomeType implements IBiome {

    private final BiomeType biometype;

    public SpongeBiomeType(BiomeType biometype) {
        this.biometype = biometype;
    }

    public BiomeType getBiomeType() {
        return biometype;
    }
}
