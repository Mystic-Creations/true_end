package net.mysticcreations.true_end.procedures.alphaFeatures;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
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
            if (player.isSprinting()) player.setSprinting(false);
            //Kind of cancels sprint?
            //I think this updates fast enough that sprint by double-tapping W doesn't let you sprint,
            // but it'll let you sprint when holding CTRL
            //Other mods like Subtle Effects recognize that "sprint is canceled" when tested with "... instanceof Player player",
            // but not with "... instanceof ServerPlayer player", so we'll use "Player player"
            //It's not anything visual on the client either, you still sprint on the server side, it's not just visual.
            //I also went through LivingEntity.java, and found out that setSprinting works like this:
//        private static final UUID SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
//        private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(SPEED_MODIFIER_SPRINTING_UUID, "Sprinting speed boost", (double)0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL);
//            public void setSprinting(boolean p_21284_) {
//                super.setSprinting(p_21284_);
//                AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
//                if (attributeinstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
//                    attributeinstance.removeModifier(SPEED_MODIFIER_SPRINTING);
//                }
//
//                if (p_21284_) {
//                    attributeinstance.addTransientModifier(SPEED_MODIFIER_SPRINTING);
//                }
//            }
            //Altho it seems it won't do much really
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        //Works fine, don't touch
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        if (serverPlayer.level().dimension() != BTD) return;
        float amount = event.getAmount();
        if (amount <= 1.0F && serverPlayer.getFoodData().getFoodLevel() >= 18) {
            event.setCanceled(true);
        }
    }
}
