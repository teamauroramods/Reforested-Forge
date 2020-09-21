package com.teamaurora.reforested.core.compatibility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

public class Fruitful {
    @ObjectHolder("fruitful:peach_birch_leaves")
    public static final Block PEACH_BIRCH_LEAVES = null;

    //public static final Supplier<BlockState> PEACH_BIRCH_LEAVES_SUPPLIER = PEACH_BIRCH_LEAVES::getDefaultState;

    public static boolean isInstalled()
    {
        return ModList.get() != null && ModList.get().getModContainerById("fruitful").isPresent();
    }
}
