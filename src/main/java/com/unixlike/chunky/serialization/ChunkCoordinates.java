package com.unixlike.chunky.serialization;

public class ChunkCoordinates {
    public final int X;
    public final int Z;

    public ChunkCoordinates(int X, int Z) {
        this.X = X;
        this.Z = Z;
    }

    public boolean equals(ChunkCoordinates that) {
        return ((this.X == that.X) && (this.Z == that.Z));
    }

    public void print() {
        System.out.format("X: %d, Z: %d\n", this.X, this.Z);
    }
}
