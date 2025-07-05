package net.justmili.trueend.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class DreamersCompass extends CompassItem {
    private static final TagKey<Structure> STRUCTURE_KEY =
        TagKey.create(Registries.STRUCTURE, ResourceLocation.parse("true_end:the_dreaming_tree"));
    public DreamersCompass() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel) || entity == null) {
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains("LodestonePos") || !tag.contains("LodestoneDimension") || !tag.getBoolean("LodestoneTracked")) {
            BlockPos origin = entity.blockPosition();
            BlockPos targetPos = serverLevel.findNearestMapStructure(
                STRUCTURE_KEY,
                origin,
                100,
                true
            );

            if (targetPos != null) {
                tag.putLong("LodestonePos", targetPos.asLong());
                tag.putString("LodestoneDimension", serverLevel.dimension().location().toString());
                tag.putBoolean("LodestoneTracked", true);
            }
        }
        super.inventoryTick(stack, level, entity, slot, selected);
    }
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}