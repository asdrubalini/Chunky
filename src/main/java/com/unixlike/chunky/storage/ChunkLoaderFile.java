package com.unixlike.chunky.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unixlike.chunky.Chunky;
import com.unixlike.chunky.serialization.ChunkCoordinates;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ChunkLoaderFile {
    private static Logger logger = Logger.getLogger(Chunky.MOD_ID);

    private static final FolderName FOLDER = new FolderName(Chunky.MOD_ID);
    private static final String CHUNK_FILE_NAME = "chunks.%s.json";

    private List<ChunkCoordinates> addQueque = new ArrayList<>();
    private List<ChunkCoordinates> removeQueque = new ArrayList<>();

    private static Type coordinatesListType = new TypeToken<List<ChunkCoordinates>>(){}.getType();

    private String dimension;

    public ChunkLoaderFile(String dimension) {
        this.dimension = dimension;

        // Create dir and file if not exists
        File dir = getBaseDir();
        dir.mkdirs();

        File chunkFile = getChunkFile();

        if (chunkFile.exists()) {
            return;
        }

        try {
            chunkFile.createNewFile();
            FileWriter writer = new FileWriter(chunkFile);

            // Write empty list
            writer.write("[]");

            writer.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Error while creating chunk file: %s", e));
        }
    }

    private File getBaseDir() {
        return ServerLifecycleHooks.getCurrentServer().func_240776_a_( FOLDER ).toFile();
    }

    private File getChunkFile() {
        return new File(getBaseDir(), getFileName());
    }

    private String getFileName() {
        return String.format(CHUNK_FILE_NAME, dimension);
    }

    public void addToAddQueque(int chunkX, int chunkZ) {
        ChunkCoordinates coords = new ChunkCoordinates(chunkX, chunkZ);
        addQueque.add(coords);
    }

    public void addToRemoveQueque(int chunkX, int chunkZ) {
        ChunkCoordinates coords = new ChunkCoordinates(chunkX, chunkZ);
        removeQueque.add(coords);
    }

    public void flushQuequeToFile() {
        List<ChunkCoordinates> chunks = readChunksFromFile();

        for (ChunkCoordinates coords : addQueque) {
            if (chunks != null && chunks.contains(coords)) {
                continue;
            }

            chunks.add(coords);
        }

        for (ChunkCoordinates coords : removeQueque) {
            if (chunks != null && !chunks.contains(coords)) {
                continue;
            }

            chunks.remove(coords);
        }

        Gson gson = new Gson();
        File chunkFile = getChunkFile();

        try {
            FileWriter writer = new FileWriter(chunkFile);

            String jsonString = gson.toJson(chunks);
            writer.write(jsonString);

            writer.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Error writing to file: %s", e));
        }
    }

    public void loadFromFile(ServerWorld world) {
        for (ChunkCoordinates coordinate : readChunksFromFile()) {
            logger.log(Level.INFO, String.format("Loading chunk with x = %d and z = %d\n", coordinate.X, coordinate.Z));
            world.forceChunk(coordinate.X, coordinate.Z, true);
        }
    }

    public List<ChunkCoordinates> readChunksFromFile() {
        File chunkFile = getChunkFile();
        List<ChunkCoordinates> coordinatesList = new ArrayList<>();
        Gson gson = new Gson();

        try {
            JsonReader reader = new JsonReader(new FileReader(chunkFile));
            coordinatesList = gson.fromJson(reader, coordinatesListType);
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Error while from file: %s", e));
        }

        if (coordinatesList == null) {
            return new ArrayList<ChunkCoordinates>();
        }

        return coordinatesList;
    }
}
