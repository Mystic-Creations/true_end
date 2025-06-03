package net.justmili.trueend.init;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.entity.UnknownEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TrueEndEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TrueEnd.MODID);

    public static final RegistryObject<EntityType<UnknownEntity>> UNKNOWN =
        ENTITY_TYPES.register("unknown", () ->
            EntityType.Builder.<UnknownEntity>of(UnknownEntity::new, MobCategory.MONSTER)
                .sized(0.6f, 1.95f)
                .build(new ResourceLocation(TrueEnd.MODID, "unknown").toString())
        );
}