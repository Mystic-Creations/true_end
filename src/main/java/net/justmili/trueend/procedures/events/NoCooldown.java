package net.justmili.trueend.procedures.events;

import net.justmili.trueend.regs.DimKeyRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class NoCooldown {

    private static final double DEFAULT_ATTACK_SPEED = 4.0;
    private static final double BTD_ATTACK_SPEED = 200.0;

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ResourceKey<Level> toDim = event.getTo();

        AttributeInstance attackSpeedAttr = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attackSpeedAttr == null) return;

        if (toDim.equals(DimKeyRegistry.BTD)) {
            attackSpeedAttr.setBaseValue(BTD_ATTACK_SPEED);
        } else if (toDim.equals(Level.OVERWORLD)) {
            attackSpeedAttr.setBaseValue(DEFAULT_ATTACK_SPEED);
        } else {

            attackSpeedAttr.setBaseValue(DEFAULT_ATTACK_SPEED);
        }
    }
}