package net.justmili.trueend.entity;

import java.util.EnumSet;
import java.util.Random;

import net.justmili.trueend.network.Variables;
import net.minecraft.nbt.CompoundTag;
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
    private String textureName;

    public Unknown(EntityType<? extends AmbientCreature> type, Level level) {
        super(type, level);
        setPersistenceRequired();
        this.navigation = new GroundPathNavigation(this, level);
        setBehavior(behavior);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20)
            .add(Attributes.MOVEMENT_SPEED, 0.38)
            .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
            .add(Attributes.ATTACK_DAMAGE, 6);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new LookAtNearestPlayerGoal(this));
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
            target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, true));
        }

        switch (behavior) {
            case STALKING -> doStalking(target);
            case WEEPING -> doWeeping(target);
            case ATTACKING -> doAttacking(target);
        }
    }

    private void doStalking(Player p) {
        if (distanceTo(p) <= 6) {
            playAndDespawn();
            return;
        }
        Vec3 toEntity = position().subtract(p.position()).normalize();
        double angle = Math.toDegrees(Math.acos(toEntity.dot(p.getLookAngle().normalize())));
        if (hasLineOfSight(p) && angle < 18 && ++visibleTicks >= MAX_VISIBLE_TICKS) playAndDespawn();
        else if (!hasLineOfSight(p) || angle >= 18) visibleTicks = 0;
        getNavigation().stop();
    }

    private void doWeeping(Player p) {
        if (distanceTo(p) <= 8) {
            playAndDespawn();
            return;
        }
        Vec3 toEntity = position().subtract(p.position()).normalize();
        double angle = Math.toDegrees(Math.acos(toEntity.dot(p.getLookAngle().normalize())));
        boolean seen = hasLineOfSight(p) && angle < 18;
        if (!seen) getNavigation().moveTo(p, 0.5025);
        else getNavigation().stop();
    }

    private void doAttacking(Player p) {
        getNavigation().moveTo(p, 0.75);
        if (distanceTo(p) < 1.5 && p.hurt(damageSources().mobAttack(this),
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

    @Override public boolean isInvulnerableTo(DamageSource src) { return true; }
    @Override public MobType getMobType() { return MobType.UNDEFINED; }
    @Override public boolean isPushable() { return false; }
    @Override public boolean canBeCollidedWith() { return true; }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        behavior = UnknownBehavior.fromString(tag.getString("Behavior"));
        textureName = tag.contains("TextureName", CompoundTag.TAG_STRING) ? tag.getString("TextureName") : textureName;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Behavior", behavior.name());
        if (textureName != null && !textureName.isEmpty()) tag.putString("TextureName", textureName);
    }

    public void setBehavior(UnknownBehavior b) {
        behavior = b;
        String[] textures = {"unknown_0", "unknown_1", "unknown_2", "unknown_3", "unknown_4"};
        textureName = textures[new Random().nextInt(textures.length)];
        System.out.println("Assigned texture: " + textureName + " for behavior: " + behavior);
    }

    public String getTextureName() { return textureName; }

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
            Player p = mob.level().getNearestPlayer(mob, FOLLOW_RANGE);
            if (p != null) mob.getLookControl().setLookAt(p.getX(), p.getEyeY(), p.getZ());
        }
    }

    public enum UnknownBehavior {
        STALKING, WEEPING, ATTACKING;

        public static UnknownBehavior fromString(String s) {
            try { return valueOf(s.toUpperCase()); }
            catch (IllegalArgumentException e) { return STALKING; }
        }
    }
}