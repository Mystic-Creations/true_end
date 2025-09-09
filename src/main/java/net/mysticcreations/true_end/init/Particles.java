package net.mysticcreations.true_end.init;

import net.mysticcreations.true_end.TrueEnd;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mysticcreations.true_end.client.particle.DreamPortalParticle;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Particles {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TrueEnd.MODID);
	public static final RegistryObject<SimpleParticleType> DREAM_PORTAL_PARTICLE = REGISTRY.register("dream_portal_particle", () -> new SimpleParticleType(false));

	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(DREAM_PORTAL_PARTICLE.get(), DreamPortalParticle::provider);
	}
}
