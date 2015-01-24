package com.worldcretornica.plotme_core.sponge.api;

import org.spongepowered.api.item.ItemType;

import com.worldcretornica.plotme_core.api.IMaterial;

public class SpongeMaterial implements IMaterial {

    private final ItemType itemtype;
    
    public SpongeMaterial(ItemType itemtype) {
        this.itemtype = itemtype;
    }
    
    public ItemType getItemType() {
        return itemtype;
    }
    
    @Override
    public String getId() {
        return itemtype.getId();
    }
}
