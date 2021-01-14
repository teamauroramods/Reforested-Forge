package com.teamaurora.reforested.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.BiomeUtil;
import com.minecraftabnormals.abnormals_core.core.util.registry.BiomeSubRegistryHelper;
import com.mojang.datafixers.util.Pair;
import com.teamaurora.reforested.common.world.biome.ReforestedBiomeFeatures;
import com.teamaurora.reforested.core.Reforested;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reforested.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ReforestedBiomes {
    private static final BiomeSubRegistryHelper HELPER = Reforested.REGISTRY_HELPER.getBiomeSubHelper();

    public static final BiomeSubRegistryHelper.KeyedBiome TALL_FOREST = HELPER.createBiome("tall_forest", () -> makeTallForestBiome(0.2F, 0.4F));
    public static final BiomeSubRegistryHelper.KeyedBiome TALL_FOREST_HILLS = HELPER.createBiome("tall_forest_hills", () -> makeTallForestBiome(0.55F, 0.5F));

    public static void addBiomeVariants() {
        BiomeUtil.addHillBiome(TALL_FOREST.getKey(), Pair.of(TALL_FOREST_HILLS.getKey(), 1));
    }

    public static void registerBiomesToDictionary() {
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(TALL_FOREST.getKey(), 2));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(TALL_FOREST_HILLS.getKey(), 0));
    }

    public static void addBiomeTypes() {
        BiomeDictionary.addTypes(TALL_FOREST.getKey(), BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.RARE, BiomeDictionary.Type.OVERWORLD);
        BiomeDictionary.addTypes(TALL_FOREST_HILLS.getKey(), BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.RARE, BiomeDictionary.Type.OVERWORLD);
    }

    private static Biome makeTallForestBiome(float depth, float scale) {
        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN).category(Biome.Category.FOREST).depth(depth).scale(scale).temperature(0.7F).downfall(0.8F).setEffects((new BiomeAmbience.Builder()).setWaterColor(4159204).setWaterFogColor(329011).setFogColor(12638463).withSkyColor(getSkyColorWithTemperatureModifier(0.7F)).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build()).withMobSpawnSettings(new MobSpawnInfo.Builder().copy()).withGenerationSettings((new BiomeGenerationSettings.Builder()).withSurfaceBuilder(ConfiguredSurfaceBuilders.field_244178_j).build()).build();

    }

    private static int getSkyColorWithTemperatureModifier(float temperature) {
        float lvt_1_1_ = temperature / 3.0F;
        lvt_1_1_ = MathHelper.clamp(lvt_1_1_, -1.0F, 1.0F);
        return MathHelper.hsvToRGB(0.62222224F - lvt_1_1_ * 0.05F, 0.5F + lvt_1_1_ * 0.1F, 1.0F);
    }
}
