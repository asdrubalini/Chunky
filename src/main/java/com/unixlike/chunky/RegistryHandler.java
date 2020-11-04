package com.unixlike.chunky;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.unixlike.chunky.blocks.ChunkLoaderBlock;

public class RegistryHandler {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Chunky.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Chunky.MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> CHUNK_LOADER = BLOCKS.register("chunk_loader", () ->
        new ChunkLoaderBlock()
    );

    public static final RegistryObject<Item> CHUNK_LOADER_ITEM = ITEMS.register("chunk_loader", () ->
        new BlockItem(
            CHUNK_LOADER.get(), new Item.Properties().group(ItemGroup.MISC)
        )
   );

}
