package net.justmili.trueend.block;

import net.justmili.trueend.procedures.GrassBlockOnRightClick;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;

import net.justmili.trueend.init.TrueEndModBlocks;
import net.justmili.trueend.procedures.GrassBlockOnLeftClick;
import net.minecraft.world.phys.BlockHitResult;

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
        return aboveState.getLightBlock(world, abovePos) <= 0;
    }
    
	@Override
	public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
		super.use(blockstate, world, pos, entity, hand, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		double hitX = hit.getLocation().x;
		double hitY = hit.getLocation().y;
		double hitZ = hit.getLocation().z;
		Direction direction = hit.getDirection();
		GrassBlockOnRightClick.execute(world, x, y, z, entity);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void attack(BlockState blockstate, Level world, BlockPos pos, Player entity) {
		super.attack(blockstate, world, pos, entity);
		GrassBlockOnLeftClick.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
	}
}