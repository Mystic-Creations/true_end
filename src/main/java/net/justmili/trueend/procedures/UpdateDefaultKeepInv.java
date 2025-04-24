package net.justmili.trueend.procedures;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.core.registries.Registries;
import net.justmili.trueend.variables.VariableMap;

@Mod.EventBusSubscriber
public class UpdateDefaultKeepInv {
    // The Nightmare dimension key
    private static final ResourceKey<Level> NWAD_DIMENSION = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.parse("true_end:beyond_the_dream")
    );

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel world = (ServerLevel) player.getLevel();
        if (world.dimension() == NWAD_DIMENSION) return;

        // Read vanilla keepInventory gamerule and store it
        boolean keepInvRule = world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        VariableMap.GlobalVariables.get(world).setDefaultKeepInv(keepInvRule);
    }

    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        CommandSourceStack source = event.getParseResults().getContext().getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel world = (ServerLevel) player.getLevel();
        if (world.dimension() == NWAD_DIMENSION) return;

        // Read vanilla keepInventory gamerule and store it
        boolean keepInvRule = world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        VariableMap.GlobalVariables.get(world).setDefaultKeepInv(keepInvRule);
    }
}
