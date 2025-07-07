package net.justmili.trueend.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import net.justmili.trueend.TrueEnd;

public class Sounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TrueEnd.MODID);
    public static final RegistryObject<SoundEvent> VINE_BOOM = REGISTRY.register("easter_egg.vine_boom", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("true_end:easter_egg.vine_boom")));
    public static final RegistryObject<SoundEvent> MOD_CREDITS_MUSIC = REGISTRY.register("music.modcredits", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("true_end:mod_credits")));
    public static final RegistryObject<SoundEvent> MUSIC_FARLANDS = REGISTRY.register("music_disc.farlands", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("true_end:mod_credits")));
    public static final RegistryObject<SoundEvent> MUSIC_NEVER_ALONE = REGISTRY.register("music_disc.never_alone", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("true_end:mod_credits")));
}
