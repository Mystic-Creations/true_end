package net.justmili.trueend.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class UnknownEntity extends Monster {

    private static final double MIN_DISTANCE = 28.0D;
    private static final double MAX_DISTANCE = 35.0D;
    private static final double WALK_SPEED = 0.276D;
    private static final double SPRINT_SPEED = 1.90D;

    private int sprintingTicks = 0;

    public UnknownEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.navigation = new GroundPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, WALK_SPEED)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, (float) MAX_DISTANCE));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInWaterOrBubble()) {
            this.discard();
            return;
        }

        LivingEntity nearest = this.level().getNearestPlayer(this, MAX_DISTANCE * 2);
        if (nearest == null) {
            return;
        }

        double dx = this.getX() - nearest.getX();
        double dz = this.getZ() - nearest.getZ();
        double dy = this.getY() - nearest.getY();
        double distanceSq = dx * dx + dy * dy + dz * dz;
        double distance = Math.sqrt(distanceSq);

        if (distance >= MIN_DISTANCE && distance <= MAX_DISTANCE) {
            BlockPos mobPos = this.blockPosition();
            int radius = 16;
            BlockPos nearestLog = null;

            outer:
            for (int x = -radius; x <= radius; x++) {
                for (int y = -4; y <= 4; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = mobPos.offset(x, y, z);
                        BlockState state = this.level().getBlockState(pos);
                        if (state.is(Blocks.OAK_LOG)) {
                            nearestLog = pos;
                            break outer;
                        }
                    }
                }
            }

            if (nearestLog != null) {
                this.getNavigation().moveTo(
                        nearestLog.getX() + 0.5,
                        nearestLog.getY(),
                        nearestLog.getZ() + 0.5,
                        WALK_SPEED
                );
            }

            sprintingTicks = 0;
            return;
        }

        if (distance > MAX_DISTANCE || distance < MIN_DISTANCE) {
            Vec3 dir = new Vec3(nearest.getX() - this.getX(), nearest.getY() - this.getY(), nearest.getZ() - this.getZ()).normalize();
            double targetX, targetZ;

            if (distance > MAX_DISTANCE) {
                targetX = nearest.getX() - dir.x * MAX_DISTANCE;
                targetZ = nearest.getZ() - dir.z * MAX_DISTANCE;
            } else {
                targetX = nearest.getX() - dir.x * MIN_DISTANCE;
                targetZ = nearest.getZ() - dir.z * MIN_DISTANCE;
            }

            double targetY = this.getY();
            this.getNavigation().moveTo(targetX, targetY, targetZ, SPRINT_SPEED);
            if (this.getNavigation().isInProgress()) {
                sprintingTicks++;
                if (sprintingTicks > 7 * 20) {
                    this.discard();
                    return;
                }
            } else {
                sprintingTicks = 0;
            }

            return;
        }

        sprintingTicks = 0;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }
}