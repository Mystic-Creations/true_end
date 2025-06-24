package net.justmili.trueend.init;

import net.justmili.trueend.item.DreamersCompass;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.justmili.trueend.item.MysteriousCube;
import net.justmili.trueend.TrueEnd;

public class Items {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TrueEnd.MODID);
	public static final RegistryObject<Item> DIRT = block(Blocks.DIRT);
	public static final RegistryObject<Item> GRASS_BLOCK = block(Blocks.GRASS_BLOCK);
	public static final RegistryObject<Item> FARMLAND = block(Blocks.FARMLAND);
	public static final RegistryObject<Item> COBBLESTONE = block(Blocks.COBBLESTONE);
	public static final RegistryObject<Item> MOSSY_COBBLESTONE = block(Blocks.MOSSY_COBBLESTONE);
	public static final RegistryObject<Item> STONE = block(Blocks.STONE);
	public static final RegistryObject<Item> COAL_ORE = block(Blocks.COAL_ORE);
	public static final RegistryObject<Item> IRON_ORE = block(Blocks.IRON_ORE);
	public static final RegistryObject<Item> GOLD_ORE = block(Blocks.GOLD_ORE);
	public static final RegistryObject<Item> REDSTONE_ORE = block(Blocks.REDSTONE_ORE);
	public static final RegistryObject<Item> DIAMOND_ORE = block(Blocks.DIAMOND_ORE);
	public static final RegistryObject<Item> LEAVES = block(Blocks.LEAVES);
	public static final RegistryObject<Item> WOOD = block(Blocks.WOOD);
	public static final RegistryObject<Item> WOOD_6_SIDED = block(Blocks.WOOD_6_SIDED);
	public static final RegistryObject<Item> WOODEN_PLANKS = block(Blocks.WOODEN_PLANKS);
	public static final RegistryObject<Item> WOODEN_STAIRS = block(Blocks.WOODEN_STAIRS);
	public static final RegistryObject<Item> WOODEN_SLAB = block(Blocks.WOODEN_SLAB);
	public static final RegistryObject<Item> FENCE = block(Blocks.FENCE);
	public static final RegistryObject<Item> FENCE_GATE = block(Blocks.FENCE_GATE);
	public static final RegistryObject<Item> DOOR = doubleBlock(Blocks.DOOR);
	public static final RegistryObject<Item> TRAPDOOR = block(Blocks.TRAPDOOR);
	public static final RegistryObject<Item> PRESSURE_PLATE = block(Blocks.PRESSURE_PLATE);
	public static final RegistryObject<Item> BUTTON = block(Blocks.BUTTON);
	public static final RegistryObject<Item> OBSIDIAN = block(Blocks.OBSIDIAN);
	public static final RegistryObject<Item> GRAVEL = block(Blocks.GRAVEL);
	public static final RegistryObject<Item> GLASS = block(Blocks.GLASS);
	public static final RegistryObject<Item> SAND = block(Blocks.SAND);
	public static final RegistryObject<Item> MYSTERIOUS_CUBE = REGISTRY.register("mysterious_cube", MysteriousCube::new);
	public static final RegistryObject<Item> UNKNOWN_SPAWN_EGG = REGISTRY.register("unknown_spawn_egg", () -> new ForgeSpawnEggItem(Entities.UNKNOWN, -16777216, -1, new Item.Properties()));
	public static final RegistryObject<Item> FLOWER = block(Blocks.FLOWER);
	public static final RegistryObject<Item> ROSE = block(Blocks.ROSE);
	public static final RegistryObject<Item> SAPLING = block(Blocks.SAPLING);
	public static final RegistryObject<Item> DREAMERS_COMPASS = REGISTRY.register("dreamers_compass", DreamersCompass::new);

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
	private static RegistryObject<Item> doubleBlock(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
	}
}
