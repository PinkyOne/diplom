/**
 * Command-line program encodes one file using Reed-Solomon 4+2.
 * <p>
 * Copyright 2015, Backblaze, Inc.
 */
package com.ilona.coding;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Command-line program encodes one file using Reed-Solomon 4+2.
 * <p>
 * The one argument should be a file name, say "foo.txt". This program will
 * create six files in the same directory, breaking the input file into four
 * data shards, and two parity shards. The output files are called "foo.txt.0",
 * "foo.txt.1", ..., and "foo.txt.5". Numbers 4 and 5 are the parity shards.
 * <p>
 * The data stored is the file size (four byte int), followed by the contents of
 * the file, and then padded to a multiple of four bytes with zeros. The padding
 * is because all four data shards must be the same size.
 */
public class SampleEncoder2 {

    public static int DATA_SHARDS = 4;
    public static int PARITY_SHARDS = 8;
    public static int TOTAL_SHARDS = DATA_SHARDS + PARITY_SHARDS;

    public static void setShards(int data, int parity) {
        DATA_SHARDS = data;
        PARITY_SHARDS = parity;
        TOTAL_SHARDS = DATA_SHARDS + PARITY_SHARDS;
    }
    public static final int BYTES_IN_INT = 4;

    public static void encode(String pathToFile) throws IOException {

        final File inputFile = new File(pathToFile);
        if (!inputFile.exists()) {
            System.out.println("Cannot read input file: " + inputFile);
            return;
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(inputFile);
        } catch (IOException e) {
        }
        // Get the size of the input file.  (Files bigger that
        // Integer.MAX_VALUE will fail here!)
        final int fileSize = img.getWidth() * img.getHeight() * 3;

        // Figure out how big each shard will be.  The total size stored
        // will be the file size (8 bytes) plus the file.
        final int storedSize = fileSize + BYTES_IN_INT;
        final int shardSize = (storedSize + DATA_SHARDS - 1) / DATA_SHARDS;

        // Create a buffer holding the file size, followed by
        // the contents of the file.
        final int bufferSize = shardSize * DATA_SHARDS;
        final byte[] allBytes = new byte[bufferSize];
        ByteBuffer.wrap(allBytes).putInt(fileSize);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int color = img.getRGB(x, y);
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                allBytes[y * img.getWidth() + x * 3] = (byte) red;
                allBytes[y * img.getWidth() + x * 3 + 1] = (byte) green;
                allBytes[y * img.getWidth() + x * 3 + 2] = (byte) blue;
            }
        }
        img = null;
        long before = System.currentTimeMillis();
        for (int index = 0; index < 1; index++) {
            // Make the buffers to hold the shards.
            byte[][] shards = new byte[TOTAL_SHARDS][shardSize];

            // Fill in the data shards
            for (int i = 0; i < DATA_SHARDS; i++) {
                System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
            }

            // Use Reed-Solomon to calculate the parity.
//        ReedSolomon reedSolomon = ReedSolomon.create(DATA_SHARDS, PARITY_SHARDS);
            ReedSolomon reedSolomon = new ReedSolomon(DATA_SHARDS, PARITY_SHARDS, new OutputInputByteTableCodingLoop());
            reedSolomon.encodeParity(shards, 0, shardSize);

            // Write out the resulting files.
            for (int i = 0; i < TOTAL_SHARDS; i++) {
                File outputFile = new File(
                        inputFile.getParentFile(),
                        inputFile.getName() + "." + i);
                OutputStream out = new FileOutputStream(outputFile);
                out.write(shards[i]);
                out.close();
                //System.out.println("wrote " + outputFile);
            }
        }
        long after = System.currentTimeMillis();
        System.out.println("file coding time " + String.valueOf((after - before) / 1000) + "s"
                + String.valueOf((after - before) % 1000) + "ms");

    }
}