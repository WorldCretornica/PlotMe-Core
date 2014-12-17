package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.IMaterial;
import org.bukkit.inventory.ItemStack;

public class BukkitItemStack implements IItemStack {

    private final ItemStack itemstack;
    
    public BukkitItemStack(ItemStack is) {
        itemstack = is;
    }

    @Override
    public IMaterial getType() {
        return new BukkitMaterial(itemstack.getType());
    }
}
