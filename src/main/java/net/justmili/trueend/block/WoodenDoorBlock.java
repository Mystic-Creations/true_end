package net.justmili.trueend.block;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class WoodenDoorBlock extends DoorBlock {
    public WoodenDoorBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).sound(SoundType.WOOD).strength(3f).noOcclusion().isRedstoneConductor((state, reader, pos) -> false), BlockSetType.OAK);
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0;
    }
    
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
		if (state.getBlock() instanceof WoodenDoorBlock) {
			if (state.getValue(WoodenDoorBlock.OPEN)) {
				return BlockPathTypes.DOOR_OPEN;
			} else {
				return BlockPathTypes.DOOR_WOOD_CLOSED;
			}
		}
		return super.getBlockPathType(state, world, pos, entity);
	}
}
