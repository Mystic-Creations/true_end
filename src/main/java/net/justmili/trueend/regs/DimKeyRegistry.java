package net.justmili.trueend.regs;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DimKeyRegistry {
	public static final ResourceKey<Level> NWAD = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("true_end:nightmare_within_a_dream"));
	public static final ResourceKey<Level> BTD = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("true_end:beyond_the_dream"));
	public static final ResourceKey<Level> OVERWORLD = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minecraft:overworld"));
}
