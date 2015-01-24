package com.worldcretornica.plotme_core.sponge.api;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Chunk;

import com.worldcretornica.plotme_core.api.IChunk;
import com.worldcretornica.plotme_core.api.IEntity;

public class SpongeChunk implements IChunk {
    
    private final Chunk chunk;
    
    public SpongeChunk(Chunk chunk) {
        this.chunk = chunk;
    }
    
    @Override
    public List<IEntity> getEntities() {
        List<IEntity> entities = new ArrayList<>();
        
        for (Entity e: chunk.getEntities()) {
            entities.add(new SpongeEntity(e));
        }
        
        return entities;
    }

}
