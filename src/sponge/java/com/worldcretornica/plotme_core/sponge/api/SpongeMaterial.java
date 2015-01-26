package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IMaterial;
import org.spongepowered.api.item.ItemType;

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
