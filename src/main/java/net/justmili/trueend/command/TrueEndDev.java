package net.justmili.trueend.command;

import net.justmili.trueend.init.Entities;
import net.justmili.trueend.procedures.DimSwapToBTD;
import net.justmili.trueend.procedures.devcmd.*;
import net.justmili.trueend.procedures.devcmd.screentests.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;

import java.util.Random;

@Mod.EventBusSubscriber
public class TrueEndDev {

	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("trueend").requires(s -> s.hasPermission(4))
				.then(Commands.literal("testScreen")
						.then(Commands.literal("credits").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();
							Entity entity = arguments.getSource().getEntity();
							if (entity == null && world instanceof ServerLevel _servLevel)
								entity = FakePlayerFactory.getMinecraft(_servLevel);
							Direction direction = Direction.DOWN;
							if (entity != null)
								direction = entity.getDirection();

							TestCredits.execute(world, x, y, z);
							return 0;
						})).then(Commands.literal("funny").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();
							Entity entity = arguments.getSource().getEntity();
							if (entity == null && world instanceof ServerLevel _servLevel)
								entity = FakePlayerFactory.getMinecraft(_servLevel);
							Direction direction = Direction.DOWN;
							if (entity != null)
								direction = entity.getDirection();
							Funny.execute(world, x, y, z, entity);
							return 0;
						})))
				.then(Commands.literal("testBTD-direct").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();

					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();

					Entity entity = arguments.getSource().getEntity();
					if (entity == null && world instanceof ServerLevel _servLevel)
						entity = FakePlayerFactory.getMinecraft(_servLevel);

					Direction direction = Direction.DOWN;
					if (entity != null)
						direction = entity.getDirection();

					TestBTDDirect.execute(world, entity);
					return 0;
				})).then(Commands.literal("adaptTerrain").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					DimSwapToBTD.adaptTerrain(world, arguments.getSource().getEntity().blockPosition());
					return 0;
				})).then(Commands.literal("removeTrees").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					DimSwapToBTD.removeNearbyTrees(world, arguments.getSource().getEntity().blockPosition(), 3);
					return 0;
				})).then(Commands.literal("spawnUnknown").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					BlockPos blockPos = arguments.getSource().getEntity().blockPosition();
					blockPos = blockPos.mutable().offset(10,0,0);
					EntityType<?> unknownType = Entities.UNKNOWN.get();
					Entity unknownEntity = unknownType.create(world);

					double minDist = 32.0;
					double maxDist = (Minecraft.getInstance().gameRenderer.getRenderDistance() * 16) - 16;

					Random random = new Random();

					double dist = random.nextDouble() * (maxDist - minDist) + minDist;

					unknownEntity.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());

					unknownEntity.getPersistentData().putBoolean("PersistenceRequired", true);
					world.addFreshEntity(unknownEntity);
					return 0;
				})).then(Commands.literal("localHeight").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					int localMax = DimSwapToBTD.getLocalMax(world, arguments.getSource().getEntity().blockPosition());
					arguments.getSource().getEntity().sendSystemMessage(Component.literal(Integer.toString(localMax)));
					return 0;
				}))

				.then(Commands.literal("testHome").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();

					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();

					Entity entity = arguments.getSource().getEntity();
					if (entity == null && world instanceof ServerLevel _servLevel)
						entity = FakePlayerFactory.getMinecraft(_servLevel);

					Direction direction = Direction.DOWN;
					if (entity != null)
						direction = entity.getDirection();

					TestHome.execute((ServerPlayer) entity);
					return 0;
				})).then(Commands.literal("printVars").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();

					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();

					Entity entity = arguments.getSource().getEntity();
					if (entity == null && world instanceof ServerLevel _servLevel)
						entity = FakePlayerFactory.getMinecraft(_servLevel);

					Direction direction = Direction.DOWN;
					if (entity != null)
						direction = entity.getDirection();

					PrintVars.execute(world, (ServerPlayer) entity, arguments.getSource());
					return 0;
				})).then(Commands.literal("testBTD").executes(arguments -> {
					Level world = arguments.getSource().getUnsidedLevel();

					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();

					Entity entity = arguments.getSource().getEntity();
					if (entity == null && world instanceof ServerLevel _servLevel)
						entity = FakePlayerFactory.getMinecraft(_servLevel);

					Direction direction = Direction.DOWN;
					if (entity != null)
						direction = entity.getDirection();

					BTDTest.execute(world, entity);
					return 0;
				})).then(Commands.literal("testNWAD").then(Commands.literal("keepInvTrue").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();

							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();

							Entity entity = arguments.getSource().getEntity();
							if (entity == null && world instanceof ServerLevel _servLevel)
								entity = FakePlayerFactory.getMinecraft(_servLevel);

							Direction direction = Direction.DOWN;
							if (entity != null)
								direction = entity.getDirection();

							NWADTestKIT.execute(world, entity);
							return 0;
						})).then(Commands.literal("keepInvFalse").executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();

							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();

							Entity entity = arguments.getSource().getEntity();
							if (entity == null && world instanceof ServerLevel _servLevel)
								entity = FakePlayerFactory.getMinecraft(_servLevel);

							Direction direction = Direction.DOWN;
							if (entity != null)
								direction = entity.getDirection();

							NWADTestKIF.execute(world, entity);
							return 0;
						}))
				)
		);
	}
}