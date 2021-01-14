package com.teamaurora.reforested.core.registry;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationPredicates;
import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.teamaurora.reforested.common.world.gen.feature.BirchFeature;
import com.teamaurora.reforested.common.world.gen.feature.FancyBirchFeature;
import com.teamaurora.reforested.core.Reforested;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Reforested.MODID)
public class ReforestedFeatures {

    private static boolean isBirch(BaseTreeFeatureConfig config) {
        if (config.trunkProvider instanceof SimpleBlockStateProvider) {
            if (((SimpleBlockStateProvider)config.trunkProvider).getBlockState(new Random(), new BlockPos(0, 0, 0)).getBlock() == Blocks.BIRCH_LOG) {
                return config.foliagePlacer instanceof BlobFoliagePlacer;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public static void addFeatures(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();

        boolean birchBiome = DataUtil.matchesKeys(biomeName, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS);

        List<Supplier<ConfiguredFeature<?, ?>>> features = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
        if (event.getName() != null) {
            List<Supplier<ConfiguredFeature<?, ?>>> toRemove = new ArrayList<>();
            List<ConfiguredFeature<?, ?>> toAdd = new ArrayList<>();
            for (Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier : features) {
                IFeatureConfig config = configuredFeatureSupplier.get().config;
                if (config instanceof DecoratedFeatureConfig) {
                    ConfiguredFeature<?, ?> configuredFeature1 = ((DecoratedFeatureConfig) config).feature.get();
                    IFeatureConfig config1 = configuredFeature1.config;
                    if (config1 instanceof DecoratedFeatureConfig) {
                        ConfiguredFeature<?, ?> configuredFeature = ((DecoratedFeatureConfig) config1).feature.get();
                        if (configuredFeature.config instanceof MultipleRandomFeatureConfig) {
                            MultipleRandomFeatureConfig mrfconfig = (MultipleRandomFeatureConfig) configuredFeature.config;

                            ConfiguredFeature<?, ?> tempDef = mrfconfig.defaultFeature.get();
                            if (tempDef.config instanceof BaseTreeFeatureConfig && isBirch((BaseTreeFeatureConfig) tempDef.config)) {
                                tempDef = Configured.BIRCH_BEES_0002;
                            }

                            List<ConfiguredRandomFeatureList> tempFeatures = new ArrayList<>();
                            for (ConfiguredRandomFeatureList crfl : mrfconfig.features) {
                                ConfiguredFeature<?, ?> crflFeature = crfl.feature.get();
                                if (crflFeature.config instanceof BaseTreeFeatureConfig && isBirch((BaseTreeFeatureConfig) crflFeature.config)) {
                                    tempFeatures.add(new ConfiguredRandomFeatureList(Configured.BIRCH_BEES_002, crfl.chance));
                                } else {
                                    tempFeatures.add(crfl);
                                }
                            }

                            MultipleRandomFeatureConfig tempConfig = new MultipleRandomFeatureConfig(tempFeatures, tempDef);
                            if (tempConfig != mrfconfig) {
                                toRemove.add(configuredFeatureSupplier);
                                Feature<MultipleRandomFeatureConfig> featureMRFC = (Feature<MultipleRandomFeatureConfig>) configuredFeature.feature;
                                ConfiguredFeature<?, ?> featurePlaced =  featureMRFC.withConfiguration(tempConfig).withPlacement(((DecoratedFeatureConfig) config1).decorator);
                                if (!birchBiome) toAdd.add(featurePlaced.withPlacement(((DecoratedFeatureConfig) config).decorator));
                            }
                        } else if (configuredFeature.config instanceof BaseTreeFeatureConfig) {
                            if (isBirch((BaseTreeFeatureConfig) configuredFeature.config)) {
                                toRemove.add(configuredFeatureSupplier);
                                ConfiguredFeature<?, ?> featurePlaced =  Configured.BIRCH_BEES_002.withPlacement(((DecoratedFeatureConfig) config1).decorator);
                                if (!birchBiome) toAdd.add(featurePlaced.withPlacement(((DecoratedFeatureConfig) config).decorator));
                            }
                        }
                    } else if (config1 instanceof MultipleRandomFeatureConfig) {
                        MultipleRandomFeatureConfig mrfconfig = (MultipleRandomFeatureConfig) configuredFeature1.config;

                        ConfiguredFeature<?, ?> tempDef = mrfconfig.defaultFeature.get();
                        if (tempDef.config instanceof BaseTreeFeatureConfig && isBirch((BaseTreeFeatureConfig) tempDef.config)) {
                            tempDef = Configured.BIRCH_BEES_0002;
                        }

                        List<ConfiguredRandomFeatureList> tempFeatures = new ArrayList<>();
                        for (ConfiguredRandomFeatureList crfl : mrfconfig.features) {
                            ConfiguredFeature<?, ?> crflFeature = crfl.feature.get();
                            if (crflFeature.config instanceof BaseTreeFeatureConfig && isBirch((BaseTreeFeatureConfig) crflFeature.config)) {
                                tempFeatures.add(new ConfiguredRandomFeatureList(Configured.BIRCH_BEES_002, crfl.chance));
                            } else {
                                tempFeatures.add(crfl);
                            }
                        }

                        MultipleRandomFeatureConfig tempConfig = new MultipleRandomFeatureConfig(tempFeatures, tempDef);
                        if (tempConfig != mrfconfig) {
                            toRemove.add(configuredFeatureSupplier);
                            Feature<MultipleRandomFeatureConfig> featureMRFC = (Feature<MultipleRandomFeatureConfig>) configuredFeature1.feature;
                            if (!birchBiome) toAdd.add(featureMRFC.withConfiguration(tempConfig).withPlacement(((DecoratedFeatureConfig) config).decorator));
                        }
                    }
                }
            }
            toRemove.forEach(features::remove);
            for (ConfiguredFeature<?, ?> f : toAdd) {
                event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, f);
            }
        }

        if (DataUtil.matchesKeys(biomeName, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Configured.TREES_BIRCH);
        } else if (DataUtil.matchesKeys(biomeName, Biomes.TALL_BIRCH_HILLS, Biomes.TALL_BIRCH_FOREST)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Configured.TREES_BIRCH_TALL);
        }
    }

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Reforested.MODID);

    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> BIRCH_TREE = FEATURES.register("birch_tree", ()->new BirchFeature(BaseTreeFeatureConfig.CODEC));
    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> FANCY_BIRCH_TREE = FEATURES.register("fancy_birch_tree", ()->new FancyBirchFeature(BaseTreeFeatureConfig.CODEC));

    public static final class BlockStates {
        public static final BlockState BIRCH_LOG = Blocks.BIRCH_LOG.getDefaultState();
        public static final BlockState BIRCH_LEAVES = Blocks.BIRCH_LEAVES.getDefaultState();
    }

    public static final class Configs {
        public static final BaseTreeFeatureConfig BIRCH_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(BlockStates.BIRCH_LOG), new SimpleBlockStateProvider(BlockStates.BIRCH_LEAVES), new BlobFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0), 0), new StraightTrunkPlacer(0, 0, 0), new TwoLayerFeature(0, 0, 0))).setIgnoreVines().build();
    }

    public static final class Configured {
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> BIRCH = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(Configs.BIRCH_TREE_CONFIG);
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FANCY_BIRCH = ReforestedFeatures.FANCY_BIRCH_TREE.get().withConfiguration(Configs.BIRCH_TREE_CONFIG);
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FANCY_BIRCH_BEES_0002 = ReforestedFeatures.FANCY_BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_0002_PLACEMENT)));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> BIRCH_BEES_0002 = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_0002_PLACEMENT)));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> BIRCH_BEES_002 = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_002_PLACEMENT)));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> BIRCH_BEES_005 = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_005_PLACEMENT)));

        public static final ConfiguredFeature<?, ?> TREES_BIRCH_TALL = Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(FANCY_BIRCH_BEES_0002.withChance(0.5F)), BIRCH_BEES_0002)).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1)));
        public static final ConfiguredFeature<?, ?> TREES_BIRCH = Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(FANCY_BIRCH_BEES_0002.withChance(0.1F)), BIRCH_BEES_0002)).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1)));



        private static <FC extends IFeatureConfig> void register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Reforested.MODID, name), configuredFeature);
        }

        public static void registerConfiguredFeatures() {
            register("birch", BIRCH);
            register("fancy_birch", FANCY_BIRCH);
            register("fancy_birch_bees_0002", FANCY_BIRCH_BEES_0002);
            register("birch_bees_0002", BIRCH_BEES_0002);
            register("birch_bees_002", BIRCH_BEES_002);
            register("birch_bees_005", BIRCH_BEES_005);

            register("trees_birch", TREES_BIRCH);
            register("trees_birch_tall", TREES_BIRCH_TALL);
        }
    }
}
