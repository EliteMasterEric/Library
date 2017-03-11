package com.mastereric.library.common.world.dimension;

import com.mastereric.library.util.LogUtility;
import mcjty.lib.compat.CompatChunkGenerator;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LibraryChunkGenerator implements CompatChunkGenerator {
    private final World world;
    private final Random random;
    private final LibraryTerrainGenerator terrainGenerator;
    private MapGenBookshelfMaze mapGenBookshelfMaze;

    public LibraryChunkGenerator(World world, long seed) {
        this.world = world;
        this.random = new Random(seed);

        this.terrainGenerator = new LibraryTerrainGenerator();
        terrainGenerator.setup(world, this);

        this.mapGenBookshelfMaze = new MapGenBookshelfMaze();


        world.setSeaLevel(4);
    }

    @Override
    public Chunk provideChunk(int x, int z)  {
        ChunkPrimer chunkPrimer = new ChunkPrimer();

        this.terrainGenerator.generate(this.world, x, z, chunkPrimer);

        Chunk chunk = new Chunk(this.world, chunkPrimer, x, z);

        // What does this even do... McJty explain plz.
        Biome[] abiome = this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();
        for (int l = 0; l < abyte.length; ++l) {
            abyte[l] = (byte)Biome.getIdForBiome(abiome[l]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int chunkX, int chunkZ) {
        net.minecraft.block.BlockFalling.fallInstantly = true;
        int i = chunkX * 16;
        int j = chunkZ * 16;
        //Biome biome = this.world.getBiomeForCoordsBody(new BlockPos(i + 16, 0, j + 16));
        this.random.setSeed(this.world.getSeed());
        long rand1 = this.random.nextLong() / 2L * 2L + 1L;
        long rand2 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)chunkX * rand1 + (long)chunkZ * rand2 ^ this.world.getSeed());

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(this, this.world, this.random, chunkX, chunkZ, false));

        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

        // GENERATE FEATURES AND STRUCTURES HERE
        // Everything but ocean temples...
        LogUtility.info("populate()");
        mapGenBookshelfMaze.generateStructure(world, random, new ChunkPos(chunkX, chunkZ));

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(this, this.world, this.random, chunkX, chunkZ, false));
        net.minecraft.block.BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        // Ocean Structure is generated here for some reason...
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        // I want nothing spawning here, give an empty list!
        return new ArrayList<Biome.SpawnListEntry>();
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
        // IDK what this does... recreate structures I guess?
    }
    @Override
    public BlockPos clGetStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }
    @Override
    public BlockPos getStrongholdGen(World world, String structureName, BlockPos position, boolean unexplored) {
        return clGetStrongholdGen(world, structureName, position);
    }

}