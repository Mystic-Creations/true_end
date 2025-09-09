package net.mysticcreations.true_end.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mysticcreations.true_end.TrueEnd;

public class Tabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TrueEnd.MODID);
	public static final RegistryObject<CreativeModeTab> TRUE_END = REGISTRY.register("true_end",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.true_end.true_end")).icon(() -> new ItemStack(Blocks.GRASS_BLOCK.get())).displayItems((parameters, tabData) -> {
				tabData.accept(Blocks.GRASS_BLOCK.get().asItem());
				tabData.accept(Blocks.DIRT.get().asItem());
				tabData.accept(Blocks.FARMLAND.get().asItem());
				tabData.accept(Blocks.FLOWER.get().asItem());
				tabData.accept(Blocks.ROSE.get().asItem());
				tabData.accept(Blocks.SAPLING.get().asItem());
				tabData.accept(Blocks.LEAVES.get().asItem());
				tabData.accept(Blocks.WOOD.get().asItem());
				tabData.accept(Blocks.WOOD_6_SIDED.get().asItem());
				tabData.accept(Blocks.WOODEN_PLANKS.get().asItem());
				tabData.accept(Blocks.WOODEN_STAIRS.get().asItem());
				tabData.accept(Blocks.WOODEN_SLAB.get().asItem());
				tabData.accept(Blocks.FENCE.get().asItem());
				tabData.accept(Blocks.FENCE_GATE.get().asItem());
				tabData.accept(Blocks.DOOR.get().asItem());
				tabData.accept(Blocks.TRAPDOOR.get().asItem());
				tabData.accept(Blocks.PRESSURE_PLATE.get().asItem());
				tabData.accept(Blocks.BUTTON.get().asItem());
				tabData.accept(Blocks.STONE.get().asItem());
				tabData.accept(Blocks.COAL_ORE.get().asItem());
				tabData.accept(Blocks.IRON_ORE.get().asItem());
				tabData.accept(Blocks.GOLD_ORE.get().asItem());
				tabData.accept(Blocks.REDSTONE_ORE.get().asItem());
				tabData.accept(Blocks.DIAMOND_ORE.get().asItem());
				tabData.accept(Blocks.COBBLESTONE.get().asItem());
				tabData.accept(Blocks.MOSSY_COBBLESTONE.get().asItem());
				tabData.accept(Blocks.GRAVEL.get().asItem());
				tabData.accept(Blocks.SAND.get().asItem());
				tabData.accept(Blocks.GLASS.get().asItem());
				tabData.accept(Blocks.OBSIDIAN.get().asItem());
				tabData.accept(Items.MYSTERIOUS_CUBE.get());
				tabData.accept(Items.DREAMERS_COMPASS.get());
				tabData.accept(Items.MUSIC_DISC_FARLANDS.get());
				tabData.accept(Items.MUSIC_DISC_NEVER_ALONE.get());

				ItemStack blackVoid = new ItemStack(Items.VOID.get());
				blackVoid.getOrCreateTagElement("BlockStateTag").putString("type", "black");
				tabData.accept(blackVoid);
				ItemStack whiteVoid = new ItemStack(Items.VOID.get());
				whiteVoid.getOrCreateTagElement("BlockStateTag").putString("type", "white");
				tabData.accept(whiteVoid);
			}).withSearchBar().build()
	);
}
