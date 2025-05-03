package net.justmili.trueend.procedures.dev;

import net.justmili.trueend.network.TrueEndVariables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;

public class PrintVars {

    public static void execute(LevelAccessor world, ServerPlayer player, CommandSourceStack source) {
        if (player == null || source == null)
            return;

		TrueEndVariables.MapVariables getVariable = TrueEndVariables.MapVariables.get(world);

        int btdSpawnXc = (int) getVariable.getBtdSpawnX();
        int btdSpawnYc = (int) getVariable.getBtdSpawnY();
        int btdSpawnZc = (int) getVariable.getBtdSpawnZ();

        // Per-Player Variables
        source.sendSystemMessage(Component.literal("----= Per-Player"));
        for (ServerPlayer otherPlayer : player.server.getPlayerList().getPlayers()) {
            TrueEndVariables.PlayerVariables vars =
                otherPlayer.getCapability(TrueEndVariables.PLAYER_VARS_CAP).orElse(new TrueEndVariables.PlayerVariables());
            // use hasBeenBeyond() getter instead of private field
            source.sendSystemMessage(Component.literal(
                "beenBeyond (" + otherPlayer.getName().getString() + "): " + vars.hasBeenBeyond()
            ));
        }

        // Global Variables
        source.sendSystemMessage(Component.literal("\n----= Global"));
        TrueEndVariables.MapVariables globalVars = TrueEndVariables.MapVariables.get(world);
        source.sendSystemMessage(Component.literal("defaultKeepInv: " + globalVars.isDefaultKeepInv()));
        source.sendSystemMessage(Component.literal(
            "btdSpawnX/Y/Z: " + btdSpawnXc + "/" + btdSpawnYc + "/" + btdSpawnZc
        ));
        source.sendSystemMessage(Component.literal("randomEventChance: " + globalVars.getRandomEventChance()));
    }
}
