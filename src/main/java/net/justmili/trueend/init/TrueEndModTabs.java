package net.justmili.trueend.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.justmili.trueend.TrueEndMod;

public class TrueEndModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TrueEndMod.MODID);
	public static final RegistryObject<CreativeModeTab> TRUE_END = REGISTRY.register("true_end",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.true_end.true_end")).icon(() -> new ItemStack(TrueEndModBlocks.GRASS_BLOCK.get())).displayItems((parameters, tabData) -> {
				tabData.accept(TrueEndModBlocks.GRASS_BLOCK.get().asItem());
				tabData.accept(TrueEndModBlocks.DIRT.get().asItem());
				tabData.accept(TrueEndModBlocks.FARMLAND.get().asItem());
				tabData.accept(TrueEndModBlocks.STONE.get().asItem());
				tabData.accept(TrueEndModBlocks.COAL_ORE.get().asItem());
				tabData.accept(TrueEndModBlocks.IRON_ORE.get().asItem());
				tabData.accept(TrueEndModBlocks.GOLD_ORE.get().asItem());
				tabData.accept(TrueEndModBlocks.REDSTONE_ORE.get().asItem());
				tabData.accept(TrueEndModBlocks.DIAMOND_ORE.get().asItem());
				tabData.accept(TrueEndModBlocks.COBBLESTONE.get().asItem());
				tabData.accept(TrueEndModBlocks.MOSSY_COBBLESTONE.get().asItem());
				tabData.accept(TrueEndModBlocks.TREE_LEAVES.get().asItem());
				tabData.accept(TrueEndModBlocks.WOOD.get().asItem());
				tabData.accept(TrueEndModBlocks.WOOD_6_SIDED.get().asItem());
				tabData.accept(TrueEndModBlocks.WOODEN_PLANKS.get().asItem());
				tabData.accept(TrueEndModBlocks.WOODEN_STAIRS.get().asItem());
				tabData.accept(TrueEndModBlocks.WOODEN_SLAB.get().asItem());
				tabData.accept(TrueEndModBlocks.FENCE.get().asItem());
				tabData.accept(TrueEndModBlocks.FENCE_GATE.get().asItem());
				tabData.accept(TrueEndModBlocks.DOOR.get().asItem());
				tabData.accept(TrueEndModBlocks.TRAPDOOR.get().asItem());
				tabData.accept(TrueEndModBlocks.PRESSURE_PLATE.get().asItem());
				tabData.accept(TrueEndModBlocks.BUTTON.get().asItem());
				tabData.accept(TrueEndModBlocks.GRAVEL.get().asItem());
				tabData.accept(TrueEndModBlocks.SAND.get().asItem());
				tabData.accept(TrueEndModBlocks.GLASS.get().asItem());
				tabData.accept(TrueEndModBlocks.OBSIDIAN.get().asItem());
				tabData.accept(TrueEndModItems.MYSTERIOUS_CUBE.get());
			}).withSearchBar().build());
}
