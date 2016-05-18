package com.ilona.coding.image;

public class DivideBytes {
    public static byte[][] divide(byte[] input, int chunkSize) {
        int chunkCount = input.length / chunkSize + 1;
        byte[][] output = new byte[chunkCount][chunkSize];
        for (int i = 0; i < chunkCount; i++) {
            for (int j = 0; j < chunkSize; j++) {
                if (i * chunkSize + j >= input.length) break;
                output[i][j] = input[i * chunkSize + j];
            }
        }
        return output;
    }
}
