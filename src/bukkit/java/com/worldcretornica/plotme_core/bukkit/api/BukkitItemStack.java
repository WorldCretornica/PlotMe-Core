package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.inventory.ItemStack;

import com.worldcretornica.plotme_core.api.IItemStack;
import com.worldcretornica.plotme_core.api.IMaterial;

public class BukkitItemStack implements IItemStack {

    ItemStack itemstack;
    
    public BukkitItemStack(ItemStack is) {
        this.itemstack = is;
    }

    @Override
    public IMaterial getType() {
        return new BukkitMaterial(itemstack.getType());
    }
}
