package com.mastereric.library.common.world.dimension;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

public class LibraryTerrainGenerator {
    private World world;
    private IChunkGenerator generator;
    private final IBlockState[] cachedLayers = new IBlockState[256];

    public void setup(World world, LibraryChunkGenerator generator) {
        this.world = world;
        this.generator = generator;

        cachedLayers[0] = Blocks.BEDROCK.getDefaultState();
        cachedLayers[1] = Blocks.DIRT.getDefaultState();
        cachedLayers[2] = Blocks.DIRT.getDefaultState();
        cachedLayers[3] = Blocks.DIRT.getDefaultState();
        cachedLayers[4] = Blocks.QUARTZ_BLOCK.getDefaultState();
    }

    public void generate(World world, int chunkX, int chunkZ, ChunkPrimer chunkPrimer) {
        for (int y = 0; y < this.cachedLayers.length; y++) {
            IBlockState iblockstate = this.cachedLayers[y];

            if (iblockstate != null) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        chunkPrimer.setBlockState(x, y, z, iblockstate);
                    }
                }
            }
        }
    }
}
