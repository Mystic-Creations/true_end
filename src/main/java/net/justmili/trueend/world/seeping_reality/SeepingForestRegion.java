package net.justmili.trueend.world.seeping_reality;

import com.mojang.datafixers.util.Pair;
import net.justmili.trueend.init.Biomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class SeepingForestRegion extends Region {
    public SeepingForestRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        new ParameterUtils.ParameterPointListBuilder()
                .temperature(ParameterUtils.Temperature.NEUTRAL) // moderate
                .humidity(ParameterUtils.Humidity.DRY) // narrowed to just DRY
                .continentalness(ParameterUtils.Continentalness.INLAND)
                .erosion(ParameterUtils.Erosion.EROSION_1) // still 2 options
                .weirdness(ParameterUtils.Weirdness.span(
                        ParameterUtils.Weirdness.MID_SLICE_NORMAL_ASCENDING,
                        ParameterUtils.Weirdness.MID_SLICE_NORMAL_DESCENDING)) // slight variety
                .build().forEach(point -> builder.add(point, Biomes.SEEPING_REALITY));
        builder.build().forEach(mapper);
    }


}