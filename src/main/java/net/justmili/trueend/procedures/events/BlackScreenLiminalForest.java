package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.client.gui.inventory.BlackOverlay;
import net.justmili.trueend.init.Biomes;
import net.justmili.trueend.network.Variables;
import net.justmili.trueend.procedures.devcmd.screentests.TestBlackOverlay;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Random;

@Mod.EventBusSubscriber
public class BlackScreenLiminalForest {

    private static Random random = new Random();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        Player player = event.player;

        if (player.level().getBiome(player.blockPosition()).is(Biomes.SEEPING_REALITY)) {

            player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(cap -> {
                int liminalTime = cap.getLiminalForestTime() + 1;
                cap.setLiminalForestTime(liminalTime);

                double chance =  random.nextDouble();
                player.sendSystemMessage(Component.literal(Double.toString(Variables.randomEventChance * Math.log(liminalTime) / Math.log(600))));
                if (chance < Variables.randomEventChance * Math.log(liminalTime) / Math.log(2400)) {
                    TestBlackOverlay.execute(player.level(), player.getX(), player.getY(), player.getZ(), player);
                }
            });

        } else {
            player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(cap -> {
                cap.setLiminalForestTime(0);
            });
        }
    }

}
