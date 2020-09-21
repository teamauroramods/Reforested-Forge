package com.teamaurora.reforested.core.other;

import com.teamaurora.reforested.common.world.biome.ReforestedBiomeFeatures;
import com.teamaurora.reforested.common.world.gen.feature.config.BirchFeatureConfig;
import com.teamaurora.reforested.core.Reforested;
import com.teamaurora.reforested.core.compatibility.Fruitful;
import com.teamaurora.reforested.core.registry.ReforestedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

public class ReforestedEvents {
    @SubscribeEvent
    public void saplingGrowTree (SaplingGrowTreeEvent event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            BlockPos pos = event.getPos();
            BlockState state = world.getBlockState(pos);
            Random rand = event.getRand();
            if (state.getBlock() == Blocks.BIRCH_SAPLING) {
                event.setResult(Event.Result.DENY);
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                ConfiguredFeature<BirchFeatureConfig, ?> configuredFeature;
                BirchFeatureConfig DENSE_PEACH_BIRCH_TREE_CONFIG = (new BirchFeatureConfig.Builder(0.0F, new SimpleBlockStateProvider(ReforestedBiomeFeatures.BIRCH_LOG), (new WeightedBlockStateProvider()).addWeightedBlockstate(ReforestedBiomeFeatures.BIRCH_LEAVES,10).addWeightedBlockstate(Fruitful.PEACH_BIRCH_LEAVES.getDefaultState(),2), null, null, null)).func_236700_a_().build();
                BirchFeatureConfig CONFIG = ModList.get().isLoaded("fruitful") && world.getBlockState(pos.down()).getBlock() == Blocks.PODZOL ? DENSE_PEACH_BIRCH_TREE_CONFIG : ReforestedBiomeFeatures.BIRCH_TREE_CONFIG;
                if (rand.nextInt(10) == 0) {
                    configuredFeature = ReforestedFeatures.TALL_BIRCH_TREE.withConfiguration(CONFIG);
                } else {
                    configuredFeature = ReforestedFeatures.BIRCH_TREE.withConfiguration(CONFIG);
                }
                if (!configuredFeature.func_236265_a_(world, world.func_241112_a_(), world.getChunkProvider().getChunkGenerator(), rand, pos)) {
                    world.setBlockState(pos, state);
                }
            }
        }
    }
}
