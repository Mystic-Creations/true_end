
package net.justmili.trueend.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class UnbreakableStone extends Block {
	public UnbreakableStone() {
		super(Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(-1, 3600000).requiresCorrectToolForDrops());
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
		return player.getInventory().getSelected().getItem() instanceof PickaxeItem;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}
