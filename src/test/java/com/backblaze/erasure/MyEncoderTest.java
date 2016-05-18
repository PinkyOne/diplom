package com.backblaze.erasure;

import com.ilona.coding.encoder.Encoder;
import com.ilona.coding.file.FileReader;
import com.ilona.coding.image.DivideBytes;
import com.ilona.coding.image.ImageParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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
    public void parseImageToRGBByteArray() {
        byte[] inputFileBytes = ImageParser.parse("G:/red.bmp");
        for (int i = 0; i < inputFileBytes.length / 3; i++) {
            int i1 = i * 3;
            Assert.assertEquals("index = " + i1, -1, inputFileBytes[i1]);
            Assert.assertEquals("index = " + (i1 + 1), 0, inputFileBytes[i1 + 1]);
            Assert.assertEquals("index = " + (i1 + 2), 0, inputFileBytes[i1 + 2]);
        }
    }

    @Test
    public void testDivide() {
        byte[] array = new byte[]
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        0, 1, 2, 3, 4};
        byte[][] result = DivideBytes.divide(array, 10);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result[0]);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result[1]);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result[2]);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 0, 0, 0, 0, 0}, result[3]);

    }
}
