package com.ilona.coding.file;

import com.ilona.Pair;
import com.ilona.coding.SampleEncoder;
import com.ilona.coding.image.ByteArraysOperations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String getEncodedFileAsStringArrayView(String pathToFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[][] vectors = getVectors(pathToFile);
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                if (j == 0) sb.append("[");
                sb.append(Byte.toUnsignedInt(vectors[i][j]));
                if (j == vectors[i].length - 1) sb.append("]\n");
                else sb.append(",");
            }
        }
        return sb.toString();
    }

    public static byte[][] getVectors(String pathToFile) throws IOException {
        File file = new File(pathToFile);
        InputStream in = new FileInputStream(pathToFile);
        long length = file.length();
        byte[] allBytes = new byte[(int) length];
        in.read(allBytes, 0, (int) length);
        int shardSize = SampleEncoder.getShardSize();
        byte[][] shards = ByteArraysOperations.divide(allBytes, shardSize);
        byte[][] vectors = new byte[shards[0].length][shards.length];
        int i = 0;
        int j = 0;

        for (i = 0; i < vectors.length; i++) {
            try {
                for (j = 0; j < vectors[i].length; j++) {
                    vectors[i][j] = shards[j][i];
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("i " + i + " j " + j);
            }
        }


        return vectors;
    }

    public static String getDecodedFileAsString(String decodedFile) {
        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(decodedFile)).forEach(s -> {
                sb.append(s).append("\n");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String transformRGBToFile(String path) {
        return null;
    }

    public static Pair convertStringsToPair(String strings, String pathToFile) throws Exception {
        byte[][] originalVectors = new byte[0][];

        try {
            originalVectors = getVectors(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();

        }
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(strings);
        byte[][] errorVectors;
        List<boolean[]> errorPlaces = new LinkedList<>();
        List<byte[]> bytes = new LinkedList<>();
        while (m.find()) {
            String s = m.group(1);
            String[] numbers = s.split(",");
            if (numbers.length != 255) throw new Exception("Сделайте ошибки, а не измените длину вектора");
            byte[] numbersAsBytes = new byte[numbers.length];
            errorPlaces.add(new boolean[255]);
            for (int i = 0; i < numbers.length; i++) {
                numbersAsBytes[i] = (byte) Integer.parseInt(numbers[i]);
                if (originalVectors[bytes.size()][i] != numbersAsBytes[i])
                    errorPlaces.get(bytes.size())[i] = true;
            }
            bytes.add(numbersAsBytes);
        }
        Pair pair = new Pair(bytes, errorPlaces);

        return pair;
    }

    public static Pair readVectors(String path) {
        Pair pair;

        try {

            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            pair = (Pair) ois.readObject();
            ois.close();

            return pair;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void saveVectors(Pair pair, String path) {
        try {

            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(pair);
            oos.close();
            System.out.println("Done");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
