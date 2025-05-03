package net.justmili.trueend.regs;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IntegerRegistry {
	// CheckIfExitingEnd - random block position for player, between 16 and 48
	public static final int BlockPosRandomX = 16 + (int)(Math.random() * ((48 - 16) + 1));
	public static final int BlockPosRandomY = 128 + (int)(Math.random() * ((256 - 128) + 1));
	public static final int BlockPosRandomZ = 16 + (int)(Math.random() * ((48 - 16) + 1));

	// SoundPlayer - random sound repeat amount between 3 and 9
	public static final int randomRepeatCount = 3 + (int)(Math.random() * ((9 - 3) + 1));
}
