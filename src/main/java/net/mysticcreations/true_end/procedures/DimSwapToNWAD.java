package net.mysticcreations.true_end.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemHandlerHelper;
import net.mysticcreations.true_end.TrueEnd;
import net.mysticcreations.true_end.network.Variables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.mysticcreations.true_end.init.Dimensions.BTD;
import static net.mysticcreations.true_end.init.Dimensions.NWAD;

@Mod.EventBusSubscriber
public class DimSwapToNWAD {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) return;
        Level world = player.level();
        if (world.isClientSide()) return;
        DamageSource source = event.getSource();
        if (source == null) return;

        //Entry
        if (!world.dimension().equals(BTD) && !world.dimension().equals(NWAD)) {
            if (!(source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FELL_OUT_OF_WORLD))) return;
            if (Math.random() > Variables.randomEventChance * 2) return;

            if (TrueEnd.hasAdvancement(player, "true_end:leave_the_nightmare_within_a_dream")) return;

            PlayerInvManager.saveInvNWAD(player);
            player.setGameMode(GameType.ADVENTURE);
            teleportToNWAD(player);
            //A journal, as requested by Gonza on Discord
            ItemStack cube = new ItemStack(Items.WRITABLE_BOOK);
            cube.setCount(1);
            ItemHandlerHelper.giveItemToPlayer(player, cube);
        }

        //Leave
        if (world.dimension().equals(NWAD)) {
            float damage = event.getAmount();
            float health = player.getHealth();
            if (damage <= health) return;
            event.setCanceled(true);

            player.setHealth(player.getMaxHealth());
            player.clearFire();
            player.resetFallDistance();
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(10.0f);
            BlockPos respawnPos = player.getRespawnPosition();
            ResourceKey<Level> respawnDim = player.getRespawnDimension();
            ServerLevel targetLevel = null;
            BlockPos targetPos = null;

            if (respawnPos != null) {
                targetLevel = player.server.getLevel(respawnDim);
                if (targetLevel != null) {
                    targetPos = respawnPos;
                }
            }
            if (targetLevel == null) {
                targetLevel = player.server.getLevel(Level.OVERWORLD);
                if (targetLevel == null) {
                    targetLevel = (ServerLevel) world;
                }
                targetPos = targetLevel.getSharedSpawnPos();
            }


            double x = targetPos.getX() + 0.5;
            double y = targetPos.getY() + 0.1;
            double z = targetPos.getZ() + 0.5;
            player.teleportTo(targetLevel, x, y, z, player.getYRot(), player.getXRot());
            PlayerInvManager.restoreInv(player);
            player.setGameMode(GameType.SURVIVAL);
            Advancement advancement = player.server.getAdvancements()
                .getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"));
            if (advancement != null) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
                if (!progress.isDone()) {
                    for (String criteria : progress.getRemainingCriteria()) {
                        player.getAdvancements().award(advancement, criteria);
                    }
                }
            }
        }

        player.connection.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
        player.getActiveEffects().forEach(effect ->
            player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effect))
        );
    }

    private static void teleportToNWAD(ServerPlayer player) {
        if (player.level().dimension() == NWAD) return;
        ServerLevel next = player.server.getLevel(NWAD);
        if (next == null) return;

        player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));

        int x = player.getBlockX(), z = player.getBlockZ(), y = 120;
        boolean found = false;
        while (y > 0) {
            BlockPos pos = new BlockPos(x, y, z);
            if (next.isEmptyBlock(pos)
                && next.isEmptyBlock(pos.above())
                && !next.isEmptyBlock(pos.below())) {
                found = true;
                break;
            }
            y--;
        }
        if (!found) y = 129;

        player.teleportTo(next, x, y + 1, z, player.getYRot(), player.getXRot());
        player.connection.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
        player.getActiveEffects().forEach(e ->
            player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), e))
        );
    }
}
