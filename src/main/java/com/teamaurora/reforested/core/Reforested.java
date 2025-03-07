package com.teamaurora.reforested.core;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.abnormals_core.core.util.registry.RegistryHelper;
import com.teamaurora.reforested.core.other.ReforestedEvents;
import com.teamaurora.reforested.core.registry.ReforestedBiomes;
import com.teamaurora.reforested.core.registry.ReforestedFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.Features;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

import static com.teamaurora.reforested.core.Reforested.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
@Mod.EventBusSubscriber(modid = MODID)
@SuppressWarnings("deprecation")
public class Reforested
{
    public static final String MODID = "reforested";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MODID);

    public Reforested() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRY_HELPER.register(eventBus);

        ReforestedFeatures.FEATURES.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);

        eventBus.addListener(EventPriority.LOWEST, this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ReforestedEvents());
        event.enqueueWork(() -> {
            ReforestedFeatures.Configured.registerConfiguredFeatures();
            Features.BIRCH = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG);
            Features.SUPER_BIRCH_BEES_0002 = ReforestedFeatures.FANCY_BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_0002_PLACEMENT)));
            Features.BIRCH_BEES_0002 = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_0002_PLACEMENT)));
            Features.BIRCH_BEES_002 = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_002_PLACEMENT)));
            Features.BIRCH_BEES_005 = ReforestedFeatures.BIRCH_TREE.get().withConfiguration(ReforestedFeatures.Configs.BIRCH_TREE_CONFIG.func_236685_a_(ImmutableList.of(Features.Placements.BEES_005_PLACEMENT)));

            ReforestedBiomes.addBiomeTypes();
            ReforestedBiomes.registerBiomesToDictionary();
            ReforestedBiomes.addBiomeVariants();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {

    }
}
