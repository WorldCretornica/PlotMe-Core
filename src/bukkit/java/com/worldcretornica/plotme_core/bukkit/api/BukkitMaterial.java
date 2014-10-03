package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.Material;

import com.worldcretornica.plotme_core.api.IMaterial;

public class BukkitMaterial implements IMaterial {

    Material material;
    
    public BukkitMaterial(Material mat) {
        this.material = mat;
    }
    
    public Material getMaterial() {
        return material;
    }
}
