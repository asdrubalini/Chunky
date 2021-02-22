package com.unixlike.chunky.serialization;

public class ChunkCoordinates {
    public final int X;
    public final int Z;

    public ChunkCoordinates(int X, int Z) {
        this.X = X;
        this.Z = Z;
    }

    @Override
    public boolean equals(Object obj) {
        ChunkCoordinates that = (ChunkCoordinates) obj;

        boolean result = ((this.X == that.X) && (this.Z == that.Z));
        System.out.print(String.format("Checking x=%s, z=%s with x=%s, z=%s, %s", this.X, this.Z, that.X, that.Z, result));

        return result;
    }

    @Override
    public String toString() {
        return String.format("X: %d, Z: %d", this.X, this.Z);
    }

    public void print() {
        System.out.println(this.toString());
    }
}
