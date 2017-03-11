package com.mastereric.library.common.world.dimension;

import com.mastereric.library.util.LogUtility;
import mcjty.lib.compat.CompatMapGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class MapGenBookshelfMaze extends CompatMapGenStructure {

    public MapGenBookshelfMaze() {
    }

    @Override
    public BlockPos clGetClosestStrongholdPos(World worldIn, BlockPos position) {
        return null;
    }

    @Override
    public String getStructureName() {
        return "SHELFMAZE";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        LogUtility.info("canSpawnStructureAtCoords(%d, %d)", chunkX, chunkZ);
        return chunkX % 10 == 5 && chunkZ % 10 == 5;
    }

    @Override
    public boolean generateStructure(World worldIn, Random randomIn, ChunkPos chunkCoord) {
        // This gets called, but canSpawnStructure and getStructureStart
        LogUtility.info("generateStructure()");
        return super.generateStructure(worldIn, randomIn, chunkCoord);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        //Biome biome = this.world.getBiome(new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8));
        LogUtility.info("getStructureStart()");
        return new StructureBookshelfMaze(this.world, this.rand, chunkX, chunkZ);
    }
}