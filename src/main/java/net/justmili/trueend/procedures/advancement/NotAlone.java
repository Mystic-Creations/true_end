package net.justmili.trueend.procedures.advancement;

import net.justmili.trueend.network.Variables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class NotAlone {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Entity entity = event.player;
        if (entity == null) return;
        if (!((entity.level().dimension()) == BTD)) return;
        if (!Variables.randomEventsToggle) return;
        if (!(Math.random() < Variables.randomEventChance / 24)) return;
        if (entity instanceof ServerPlayer player) {
            Advancement advancement = player.server.getAdvancements().getAdvancement(ResourceLocation.parse("true_end:not_alone"));
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) {
                for (String criteria : progress.getRemainingCriteria())
                    player.getAdvancements().award(advancement, criteria);
            }
        }
    }
}
