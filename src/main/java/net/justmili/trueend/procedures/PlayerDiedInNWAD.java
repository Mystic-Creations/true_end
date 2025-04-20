package net.justmili.trueend.procedures;

import net.justmili.trueend.TrueEndMod;
import net.justmili.trueend.init.TrueEndModGameRules;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.level.GameRules;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerDiedInNWAD {

    @SubscribeEvent
    public static void onDimensionChange(LivingDeathEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }
        if (event.getEntity().level().dimension().location().toString().equals("true_end:nightmare_within_a_dream")) {

            // get orignal keepinventory gamerule
            boolean shouldKeepInvetoryBeOn = event.getEntity().level().getGameRules().getBoolean(TrueEndModGameRules.KEEP_INV_DEFAULT_GAMEPLAY_VALUE);

            // set to orignal value to gamerule
            event.getEntity().level().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(shouldKeepInvetoryBeOn, event.getEntity().getCommandSenderWorld().getServer());

            // grand advencment
            Advancement advancement = event.getEntity().getServer().getAdvancements().getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"));
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