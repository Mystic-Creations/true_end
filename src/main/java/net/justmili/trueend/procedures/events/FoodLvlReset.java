package net.justmili.trueend.procedures.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static net.justmili.trueend.regs.DimKeyRegistry.BTD;

@Mod.EventBusSubscriber
public class FoodLvlReset {
    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        // Only run on server side
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        // Check if player came from BTD and is now in the Overworld
        if (event.getFrom() == BTD && event.getTo() == Level.OVERWORLD) {
            // Reset hunger and saturation to full
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(10.0f);
        }
    }
}
