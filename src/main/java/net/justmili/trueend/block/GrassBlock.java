package net.justmili.trueend.block;

import net.justmili.trueend.procedures.blockinteractions.GrassBlockOnLeftClick;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import net.justmili.trueend.init.TrueEndBlocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class GrassBlock extends Block {
    public GrassBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.GRASS).strength(0.6f).randomTicks());
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!canStayGrass(world, pos)) {
            if (!world.isClientSide()) {
                world.setBlock(pos, TrueEndBlocks.DIRT.get().defaultBlockState(), 3);
            }
        }
    }

    private static boolean canStayGrass(LevelReader world, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = world.getBlockState(abovePos);
        if (aboveState.is(TrueEndBlocks.LEAVES.get())) {
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
        if (entity.getMainHandItem().is(ItemTags.HOES)) {
            world.setBlock(BlockPos.containing(x, y, z), TrueEndBlocks.FARMLAND.get().defaultBlockState(), 3);
            if (world.isClientSide()) {
                world.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("item.hoe.till"))), SoundSource.BLOCKS, 1, 1, false);
            } else {
                world.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("item.hoe.till"))), SoundSource.BLOCKS, 1, 1, false);
            }
            return InteractionResult.SUCCESS;
        }
		return InteractionResult.FAIL;
	}

	@Override
	public void attack(BlockState blockstate, Level world, BlockPos pos, Player entity) {
		super.attack(blockstate, world, pos, entity);
		GrassBlockOnLeftClick.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
	}
}