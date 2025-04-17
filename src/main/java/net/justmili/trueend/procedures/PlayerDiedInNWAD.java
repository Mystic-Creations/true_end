package net.justmili.trueend.procedures;

import net.justmili.trueend.variables.VariableMap;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerDiedInNWAD {
    private static final ResourceKey<Level> NIGHTMARE_DIM = ResourceKey.create(
        new ResourceKey.create(Registries.DIMENSION, new ResourceLocation("true_end", "nightmare_within_a_dream"))
    );

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ServerLevel world = (ServerLevel) player.level();
        if (world.dimension() != NIGHTMARE_DIM) {
            return;
        }

        // get defaultKeepInv value
        boolean defaultKeepInv = VariableMap.GlobalVariables.get(world).isDefaultKeepInv();

        // set keepInventory gamerule
        world.getLevelData()
            .getGameRules()
            .getRule(GameRules.RULE_KEEPINVENTORY)
            .set(defaultKeepInv, world.getServer());

        // grant advancement
        Advancement adv = world.getServer()
            .getAdvancements()
            .getAdvancement(new ResourceLocation("true_end:leave_the_nightmare_within_a_dream"));
        if (adv != null) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
            if (!progress.isDone()) {
                for (String crit : progress.getRemainingCriteria()) {
                    player.getAdvancements().award(adv, crit);
                }
            }
        }
    }
}
