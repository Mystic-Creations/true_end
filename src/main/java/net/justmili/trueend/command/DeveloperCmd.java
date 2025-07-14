package net.justmili.trueend.command;

import net.justmili.trueend.procedures.devcmd.*;
import net.justmili.trueend.procedures.devcmd.screentests.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
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
							TestCredits.execute(world, x, y, z);
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
				.then(Commands.literal("testBTD-direct").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();
					Entity entity = arguments.getSource().getEntity();

					TestBTDDirect.execute(world, entity);
					return 0;
				})).then(Commands.literal("testHome").executes(arguments -> {
					Entity entity = arguments.getSource().getEntity();

					TestHome.execute(entity);
					return 0;
				})).then(Commands.literal("printVars").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();
					Entity entity = arguments.getSource().getEntity();

					PrintVars.execute(world, (ServerPlayer) entity, arguments.getSource());
					return 0;
				})).then(Commands.literal("testBTD").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();
					Entity entity = arguments.getSource().getEntity();

					BTDTest.execute(world, entity);
					return 0;}))
				.then(Commands.literal("findDammTree").executes(arguments -> {
					BlockPos targetPos = arguments.getSource().getLevel().findNearestMapStructure(
							TagKey.create(Registries.STRUCTURE, ResourceLocation.parse("true_end:the_dreaming_tree")),
							arguments.getSource().getEntity().blockPosition(),
							100,
							true
					);
					if (targetPos != null) {
						arguments.getSource().getEntity().sendSystemMessage(Component.literal("%d %d %d".formatted(targetPos.getX(), targetPos.getY(), targetPos.getZ())));
					}
					return 0;
				})).then(Commands.literal("testNWAD").then(Commands.literal("keepInvTrue").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							Entity entity = arguments.getSource().getEntity();

							NWADTestKIT.execute(world, entity);
							return 0;
						})).then(Commands.literal("keepInvFalse").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							Entity entity = arguments.getSource().getEntity();

							NWADTestKIF.execute(world, entity);
							return 0;
						}))
				)
		);
	}
}