package com.ilona;

import java.io.Serializable;
import java.util.List;

public class Pair implements Serializable {
    byte[][] key;
    boolean[][] value;

    public Pair(List<byte[]> bytes, List<boolean[]> errorPlaces) {
        this.key = new byte[bytes.size()][255];
        this.value = new boolean[bytes.size()][255];
        for (int i = 0; i < bytes.size(); i++) {
            for (int j = 0; j < bytes.get(i).length; j++) {
                key[i][j] = bytes.get(i)[j];
                value[i][j] = errorPlaces.get(i)[j];
            }
        }
    }

    public byte[][] getKey() {
        return key;
    }

    public void setKey(byte[][] key) {
        this.key = key;
    }

    public boolean[][] getValue() {
        return value;
    }

    public void setValue(boolean[][] value) {
        this.value = value;
    }

    public Pair(byte[][] key, boolean[][] value) {
        this.key = key;
        this.value = value;
    }
}
