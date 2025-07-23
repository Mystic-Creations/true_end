package net.justmili.trueend.command.calls;

import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;

import static net.justmili.trueend.init.Dimensions.BTD;

public class PrintVars {

    public static void execute(LevelAccessor world, ServerPlayer player, CommandSourceStack source) {
        if (player == null || source == null)
            return;

		Variables.MapVariables getVariable = Variables.MapVariables.get(world);

        int btdSpawnX = (int) getVariable.getBtdSpawnX();
        int btdSpawnY = (int) getVariable.getBtdSpawnY();
        int btdSpawnZ = (int) getVariable.getBtdSpawnZ();

        // Player vars
        source.sendSystemMessage(Component.literal("----= Per-Player"));
        for (ServerPlayer otherPlayer : player.server.getPlayerList().getPlayers()) {
            Variables.PlayerVariables vars = otherPlayer.getCapability(Variables.PLAYER_VARS_CAP).orElse(new Variables.PlayerVariables());
            source.sendSystemMessage(
                    Component.literal("beenBeyond (" + otherPlayer.getName().getString() + "): " + vars.hasBeenBeyond())
            );
        }

        // World vars
        source.sendSystemMessage(Component.literal("\n----= World Variables"));
        Variables.MapVariables globalVars = Variables.MapVariables.get(world);
        source.sendSystemMessage(Component.literal("btdSpawnX/Y/Z: "+btdSpawnX+"/"+btdSpawnY +"/"+btdSpawnZ));
        if (player.level().dimension() == BTD) {
            source.sendSystemMessage(Component.literal("unknownInWorld: " + globalVars.isUnknownInWorld()));
        } else {
            source.sendSystemMessage(Component.literal("You're not in BTD"));
        }
        // Config vars
        source.sendSystemMessage(Component.literal("\n----= Config Variables"));
        source.sendSystemMessage(Component.literal("btdConversationDelay: " + Variables.btdConversationDelay));
        source.sendSystemMessage(Component.literal("randomEventChance: " + Variables.randomEventChance));
        source.sendSystemMessage(Component.literal("entitySpawnChance: " + Variables.entitySpawnChance));
        source.sendSystemMessage(Component.literal("creditsToggle: " + Variables.creditsToggle));
        source.sendSystemMessage(Component.literal("popupsToggle: " + Variables.popupsToggle));
        source.sendSystemMessage(Component.literal("fogToggle: " + Variables.fogToggle));
        source.sendSystemMessage(Component.literal("daytimeChangeToggle: " + Variables.daytimeChangeToggle));
        source.sendSystemMessage(Component.literal("clearDreamItems: " + Variables.daytimeChangeToggle));
        source.sendSystemMessage(Component.literal("flashingLights: " + Variables.flashingLights));
    }
}
