package com.mastereric.library.init;

import com.mastereric.library.Reference;
import com.mastereric.library.util.LangUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class ModAchivements {
    //public static Achievement activateBomb;
    private static AchievementPage page;

    public static void initializeAchievements() {
        //activateBomb = createAchievement(Reference.NAME_ACHIEVEMENT_ACTIVATE_BOMB,
        //        0, 0, ModBlocks.itemBlockChatBomb);

        page = new AchievementPage(LangUtility.getTranslation(Reference.NAME_ACHIEVEMENT_PAGE));

        AchievementPage.registerAchievementPage(page);
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon) {
        return createAchievement(name, xPos, yPos, icon, null, false);
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon, boolean special) {
        return createAchievement(name, xPos, yPos, icon, null, special);
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon, Achievement parent) {
        return createAchievement(name, xPos, yPos, icon, parent, false);
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon, Achievement parent, boolean special) {
        Achievement achievement = new Achievement("achievement." + name, name, xPos, yPos, icon, parent);
        if (special)
            achievement.setSpecial();
        achievement.registerStat();
        return achievement;
    }

    public static void grantAchivement(EntityPlayer player, Achievement achievement) {
        if (!player.hasAchievement(achievement))
            player.addStat(achievement, 1);
    }
}
