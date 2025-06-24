package net.justmili.trueend.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.sounds.SoundEvents;

import net.justmili.trueend.init.Blocks;
import net.minecraft.world.phys.BlockHitResult;

public class GrassBlock extends Block {
    public GrassBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.GRASS).strength(0.6f).randomTicks());
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!canStayGrass(world, pos)) {
            if (!world.isClientSide()) {
                world.setBlock(pos, Blocks.DIRT.get().defaultBlockState(), 3);
            }
        }
    }

    private static boolean canStayGrass(LevelReader world, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = world.getBlockState(abovePos);
        if (aboveState.is(Blocks.LEAVES.get())) {
            return true;
        }
        return aboveState.getLightBlock(world, abovePos) <= 0;
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        float pitch = 0.9f + world.getRandom().nextFloat() * 0.2f;

        if (player.getMainHandItem().is(ItemTags.HOES)) {
            world.setBlock(BlockPos.containing(x, y, z), Blocks.FARMLAND.get().defaultBlockState(), 3);
            world.playSound(null, x, y, z, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, pitch);
            player.getMainHandItem().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            getSeeds(world, x, y, z, player, player);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public static void getSeeds(LevelAccessor world, double x, double y, double z, Entity entity, Player player) {
        if (entity == null)
            return;
        if (player.getMainHandItem().is(ItemTags.HOES)) {
            if (Math.random() < 0.125) {
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, (y + 1.1), z, new ItemStack(Items.WHEAT_SEEDS));
                    entityToSpawn.setPickUpDelay(15);
                    _level.addFreshEntity(entityToSpawn);
                }
            }
        }
    }
}