package net.justmili.trueend.procedures.events;

import net.justmili.trueend.command.calls.screentests.TestBlackOverlay;
import net.justmili.trueend.init.Biomes;
import net.justmili.trueend.network.Variables;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class BlackScreenSeepingReality {

    private static Random random = new Random();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        Player player = event.player;

        if (player.level().getBiome(player.blockPosition()).is(Biomes.SEEPING_REALITY)) {

            player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(cap -> {
                int liminalTime = cap.getSeepingRealityTime() + 1;
                cap.setSeepingRealityTime(liminalTime);

                double chance =  random.nextDouble();
                player.sendSystemMessage(Component.literal(Double.toString(Variables.randomEventChance * Math.log(liminalTime) / Math.log(600))));
                if (chance < Variables.randomEventChance * Math.log(liminalTime) / Math.log(2400)) {
                    TestBlackOverlay.execute(player.level(), player.getX(), player.getY(), player.getZ(), player);
                }
            });

        } else {
            player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(cap -> {
                cap.setSeepingRealityTime(0);
            });
        }
    }

}
