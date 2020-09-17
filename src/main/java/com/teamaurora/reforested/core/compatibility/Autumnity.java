package com.teamaurora.reforested.core.compatibility;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ObjectHolder;

public class Autumnity {
    @ObjectHolder("autumnity:maple_tree")
    public static final Feature<BaseTreeFeatureConfig> MAPLE_TREE = null;

    @ObjectHolder("autumnity:maple_log")
    public static final Block MAPLE_LOG_BLOCK = null;
    @ObjectHolder("autumnity:yellow_maple_leaves")
    public static final Block YELLOW_MAPLE_LEAVES_BLOCK = null;

    public static boolean isInstalled()
    {
        return ModList.get() != null && ModList.get().getModContainerById("autumnity").isPresent();
    }
}
