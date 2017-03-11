package com.mastereric.library.common.world.dimension;

import com.mastereric.library.init.ModStructures;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DebugTeleporter extends Teleporter {
    private int dimFrom;
    private BlockPos posFrom;
    private BlockPos posTo;
    private WorldServer worldServer;

    public DebugTeleporter(int dimFrom, WorldServer worldTo, BlockPos posFrom, BlockPos posTo) {
        super(worldTo);
        this.worldServer = worldTo;
        this.dimFrom = dimFrom;
        this.posFrom = posFrom;
        this.posTo = posTo;
    }

    @Override
    public void placeInPortal(Entity entity, float rotationYaw) {
        // TODO portal generation logic
        ModStructures.generatePlatform(worldServer, posTo, 4);
        entity.setLocationAndAngles(posFrom.getX(), posFrom.getY(), posFrom.getZ(), rotationYaw, entity.rotationPitch);
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, float f) {
        return true;
    }
}
