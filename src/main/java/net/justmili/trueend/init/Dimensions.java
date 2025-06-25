package net.justmili.trueend.init;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Dimensions {
	public static final ResourceKey<Level> NWAD = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("true_end:nightmare_within_a_dream"));
	public static final ResourceKey<Level> BTD = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("true_end:beyond_the_dream"));
}
