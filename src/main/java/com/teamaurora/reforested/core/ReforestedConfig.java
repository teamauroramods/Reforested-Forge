package com.teamaurora.reforested.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class ReforestedConfig {
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<ArrayList<String>> peachBiomes;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common configurations for Reforested").push("common");

            builder.comment("Birch forest config").push("Birch");
            ArrayList<String> defaultPeachBiomes = new ArrayList<>();
            defaultPeachBiomes.add("minecraft:birch_forest");
            defaultPeachBiomes.add("minecraft:birch_forest_hills");
            defaultPeachBiomes.add("minecraft:tall_birch_forest");
            defaultPeachBiomes.add("minecraft:tall_birch_hills");
            peachBiomes = builder.define("Biomes peach trees can spawn in if Fruitful is installed", defaultPeachBiomes);
            builder.pop();

            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
