package net.justmili.trueend.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.justmili.trueend.TrueEnd;

public class TrueEndTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TrueEnd.MODID);
	public static final RegistryObject<CreativeModeTab> TRUE_END = REGISTRY.register("true_end",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.true_end.true_end")).icon(() -> new ItemStack(TrueEndBlocks.GRASS_BLOCK.get())).displayItems((parameters, tabData) -> {
				tabData.accept(TrueEndBlocks.GRASS_BLOCK.get().asItem());
				tabData.accept(TrueEndBlocks.DIRT.get().asItem());
				tabData.accept(TrueEndBlocks.FARMLAND.get().asItem());
				tabData.accept(TrueEndBlocks.LEAVES.get().asItem());
				tabData.accept(TrueEndBlocks.WOOD.get().asItem());
				tabData.accept(TrueEndBlocks.WOOD_6_SIDED.get().asItem());
				tabData.accept(TrueEndBlocks.WOODEN_PLANKS.get().asItem());
				tabData.accept(TrueEndBlocks.WOODEN_STAIRS.get().asItem());
				tabData.accept(TrueEndBlocks.WOODEN_SLAB.get().asItem());
				tabData.accept(TrueEndBlocks.FENCE.get().asItem());
				tabData.accept(TrueEndBlocks.FENCE_GATE.get().asItem());
				tabData.accept(TrueEndBlocks.DOOR.get().asItem());
				tabData.accept(TrueEndBlocks.TRAPDOOR.get().asItem());
				tabData.accept(TrueEndBlocks.PRESSURE_PLATE.get().asItem());
				tabData.accept(TrueEndBlocks.BUTTON.get().asItem());
				tabData.accept(TrueEndBlocks.STONE.get().asItem());
				tabData.accept(TrueEndBlocks.COAL_ORE.get().asItem());
				tabData.accept(TrueEndBlocks.IRON_ORE.get().asItem());
				tabData.accept(TrueEndBlocks.GOLD_ORE.get().asItem());
				tabData.accept(TrueEndBlocks.REDSTONE_ORE.get().asItem());
				tabData.accept(TrueEndBlocks.DIAMOND_ORE.get().asItem());
				tabData.accept(TrueEndBlocks.COBBLESTONE.get().asItem());
				tabData.accept(TrueEndBlocks.MOSSY_COBBLESTONE.get().asItem());
				tabData.accept(TrueEndBlocks.GRAVEL.get().asItem());
				tabData.accept(TrueEndBlocks.SAND.get().asItem());
				tabData.accept(TrueEndBlocks.GLASS.get().asItem());
				tabData.accept(TrueEndBlocks.OBSIDIAN.get().asItem());
				tabData.accept(TrueEndItems.MYSTERIOUS_CUBE.get());
			}).withSearchBar().build());
}
