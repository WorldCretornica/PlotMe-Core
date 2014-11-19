package com.worldcretornica.plotme_core.bukkit;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public class MultiverseWrapper implements Delegate<MultiverseCore> {

    private final MultiverseCore multiverseCore;

    public MultiverseWrapper(JavaPlugin multiverseCore) {
        if (!(multiverseCore instanceof MultiverseCore)) {
            throw new IllegalArgumentException("JavaPlugin must be castable to MultiverseCore");
        }
        this.multiverseCore = (MultiverseCore) multiverseCore;
    }

    @Override
    public MultiverseCore getDelegate() {
        return multiverseCore;
    }

    public final boolean isEnabled() {
        return multiverseCore.isEnabled();
    }

    public MVWorldManagerWrapper getMVWorldManager() {
        return new MVWorldManagerWrapper(multiverseCore.getMVWorldManager());
    }

    public static class MVWorldManagerWrapper implements Delegate<MVWorldManager> {

        private final MVWorldManager worldManager;

        public MVWorldManagerWrapper(MVWorldManager worldManager) {
            this.worldManager = worldManager;
        }

        @Override
        public MVWorldManager getDelegate() {
            return worldManager;
        }

        public boolean addWorld(String name, World.Environment environment, String seedString, WorldType type, boolean generateStructures, String generator) {
            return worldManager.addWorld(name, environment, seedString, type, generateStructures, generator);
        }

    }

}
