package com.ilona.coding.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageParser {
    public static final int BYTES_IN_INT = 4;

    public static byte[] parse(String pathToFile) {
        final File inputFile = new File(pathToFile);
        if (!inputFile.exists()) {
            System.out.println("Cannot read input file: " + inputFile);
            return null;
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(inputFile);
        } catch (IOException e) {
            System.out.println("Cannot read input file: " + inputFile);
            return null;
        }
        int width = img.getWidth();
        int height = img.getHeight();
        final byte[] allBytes = new byte[(int) (width * height * 3) + BYTES_IN_INT];
        ByteBuffer.wrap(allBytes).putInt(width);

        final byte[][] imageBytes = new byte[height][width * 3];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = img.getRGB(x, y);
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                imageBytes[y][x * 3] = (byte) red;
                imageBytes[y][x * 3 + 1] = (byte) green;
                imageBytes[y][x * 3 + 2] = (byte) blue;
            }
        }
        for (int i = 0; i < imageBytes.length; i++) {
            byte[] row = imageBytes[i];
            for (int j = 0; j < row.length; j++) {
                allBytes[i * width * 3 + j + BYTES_IN_INT] = row[j];
            }
        }
        return allBytes;
    }

    public static BufferedImage parse(byte[] allBytes, int fileSize) {
        // retrieve image
        int width = fileSize;
        BufferedImage bi = new BufferedImage(width, (allBytes.length - BYTES_IN_INT) / (width * 3), TYPE_INT_RGB);

        int height = bi.getHeight();
        byte[] imageAllBytes = new byte[allBytes.length - BYTES_IN_INT];
        System.arraycopy(allBytes, BYTES_IN_INT, imageAllBytes, 0, allBytes.length - BYTES_IN_INT);

        final byte[][] imageBytes = ByteArraysOperations.divide(imageAllBytes,width*3);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                int color = 0;
                int blue = imageBytes[y][x*3+2];
                int green = imageBytes[y][x*3+1];
                int red = imageBytes[y][x*3];
                color = getIntFromColor(red, green, blue);
                bi.setRGB(x, y, color);
            }
        }
        return bi;
    }

    public static int toInt(byte[] bytes, int offset) {
        int ret = 0;
        for (int i = 0; i < 4 && i + offset < bytes.length; i++) {
            ret <<= 8;
            ret |= (int) bytes[i] & 0xFF;
        }
        return ret;
    }

    public static int getIntFromColor(int Red, int Green, int Blue) {
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
