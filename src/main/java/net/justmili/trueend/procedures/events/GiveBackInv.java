package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.GameRules;
import net.justmili.trueend.network.Variables;
import static net.justmili.trueend.init.Dimensions.BTD;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GiveBackInv {
    @SubscribeEvent
    public static void onDimensionChange(PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getFrom() != BTD || event.getTo() != Level.OVERWORLD) return;
        if (!player.level().getGameRules().getBoolean(GameRules.CLEAR_DREAM_ITEMS)) return;

        player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(data -> {
            if (data.hasBeenBeyond()) {
                PlayerInvManager.restorePlayerInventory(player);
            }
        });
    }
}