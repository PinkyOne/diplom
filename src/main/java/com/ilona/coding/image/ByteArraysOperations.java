package com.ilona.coding.image;

public class ByteArraysOperations {
    public static byte[][] divide(byte[] input, int chunkSize) {
        int chunkCount = input.length / chunkSize + (input.length % chunkSize != 0 ? 1 : 0);
        byte[][] output = new byte[chunkCount][chunkSize];
        for (int i = 0; i < chunkCount; i++) {
            for (int j = 0; j < chunkSize; j++) {
                if (i * chunkSize + j >= input.length) break;
                output[i][j] = input[i * chunkSize + j];
            }
        }
        return output;
    }

    public static byte[] concat(byte[][] input, int chunkSize) {
        byte[] output = new byte[chunkSize * input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < chunkSize; j++) {
                if (i * chunkSize + j >= input.length) break;
                output[i * chunkSize + j] = input[i][j];
            }
        }
        return output;
    }
}
