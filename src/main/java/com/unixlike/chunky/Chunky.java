package com.unixlike.chunky;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.unixlike.chunky.data.ChunkCoordinates;
import com.unixlike.chunky.storage.ChunkLoaderPosition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("chunky")
public class Chunky {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "chunky";

    public Chunky() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.addListener(this::onWorldLoaded);
        // MinecraftForge.EVENT_BUS.addListener(this::onWorldSaved);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.init();
    }

    public void onWorldLoaded(WorldEvent.Load event) {
		if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();

            ChunkLoaderPosition.init();

            for (ChunkCoordinates coordinate : ChunkLoaderPosition.getChunks()) {
                System.out.format("Loading chunk with x = %d and z = %d\n", coordinate.X, coordinate.Z);
                world.forceChunk(coordinate.X, coordinate.Z, true);
            }
        }
    }

    // public void onWorldSaved(WorldEvent.Save event) {
    //     if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld) {

    //     }
    // }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Chunky mod setup");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
    }
}
