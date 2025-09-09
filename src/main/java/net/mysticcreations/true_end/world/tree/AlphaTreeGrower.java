package net.mysticcreations.true_end.world.tree;

import net.mysticcreations.true_end.world.ConfiguredFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class AlphaTreeGrower extends AbstractTreeGrower {
    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean p_222911_) {
        if (random.nextDouble() > 0.9) {
            return ConfiguredFeatures.ALPHA_TREE;
        } else {
            return ConfiguredFeatures.ALPHA_TREE_BIG;
        }
    }
}
