package net.justmili.trueend.procedures.alphafeatures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class NoSprint {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.phase == TickEvent.Phase.END)) return;
        Entity entity = event.player;

        if (entity.level().dimension() == BTD && entity instanceof Player player) {
            player.getFoodData().setFoodLevel(4);
            player.getFoodData().setSaturation(0.0F);
        }
    }
}
