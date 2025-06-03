package net.justmili.trueend.init;

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

public class TrueEndItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TrueEnd.MODID);
	public static final RegistryObject<Item> DIRT = block(TrueEndBlocks.DIRT);
	public static final RegistryObject<Item> GRASS_BLOCK = block(TrueEndBlocks.GRASS_BLOCK);
	public static final RegistryObject<Item> FARMLAND = block(TrueEndBlocks.FARMLAND);
	public static final RegistryObject<Item> COBBLESTONE = block(TrueEndBlocks.COBBLESTONE);
	public static final RegistryObject<Item> MOSSY_COBBLESTONE = block(TrueEndBlocks.MOSSY_COBBLESTONE);
	public static final RegistryObject<Item> STONE = block(TrueEndBlocks.STONE);
	public static final RegistryObject<Item> COAL_ORE = block(TrueEndBlocks.COAL_ORE);
	public static final RegistryObject<Item> IRON_ORE = block(TrueEndBlocks.IRON_ORE);
	public static final RegistryObject<Item> GOLD_ORE = block(TrueEndBlocks.GOLD_ORE);
	public static final RegistryObject<Item> REDSTONE_ORE = block(TrueEndBlocks.REDSTONE_ORE);
	public static final RegistryObject<Item> DIAMOND_ORE = block(TrueEndBlocks.DIAMOND_ORE);
	public static final RegistryObject<Item> LEAVES = block(TrueEndBlocks.LEAVES);
	public static final RegistryObject<Item> WOOD = block(TrueEndBlocks.WOOD);
	public static final RegistryObject<Item> WOOD_6_SIDED = block(TrueEndBlocks.WOOD_6_SIDED);
	public static final RegistryObject<Item> WOODEN_PLANKS = block(TrueEndBlocks.WOODEN_PLANKS);
	public static final RegistryObject<Item> WOODEN_STAIRS = block(TrueEndBlocks.WOODEN_STAIRS);
	public static final RegistryObject<Item> WOODEN_SLAB = block(TrueEndBlocks.WOODEN_SLAB);
	public static final RegistryObject<Item> FENCE = block(TrueEndBlocks.FENCE);
	public static final RegistryObject<Item> FENCE_GATE = block(TrueEndBlocks.FENCE_GATE);
	public static final RegistryObject<Item> DOOR = doubleBlock(TrueEndBlocks.DOOR);
	public static final RegistryObject<Item> TRAPDOOR = block(TrueEndBlocks.TRAPDOOR);
	public static final RegistryObject<Item> PRESSURE_PLATE = block(TrueEndBlocks.PRESSURE_PLATE);
	public static final RegistryObject<Item> BUTTON = block(TrueEndBlocks.BUTTON);
	public static final RegistryObject<Item> OBSIDIAN = block(TrueEndBlocks.OBSIDIAN);
	public static final RegistryObject<Item> GRAVEL = block(TrueEndBlocks.GRAVEL);
	public static final RegistryObject<Item> GLASS = block(TrueEndBlocks.GLASS);
	public static final RegistryObject<Item> SAND = block(TrueEndBlocks.SAND);
	public static final RegistryObject<Item> MYSTERIOUS_CUBE = REGISTRY.register("mysterious_cube", MysteriousCube::new);
	public static final RegistryObject<Item> UNKNOWN_SPAWN_EGG = REGISTRY.register("unknown_spawn_egg", () -> new ForgeSpawnEggItem(TrueEndEntities.UNKNOWN, -16777216, -1, new Item.Properties()));
	// do something to not make the game tint the item
	
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
	private static RegistryObject<Item> doubleBlock(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
	}
}
