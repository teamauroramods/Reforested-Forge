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
import net.minecraft.world.gen.placement.ChanceConfig;
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

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Reforested.MODID);

    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> BIRCH_TREE = FEATURES.register("birch_tree", ()->new BirchFeature(BaseTreeFeatureConfig.CODEC));
    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> FANCY_BIRCH_TREE = FEATURES.register("fancy_birch_tree", ()->new FancyBirchFeature(BaseTreeFeatureConfig.CODEC));

    public static final class BlockStates {
        public static final BlockState BIRCH_LOG = Blocks.BIRCH_LOG.getDefaultState();
        public static final BlockState BIRCH_LEAVES = Blocks.BIRCH_LEAVES.getDefaultState();
    }

    public static final class Configs {
        public static final BaseTreeFeatureConfig BIRCH_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(BlockStates.BIRCH_LOG), new SimpleBlockStateProvider(BlockStates.BIRCH_LEAVES), new BlobFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0), 0), new StraightTrunkPlacer(0, 0, 0), new TwoLayerFeature(0, 0, 0))).setIgnoreVines().build();

        //public static final MultipleRandomFeatureConfig TREES_MIXED_TALL_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Configured.BIRCH_BEES_0002.withChance(0.1F), Configured.FANCY_BIRCH_BEES_0002.withChance(0.1F), Features.FANCY_OAK_BEES_0002.withChance(0.4F)), Features.OAK_BEES_0002)); //.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1)))
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

        public static final ConfiguredFeature<?, ?> TREES_FANCY_OAK = Features.FANCY_OAK.withPlacement(Placement.CHANCE.configure(new ChanceConfig(2)));
        public static final ConfiguredFeature<?, ?> TREES_MIXED_TALL = Feature.RANDOM_SELECTOR.withConfiguration((new MultipleRandomFeatureConfig(ImmutableList.of(Configured.BIRCH_BEES_0002.withChance(0.1F), Configured.FANCY_BIRCH_BEES_0002.withChance(0.1F), Features.FANCY_OAK_BEES_0002.withChance(0.4F)), Features.OAK_BEES_0002))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1)));

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

            register("trees_fancy_oak", TREES_FANCY_OAK);
        }
    }
}
