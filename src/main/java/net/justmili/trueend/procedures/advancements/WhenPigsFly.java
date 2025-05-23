package net.justmili.trueend.procedures.advancements;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WhenPigsFly {

    @SubscribeEvent
    public static void onPigFallDeath(LivingDeathEvent event) {
        // Check if the entity that died is a pig
        if (!(event.getEntity() instanceof Pig pig)) {
            return;
        }

        // Check if the damage source is fall damage
        DamageSource source = event.getSource();
        if (!source.is(DamageTypes.FALL)) {
            return;
        }

        // Check if the pig had any passengers
        for (Entity passenger : pig.getPassengers()) {
            if (passenger instanceof ServerPlayer player) {
                // Grant the advancement to the player
                Advancement advancement = player.server.getAdvancements().getAdvancement(
                        ResourceLocation.parse("true_end:story/flying_pig"));
                if (advancement != null) {
                    AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
                    if (!progress.isDone()) {
                        for (String criterion : progress.getRemainingCriteria()) {
                            player.getAdvancements().award(advancement, criterion);
                        }
                    }
                }
            }
        }
    }
}
