package net.justmili.trueend.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrueEndGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> CLEAR_DREAM_ITEMS = GameRules.register("clearDreamItems", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
}
