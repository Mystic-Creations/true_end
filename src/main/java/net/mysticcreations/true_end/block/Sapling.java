package net.mysticcreations.true_end.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.mysticcreations.true_end.world.tree.AlphaTreeGrower;

public class Sapling extends SaplingBlock {

    public Sapling() {
        super(
                new AlphaTreeGrower(),
                BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)
                        .mapColor(MapColor.COLOR_LIGHT_GREEN)
        );
    }

    @Override
    public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
        return groundState.is(BlockTags.DIRT);
    }

    @Override
    public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState groundState = worldIn.getBlockState(blockpos);
        return this.mayPlaceOn(groundState, worldIn, blockpos);
    }
}
