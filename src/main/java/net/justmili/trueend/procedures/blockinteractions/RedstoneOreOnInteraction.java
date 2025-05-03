package net.justmili.trueend.procedures.blockinteractions;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.justmili.trueend.TrueEndMod;
import net.minecraft.util.RandomSource;

public class RedstoneOreOnInteraction {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        BlockPos _pos = BlockPos.containing(x, y, z);
        BlockState _bs = world.getBlockState(_pos);

        // Set the block to lit
        if (_bs.getBlock().getStateDefinition().getProperty("lit") instanceof BooleanProperty _booleanProp)
            world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);

        // Generate a random delay (between 1365 ticks +/- 10% to simulate randomness)
        int randomDelay = 1365 + RandomSource.create().nextInt(273);  // 1365 ticks +/- 10% (136.5 ticks)

        // Schedule the task after the random delay
        TrueEndMod.queueServerWork(randomDelay, () -> {
            // Set the block back to unlit after the random delay
            BlockState _bs2 = world.getBlockState(_pos);
            if (_bs2.getBlock().getStateDefinition().getProperty("lit") instanceof BooleanProperty _booleanProp2)
                world.setBlock(_pos, _bs2.setValue(_booleanProp2, false), 3);
        });
    }
}
