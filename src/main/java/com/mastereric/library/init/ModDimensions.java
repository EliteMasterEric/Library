package com.mastereric.library.init;

import com.mastereric.library.Reference;
import com.mastereric.library.common.world.dimension.LibraryWorldGenerator;
import com.mastereric.library.common.world.dimension.LibraryWorldProvider;
import com.mastereric.library.common.world.dimension.StructureBookshelfMaze;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModDimensions {
    public static DimensionType dimensionLibrary;

    public static void initializeDimensions() {
        dimensionLibrary = registerDimension(Reference.DIM_NAME_LIBRARY, ModConfig.DIM_ID_LIBRARY,
                LibraryWorldProvider.class, new LibraryWorldGenerator());
        MapGenStructureIO.registerStructure(StructureBookshelfMaze.class, "SHELFMAZE");
        MapGenStructureIO.registerStructureComponent(StructureBookshelfMaze.StructureMaze.class, "SHELFMAZEPART");
    }

    public static DimensionType registerDimension(String name, int id, Class<? extends WorldProvider> worldProvider, IWorldGenerator worldGenerator) {
        DimensionType dimension = DimensionType.register(Reference.MOD_ID, "_"+name, id, worldProvider, false);
        // The second number is generation weight; higher numbers run later.
        GameRegistry.registerWorldGenerator(worldGenerator, 1000);
        DimensionManager.registerDimension(id, dimension);
        return dimension;
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimensionID, Teleporter teleporter) {
        player.getServer().getPlayerList().transferPlayerToDimension(player, dimensionID, teleporter);
    }

    public static boolean isDimension(World w, int id) {
        return w.provider.getDimension() == id;
    }
}
