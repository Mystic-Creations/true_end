package net.justmili.trueend.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.config.Config;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.config.Config.*;

@Mod.EventBusSubscriber
public class ConfigCmd {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("te-config").requires(s -> s.hasPermission(4))
                        .then(Commands.literal("randomEventChance")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 0.5))
                                        .executes(ctx -> handleDouble(ctx.getSource(), "randomEventChance", DoubleArgumentType.getDouble(ctx, "value")))))
                        .then(Commands.literal("entitySpawnChance")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 1))
                                        .executes(ctx -> handleDouble(ctx.getSource(), "entitySpawnChance", DoubleArgumentType.getDouble(ctx, "value")))))
                        .then(Commands.literal("btdConversationDelay")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 60))
                                        .executes(ctx -> handleInt(ctx.getSource(), "btdConversationDelay", IntegerArgumentType.getInteger(ctx, "value")))))
                        .then(Commands.literal("creditsToggle")
                                .then(Commands.literal("true")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "creditsToggle", true)))
                                .then(Commands.literal("false")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "creditsToggle", false))))
                        .then(Commands.literal("fogToggle")
                                .then(Commands.literal("true")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "fogToggle", true)))
                                .then(Commands.literal("false")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "fogToggle", false))))
                        .then(Commands.literal("popupsToggle")
                                .then(Commands.literal("true")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "popupsToggle", true)))
                                .then(Commands.literal("false")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "popupsToggle", false))))
                        .then(Commands.literal("flashingLights")
                                .then(Commands.literal("true")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "flashingLights", true)))
                                .then(Commands.literal("false")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "flashingLights", false))))
                        .then(Commands.literal("daytimeChangeToggle")
                                .then(Commands.literal("true")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "daytimeChangeToggle", true)))
                                .then(Commands.literal("false")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "daytimeChangeToggle", false))))
                        .then(Commands.literal("clearDreamItems")
                                .then(Commands.literal("true")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "clearDreamItems", true)))
                                .then(Commands.literal("false")
                                        .executes(ctx -> handleBoolean(ctx.getSource(), "clearDreamItems", false))))
        );
    }
}
