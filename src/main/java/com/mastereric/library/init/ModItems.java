package com.mastereric.library.init;

import com.mastereric.library.Library;
import com.mastereric.library.Reference;
import com.mastereric.library.common.items.ItemLibraryDebug;
import com.mastereric.library.util.LogUtility;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModItems {
    public static Item itemMod;
    public static Item itemModDumb;

    public static void initializeItems() {
        LogUtility.info("Initializing items.");
        itemMod = new ItemLibraryDebug();
        registerItem(itemMod, Reference.NAME_ITEM_DEBUG);
    }

    @SideOnly(Side.CLIENT)
    public static void initializeItemModels() {
        LogUtility.info("Initializing item models.");
        // Run this on the ClientProxy after running initializeItems.
        //registerItemModel(itemModDumb);
    }

    private static void registerItem(Item item, String registryName) {
        registerItem(item, registryName, true);
    }

    private static void registerItem(Item item, String registryName, boolean creativeTab) {
        // Set the registry name.
        item.setRegistryName(Reference.MOD_ID, registryName);
        item.setUnlocalizedName(Reference.MOD_ID + "." + registryName);
        // Add to the game registry.
        GameRegistry.register(item);

        if(creativeTab)
            item.setCreativeTab(Library.creativeTab);

        LogUtility.info("Registered item ~ %s", item.getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Item item) {
        // Function overloads make everything simpler.
        registerItemModel(item, 0);
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Item item, int metadata) {
        registerItemModel(item, metadata, item.getRegistryName().toString());
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Item item, int metadata, String modelName) {
        // Register the item model.
        ModelLoader.setCustomModelResourceLocation(item, metadata,
                new ModelResourceLocation(modelName, "inventory"));

        LogUtility.info("Registered item model ~ %s#%s", modelName, "inventory");
    }
}