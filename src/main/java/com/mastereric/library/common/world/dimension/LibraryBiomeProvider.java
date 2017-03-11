package com.mastereric.library.common.world.dimension;

import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.storage.WorldInfo;

public class LibraryBiomeProvider extends BiomeProvider {
    private long seed;

    public LibraryBiomeProvider(long seed, WorldInfo worldInfo) {
        super(worldInfo);
        this.seed = seed;
    }
}
