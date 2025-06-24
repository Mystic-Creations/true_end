package net.justmili.trueend.init;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.entity.Unknown;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Entities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TrueEnd.MODID);

    public static final RegistryObject<EntityType<Unknown>> UNKNOWN = ENTITY_TYPES
            .register("unknown", () -> EntityType.Builder.of(Unknown::new, MobCategory.CREATURE)
            .sized(0.6f, 1.95f)
            .build(ResourceLocation.parse("true_end:unknown").toString())
    );
}