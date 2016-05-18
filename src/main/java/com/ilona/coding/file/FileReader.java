package com.ilona.coding.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {
    public static byte[] getBytesOfFile(String pathToFile) throws IOException {
        final File inputFile = new File(pathToFile);
        if (!inputFile.exists()) {
            System.out.println("Cannot read input file: " + inputFile);
            return null;
        }
        final byte[] allBytes = new byte[(int) (inputFile.length())];
        InputStream in = new FileInputStream(inputFile);
        int bytesRead = in.read(allBytes);
        if (bytesRead != inputFile.length()) {
            System.out.println("Cannot read input file: " + inputFile);
            return null;
        }
        return allBytes;
    }
}
