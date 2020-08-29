package com.teamaurora.reforested.core.registry;

import com.teamaurora.reforested.common.world.gen.feature.BirchTreeFeature;
import com.teamaurora.reforested.common.world.gen.feature.TallBirchTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ReforestedFeatures {
    public static final Feature<BaseTreeFeatureConfig> BIRCH_TREE = new BirchTreeFeature(BaseTreeFeatureConfig.field_236676_a_);
    public static final Feature<BaseTreeFeatureConfig> TALL_BIRCH_TREE = new TallBirchTreeFeature(BaseTreeFeatureConfig.field_236676_a_);
}
