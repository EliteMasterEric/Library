package com.mastereric.library.init;


import com.mastereric.library.util.LogUtility;
import net.minecraftforge.common.config.Configuration;

public class ModConfig {
    public static Configuration config;

    public static int DIM_ID_LIBRARY = 76;

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_GENERAL_DESC = "General configuration";

    private static final String DIM_ID_LIBRARY_NAME = "dimensionIDLibrary";
    private static final String DIM_ID_LIBRARY_DESC = "Dimension ID for Library. Only change for mod compatibility.";
    // Min of 2 means you can't override vanilla
    private static final int DIM_ID_LIBRARY_MIN = 2;
    private static final int DIM_ID_LIBRARY_MAX = 100000;

    public static void parseConfig() {
        if (config != null) {
            try {
                config.load();
                parseConfigGeneral();
            } catch (Exception e) {
                LogUtility.error("Error loading config file! %s", e);
            } finally {
                saveConfig();
            }
        }
    }

    public static void saveConfig() {
        if (config != null) {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static void parseConfigGeneral() {
        config.addCustomCategoryComment(CATEGORY_GENERAL, CATEGORY_GENERAL_DESC);
        DIM_ID_LIBRARY = config.getInt(DIM_ID_LIBRARY_NAME, CATEGORY_GENERAL, DIM_ID_LIBRARY, DIM_ID_LIBRARY_MIN, DIM_ID_LIBRARY_MAX, DIM_ID_LIBRARY_DESC);
    }
}
