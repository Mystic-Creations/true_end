package net.justmili.trueend.procedures;

import net.justmili.trueend.network.Variables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.level.GameRules;

import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PlayerDiedInNWAD {

    @SubscribeEvent
    public static void onDimensionChange(LivingDeathEvent event) {
        LevelAccessor world = event.getEntity().level();
        
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }
        if (event.getEntity().level().dimension().location().toString().equals("true_end:nightmare_within_a_dream")) {

            // get orignal keepinventory gamerule
            boolean setKeepInv = Variables.MapVariables.get(world).isDefaultKeepInv();

            // set to orignal value to gamerule
            event.getEntity().level().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(setKeepInv, event.getEntity().getCommandSenderWorld().getServer());

            // grant advencment
            Advancement advancement = Objects.requireNonNull(event.getEntity().getServer()).getAdvancements().getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"));
            if (advancement != null) {
                AdvancementProgress advancementProgress = ((ServerPlayer) event.getEntity()).getAdvancements().getOrStartProgress(advancement);
                if (!advancementProgress.isDone()) {
                    for (String criteria : advancementProgress.getRemainingCriteria())
                        ((ServerPlayer) event.getEntity()).getAdvancements().award(advancement, criteria);
                }
            }
        }

    }
}