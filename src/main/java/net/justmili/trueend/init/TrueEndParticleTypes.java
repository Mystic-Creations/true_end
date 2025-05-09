
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.justmili.trueend.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.justmili.trueend.TrueEnd;

public class TrueEndParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TrueEnd.MODID);
	public static final RegistryObject<SimpleParticleType> DREAM_PORTAL_PARTICLE = REGISTRY.register("dream_portal_particle", () -> new SimpleParticleType(false));
}
