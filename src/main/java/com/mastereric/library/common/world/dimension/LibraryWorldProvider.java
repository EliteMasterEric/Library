package com.mastereric.library.common.world.dimension;

import com.mastereric.library.Reference;
import com.mastereric.library.init.ModDimensions;
import com.mastereric.library.util.LangUtility;
import mcjty.lib.compat.CompatWorldProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibraryWorldProvider extends CompatWorldProvider {
    private long seed;
    //private Set<String> dimensionTypes = null;  // Used for Recurrent Complex support

    private long calculateSeed(long seed, int dim) {
        return dim * 13L + seed;
    }

    @Override
    public DimensionType getDimensionType() {
        return ModDimensions.dimensionLibrary;
    }

    @Override
    public long getSeed() {
        return super.getSeed();
    }

    @Override
    public String getSaveFolder() {
        return Reference.MOD_ID + getDimension();
    }

    private void setSeed(int dim) {
        seed = calculateSeed(getWorld().getSeed(), dim) ;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        if (biomeProvider == null) {
            createBiomeProvider();
        }
        return biomeProvider;
    }

    @Override
    protected void initialize() {
        if (getWorld() instanceof WorldServer) {
            createBiomeProvider();
            return;
        }

        biomeProvider = null;
    }

    @Override
    public void createBiomeProvider() {
        this.biomeProvider = new LibraryBiomeProvider(getSeed(), getWorld().getWorldInfo());
    }

    @Override
    public double getHorizon() {
        // Use the flat horizon.
        return 0.0D;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public String getWelcomeMessage() {
        return LangUtility.getTranslation(Reference.LANG_LIBRARY_WELCOME);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return player.getSpawnDimension();
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        int dim = getDimension();
        setSeed(dim);
        return new LibraryChunkGenerator(getWorld(), seed);
    }

    @Override
    public Biome getBiomeForCoords(BlockPos pos) {
        return super.getBiomeForCoords(pos);
    }

    @Override
    public int getActualHeight() {
        return 256;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float angle, float dt) {
        return super.getFogColor(angle, dt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1) {
        return super.getSunBrightness(par1) * 0.5f;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1) {
        return 0F;
    }

    @Override
    public void updateWeather() {
        super.updateWeather();
    }

    @Override
    public float calculateCelestialAngle(long time, float dt) {
        return super.calculateCelestialAngle(time, dt);
    }
}
