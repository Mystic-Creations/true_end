package net.justmili.trueend.sources;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IntegerRegistry {
	public static final int BlockPosRandomX = 16 + (int)(Math.random() * ((48 - 16) + 1));
	public static final int BlockPosRandomY = 128 + (int)(Math.random() * ((256 - 128) + 1));
	public static final int BlockPosRandomZ = 16 + (int)(Math.random() * ((48 - 16) + 1));

	public static final int randomRepeatCount = 3 + (int)(Math.random() * ((9 - 3) + 1));
}
