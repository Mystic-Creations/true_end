package net.justmili.trueend.procedures.devcmd;

import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;

public class PrintVars {

    public static void execute(LevelAccessor world, ServerPlayer player, CommandSourceStack source) {
        if (player == null || source == null)
            return;

		Variables.MapVariables getVariable = Variables.MapVariables.get(world);

        int btdSpawnX = (int) getVariable.getBtdSpawnX();
        int btdSpawnY = (int) getVariable.getBtdSpawnY();
        int btdSpawnZ = (int) getVariable.getBtdSpawnZ();

        // Per-Player Variables
        source.sendSystemMessage(Component.literal("----= Per-Player"));
        for (ServerPlayer otherPlayer : player.server.getPlayerList().getPlayers()) {
            Variables.PlayerVariables vars =
                otherPlayer.getCapability(Variables.PLAYER_VARS_CAP).orElse(new Variables.PlayerVariables());
            // use hasBeenBeyond() getter instead of private field
            source.sendSystemMessage(Component.literal(
                "beenBeyond (" + otherPlayer.getName().getString() + "): " + vars.hasBeenBeyond()
            ));
        }

        // Global Variables
        source.sendSystemMessage(Component.literal("\n----= Global"));
        Variables.MapVariables globalVars = Variables.MapVariables.get(world);
        source.sendSystemMessage(Component.literal("defaultKeepInv: " + globalVars.isDefaultKeepInv()));
        source.sendSystemMessage(Component.literal(
            "btdSpawnX/Y/Z: " + btdSpawnX + "/" + btdSpawnY + "/" + btdSpawnZ
        ));
        source.sendSystemMessage(Component.literal("unknownInWorld: " + globalVars.isUnknownInWorld()));
        // Configurable
        source.sendSystemMessage(Component.literal("\n----= Configurable"));
        //source.sendSystemMessage(Component.literal("clearDreamItems: " + TrueEndVariables.clearDreamItems.getValue()));
        source.sendSystemMessage(Component.literal("btdConversationDelay: " + Variables.btdConversationDelay.getValue()));
        source.sendSystemMessage(Component.literal("randomEventChance: " + Variables.randomEventChance.getValue()));
        source.sendSystemMessage(Component.literal("entitySpawnChance: " + Variables.entitySpawnChance.getValue()));
        source.sendSystemMessage(Component.literal("popupsToggle: " + Variables.popupsToggle.getValue()));
        source.sendSystemMessage(Component.literal("fogToggle: " + Variables.fogToggle.getValue()));


    }
}
