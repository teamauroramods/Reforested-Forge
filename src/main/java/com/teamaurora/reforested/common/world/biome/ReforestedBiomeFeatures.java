package com.teamaurora.reforested.common.world.biome;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.teamaurora.reforested.core.Reforested;
import com.teamaurora.reforested.core.registry.ReforestedBiomes;
import com.teamaurora.reforested.core.registry.ReforestedFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Reforested.MODID)
public class ReforestedBiomeFeatures {

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
                                tempDef = ReforestedFeatures.Configured.BIRCH_BEES_0002;
                            }

                            List<ConfiguredRandomFeatureList> tempFeatures = new ArrayList<>();
                            for (ConfiguredRandomFeatureList crfl : mrfconfig.features) {
                                ConfiguredFeature<?, ?> crflFeature = crfl.feature.get();
                                if (crflFeature.config instanceof BaseTreeFeatureConfig && isBirch((BaseTreeFeatureConfig) crflFeature.config)) {
                                    tempFeatures.add(new ConfiguredRandomFeatureList(ReforestedFeatures.Configured.BIRCH_BEES_002, crfl.chance));
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
                                ConfiguredFeature<?, ?> featurePlaced =  ReforestedFeatures.Configured.BIRCH_BEES_002.withPlacement(((DecoratedFeatureConfig) config1).decorator);
                                if (!birchBiome) toAdd.add(featurePlaced.withPlacement(((DecoratedFeatureConfig) config).decorator));
                            }
                        }
                    } else if (config1 instanceof MultipleRandomFeatureConfig) {
                        MultipleRandomFeatureConfig mrfconfig = (MultipleRandomFeatureConfig) configuredFeature1.config;

                        ConfiguredFeature<?, ?> tempDef = mrfconfig.defaultFeature.get();
                        if (tempDef.config instanceof BaseTreeFeatureConfig && isBirch((BaseTreeFeatureConfig) tempDef.config)) {
                            tempDef = ReforestedFeatures.Configured.BIRCH_BEES_0002;
                        }

                        List<ConfiguredRandomFeatureList> tempFeatures = new ArrayList<>();
                        for (ConfiguredRandomFeatureList crfl : mrfconfig.features) {
                            ConfiguredFeature<?, ?> crflFeature = crfl.feature.get();
                            if (crflFeature.config instanceof BaseTreeFeatureConfig && isBirch((BaseTreeFeatureConfig) crflFeature.config)) {
                                tempFeatures.add(new ConfiguredRandomFeatureList(ReforestedFeatures.Configured.BIRCH_BEES_002, crfl.chance));
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
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ReforestedFeatures.Configured.TREES_BIRCH);
        } else if (DataUtil.matchesKeys(biomeName, Biomes.TALL_BIRCH_HILLS, Biomes.TALL_BIRCH_FOREST)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ReforestedFeatures.Configured.TREES_BIRCH_TALL);
        } else if (DataUtil.matchesKeys(biomeName, Biomes.FOREST, Biomes.WOODED_HILLS)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ReforestedFeatures.Configured.TREES_FANCY_OAK);
        } else if (DataUtil.matchesKeys(biomeName, ReforestedBiomes.TALL_FOREST.getKey(), ReforestedBiomes.TALL_FOREST_HILLS.getKey())) {
            withTallForestFeatures(event.getGeneration(), event.getSpawns());
        }
    }

    public static void withTallForestTrees(BiomeGenerationSettings.Builder builder) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ReforestedFeatures.Configured.TREES_MIXED_TALL);
    }

    public static void withTallForestFeatures(BiomeGenerationSettingsBuilder builder, MobSpawnInfoBuilder spawns) {
        DefaultBiomeFeatures.withStrongholdAndMineshaft(builder);
        builder.withStructure(StructureFeatures.RUINED_PORTAL);
        DefaultBiomeFeatures.withCavesAndCanyons(builder);
        DefaultBiomeFeatures.withLavaAndWaterLakes(builder);
        DefaultBiomeFeatures.withMonsterRoom(builder);

        DefaultBiomeFeatures.withAllForestFlowerGeneration(builder);

        DefaultBiomeFeatures.withCommonOverworldBlocks(builder);
        DefaultBiomeFeatures.withOverworldOres(builder);
        DefaultBiomeFeatures.withDisks(builder);

        ReforestedBiomeFeatures.withTallForestTrees(builder);
        DefaultBiomeFeatures.withDefaultFlowers(builder);
        DefaultBiomeFeatures.withForestGrass(builder);

        DefaultBiomeFeatures.withNormalMushroomGeneration(builder);
        DefaultBiomeFeatures.withSugarCaneAndPumpkins(builder);
        DefaultBiomeFeatures.withLavaAndWaterSprings(builder);
        DefaultBiomeFeatures.withFrozenTopLayer(builder);

        DefaultBiomeFeatures.withBatsAndHostiles(spawns);
        DefaultBiomeFeatures.withPassiveMobs(spawns);
        spawns.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.WOLF, 5, 4, 4)).isValidSpawnBiomeForPlayer();
    }
}
