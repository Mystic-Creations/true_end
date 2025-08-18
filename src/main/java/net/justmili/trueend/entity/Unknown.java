package net.justmili.trueend.entity;

import java.util.EnumSet;
import java.util.Random;

import net.justmili.trueend.network.Variables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Unknown extends AmbientCreature {
    private static final double FOLLOW_RANGE = 128;
    private static final int MAX_VISIBLE_TICKS = 60, COOLDOWN_TICKS = 3600;
    private int visibleTicks = 0, existenceTicks = 0;

    private UnknownBehavior behavior = UnknownBehavior.STALKING;
    private static final EntityDataAccessor<String> TEXTURE_NAME = SynchedEntityData.defineId(Unknown.class, EntityDataSerializers.STRING);

    public Unknown(EntityType<? extends AmbientCreature> type, Level level) {
        super(type, level);
        setPersistenceRequired();
        this.navigation = new GroundPathNavigation(this, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20)
            .add(Attributes.MOVEMENT_SPEED, 0.4)
            .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
            .add(Attributes.ATTACK_DAMAGE, 6);
    }

    @Override public MobType getMobType() { return MobType.UNDEFINED; }
    @Override public boolean isInvulnerableTo(DamageSource source) { return true; }
    @Override public boolean isPushable() { return false; }
    @Override public boolean canBeCollidedWith() { return true; }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TEXTURE_NAME, "unknown_0");
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        behavior = UnknownBehavior.fromString(tag.getString("Behavior"));
        if (tag.getBoolean("doWeeping")) behavior = UnknownBehavior.WEEPING;
        if (tag.getBoolean("doStalking")) behavior = UnknownBehavior.STALKING;
        if (tag.getBoolean("doAttacking")) behavior = UnknownBehavior.ATTACKING;
        setTexture(behavior);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Behavior", behavior.name());
        tag.putBoolean("doWeeping",  behavior == UnknownBehavior.WEEPING);
        tag.putBoolean("doStalking", behavior == UnknownBehavior.STALKING);
        tag.putBoolean("doAttacking",behavior == UnknownBehavior.ATTACKING);
    }

    @Override
    public void tick() {
        super.tick();
        if (++existenceTicks >= COOLDOWN_TICKS && behavior == UnknownBehavior.STALKING) {
            playAndDespawn();
            return;
        }

        Player target = level().getNearestPlayer(this, FOLLOW_RANGE);
        if (target == null) return;

        if (distanceTo(target) < 9 && !target.hasEffect(MobEffects.DARKNESS)) {
            target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false));
        }
        switch (behavior) {
            case STALKING -> doStalking(target);
            case WEEPING -> doWeeping(target);
            case ATTACKING -> doAttacking(target);
            default -> doStalking(target);
        }
    }
    private static class LookAtNearestPlayerGoal extends Goal {
        private final Mob mob;
        LookAtNearestPlayerGoal(Mob mob) {
            this.mob = mob;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return mob.level().getNearestPlayer(mob, FOLLOW_RANGE) != null;
        }
        @Override
        public void tick() {
            Player player = mob.level().getNearestPlayer(mob, FOLLOW_RANGE);
            if (player != null) mob.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ());
        }
    }
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new LookAtNearestPlayerGoal(this));
    }
    public enum UnknownBehavior {
        STALKING, WEEPING, ATTACKING;
        public static UnknownBehavior fromString(String s) {
            try { return valueOf(s.toUpperCase()); }
            catch (IllegalArgumentException e) { return STALKING; }
        }
    }
    private void doStalking(Player player) {
        if (distanceTo(player) <= 16) { playAndDespawn(); return; }
        Vec3 toEntity = position().subtract(player.position()).normalize();
        double angle = Math.toDegrees(Math.acos(toEntity.dot(player.getLookAngle().normalize())));
        if (hasLineOfSight(player) && angle < 12 && ++visibleTicks >= MAX_VISIBLE_TICKS) playAndDespawn();
        else if (!hasLineOfSight(player) || angle >= 12) visibleTicks = 0;
        getNavigation().stop();
    }
    private void doWeeping(Player player) {
        if (distanceTo(player) <= 3) { playAndDespawn(); return; }
        Vec3 toEntity = position().subtract(player.position()).normalize();
        double angle = Math.toDegrees(Math.acos(toEntity.dot(player.getLookAngle().normalize())));
        boolean seen = hasLineOfSight(player) && angle < 30;
        if (!seen) getNavigation().moveTo(player, 0.5025);
        else getNavigation().stop();
    }
    private void doAttacking(Player player) {
        getNavigation().moveTo(player, 0.75);
        if (distanceTo(player) < 1.5 && player.hurt(damageSources().mobAttack(this),
                (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue())) {
            playAndDespawn();
        }
    }
    private void playAndDespawn() {
        getNavigation().stop();
        if (!(level() instanceof ServerLevel server)) return;
        level().playSound(null, blockPosition(), SoundEvents.AMBIENT_CAVE.get(), SoundSource.MASTER, 1, 1);
        Variables.MapVariables.get(server).setUnknownInWorld(false);
        remove(RemovalReason.DISCARDED);
        BlockState state = level().getBlockState(blockPosition());
        level().updateNeighborsAt(blockPosition(), state.getBlock());
    }

    public void setTexture(UnknownBehavior texture) {
        behavior = texture;
        String[] textures;
        switch (texture) {
            case STALKING -> textures = new String[]{ "unknown_0", "unknown_1" };
            case WEEPING -> textures = new String[]{ "unknown_3" };
            case ATTACKING -> textures = new String[]{ "unknown_0", "unknown_2" };
            default -> textures = new String[]{ "unknown_0", "unknown_1" };
        }
        String selected = textures[new Random().nextInt(textures.length)];
        entityData.set(TEXTURE_NAME, selected);
    }
    public String getTextureName() {
        return entityData.get(TEXTURE_NAME);
    }
}