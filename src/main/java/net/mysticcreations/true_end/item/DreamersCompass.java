package net.mysticcreations.true_end.item;

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
        Pair<BlockPos, Holder<Structure>> result = serverLevel.getChunkSource()
                .getGenerator()
                .findNearestMapStructure(serverLevel, holderSet, origin, 100, false);

        tag.putBoolean("TargetFeet", false);

        if (result != null) {
            BlockPos structureRef = result.getFirst();
            int centerX = structureRef.getX() + (35 / 2); //Structure size X/2
            int centerZ = structureRef.getZ() + (40 / 2); //Structure size Z/2
            int centerY = structureRef.getY() + 1;

            net.minecraft.world.level.ChunkPos chunkPos = new net.minecraft.world.level.ChunkPos(structureRef);
            net.minecraft.world.level.chunk.LevelChunk chunk = serverLevel.getChunk(chunkPos.x, chunkPos.z);
            Holder<Structure> holder = result.getSecond();
            net.minecraft.world.level.levelgen.structure.StructureStart start = chunk.getStartForStructure(holder.value());

            if (start != null) {
                var box = start.getBoundingBox();

                int minX = box.minX();
                int maxX = box.maxX();
                int minY = box.minY();
                int maxY = box.maxY();
                int minZ = box.minZ();
                int maxZ = box.maxZ();

                centerX = (minX + maxX) / 2;
                centerZ = (minZ + maxZ) / 2;
                centerY = Math.max(minY + 1, (minY + maxY) / 2);
            }

            tag.putInt("TargetX", centerX - 3);
            tag.putInt("TargetY", centerY);
            tag.putInt("TargetZ", centerZ - 3);
            tag.putString("TargetDimension", serverLevel.dimension().location().toString());
            tag.putBoolean("TargetTracked", true);
            tag.putBoolean("TargetFeet", true);
        } else {
            tag.putBoolean("TargetTracked", false);
        }
        super.inventoryTick(stack, level, entity, slot, selected);
    }
}