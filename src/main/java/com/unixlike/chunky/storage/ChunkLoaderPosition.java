package com.unixlike.chunky.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.unixlike.chunky.Chunky;
import com.unixlike.chunky.data.ChunkCoordinates;

import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ChunkLoaderPosition {
    private static final FolderName FOLDER = new FolderName(Chunky.MOD_ID);
    private static final String CHUNK_FILE_NAME = "chunks.txt";
    private static final String TEMP_CHUNK_FILE_NAME = "chunks.txt.tmp";

    public static void init() {
        // Create dir and file if not exists
        File dir = getBaseDir();
        dir.mkdirs();

        File chunkFile = getChunkFile();

        try {
            chunkFile.createNewFile();
        } catch (Exception e) {
            System.out.print("Error while creating chunk file: ");
            System.out.println(e);
        }
    }

    private static File getBaseDir() {
        return ServerLifecycleHooks.getCurrentServer().func_240776_a_( FOLDER ).toFile();
    }

    private static File getChunkFile() {
        return new File(getBaseDir(), CHUNK_FILE_NAME);
    }

    private static File getTempChunkFile() {
        return new File(getBaseDir(), TEMP_CHUNK_FILE_NAME);
    }

    public static void addChunk(int chunkX, int chunkY) {
        File chunkFile = getChunkFile();
        String chunkLine = String.format("%d,%d", chunkX, chunkY);

        try {
            BufferedWriter chunkWriter = new BufferedWriter(new FileWriter(chunkFile));
            chunkWriter.append(chunkLine);
            chunkWriter.close();
        } catch (Exception e) {
            System.out.print("Error while saving chunk file: ");
            System.out.println(e);
        }
    }

    public static void removeChunk(int chunkX, int chunkZ) {
        File tempChunkFile = getTempChunkFile();
        
        try {
            tempChunkFile.createNewFile();
        } catch (Exception e) {
            System.out.print("Error while creating chunk file: ");
            System.out.println(e);
        }

        File chunkFile = getChunkFile();
        String chunkLine = String.format("%d,%d", chunkX, chunkZ);

        try {
            BufferedReader chunkReader = new BufferedReader(new FileReader(chunkFile));
            BufferedWriter tempChunkWriter = new BufferedWriter(new FileWriter(tempChunkFile));

            String line;

            while ((line = chunkReader.readLine()) != null ){
                if (chunkLine == line) continue;
                tempChunkWriter.append(line);
            }

            chunkReader.close();
            tempChunkWriter.close();
        } catch (Exception e) {
            System.out.print("Error while saving chunk file: ");
            System.out.println(e);
        }

        chunkFile.delete();
        tempChunkFile.renameTo(chunkFile);
    }

    public static List<ChunkCoordinates> getChunks() {
        File chunkFile = getChunkFile();
        List<ChunkCoordinates> coordinatesList = new ArrayList<>();

        try {
            BufferedReader chunkReader = new BufferedReader(new FileReader(chunkFile));
            String line;

            while ((line = chunkReader.readLine()) != null ){
                String[] coordinatesStr = line.split(",");

                int chunkX = Integer.parseInt(coordinatesStr[0]);
                int chunkZ = Integer.parseInt(coordinatesStr[1]);

                ChunkCoordinates coordinates = new ChunkCoordinates(chunkX, chunkZ);
                coordinatesList.add(coordinates);
            }

            chunkReader.close();
        } catch (Exception e) {
            System.out.print("Error while saving chunk file: ");
            System.out.println(e);
        }

        return coordinatesList;
    }
}
