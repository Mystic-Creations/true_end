package net.justmili.trueend.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.justmili.trueend.TrueEnd;

public class Sounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TrueEnd.MODID);
    private static RegistryObject<SoundEvent> sound(String name) {
        return REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse(TrueEnd.MODID+":"+name)));
    }
    public static final RegistryObject<SoundEvent> VINE_BOOM = sound("vine_boom");
    public static final RegistryObject<SoundEvent> MOD_CREDITS_MUSIC = sound("back_in_the_game");
    public static final RegistryObject<SoundEvent> MUSIC_FARLANDS = sound("farlands");
    public static final RegistryObject<SoundEvent> MUSIC_NEVER_ALONE = sound("never_alone");
}