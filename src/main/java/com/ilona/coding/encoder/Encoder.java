package com.ilona.coding.encoder;

import com.ilona.coding.Galois;

public class Encoder {
    public static byte[] encode(byte[] input) {
        byte[] output = new byte[Galois.FIELD_SIZE - 1];
        byte[] primitive = Galois.getMaxPrimitive();
        for (int i = 0; i < Galois.FIELD_SIZE - 1; i++) {
            byte x = primitive[i];
            for (int j = 0; j < input.length; j++) {
                byte xExpJ = Galois.exp(x, j);
                byte aJMulXExpJ = Galois.multiply(input[j], xExpJ);
                output[i] ^= aJMulXExpJ;
            }
        }
        return output;
    }

    public static byte[][] encode(byte[][] input) {
        byte[][] output = new byte[input.length][Galois.FIELD_SIZE - 1];
        for (int i = 0; i < input.length; i++) {
            output[i] = encode(input[i]);
        }
        return output;
    }
}
