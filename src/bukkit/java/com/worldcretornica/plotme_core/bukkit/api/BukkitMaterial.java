package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IMaterial;
import org.bukkit.Material;

public class BukkitMaterial implements IMaterial {

    final Material material;

    public BukkitMaterial(Material mat) {
        material = mat;
    }

    public Material getMaterial() {
        return material;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getId() {
        return material.getId();
    }
}
