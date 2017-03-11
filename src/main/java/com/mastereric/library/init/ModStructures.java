package com.mastereric.library.init;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModStructures {
    public static boolean isReplacable(Material m) {
        return m == Material.AIR || m == Material.PLANTS
                || m == Material.LAVA || m == Material.WATER
                || m == Material.LEAVES || m == Material.SNOW
                || m == Material.VINE;
    }

    public static boolean generatePlatform(World worldIn, BlockPos corner, int sqWidth) {
        if(worldIn.isAirBlock(corner) && worldIn.isAirBlock(corner.down(1))) {
            // make a base from corner x,y,z to +x,y,+z
            for(int i = 0; i < sqWidth; i++) {
                for(int j = 0; j < sqWidth; j++) {
                    BlockPos at = new BlockPos(corner.getX() + i, corner.getY(), corner.getZ() + j);
                    worldIn.setBlockState(at, Blocks.DIRT.getDefaultState(), 2);
                    worldIn.setBlockState(at.down(1), Blocks.BEDROCK.getDefaultState(), 2);
                    worldIn.setBlockState(at.up(1), Blocks.AIR.getDefaultState(), 2);
                    worldIn.setBlockState(at.up(1), Blocks.AIR.getDefaultState(), 2);
                }
            }
            return true;
        }
        return false;
    }

}
