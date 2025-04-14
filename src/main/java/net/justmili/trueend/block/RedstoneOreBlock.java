package net.justmili.trueend.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.material.MapColor;

import net.justmili.trueend.procedures.RedstoneOreOnInteraction;

import org.joml.Vector3f;

public class RedstoneOreBlock extends Block {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public RedstoneOreBlock() {
		super(BlockBehaviour.Properties.of()
			.mapColor(MapColor.STONE)
			.sound(SoundType.STONE)
			.strength(3f)
			.lightLevel(state -> state.getValue(LIT) ? 9 : 0)
			.randomTicks()
		);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(LIT, false);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LIT);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

@Override
public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (state.getValue(LIT)) {
        for (int i = 0; i < 15; ++i) {
            double dx = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.2;  // Spread on the X-axis
            double dy = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 1.2;  // Spread on the Y-axis
            double dz = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.2;  // Spread on the Z-axis
            level.addParticle(
                new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F),  // Red color, size 1.0F
                dx, dy, dz,  // Particle location with increased spread
                0.0, 0.0, 0.0  // No velocity
            );
        }
    }
}


	@Override
	public void attack(BlockState blockstate, Level world, BlockPos pos, Player entity) {
		super.attack(blockstate, world, pos, entity);
		RedstoneOreOnInteraction.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState blockstate, Entity entity) {
		super.stepOn(world, pos, blockstate, entity);
		RedstoneOreOnInteraction.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void onProjectileHit(Level world, BlockState blockstate, BlockHitResult hit, Projectile projectile) {
		RedstoneOreOnInteraction.execute(world, hit.getBlockPos().getX(), hit.getBlockPos().getY(), hit.getBlockPos().getZ());
	}

	@Override
	public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
		super.use(blockstate, world, pos, entity, hand, hit);
		RedstoneOreOnInteraction.execute(world, pos.getX(), pos.getY(), pos.getZ());
		ItemStack itemstack = entity.getItemInHand(hand);
		return itemstack.getItem() instanceof BlockItem && (new BlockPlaceContext(entity, hand, itemstack, hit)).canPlace() ? InteractionResult.PASS : InteractionResult.SUCCESS;
	}
}
