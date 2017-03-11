package com.mastereric.library.common.items;

import com.mastereric.library.common.world.dimension.DebugTeleporter;
import com.mastereric.library.init.ModConfig;
import com.mastereric.library.init.ModDimensions;
import com.mastereric.library.util.LogUtility;
import mcjty.lib.compat.CompatItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;

public class ItemLibraryDebug extends CompatItem {
    private double prevPosX;
    private double prevPosY;
    private double prevPosZ;
    private int prevDim;

    public ItemLibraryDebug() {}

    @SuppressWarnings("Duplicates")
    @Override
    protected ActionResult<ItemStack> clOnItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.isRiding() && !player.isBeingRidden() && player.isNonBoss()) {
            if (player.isSneaking()) {
                if (!world.isRemote) {
                    // "Touch" the library dimension to generate the spawn chunk.
                    WorldServer worldServerLibrary = world.getMinecraftServer().worldServerForDimension(ModConfig.DIM_ID_LIBRARY);
                    ChunkProviderServer providerServer = worldServerLibrary.getChunkProvider();
                    if (!providerServer.chunkExists(0, 0)) {
                        try {

                            providerServer.provideChunk(0, 0);
                            providerServer.chunkGenerator.populate(0, 0);
                        } catch (Exception e) {
                            LogUtility.error("Error while creating library dimension.");
                            e.printStackTrace();
                        }
                    }


                    player.setPortal(player.getPosition());
                    double newX, newY, newZ;
                    int dimFrom, dimTo;
                    dimFrom = player.getEntityWorld().provider.getDimension();

                    if (ModDimensions.isDimension(world, ModConfig.DIM_ID_LIBRARY)) {
                        // You are in the library, return to the overworld.
                        // Default to spawn point.
                        newX = world.getSpawnPoint().getX();
                        newY = world.getSpawnPoint().getY();
                        newZ = world.getSpawnPoint().getZ();
                        dimTo = player.getSpawnDimension();
                        NBTTagCompound tag = stack.getTagCompound();
                        if (tag != null) {
                            if (tag.hasKey("prevDim") && tag.getInteger("prevDim") == ModConfig.DIM_ID_LIBRARY) {
                                // The item remembers where you were!
                                newX  = tag.getDouble("prevX");
                                newY  = tag.getDouble("prevY");
                                newZ  = tag.getDouble("prevZ");
                                dimTo = tag.getInteger("prevDim");
                            } else {
                                // The item remembers you as being in the library already though.
                                LogUtility.info("Previous dimension does not exist or was library. Ignoring.");
                            }
                        }
                    } else {
                        newX = 8D;
                        newY = 6D;
                        newZ = 8D;
                        dimTo = ModConfig.DIM_ID_LIBRARY;
                        NBTTagCompound tag = stack.getTagCompound();
                        if (tag != null) {
                            if (tag.hasKey("prevDim") && tag.getInteger("prevDim") == ModConfig.DIM_ID_LIBRARY) {
                                // The item remembers where you were!
                                newX  = tag.getDouble("prevX");
                                newY  = tag.getDouble("prevY");
                                newZ  = tag.getDouble("prevZ");
                                dimTo = tag.getInteger("prevDim");
                            } else {
                                // The item remembers you as being in the library already though.
                                LogUtility.info("Previous dimension does not exist or was library. Ignoring.");
                            }
                        }
                    }
                    DebugTeleporter teleporter = new DebugTeleporter(dimFrom, player.getServer().worldServerForDimension(dimTo),
                            player.getPosition(), new BlockPos(newX, newY, newZ));
                    ModDimensions.transferPlayerToDimension((EntityPlayerMP) player, dimTo, teleporter);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
                }
            } else {
                LogUtility.info("Current dimension: %d", player.getEntityWorld().provider.getDimension());
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }

    private void setPrevious(ItemStack stack, int dimID, BlockPos position) {
        this.prevPosX = position.getX();
        this.prevPosY = position.getY();
        this.prevPosZ = position.getZ();
        this.prevDim = dimID;
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setDouble("prevPosX", this.prevPosX);
        nbtTagCompound.setDouble("prevPosY", this.prevPosY);
        nbtTagCompound.setDouble("prevPosZ", this.prevPosZ);
        nbtTagCompound.setInteger("prevDim", this.prevDim);
    }
}
