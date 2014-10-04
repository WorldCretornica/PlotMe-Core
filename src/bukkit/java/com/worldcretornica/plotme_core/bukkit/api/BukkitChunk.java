package com.worldcretornica.plotme_core.bukkit.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

import com.worldcretornica.plotme_core.api.IChunk;
import com.worldcretornica.plotme_core.api.IEntity;

public class BukkitChunk implements IChunk {

    private Chunk chunk;
    
    public BukkitChunk(Chunk chunk) {
        this.chunk = chunk;
    }
    
    @Override
    public List<IEntity> getEntities() {
        List<IEntity> entities = new ArrayList<>();
        
        for(Entity e : chunk.getEntities()) {
            entities.add(new BukkitEntity(e));
        }
        
        return entities; 
    }
}
