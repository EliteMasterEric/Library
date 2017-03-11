package com.mastereric.library.proxy;

import com.mastereric.library.init.*;
import com.mastereric.library.util.LogUtility;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		LogUtility.info("Performing common initialization.");
		// Add achievements.
        ModItems.initializeItems();
        ModBlocks.initializeBlocks();
		ModEntities.initializeEntities();
		ModRecipes.initializeCraftingRecipes();
		ModAchivements.initializeAchievements();
		ModDimensions.initializeDimensions();

		ModConfig.config = new Configuration(new File(event.getModConfigurationDirectory().getPath(), "library.cfg"));
		ModConfig.parseConfig();
	}

	public void init(FMLInitializationEvent event) {
	    // Register GUI handler.
		//NetworkRegistry.INSTANCE.registerGuiHandler(Library.instance, new GuiHandler());
		//MinecraftForge.EVENT_BUS.register(new AchievementHandler());
		//MinecraftForge.EVENT_BUS.register(new ChatMessageHandler());
	}

	public void postInit(FMLPostInitializationEvent e) {
		ModConfig.saveConfig();
		// Initialize compatibility!
		ModCompat.initializeCompat();
	}
}
