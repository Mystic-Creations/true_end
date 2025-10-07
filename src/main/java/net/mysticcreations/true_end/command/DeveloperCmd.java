package net.mysticcreations.true_end.command;

import net.mysticcreations.true_end.command.calls.*;
import net.mysticcreations.true_end.command.calls.screentests.*;
import net.mysticcreations.true_end.procedures.PlayerInvManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.Commands;

import static net.mysticcreations.true_end.init.Dimensions.*;

@Mod.EventBusSubscriber
public class DeveloperCmd {

	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("trueend")
			.then(Commands.literal("testScreen")
				.then(Commands.literal("credits")
					.requires(s -> s.hasPermission(4))
					.executes(arguments -> {
						TestCredits.execute();
						return 0;
					})).then(Commands.literal("funny")
					.executes(arguments -> {
						Level world = arguments.getSource().getUnsidedLevel();
						double x = arguments.getSource().getPosition().x();
						double y = arguments.getSource().getPosition().y();
						double z = arguments.getSource().getPosition().z();
						Entity entity = arguments.getSource().getEntity();

						TestFunny.execute(world, x, y, z, entity);
						return 0;
					})).then(Commands.literal("black")
					.requires(s -> s.hasPermission(4))
					.executes(arguments -> {
						Level world = arguments.getSource().getUnsidedLevel();
						double x = arguments.getSource().getPosition().x();
						double y = arguments.getSource().getPosition().y();
						double z = arguments.getSource().getPosition().z();
						Entity entity = arguments.getSource().getEntity();

						TestBlackOverlay.execute(world, x, y, z, entity);
						return 0;
					})))
			.then(Commands.literal("printVars")
				.requires(s -> s.hasPermission(4))
				.executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();
					Entity entity = arguments.getSource().getEntity();

					PrintVars.execute(world, (ServerPlayer) entity, arguments.getSource());
					return 0;
				})).then(Commands.literal("testBTD")
				.requires(s -> s.hasPermission(4))
				.executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();
					Entity entity = arguments.getSource().getEntity();

					BTDTest.execute(world, entity);
					return 0;
				}))			.then(Commands.literal("clearAccessories")
				.requires(s -> s.hasPermission(4))
				.executes(arguments -> {
					ServerPlayer player = arguments.getSource().getPlayer();

					PlayerInvManager.clearAccessories(player);
					return 0;
				})).then(Commands.literal("convertInvBackup")
				.requires(s -> s.hasPermission(4))
				.then(Commands.literal(BTD.location().toString())
					.executes(arguments -> {
						ServerPlayer player = arguments.getSource().getPlayer();
						InvFileConvert.execute(player, "BTD");
						return 0;
					}))
				.then(Commands.literal(NWAD.location().toString())
					.executes(arguments -> {
						ServerPlayer player = arguments.getSource().getPlayer();
						InvFileConvert.execute(player, "NWAD");
						return 0;
					}))
			)
		);
	}
}