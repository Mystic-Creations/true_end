
package net.justmili.trueend.block;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class WoodenTrapdoorBlock extends TrapDoorBlock {
	public WoodenTrapdoorBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(3f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false), BlockSetType.OAK);
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
		if (state.getBlock() instanceof WoodenTrapdoorBlock) {
			if (state.getValue(WoodenTrapdoorBlock.OPEN)) {
				return BlockPathTypes.WALKABLE;
			} else {
				return BlockPathTypes.TRAPDOOR;
			}
		}
		return super.getBlockPathType(state, world, pos, entity);
	}
}
