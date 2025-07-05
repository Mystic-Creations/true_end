package net.justmili.trueend.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

public class DreamersCompass extends CompassItem {
    private static final ResourceKey<Structure> STRUCTURE_KEY =
        ResourceKey.create(Registries.STRUCTURE, ResourceLocation.parse("true_end:the_dreaming_tree"));
    public DreamersCompass() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel) || entity == null) {
            return;
        }

        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        HolderSet<Structure> holderSet = registry.getHolder(STRUCTURE_KEY).map(HolderSet::direct).orElseThrow();


        CompoundTag tag = stack.getOrCreateTag();

            BlockPos origin = entity.blockPosition();
            Pair<BlockPos, Holder<Structure>> result = serverLevel.getChunkSource().getGenerator().findNearestMapStructure
                    (serverLevel,holderSet, origin, 100, false);

        tag.putBoolean("feet", false);
            if (result != null) {
                BlockPos targetPos = result.getFirst();
                tag.putLong("LodestonePosX", targetPos.getX());
                tag.putLong("LodestonePosY", targetPos.getY());
                tag.putLong("LodestonePosZ", targetPos.getZ());
                tag.putString("LodestoneDimension", serverLevel.dimension().location().toString());
                tag.putBoolean("LodestoneTracked", true);
                tag.putBoolean("feet", true);
            }
        super.inventoryTick(stack, level, entity, slot, selected);
    }
}