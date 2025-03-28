package net.justmili.trueend.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;

import net.justmili.trueend.init.TrueEndModBlocks;

public class GrassBlockBlock extends Block {
    public GrassBlockBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.GRASS).strength(0.6f).randomTicks());
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!canStayGrass(world, pos)) {
            if (!world.isClientSide()) {
                world.setBlock(pos, TrueEndModBlocks.DIRT.get().defaultBlockState(), 3);
            }
        }
    }

    private static boolean canStayGrass(LevelReader world, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = world.getBlockState(abovePos);
        if (aboveState.is(TrueEndModBlocks.TREE_LEAVES.get())) {
            return true;
        }
        if (aboveState.getLightBlock(world, abovePos) > 0) {
            return false;
        } else {
            int light = world.getBrightness(net.minecraft.world.level.LightLayer.SKY, abovePos);
            return light > 8;
        }
    }
}