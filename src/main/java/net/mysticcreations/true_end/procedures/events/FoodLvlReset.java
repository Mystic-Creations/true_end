package net.mysticcreations.true_end.procedures.events;

import net.mysticcreations.true_end.init.Dimensions;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mod.EventBusSubscriber
public class FoodLvlReset {
    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        if (event.getFrom() == Dimensions.BTD && event.getTo() == Level.OVERWORLD) {
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(10.0f);
        }
    }
}
