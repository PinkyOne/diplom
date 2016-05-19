/**
 * Command-line program that decodes a file using Reed-Solomon 4+2.
 * <p>
 * Copyright 2015, Backblaze, Inc.  All rights reserved.
 */

package com.ilona.coding;

import com.ilona.Pair;
import com.ilona.coding.image.ImageParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

/**
 * Command-line program that decodes a file using Reed-Solomon 4+2.
 * <p>
 * The file name given should be the name of the file to decode, say
 * "foo.txt".  This program will expected to find "foo.txt.0" through
 * "foo.txt.5", with at most two missing.  It will then write
 * "foo.txt.decoded".
 */
public class SampleDecoder {

    public static int DATA_SHARDS = 128;
    public static int PARITY_SHARDS = 127;
    public static int TOTAL_SHARDS = DATA_SHARDS + PARITY_SHARDS;

    public static final int BYTES_IN_INT = 4;

    public static void main(String[] arguments) throws IOException {
        decode("G:/sample.bmp");
    }

    public static byte[] decode(byte[] input, boolean[] bytePresent) {
        byte[] output = new byte[input.length];
        // Use Reed-Solomon to fill in the missing shards
        ReedSolomon reedSolomon = ReedSolomon.create(DATA_SHARDS, PARITY_SHARDS);
        byte[][] inputToBidim = new byte[input.length][1];
        for (int i = 0; i < input.length; i++) {
            inputToBidim[i][0] = input[i];
        }
        for (int i = 0; i < bytePresent.length; i++) {
            bytePresent[i] = !bytePresent[i];
        }
        reedSolomon.decodeMissing(inputToBidim, bytePresent, 0, 1);
        for (int i = 0; i < input.length; i++) {
            output[i] = inputToBidim[i][0];
        }
        return output;
    }

    public static byte[][] decode(Pair pair) {
        byte[][] output = new byte[pair.getKey().length][TOTAL_SHARDS];
        for (int i = 0; i < pair.getKey().length; i++) {
            output[i] = decode(pair.getKey()[i], pair.getValue()[i]);
        }
        return output;
    }

    private static byte[][] vectorsToShards(byte[][] vectors) {
        byte[][] shards = new byte[TOTAL_SHARDS][vectors.length];
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                shards[j][i] = vectors[i][j];
            }
        }
        return shards;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void decode(String path) throws IOException {
        byte[][] shards;
        final File originalFile = new File(path.replace(".encoded", ""));
        if (!originalFile.exists()) {
            System.out.println("Cannot read input file: " + originalFile);
          //  return;
        }
        if (path.contains(".auto") || path.contains(".manually")) {
            Pair pair = com.ilona.coding.file.FileReader.readVectors(path);
            byte[][] vectors = decode(pair);
            shards = vectorsToShards(vectors);
        } else {


            // Read in any of the shards that are present.
            // (There should be checking here to make sure the input
            // shards are the same size, but there isn't.)
            shards = new byte[TOTAL_SHARDS][];
            final boolean[] shardPresent = new boolean[TOTAL_SHARDS];
            int shardSize = 0;
            int shardCount = 0;
            for (int i = 0; i < TOTAL_SHARDS; i++) {
                File shardFile = new File(
                        originalFile.getParentFile(),
                        originalFile.getName() + "." + i);
                if (shardFile.exists()) {
                    shardSize = (int) shardFile.length();
                    shards[i] = new byte[shardSize];
                    shardPresent[i] = true;
                    shardCount += 1;
                    InputStream in = new FileInputStream(shardFile);
                    in.read(shards[i], 0, shardSize);
                    in.close();
                    System.out.println("Read " + shardFile);
                }
            }

            // We need at least DATA_SHARDS to be able to reconstruct the file.
            if (shardCount < DATA_SHARDS) {
                System.out.println("Not enough shards present");
                return;
            }

            // Make empty buffers for the missing shards.
            for (int i = 0; i < TOTAL_SHARDS; i++) {
                if (!shardPresent[i]) {
                    shards[i] = new byte[shardSize];
                }
            }

            // Use Reed-Solomon to fill in the missing shards
            ReedSolomon reedSolomon = ReedSolomon.create(DATA_SHARDS, PARITY_SHARDS);
            reedSolomon.decodeMissing(shards, shardPresent, 0, shardSize);
        }
        // Combine the data shards into one buffer for convenience.
        // (This is not efficient, but it is convenient.)
        int shardSize = shards[0].length;
        byte[] allBytes = new byte[shardSize * DATA_SHARDS];
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(shards[i], 0, allBytes, shardSize * i, shardSize);
        }

        // Extract the file length
        int fileSize = ByteBuffer.wrap(allBytes).getInt();
        if (originalFile.getName().contains("bmp")) {
            try {
                BufferedImage bi = ImageParser.parse(allBytes, fileSize);
                File outputfile = new File(originalFile.getParentFile(), originalFile.getName() + ".decoded");
                ImageIO.write(bi, "bmp", outputfile);
                System.out.println("Wrote2 " + outputfile);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Write the decoded file
        File decodedFile = new File(originalFile.getParentFile(), originalFile.getName() + ".decoded");
        OutputStream out = new FileOutputStream(decodedFile);
        out.write(allBytes, BYTES_IN_INT, fileSize);
        out.flush();
        out.close();
        System.out.println("Wrote1 " + decodedFile);

    }
}
