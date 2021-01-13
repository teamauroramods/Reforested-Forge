package com.teamaurora.reforested.core.other;

import com.teamaurora.reforested.core.Reforested;
import com.teamaurora.reforested.core.registry.ReforestedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Reforested.MODID)
public class ReforestedEvents {

    @SubscribeEvent
    public void onSaplingGrowTree (SaplingGrowTreeEvent event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            BlockPos pos = event.getPos();
            BlockState state = world.getBlockState(pos);
            Random rand = event.getRand();
            if (state.getBlock() == Blocks.BIRCH_SAPLING) {
                event.setResult(Event.Result.DENY);
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                ConfiguredFeature<BaseTreeFeatureConfig, ?> configuredFeature;
                if (rand.nextInt(10) == 0) {
                    configuredFeature = ReforestedFeatures.FANCY_BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG);
                } else {
                    configuredFeature = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG);
                }
                if (!configuredFeature.generate(world, world.getChunkProvider().getChunkGenerator(), rand, pos)) {
                    world.setBlockState(pos, state);
                }
            }
        }
    }
}
