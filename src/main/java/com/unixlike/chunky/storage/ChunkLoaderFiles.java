package com.unixlike.chunky.storage;

import java.util.HashMap;
import java.util.Map;
import com.unixlike.chunky.storage.ChunkLoaderFiles;

public class ChunkLoaderFiles {
    private static Map<String, ChunkLoaderFile> dimensions = new HashMap<>();

    public static void registerDimension(String dimension, ChunkLoaderFile chunkLoaderFile) {
        dimensions.put(dimension, chunkLoaderFile);
    }

    public static ChunkLoaderFile getChunkLoaderFile(String dimension) {
        return dimensions.get(dimension);
    }

}
