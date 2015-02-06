package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.schematic.*;
import org.spongepowered.api.world.Location;

import java.io.*;

public class SchematicUtil extends SpongeAbstractSchematicUtil {

    private final PlotMe_Sponge plugin;

    public SchematicUtil(PlotMe_Sponge instance) {
        plugin = instance;
    }

    @Override
    public void pasteSchematic(Location loc, Schematic schem) {

    }

    @Override
    public Schematic loadSchematic(File file) throws IOException, IllegalArgumentException {
        return null;
    }

    @Override
    public Schematic createCompiledSchematic(Location loc1, Location loc2) {
        return null;
    }

    @Override
    public void saveCompiledSchematic(Schematic schem, String name) {

    }

    @Override
    public Schematic loadCompiledSchematic(String name) {
        return null;
    }
}
