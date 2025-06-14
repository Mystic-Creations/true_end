package net.justmili.trueend.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;

import net.justmili.trueend.init.TrueEndBlocks;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class Dirt extends Block {
    public Dirt() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.5f)
                .randomTicks());
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (canBeGrass(state, world, pos)) {
            if (!world.isClientSide()) {
                world.setBlock(pos, TrueEndBlocks.GRASS_BLOCK.get().defaultBlockState(), 3);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        float pitch = 0.9f + world.getRandom().nextFloat() * 0.2f;

        if (player.getMainHandItem().is(ItemTags.HOES)) {
            world.setBlock(BlockPos.containing(x, y, z), TrueEndBlocks.FARMLAND.get().defaultBlockState(), 3);
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

    private static boolean canBeGrass(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = world.getBlockState(abovePos);

        if (!aboveState.isAir()) {
            return false;
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    BlockPos neighborPos = pos.offset(x, y, z);
                    BlockState neighborState = world.getBlockState(neighborPos);

                    if (neighborState.is(TrueEndBlocks.GRASS_BLOCK.get())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}