package com.teamaurora.reforested.core.compatibility;

import net.minecraft.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ObjectHolder;

public class Fruitful {
    @ObjectHolder("fruiful:peach_birch_leaves")
    public static final Block PEACH_BIRCH_LEAVES = null;

    public static boolean isInstalled()
    {
        return ModList.get() != null && ModList.get().getModContainerById("fruitful").isPresent();
    }
}
