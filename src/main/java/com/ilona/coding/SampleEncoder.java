/**
 * Command-line program encodes one file using Reed-Solomon 4+2.
 * <p>
 * Copyright 2015, Backblaze, Inc.
 */

package com.ilona.coding;

import com.ilona.coding.image.ImageParser;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Command-line program encodes one file using Reed-Solomon 4+2.
 * <p>
 * The one argument should be a file name, say "foo.txt".  This program
 * will create six files in the same directory, breaking the input file
 * into four data shards, and two parity shards.  The output files are
 * called "foo.txt.0", "foo.txt.1", ..., and "foo.txt.5".  Numbers 4
 * and 5 are the parity shards.
 * <p>
 * The data stored is the file size (four byte int), followed by the
 * contents of the file, and then padded to a multiple of four bytes
 * with zeros.  The padding is because all four data shards must be
 * the same size.
 */
public class SampleEncoder {


    public static int DATA_SHARDS = 128;
    public static int PARITY_SHARDS = 127;
    public static int TOTAL_SHARDS = DATA_SHARDS + PARITY_SHARDS;

    public static final int BYTES_IN_INT = 4;
    private static int shardSize;

    public static void main(String[] strings) throws IOException {
        encode("G:/50.bmp");
    }

    public static void encode(String pathToFile) throws IOException {

        final File inputFile = new File(pathToFile);
        if (!inputFile.exists()) {
            System.out.println("Cannot read input file: " + inputFile);
            return;
        }

        // Get the size of the input file.  (Files bigger that
        // Integer.MAX_VALUE will fail here!)
        final int fileSize = (int) inputFile.length();

        // If image extention matches with bmp, ...
        String ext = getExtension(pathToFile);
        final boolean isLossLessImage = ext.equalsIgnoreCase("bmp");
        // Figure out how big each shard will be.  The total size stored
        // will be the file size (8 bytes) plus the file.
        final int storedSize;
        final int shardSize;

        // Create a buffer holding the file size, followed by
        // the contents of the file.
        final int bufferSize;
        final byte[] allBytes;
        if (!isLossLessImage) {

            storedSize = fileSize + BYTES_IN_INT;
            shardSize = (storedSize + DATA_SHARDS - 1) / DATA_SHARDS;
            bufferSize = shardSize * DATA_SHARDS;

            allBytes = new byte[bufferSize];
            ByteBuffer.wrap(allBytes).putInt(fileSize);
            InputStream in = new FileInputStream(inputFile);
            int bytesRead = in.read(allBytes, BYTES_IN_INT, fileSize);
            if (bytesRead != fileSize) {
                throw new IOException("not enough bytes read");
            }
            in.close();
        } else {
            allBytes = ImageParser.parse(pathToFile);
            storedSize = allBytes.length;
            shardSize = (storedSize) / DATA_SHARDS + 1;
        }
        SampleEncoder.shardSize = shardSize;

        // Make the buffers to hold the shards.
        byte[][] shards = new byte[TOTAL_SHARDS][shardSize];

        // Fill in the data shards
        for (int i = 0; i < DATA_SHARDS - 1; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }
        System.arraycopy(allBytes, (DATA_SHARDS - 1) * shardSize, shards[DATA_SHARDS - 1], 0, allBytes.length % shardSize);

        // Use Reed-Solomon to calculate the parity.
        ReedSolomon reedSolomon = new ReedSolomon(DATA_SHARDS, PARITY_SHARDS, new OutputInputByteTableCodingLoop());
        long before = System.nanoTime();

        reedSolomon.encodeParity(shards, 0, shardSize);
        long after = System.nanoTime();

        // Write out the resulting files.
        File encodedFile = new File(inputFile.getParentFile(),
                inputFile.getName() + ".encoded");
        if (encodedFile.exists())
            encodedFile.delete();
        encodedFile.createNewFile();
        for (int i = 0; i < TOTAL_SHARDS; i++) {
            File outputFile = new File(
                    inputFile.getParentFile(),
                    inputFile.getName() + "." + i);

//            OutputStream out = new FileOutputStream(outputFile);
//            out.write(shards[i]);
//            out.close();

            OutputStream out = new FileOutputStream(encodedFile, true);
            out.write(shards[i]);
            out.close();
          //  System.out.println("wrote " + outputFile);
        }

       System.out.println(" new alg coding time: " + String.valueOf((after - before)));

    }

    public static void refreshShardCount(int k, int t) {
        DATA_SHARDS = k;
        PARITY_SHARDS = 2 * t;
        TOTAL_SHARDS = DATA_SHARDS + PARITY_SHARDS;
        SampleDecoder.DATA_SHARDS = k;
        SampleDecoder.PARITY_SHARDS = 2 * t;
        SampleDecoder.TOTAL_SHARDS = DATA_SHARDS + PARITY_SHARDS;
        System.out.println(DATA_SHARDS);
        System.out.println(PARITY_SHARDS);
        System.out.println(TOTAL_SHARDS);
    }

    private static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf('.');
        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    public static int getShardSize() {
        return shardSize;
    }
}
