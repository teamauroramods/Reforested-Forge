package com.teamaurora.reforested.core.registry;

import com.teamaurora.reforested.common.world.biome.ReforestedBiomeFeatures;
import com.teamaurora.reforested.common.world.gen.feature.BirchShrubFeature;
import com.teamaurora.reforested.common.world.gen.feature.BirchTreeFeature;
import com.teamaurora.reforested.common.world.gen.feature.SmallMossyBoulderFeature;
import com.teamaurora.reforested.common.world.gen.feature.TallBirchTreeFeature;
import com.teamaurora.reforested.common.world.gen.feature.config.BirchFeatureConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.registries.ForgeRegistries;

public class ReforestedFeatures {
    public static final Feature<BirchFeatureConfig> BIRCH_TREE = new BirchTreeFeature(BirchFeatureConfig.field_236676_a_);
    public static final Feature<BirchFeatureConfig> TALL_BIRCH_TREE = new TallBirchTreeFeature(BirchFeatureConfig.field_236676_a_);
    public static final Feature<BaseTreeFeatureConfig> BIRCH_SHRUB = new BirchShrubFeature(BaseTreeFeatureConfig.field_236676_a_);
    public static final Feature<NoFeatureConfig> SMALL_BOULDER = new SmallMossyBoulderFeature(NoFeatureConfig.field_236558_a_);

    public static void generateFeatures() {
        ForgeRegistries.BIOMES.getValues().forEach(ReforestedFeatures::generate);
    }

    public static void generate(Biome biome) {
        ReforestedBiomeFeatures.replaceBirchTrees(biome);

        if (biome == Biomes.BIRCH_FOREST || biome == Biomes.BIRCH_FOREST_HILLS || biome == Biomes.TALL_BIRCH_HILLS) {
            ReforestedBiomeFeatures.addBirchForestFoliage(biome);
        }
        if (biome == Biomes.BIRCH_FOREST || biome == Biomes.BIRCH_FOREST_HILLS) {
            ReforestedBiomeFeatures.addSparseTallBirches(biome);
        }
        if (biome == Biomes.TALL_BIRCH_HILLS) {
            ReforestedBiomeFeatures.addSparseBirchShrubs(biome);
        }
        if (biome == Biomes.TALL_BIRCH_FOREST) {
            ReforestedBiomeFeatures.addTallBirchForestFoliage(biome);
        }
    }
}
