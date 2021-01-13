package com.teamaurora.reforested.core.registry;

import com.teamaurora.reforested.common.world.gen.feature.BirchFeature;
import com.teamaurora.reforested.common.world.gen.feature.FancyBirchFeature;
import com.teamaurora.reforested.core.Reforested;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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
    }

    public static final class Configured {
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> BIRCH = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(Configs.BIRCH_TREE_CONFIG);
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> FANCY_BIRCH = ReforestedFeatures.FANCY_BIRCH_TREE.get().withConfiguration(Configs.BIRCH_TREE_CONFIG);

        private static <FC extends IFeatureConfig> void register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Reforested.MODID, name), configuredFeature);
        }

        public static void registerConfiguredFeatures() {
            register("birch", BIRCH);
            register("fancy_birch", FANCY_BIRCH);
        }
    }
}
