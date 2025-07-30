package net.justmili.trueend.command;

import net.justmili.trueend.command.calls.BTDTest;
import net.justmili.trueend.command.calls.PrintVars;
import net.justmili.trueend.command.calls.screentests.TestBlackOverlay;
import net.justmili.trueend.command.calls.screentests.TestCredits;
import net.justmili.trueend.command.calls.screentests.TestFunny;
import net.justmili.trueend.procedures.PlayerInvManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class DeveloperCmd {

	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("trueend").requires(s -> s.hasPermission(4))
				.then(Commands.literal("testScreen")
						.then(Commands.literal("credits").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();
							TestCredits.execute(x, y, z);
							return 0;
						})).then(Commands.literal("funny").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();
							Entity entity = arguments.getSource().getEntity();

							TestFunny.execute(world, x, y, z, entity);
							return 0;
						})).then(Commands.literal("black").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();
							Entity entity = arguments.getSource().getEntity();

							TestBlackOverlay.execute(world, x, y, z, entity);
							return 0;
						})))
				.then(Commands.literal("printVars").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();
					Entity entity = arguments.getSource().getEntity();

					PrintVars.execute(world, (ServerPlayer) entity, arguments.getSource());
					return 0;
				})).then(Commands.literal("testBTD").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();
					Entity entity = arguments.getSource().getEntity();

					BTDTest.execute(world, entity);
					return 0;
				})).then(Commands.literal("clearCurios").executes(arguments -> {
					ServerPlayer player = arguments.getSource().getPlayer();

					PlayerInvManager.clearCuriosSlots(player);
					return 0;
				}))
		);
	}
}