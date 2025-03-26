
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.justmili.trueend.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrueEndModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> CLEAR_DREAM_ITEMS = GameRules.register("clearDreamItems", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.IntegerValue> BTD_CONVERSATION_MESSEGE_DELAY = GameRules.register("btdConversationMessegeDelay", GameRules.Category.PLAYER, GameRules.IntegerValue.create(20));
	public static final GameRules.Key<GameRules.BooleanValue> LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME = GameRules.register("logicHasVisitedBTDForTheFirstTime", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> KEEP_INV_DEFAULT_GAMEPLAY_VALUE = GameRules.register("keepInvDefaultGameplayValue", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
}
