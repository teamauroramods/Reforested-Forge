package com.teamaurora.reforested.common.world.biome;

import com.google.common.collect.ImmutableList;
import com.teamaurora.reforested.common.world.gen.feature.config.BirchFeatureConfig;
import com.teamaurora.reforested.common.world.gen.treedecorator.BeehiveTreeDecorator;
import com.teamaurora.reforested.core.registry.ReforestedFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class ReforestedBiomeFeatures {
    @ObjectHolder("fruitful:peach_birch_leaves")
    public static final Block PEACH_BIRCH_LEAVES_BLOCK = null;

    public static BlockState BIRCH_LOG = Blocks.BIRCH_LOG.getDefaultState();
    public static BlockState BIRCH_LEAVES = Blocks.BIRCH_LEAVES.getDefaultState();
    public static BlockState PEACH_BIRCH_LEAVES = PEACH_BIRCH_LEAVES_BLOCK == null ? Blocks.BIRCH_LEAVES.getDefaultState() : PEACH_BIRCH_LEAVES_BLOCK.getDefaultState();

    private static final BeehiveTreeDecorator field_235163_cx_ = new BeehiveTreeDecorator(0.002F);
    private static final BeehiveTreeDecorator field_235164_cy_ = new BeehiveTreeDecorator(0.02F);
    private static final BeehiveTreeDecorator field_235165_cz_ = new BeehiveTreeDecorator(0.05F);

    private static final BeehiveTreeDecorator BIRCH_BEEHIVE_DECORATOR = new BeehiveTreeDecorator(0.1F);

    public static final BaseTreeFeatureConfig BIRCH_SHRUB_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(BIRCH_LOG), new SimpleBlockStateProvider(BIRCH_LEAVES), null, null, null)).func_236700_a_().build();

    public static final BirchFeatureConfig BIRCH_TREE_CONFIG = (new BirchFeatureConfig.Builder(0.0F, new SimpleBlockStateProvider(BIRCH_LOG), new SimpleBlockStateProvider(BIRCH_LEAVES), null, null, null)).func_236700_a_().build();
    public static final BirchFeatureConfig BIRCH_TREE_BEEHIVES_1_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.002F);
    public static final BirchFeatureConfig BIRCH_TREE_BEEHIVES_2_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.02F);
    public static final BirchFeatureConfig BIRCH_TREE_BEEHIVES_3_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.05F);
    public static final BirchFeatureConfig BIRCH_TREE_BEEHIVES_4_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.025F);

    public static final BirchFeatureConfig PEACH_BIRCH_TREE_CONFIG = (new BirchFeatureConfig.Builder(0.0F, new SimpleBlockStateProvider(BIRCH_LOG), (new WeightedBlockStateProvider()).addWeightedBlockstate(BIRCH_LEAVES,149).addWeightedBlockstate(PEACH_BIRCH_LEAVES,1), null, null, null)).func_236700_a_().build();
    public static final BirchFeatureConfig PEACH_BIRCH_TREE_BEEHIVES_1_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.002F);
    public static final BirchFeatureConfig PEACH_BIRCH_TREE_BEEHIVES_2_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.02F);
    public static final BirchFeatureConfig PEACH_BIRCH_TREE_BEEHIVES_3_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.05F);
    public static final BirchFeatureConfig PEACH_BIRCH_TREE_BEEHIVES_4_CONFIG = BIRCH_TREE_CONFIG.withBeehiveChance(0.025F);

    public static final BirchFeatureConfig DENSE_PEACH_BIRCH_TREE_CONFIG = (new BirchFeatureConfig.Builder(0.0F, new SimpleBlockStateProvider(BIRCH_LOG), (new WeightedBlockStateProvider()).addWeightedBlockstate(BIRCH_LEAVES,10).addWeightedBlockstate(PEACH_BIRCH_LEAVES,2), null, null, null)).func_236700_a_().build();


    public static void replaceBirchTrees(Biome biome) {
        List<ConfiguredFeature<?, ?>> list = biome.getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
        List<ConfiguredFeature<?, ?>> toRemove = new ArrayList<>();
        int listSize = list.size();

        boolean isBirchBiome = (biome == Biomes.BIRCH_FOREST || biome == Biomes.BIRCH_FOREST_HILLS || biome == Biomes.TALL_BIRCH_FOREST || biome == Biomes.TALL_BIRCH_HILLS);

        for (int i = 0; i < listSize; i++) {
            ConfiguredFeature<?, ?> configuredFeature = list.get(i);
            if (configuredFeature.config instanceof DecoratedFeatureConfig) {
                DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) configuredFeature.config;
                if (decorated.feature.config instanceof MultipleRandomFeatureConfig) {
                    MultipleRandomFeatureConfig tree = (MultipleRandomFeatureConfig) decorated.feature.config;
                    List<ConfiguredRandomFeatureList<?>> tempFeatures = new ArrayList<>();
                    for (ConfiguredRandomFeatureList crfl : tree.features) {
                        if (crfl.feature.feature instanceof TreeFeature) {
                            if (isBirchBiome && (crfl.feature.config == DefaultBiomeFeatures.BIRCH_TREE_CONFIG || crfl.feature.config == DefaultBiomeFeatures.field_230129_h_ || crfl.feature.config == DefaultBiomeFeatures.field_230135_r_ || crfl.feature.config == DefaultBiomeFeatures.field_230136_s_)) {
                                tempFeatures.add(new ConfiguredRandomFeatureList<BirchFeatureConfig>(ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_4_CONFIG), crfl.chance));
                            } else if (crfl.feature.config == DefaultBiomeFeatures.BIRCH_TREE_CONFIG) {
                                tempFeatures.add(new ConfiguredRandomFeatureList<BirchFeatureConfig>(ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_CONFIG), crfl.chance));
                            } else if (crfl.feature.config == DefaultBiomeFeatures.field_230129_h_) {
                                tempFeatures.add(new ConfiguredRandomFeatureList<BirchFeatureConfig>(ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_1_CONFIG), crfl.chance));
                            } else if (crfl.feature.config == DefaultBiomeFeatures.field_230135_r_) {
                                tempFeatures.add(new ConfiguredRandomFeatureList<BirchFeatureConfig>(ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_2_CONFIG), crfl.chance));
                            } else if (crfl.feature.config == DefaultBiomeFeatures.field_230136_s_) {
                                tempFeatures.add(new ConfiguredRandomFeatureList<BirchFeatureConfig>(ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_3_CONFIG), crfl.chance));
                            } else if (crfl.feature.config == DefaultBiomeFeatures.field_230130_i_) {
                                tempFeatures.add(new ConfiguredRandomFeatureList<BirchFeatureConfig>(ReforestedFeatures.TALL_BIRCH_TREE.withConfiguration(BIRCH_TREE_CONFIG), crfl.chance));
                            } else {
                                tempFeatures.add(crfl);
                            }
                        } else {
                            tempFeatures.add(crfl);
                        }
                    }
                    if (tree.defaultFeature.feature instanceof TreeFeature) {
                        //BaseTreeFeatureConfig tempDefCfg = (BaseTreeFeatureConfig) tree.defaultFeature.config;
                        //if (((BaseTreeFeatureConfig) tree.defaultFeature.config).leavesProvider.getBlockState(probeRand, probePos).getBlock() == Blocks.OAK_LEAVES) {
                        BaseTreeFeatureConfig treeCfg = (BaseTreeFeatureConfig) tree.defaultFeature.config;
                        ConfiguredFeature<?, ?> cfgdTree = new ConfiguredFeature<BaseTreeFeatureConfig, TreeFeature>((TreeFeature)tree.defaultFeature.feature, (BaseTreeFeatureConfig)tree.defaultFeature.config);
                        if (isBirchBiome && (treeCfg == DefaultBiomeFeatures.BIRCH_TREE_CONFIG || treeCfg == DefaultBiomeFeatures.field_230129_h_ || treeCfg == DefaultBiomeFeatures.field_230135_r_ || treeCfg == DefaultBiomeFeatures.field_230136_s_)) {
                            cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_4_CONFIG);
                        } else if (treeCfg == DefaultBiomeFeatures.BIRCH_TREE_CONFIG) {
                            cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_CONFIG);
                        } else if (treeCfg == DefaultBiomeFeatures.field_230129_h_) {
                            cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_1_CONFIG);
                        } else if (treeCfg == DefaultBiomeFeatures.field_230135_r_) {
                            cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_2_CONFIG);
                        } else if (treeCfg == DefaultBiomeFeatures.field_230136_s_) {
                            cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_3_CONFIG);
                        } else if (treeCfg == DefaultBiomeFeatures.field_230130_i_) {
                            cfgdTree = ReforestedFeatures.TALL_BIRCH_TREE.withConfiguration(BIRCH_TREE_CONFIG);
                        }
                        //ConfiguredFeature<?,?> tempDef = new ConfiguredFeature<BaseTreeFeatureConfig, Feature<BaseTreeFeatureConfig>>((Feature<BaseTreeFeatureConfig>) tree.defaultFeature.feature, tempDefCfg);
                        ConfiguredFeature<DecoratedFeatureConfig, ?> tempFeature = new ConfiguredFeature<DecoratedFeatureConfig, DecoratedFeature>(
                                (DecoratedFeature) configuredFeature.feature, new DecoratedFeatureConfig(
                                new ConfiguredFeature<MultipleRandomFeatureConfig, Feature<MultipleRandomFeatureConfig>>((Feature<MultipleRandomFeatureConfig>) decorated.feature.feature,
                                        new MultipleRandomFeatureConfig(tempFeatures, cfgdTree)
                                ), decorated.decorator
                        ));
                        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, tempFeature);
                    } else {
                        ConfiguredFeature<DecoratedFeatureConfig, ?> tempFeature = new ConfiguredFeature<DecoratedFeatureConfig, DecoratedFeature>(
                                (DecoratedFeature) configuredFeature.feature, new DecoratedFeatureConfig(
                                new ConfiguredFeature<MultipleRandomFeatureConfig, Feature<MultipleRandomFeatureConfig>>((Feature<MultipleRandomFeatureConfig>) decorated.feature.feature,
                                        new MultipleRandomFeatureConfig(tempFeatures, tree.defaultFeature)
                                ), decorated.decorator
                        ));
                        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, tempFeature);
                    }
                    toRemove.add(configuredFeature);
                } else if (decorated.feature.config instanceof BaseTreeFeatureConfig && decorated.feature.feature instanceof TreeFeature) {
                    BaseTreeFeatureConfig treeCfg = (BaseTreeFeatureConfig) decorated.feature.config;
                    ConfiguredFeature<?, ?> cfgdTree = new ConfiguredFeature<>((TreeFeature)decorated.feature.feature, (BaseTreeFeatureConfig)decorated.feature.config);
                    if (isBirchBiome && (treeCfg == DefaultBiomeFeatures.BIRCH_TREE_CONFIG || treeCfg == DefaultBiomeFeatures.field_230129_h_ || treeCfg == DefaultBiomeFeatures.field_230135_r_ || treeCfg == DefaultBiomeFeatures.field_230136_s_)) {
                        cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_4_CONFIG);
                    } if (treeCfg == DefaultBiomeFeatures.BIRCH_TREE_CONFIG) {
                        cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_CONFIG);
                    } else if (treeCfg == DefaultBiomeFeatures.field_230129_h_) {
                        cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_1_CONFIG);
                    } else if (treeCfg == DefaultBiomeFeatures.field_230135_r_) {
                        cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_2_CONFIG);
                    } else if (treeCfg == DefaultBiomeFeatures.field_230136_s_) {
                        cfgdTree = ReforestedFeatures.BIRCH_TREE.withConfiguration(BIRCH_TREE_BEEHIVES_3_CONFIG);
                    } else if (treeCfg == DefaultBiomeFeatures.field_230130_i_) {
                        cfgdTree = ReforestedFeatures.TALL_BIRCH_TREE.withConfiguration(BIRCH_TREE_CONFIG);
                    }
                    ConfiguredFeature<DecoratedFeatureConfig, ?> tempFeature = new ConfiguredFeature<DecoratedFeatureConfig, DecoratedFeature>(
                            (DecoratedFeature) configuredFeature.feature, new DecoratedFeatureConfig(
                            cfgdTree, decorated.decorator
                    ));
                    biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, tempFeature);
                    toRemove.add(configuredFeature);
                }
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            list.remove(toRemove.get(i));
        }
    }

    public static void addSparseTallBirches(Biome biomeIn) {
        biomeIn.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ReforestedFeatures.TALL_BIRCH_TREE.withConfiguration(BIRCH_TREE_CONFIG).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(4))));
    }

    public static void addBirchForestFoliage(Biome biomeIn) {
        biomeIn.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, ReforestedFeatures.SMALL_BOULDER.withConfiguration(new NoFeatureConfig()).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(2))));
    }

    public static void addTallBirchForestFoliage(Biome biomeIn) {
        biomeIn.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, ReforestedFeatures.SMALL_BOULDER.withConfiguration(new NoFeatureConfig()).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
        biomeIn.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ReforestedFeatures.BIRCH_SHRUB.withConfiguration(BIRCH_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.25F, 1))));
    }

    public static void addSparseBirchShrubs(Biome biomeIn) {
        biomeIn.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ReforestedFeatures.BIRCH_SHRUB.withConfiguration(BIRCH_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
    }
}
