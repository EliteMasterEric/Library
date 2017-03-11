package com.mastereric.library.common.world.dimension;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class LibraryWorldGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (chunkX == 0 && chunkZ == 0) {
            generateSpawnPlatform(world);
        }
    }

    private void generateSpawnPlatform(World world) {
        // Center of the chunk.
        int centerX = 8;
        int centerZ = 8;

        // Platform height.
        int baseY = 4;

        // Side length of spawn platform.
        int radius = 8;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                world.setBlockState(new BlockPos(centerX + x, baseY, centerZ + z),
                        Blocks.STONEBRICK.getDefaultState(), 2);
                for (int y = 1; y <= 2; y++) {
                    world.setBlockState(new BlockPos(centerX + x, baseY + y, centerZ + z),
                            Blocks.AIR.getDefaultState(), 2);
                }
            }
        }
    }
}
