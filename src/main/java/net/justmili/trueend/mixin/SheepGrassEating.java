package net.justmili.trueend.mixin;

import net.justmili.trueend.init.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

import static net.minecraft.world.level.block.Blocks.*;

@Mixin(EatBlockGoal.class)
public class SheepGrassEating {
    @Shadow @Final private Mob mob;
    @Shadow @Final private static Predicate<BlockState> IS_TALL_GRASS;
    @Shadow @Final private Level level;
    @Shadow private int eatAnimationTick;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
            cir.setReturnValue(false);
        } else {
            BlockPos blockpos = this.mob.blockPosition();
            cir.setReturnValue(IS_TALL_GRASS.test(this.level.getBlockState(blockpos)) || this.level.getBlockState(blockpos.below()).is(Blocks.GRASS_BLOCK.get()));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick == Mth.positiveCeilDiv(4, 2)) {
            BlockPos blockpos = this.mob.blockPosition();
            if (IS_TALL_GRASS.test(this.level.getBlockState(blockpos))) {
                if (ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
                    this.level.destroyBlock(blockpos, false);
                }

                this.mob.ate();
            } else {
                BlockPos blockpos1 = blockpos.below();
                if (this.level.getBlockState(blockpos1).is(GRASS_BLOCK)) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
                        this.level.levelEvent(2001, blockpos1, Block.getId(GRASS_BLOCK.defaultBlockState()));
                        this.level.setBlock(blockpos1, DIRT.defaultBlockState(), 2);
                    }

                    this.mob.ate();
                }
                if (this.level.getBlockState(blockpos1).is(Blocks.GRASS_BLOCK.get())) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
                        this.level.levelEvent(2001, blockpos1, Block.getId(Blocks.GRASS_BLOCK.get().defaultBlockState()));
                        this.level.setBlock(blockpos1, Blocks.DIRT.get().defaultBlockState(), 2);
                    }

                    this.mob.ate();
                }
            }
        }
        ci.cancel();
    }
}
