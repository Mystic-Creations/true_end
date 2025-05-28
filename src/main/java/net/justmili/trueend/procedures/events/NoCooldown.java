package net.justmili.trueend.procedures.events;

import net.justmili.trueend.regs.DimKeyRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoCooldown {
    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        double defaultAttackSpeed = 4.0;

        Player player = event.getEntity();
        AttributeInstance attackSpeedAttr = player.getAttribute(Attributes.ATTACK_SPEED);
        assert attackSpeedAttr != null;

        if (event.getFrom() == Level.OVERWORLD && event.getTo() == DimKeyRegistry.BTD) {
            attackSpeedAttr.setBaseValue(24.0);
        }
        if (event.getFrom() == DimKeyRegistry.BTD && event.getTo() == Level.OVERWORLD) {
            attackSpeedAttr.setBaseValue(defaultAttackSpeed);
        }
    }
}
