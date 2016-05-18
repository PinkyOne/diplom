package com.ilona.coding.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        }
        int width = img.getWidth();
        int height = img.getHeight();
        final byte[] allBytes = new byte[(int) (width * height * 3)];
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
                allBytes[i * width * 3 + j] = row[j];
            }
        }
        return allBytes;
    }
}
