package com.ilona.coding;

public class SimpleLoop extends CodingLoopBase {
    @Override
    public void codeSomeShards(byte[][] matrixRows,
                               byte[][] inputs, int inputCount,
                               byte[][] outputs, int outputCount,
                               int offset, int byteCount) {
        final byte[][] table = Galois.MULTIPLICATION_TABLE;

        for (int iOutput = 0; iOutput < outputCount; iOutput++) {
            final byte[] outputShard = outputs[iOutput];
            final byte[] matrixRow = matrixRows[iOutput];
            for (int iInput = 0; iInput < inputCount; iInput++) {
                final byte[] inputShard = inputs[iInput];
                final byte[] multTableRow = table[matrixRow[iInput] & 0xFF];
                for (int iByte = offset; iByte < offset + byteCount; iByte++) {
                    outputShard[iByte] ^= multTableRow[inputShard[iByte] & 0xFF];
                }
            }
        }
    }
}
