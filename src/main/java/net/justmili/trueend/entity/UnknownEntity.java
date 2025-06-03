package net.justmili.trueend.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ambient.AmbientCreature;

public class UnknownEntity extends AmbientCreature {

    private static final double WALK_SPEED = 0.1D;
    private static final double VIEW_CONE_THRESHOLD = 0.809;
    private static final int MAX_VISIBLE_TICKS = 3 * 20;

    private int visibleTicks = 0;

    public UnknownEntity(EntityType<? extends AmbientCreature> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.navigation = new GroundPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, WALK_SPEED)
                .add(Attributes.FOLLOW_RANGE, 96.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 96.0F));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInWaterOrBubble()) {
            this.discard();
            return;
        }

        Player nearest = this.level().getNearestPlayer(this, 96.0D);
        if (nearest == null) return;

        if (this.distanceTo(nearest) <= 8.0D) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.AMBIENT_CAVE.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
            this.discard();
            return;
        }

        Vec3 toEntity = this.position().subtract(nearest.position()).normalize();
        Vec3 playerLook = nearest.getLookAngle().normalize();
        double dot = toEntity.dot(playerLook);

        if (this.hasLineOfSight(nearest) && dot > VIEW_CONE_THRESHOLD) {
            visibleTicks++;
            if (visibleTicks >= MAX_VISIBLE_TICKS) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.AMBIENT_CAVE.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                this.discard();
                return;
            }
        } else {
            visibleTicks = 0;
        }

        BlockPos mobPos = this.blockPosition();
        int radius = 16;
        BlockPos nearestWood = null;

        Block trueEndWood = BuiltInRegistries.BLOCK.get(new ResourceLocation("true_end", "wood"));

        outer:
        for (int x = -radius; x <= radius; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = mobPos.offset(x, y, z);
                    BlockState state = this.level().getBlockState(pos);
                    if (state.is(trueEndWood)) {
                        nearestWood = pos;
                        break outer;
                    }
                }
            }
        }

        if (nearestWood != null) {
            this.getNavigation().moveTo(
                nearestWood.getX() + 0.5,
                nearestWood.getY(),
                nearestWood.getZ() + 0.5,
                WALK_SPEED
            );
        } else {
            this.getNavigation().stop();
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
}