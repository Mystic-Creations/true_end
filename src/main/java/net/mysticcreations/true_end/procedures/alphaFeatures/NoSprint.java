package net.mysticcreations.true_end.procedures.alphaFeatures;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import static net.mysticcreations.true_end.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class NoSprint {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;

        if (player.level().dimension() == BTD) {
            player.getFoodData().setFoodLevel(4);
            player.getFoodData().setSaturation(0.0F);
        }
    }
}
