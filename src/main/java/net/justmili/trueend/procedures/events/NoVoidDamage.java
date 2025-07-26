package net.justmili.trueend.procedures.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class NoVoidDamage {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (source == null) return;
        if (source.is(DamageTypes.FELL_OUT_OF_WORLD) && entity.getY() >= -2010) event.setCanceled(true);
        //It's set to -2010 cuz year 2010 is when Alpha 1.1.2_01 released
    }
}
