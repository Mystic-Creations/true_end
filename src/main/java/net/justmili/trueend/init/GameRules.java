package net.justmili.trueend.init;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GameRules {
	public static final net.minecraft.world.level.GameRules.Key<net.minecraft.world.level.GameRules.BooleanValue> CLEAR_DREAM_ITEMS =
			net.minecraft.world.level.GameRules.register(
					"clearDreamItems",
					net.minecraft.world.level.GameRules.Category.PLAYER,
					net.minecraft.world.level.GameRules.BooleanValue.create(true)
			);
}
