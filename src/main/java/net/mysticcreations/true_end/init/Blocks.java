package net.mysticcreations.true_end.init;

import net.mysticcreations.true_end.block.*;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import net.mysticcreations.true_end.TrueEnd;
import net.mysticcreations.true_end.block.Void;

public class Blocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, TrueEnd.MODID);
	public static final RegistryObject<Block> DIRT = REGISTRY.register("dirt", Dirt::new);
	public static final RegistryObject<Block> GRASS_BLOCK = REGISTRY.register("grass_block", GrassBlock::new);
	public static final RegistryObject<Block> FARMLAND = REGISTRY.register("farmland", Farmland::new);
	public static final RegistryObject<Block> COBBLESTONE = REGISTRY.register("cobblestone", Cobblestone::new);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE = REGISTRY.register("mossy_cobblestone", MossyCobblestone::new);
	public static final RegistryObject<Block> STONE = REGISTRY.register("stone", Stone::new);
	public static final RegistryObject<Block> COAL_ORE = REGISTRY.register("coal_ore", CoalOre::new);
	public static final RegistryObject<Block> IRON_ORE = REGISTRY.register("iron_ore", IronOre::new);
	public static final RegistryObject<Block> GOLD_ORE = REGISTRY.register("gold_ore", GoldOre::new);
	public static final RegistryObject<Block> REDSTONE_ORE = REGISTRY.register("redstone_ore", RedstoneOre::new);
	public static final RegistryObject<Block> DIAMOND_ORE = REGISTRY.register("diamond_ore", DiamondOre::new);
	public static final RegistryObject<Block> WOOD = REGISTRY.register("wood", Wood::new);
	public static final RegistryObject<Block> WOODEN_PLANKS = REGISTRY.register("wooden_planks", WoodenPlanks::new);
	public static final RegistryObject<Block> WOODEN_STAIRS = REGISTRY.register("wooden_stairs", WoodenStairs::new);
	public static final RegistryObject<Block> WOODEN_SLAB = REGISTRY.register("wooden_slab", WoodenSlab::new);
	public static final RegistryObject<Block> FENCE = REGISTRY.register("fence", WoodenFence::new);
	public static final RegistryObject<Block> FENCE_GATE = REGISTRY.register("fence_gate", WoodenFenceGate::new);
	public static final RegistryObject<Block> DOOR = REGISTRY.register("door", WoodenDoor::new);
	public static final RegistryObject<Block> TRAPDOOR = REGISTRY.register("trapdoor", WoodenTrapdoor::new);
	public static final RegistryObject<Block> PRESSURE_PLATE = REGISTRY.register("pressure_plate", WoodenPressurePlate::new);
	public static final RegistryObject<Block> BUTTON = REGISTRY.register("button", WoodenButton::new);
	public static final RegistryObject<Block> LEAVES = REGISTRY.register("leaves", Leaves::new);
	public static final RegistryObject<Block> OBSIDIAN = REGISTRY.register("obsidian", Obsidian::new);
	public static final RegistryObject<Block> GRAVEL = REGISTRY.register("gravel", Gravel::new);
	public static final RegistryObject<Block> WOOD_6_SIDED = REGISTRY.register("wood_6_sided", Wood6Sided::new);
	public static final RegistryObject<Block> BEYOND_THE_DREAM_PORTAL = REGISTRY.register("beyond_the_dream_portal", BeyondTheDreamPortal::new);
	public static final RegistryObject<Block> GLASS = REGISTRY.register("glass", Glass::new);
	public static final RegistryObject<Block> SAND = REGISTRY.register("sand", Sand::new);
	public static final RegistryObject<Block> FLOWER = REGISTRY.register("flower", Flower::new);
	public static final RegistryObject<Block> ROSE = REGISTRY.register("rose", Rose::new);
	public static final RegistryObject<Block> SAPLING = REGISTRY.register("sapling", Sapling::new);
	public static final RegistryObject<Block> VOID = REGISTRY.register("void", Void::new);

}
