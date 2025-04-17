package net.justmili.trueend.procedures;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.justmili.trueend.variables.VariableMap;
import net.justmili.trueend.TrueEndMod;

@Mod.EventBusSubscriber
public class UpdateDefaultKeepInv {
    private static final ResourceKey<Level> NIGHTMARE_DIM = ResourceKey.create(
        Level.RESOURCE_KEY, new ResourceLocation("true_end", "nightmare_within_a_dream")
    );

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) return;

        ServerLevel world = player.getLevel();
        if (world.dimension() == NIGHTMARE_DIM) return;

        LevelData keepInvRule = world.getLevelData();
            world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        VariableMap.GlobalVariables.get(world).setDefaultKeepInv(keepInvRule);
    }

    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        CommandSourceStack source = event.getParseResults().getContext().getSource();
        Entity entity = source.getEntity();
        if (!(entity instanceof ServerPlayer player)) return;

        ServerLevel world = player.setLevelCallback();
        if (world.dimension() == NIGHTMARE_DIM) return;

        LevelData keepInvRule = world.getLevelData();
            world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        VariableMap.GlobalVariables.get(world).setDefaultKeepInv(keepInvRule);
    }
}
