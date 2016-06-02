package com.backblaze.erasure;

import com.ilona.Pair;
import com.ilona.coding.OriginalEncoder;
import com.ilona.coding.SampleDecoder;
import com.ilona.coding.SampleEncoder;
import com.ilona.coding.encoder.Encoder;
import com.ilona.coding.file.ErrorMaker;
import com.ilona.coding.file.FileReader;
import com.ilona.coding.image.ByteArraysOperations;
import com.ilona.coding.image.ImageParser;
import com.ilona.views.MakeErrors;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MyEncoderTest {
    @Test
    public void encoderTest() {
        byte[] input = new byte[2];
        input[0] = 3;
        input[1] = 1;
        byte[][] inputs = new byte[2][];
        inputs[0] = input;
        inputs[1] = input;
        byte[][] outputs = Encoder.encode(inputs);
        Assert.assertArrayEquals(outputs[0], outputs[1]);
    }

    @Test
    public void fileEncodeTest() throws IOException {
        byte[] input = FileReader.getBytesOfFile("G:/red.bmp");
        byte[][] inputs = new byte[2][];
        inputs[0] = input;
        inputs[1] = input;
        byte[][] outputs = Encoder.encode(inputs);
        Assert.assertArrayEquals(outputs[0], outputs[1]);
    }

    @Test
    public void newMeta() throws IOException {
        File bmpDir = new File("G:/bmps");
        for (int i = 0; i < 100; i++) {
            OriginalEncoder.main("G:/red.bmp");
        }
        for (File bmp : bmpDir.listFiles()) {
            if (bmp.getName().split("\\.").length > 2)
                continue;


            long before = System.nanoTime();
            OriginalEncoder.main(bmp.getAbsolutePath());
            long after = System.nanoTime();

            System.out.print(bmp.getName().replace(".bmp", "") + "x"
                    + bmp.getName().replace(".bmp", "") + "file size: " + bmp.length() / 1024 + "Kb | simple coding time: "
                    + (after - before));

            before = System.nanoTime();
            SampleEncoder.encode(bmp.getAbsolutePath());
            after = System.nanoTime();

            System.err.flush();
            // System.out.println(" new alg coding time: " + (after - before));
        }
    }

    String pathToFile = "G:/rest.jpg";

    @Test
    public void restErasures() throws IOException {
        String type = "Групповые";
        int count, t = 30, k = 195;
        SampleEncoder.refreshShardCount(k, t);
        SampleEncoder.encode(pathToFile);
        count = 2 * t;
        String orig = "";
        orig = new String(Files.readAllBytes(Paths.get(pathToFile)));
        int diff;
        String decoded = "";

//        FileReader.saveVectors(
//                ErrorMaker.makeErrors(FileReader.getVectors(pathToFile + ".encoded"), 100, type),
//                pathToFile + ".encoded.auto");
//        SampleDecoder.decode(pathToFile + ".encoded.auto", type + "_count_" + 100 + ".txt");
//        decoded = new String(Files.readAllBytes(Paths.get("G:/" + type + "_count_" + 100 + ".txt")));
//        diff = StringUtils.getLevenshteinDistance(orig, decoded);
//        System.out.println("diff:"+type+"error count: "+100+"Levenshtein distance:"+diff);
//
//        FileReader.saveVectors(
//                ErrorMaker.makeErrors(FileReader.getVectors(pathToFile + ".encoded"), 1000, type),
//                pathToFile + ".encoded.auto");
//        SampleDecoder.decode(pathToFile + ".encoded.auto", type + "_count_" + 1000 + ".txt");
//        decoded = new String(Files.readAllBytes(Paths.get("G:/" + type + "_count_" + 1000 + ".txt")));
//        diff = StringUtils.getLevenshteinDistance(orig, decoded);
//        System.out.println("diff:"+type+"error count: "+1000+"Levenshtein distance:"+diff);
//
//        FileReader.saveVectors(
//                ErrorMaker.makeErrors(FileReader.getVectors(pathToFile + ".encoded"), 2000, type),
//                pathToFile + ".encoded.auto");
//        SampleDecoder.decode(pathToFile + ".encoded.auto", type + "_count_" + 2000 + ".txt");
//        decoded = new String(Files.readAllBytes(Paths.get("G:/" + type + "_count_" + 2000 + ".txt")));
//        diff = StringUtils.getLevenshteinDistance(orig, decoded);
//        System.out.println("diff:"+type+"error count: "+2000+"Levenshtein distance:"+diff);
//
//        FileReader.saveVectors(
//                ErrorMaker.makeErrors(FileReader.getVectors(pathToFile + ".encoded"), 100, type),
//                pathToFile + ".encoded.auto");
//        SampleDecoder.decode(pathToFile + ".encoded.auto", type + "_count_" + 100 + ".txt");
//        decoded = new String(Files.readAllBytes(Paths.get("G:/" + type + "_count_" + 100 + ".txt")));
//        diff = StringUtils.getLevenshteinDistance(orig, decoded);
//        System.out.println("diff:"+type+"error count: "+100+"Levenshtein distance:"+diff);
//
//        FileReader.saveVectors(
//                ErrorMaker.makeErrors(FileReader.getVectors(pathToFile + ".encoded"), 1000, type),
//                pathToFile + ".encoded.auto");
//        SampleDecoder.decode(pathToFile + ".encoded.auto", type + "_count_" + 1000 + ".txt");
//        decoded = new String(Files.readAllBytes(Paths.get("G:/" + type + "_count_" + 1000 + ".txt")));
//        diff = StringUtils.getLevenshteinDistance(orig, decoded);
//        System.out.println("diff:"+type+"error count: "+1000+"Levenshtein distance:"+diff);
//
//        FileReader.saveVectors(
//                ErrorMaker.makeErrors(FileReader.getVectors(pathToFile + ".encoded"), 2000, type),
//                pathToFile + ".encoded.auto");
//        SampleDecoder.decode(pathToFile + ".encoded.auto", type + "_count_" + 2000 + ".txt");
//        decoded = new String(Files.readAllBytes(Paths.get("G:/" + type + "_count_" + 2000 + ".txt")));
//        diff = StringUtils.getLevenshteinDistance(orig, decoded);
//        System.out.println("diff:"+type+"error count: "+2000+"Levenshtein distance:"+diff);
//        for (int i = 3*t; i < 8*t; i+=t) {
//            FileReader.saveVectors(
//                    ErrorMaker.makeErrors(FileReader.getVectors("G:/rest.jpg.encoded"), i, type),
//                    pathToFile + ".encoded.auto");
//            SampleDecoder.decode(pathToFile + ".encoded.auto",type+"_count_"+i+".jpg");
//        }
        type = "Все Равно";

//        for (int i = 3*t; i < 8*t; i+=t) {
            FileReader.saveVectors(
                    ErrorMaker.makeErrors(FileReader.getVectors("G:/rest.jpg.encoded"), 120, type),
                    pathToFile + ".encoded.auto");
            SampleDecoder.decode(pathToFile + ".encoded.auto",type+"_count_"+120+".jpg");
//        }
    }

    @Test
    public void parseImageToRGBByteArray() {

        byte[] inputFileBytes = ImageParser.parse("G:/red.bmp");
        for (int i = 4; i < inputFileBytes.length; i++) {
            Assert.assertEquals("index = " + i, (i - 4) % 3 == 0 ? -1 : 0, inputFileBytes[i]);
        }
    }

    @Test
    public void restErrorMake() {
        byte[] array = new byte[]
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4};
        byte[][] result = ByteArraysOperations.divide(array, 10);
        MakeErrors.countOfErrors = 15;
        MakeErrors.typeOfErrors = "Все равно";
        Pair pair = null;

        for (int i = 0; i < 10000; i++) {
            pair = ErrorMaker.makeErrors(result);
        }
        printPair(pair);
    }

    @Test
    public void restErrorMake1() {
        byte[] array = new byte[]
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4};
        byte[][] result = ByteArraysOperations.divide(array, 10);
        MakeErrors.countOfErrors = 15;
        MakeErrors.typeOfErrors = "Групповые";
        Pair pair = null;
        for (int i = 0; i < 10000; i++) {
            pair = ErrorMaker.makeErrors(result);
        }
        printPair(pair);
    }

    @Test
    public void restErrorMake2() {
        Pair pair = null;
        for (int i = 0; i < 10000; i++) {


            byte[] array = new byte[]
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                            0, 1, 2, 3, 4};
            byte[][] result = ByteArraysOperations.divide(array, 10);
            MakeErrors.countOfErrors = 7;
            MakeErrors.typeOfErrors = "Одиночные";
            pair = ErrorMaker.makeErrors(result);

        }
        printPair(pair);
    }

    private void printPair(Pair pair) {
        for (byte[] arr : pair.getKey()) {
            System.out.println(Arrays.toString(arr));
        }
        for (boolean[] arr : pair.getValue()) {
            int counter = 0;
            if (arr != null) for (boolean b :
                    arr) {
                if (b) counter++;
            }
            System.out.println(Arrays.toString(arr) + " " + counter);
        }
    }

    @Test
    public void restDivide() {
        byte[] array = new byte[]
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4};
        byte[][] result = ByteArraysOperations.divide(array, 10);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result[0]);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result[1]);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result[2]);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 0, 0, 0, 0, 0}, result[3]);

    }
}
