package net.justmili.trueend.procedures.advancement;

import net.justmili.trueend.network.Variables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static net.justmili.trueend.init.Dimensions.BTD;

public class NotAlone {
    public static void grantAdvancement(ServerPlayer player) {
        if (!((player.level().dimension()) == BTD)) return;
        if (!Variables.randomEventsToggle) return;
        if (!(Math.random() < Variables.randomEventChance / 24)) return;

        Advancement advancement = player.server.getAdvancements().getAdvancement(ResourceLocation.parse("true_end:not_alone"));
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criteria : progress.getRemainingCriteria())
                player.getAdvancements().award(advancement, criteria);
        }
    }
}
