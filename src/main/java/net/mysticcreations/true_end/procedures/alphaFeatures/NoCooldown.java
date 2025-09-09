package net.mysticcreations.true_end.procedures.alphaFeatures;

import net.mysticcreations.true_end.init.Dimensions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class NoCooldown {
    private static final AttributeModifier modifier = new AttributeModifier("9b91a426-cc5c-4a08-a0e5-7d00627cb3ef",200.0, AttributeModifier.Operation.ADDITION);

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ResourceKey<Level> toDim = event.getTo();

        applyCooldown(player, toDim);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ResourceKey<Level> toDim = ((ServerPlayer) player).getRespawnDimension();

        applyCooldown(player, toDim);
    }

    private static void applyCooldown(Player player, ResourceKey<Level> toDim) {
        AttributeInstance attackSpeedAttr = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attackSpeedAttr == null) return;

        if (toDim.equals(Dimensions.BTD)) {
            attackSpeedAttr.addPermanentModifier(modifier);
        } else {
            attackSpeedAttr.removeModifier(modifier);
        }
    }
}