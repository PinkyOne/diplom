package com.ilona.coding.file;

import com.ilona.Pair;
import com.ilona.views.MakeErrors;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class ErrorMaker {
    public static Pair makeErrors(byte[][] vectors,int count, String type) {
        Random random = new Random();
        int errorCount;
        Queue<Integer> queue;
        int ec = count;

        boolean needSpread = type.equals("Одиночные");
        do {
            queue = new PriorityQueue<>();
            errorCount = count;
            while (errorCount > 0) {
                int num = 0;
                int counter = 0;
                while (num <= 0 && counter < 10000000) {
                    counter++;
                    num = random.nextInt(errorCount >= vectors[0].length ? vectors[0].length : errorCount + 1);
                }
                queue.add(num);
                errorCount -= num;
            }
        }
        while (!needSpread
                ? queue.size() > vectors.length
                : queue.size()
                != ((ec / vectors.length + 1 > vectors[0].length)
                ? vectors.length
                : ec / vectors.length + 1));

        Pair pair = null;
        byte[][] output = new byte[vectors.length][];
        for (int i = 0; i < vectors.length; i++) {
            output[i] = new byte[vectors[i].length];
            System.arraycopy(vectors[i], 0, output[i], 0, vectors[i].length);
        }
        boolean[][] errorPlaces = new boolean[vectors.length][];
        String typeOfErrors = type;
        switch (typeOfErrors) {
            case "Все равно": {
                while (!queue.isEmpty()) {
                    int vectorPosition = random.nextInt(vectors.length);
                    while (errorPlaces[vectorPosition] != null) {
                        vectorPosition = random.nextInt(vectors.length);
                    }
                    errorPlaces[vectorPosition] = new boolean[vectors[vectorPosition].length];

                    int errC = queue.poll();
                    for (int i = 0; i < errC; i++) {
                        int position = random.nextInt(vectors[vectorPosition].length);
                        while (errorPlaces[vectorPosition][position]) {
                            position = random.nextInt(vectors[vectorPosition].length);
                        }
                        errorPlaces[vectorPosition][position] = true;
                        output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                    }
                }
            }
            break;
            case "Групповые": {
                while (!queue.isEmpty()) {
                    int vectorPosition = random.nextInt(vectors.length);
                    while (errorPlaces[vectorPosition] != null) {
                        vectorPosition = random.nextInt(vectors.length);
                    }
                    errorPlaces[vectorPosition] = new boolean[vectors[vectorPosition].length];

                    int errC = queue.poll();
                    int startPosition = 0;
                    try {
                        startPosition = random.nextInt(vectors[vectorPosition].length - errC);
                    } catch (IllegalArgumentException e) {

                    }

                    for (int i = startPosition; i < startPosition + errC; i++) {
                        errorPlaces[vectorPosition][i] = true;
                        output[vectorPosition][i] = (byte) (random.nextInt() & 0xFF);
                    }
                }
            }
            break;
            case "Одиночные":
            default: {
                while (!queue.isEmpty()) {
                    int vectorPosition = random.nextInt(vectors.length);
                    while (errorPlaces[vectorPosition] != null) {
                        vectorPosition = random.nextInt(vectors.length);
                    }
                    errorPlaces[vectorPosition] = new boolean[vectors[vectorPosition].length];

                    int errC = queue.poll();
                    if (errC < vectors[vectorPosition].length / 2 - 1) {
                        for (int i = 0; i < errC; i++) {
                            int position = random.nextInt(vectors[vectorPosition].length);
                            while (errorPlaces[vectorPosition][position]
                                    || (errorPlaces[vectorPosition][position - (position != 0 ? 1 : 0)])
                                    || errorPlaces[vectorPosition][position +
                                    (position != vectors[vectorPosition].length - 1 ? 1 : 0)]) {
                                position = random.nextInt(vectors[vectorPosition].length);
                            }
                            errorPlaces[vectorPosition][position] = true;
                            output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                        }
                    } else {
                        int i;
                        int position = 0;
                        for (i = 0; i < errC && i < errorPlaces[vectorPosition].length / 2; i++) {
                            errorPlaces[vectorPosition][position] = true;
                            output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                            position += 2;
                        }
                        for (; i < errC; i++) {
                            position = random.nextInt(vectors[vectorPosition].length);
                            while (errorPlaces[vectorPosition][position]) {
                                position = random.nextInt(vectors[vectorPosition].length);
                            }
                            errorPlaces[vectorPosition][position] = true;
                            output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                        }
                    }
                }
            }
            break;
        }
        for (int i = 0; i < errorPlaces.length; i++)
            if (errorPlaces[i] == null) errorPlaces[i] = new boolean[255];
        pair = new Pair(output, errorPlaces);
        return pair;
    }
    public static Pair makeErrors(byte[][] vectors) {
        Random random = new Random();
        int errorCount;
        Queue<Integer> queue;
        int ec = MakeErrors.countOfErrors;

        boolean needSpread = MakeErrors.typeOfErrors.equals("Одиночные");
        do {
            queue = new PriorityQueue<>();
            errorCount = MakeErrors.countOfErrors;
            while (errorCount > 0) {
                int num = 0;
                int counter = 0;
                while (num <= 0 && counter < 10000000) {
                    counter++;
                    num = random.nextInt(errorCount >= vectors[0].length ? vectors[0].length : errorCount + 1);
                }
                queue.add(num);
                errorCount -= num;
            }
        }
        while (!needSpread
                ? queue.size() > vectors.length
                : queue.size()
                != ((ec / vectors.length + 1 > vectors[0].length)
                ? vectors.length
                : ec / vectors.length + 1));

        Pair pair = null;
        byte[][] output = new byte[vectors.length][];
        for (int i = 0; i < vectors.length; i++) {
            output[i] = new byte[vectors[i].length];
            System.arraycopy(vectors[i], 0, output[i], 0, vectors[i].length);
        }
        boolean[][] errorPlaces = new boolean[vectors.length][];
        String typeOfErrors = MakeErrors.typeOfErrors;
        switch (typeOfErrors) {
            case "Все равно": {
                while (!queue.isEmpty()) {
                    int vectorPosition = random.nextInt(vectors.length);
                    while (errorPlaces[vectorPosition] != null) {
                        vectorPosition = random.nextInt(vectors.length);
                    }
                    errorPlaces[vectorPosition] = new boolean[vectors[vectorPosition].length];

                    int errC = queue.poll();
                    for (int i = 0; i < errC; i++) {
                        int position = random.nextInt(vectors[vectorPosition].length);
                        while (errorPlaces[vectorPosition][position]) {
                            position = random.nextInt(vectors[vectorPosition].length);
                        }
                        errorPlaces[vectorPosition][position] = true;
                        output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                    }
                }
            }
            break;
            case "Групповые": {
                while (!queue.isEmpty()) {
                    int vectorPosition = random.nextInt(vectors.length);
                    while (errorPlaces[vectorPosition] != null) {
                        vectorPosition = random.nextInt(vectors.length);
                    }
                    errorPlaces[vectorPosition] = new boolean[vectors[vectorPosition].length];

                    int errC = queue.poll();
                    int startPosition = 0;
                    try {
                        startPosition = random.nextInt(vectors[vectorPosition].length - errC);
                    } catch (IllegalArgumentException e) {

                    }

                    for (int i = startPosition; i < startPosition + errC; i++) {
                        errorPlaces[vectorPosition][i] = true;
                        output[vectorPosition][i] = (byte) (random.nextInt() & 0xFF);
                    }
                }
            }
            break;
            case "Одиночные":
            default: {
                while (!queue.isEmpty()) {
                    int vectorPosition = random.nextInt(vectors.length);
                    while (errorPlaces[vectorPosition] != null) {
                        vectorPosition = random.nextInt(vectors.length);
                    }
                    errorPlaces[vectorPosition] = new boolean[vectors[vectorPosition].length];

                    int errC = queue.poll();
                    if (errC < vectors[vectorPosition].length / 2 - 1) {
                        for (int i = 0; i < errC; i++) {
                            int position = random.nextInt(vectors[vectorPosition].length);
                            while (errorPlaces[vectorPosition][position]
                                    || (errorPlaces[vectorPosition][position - (position != 0 ? 1 : 0)])
                                    || errorPlaces[vectorPosition][position +
                                    (position != vectors[vectorPosition].length - 1 ? 1 : 0)]) {
                                position = random.nextInt(vectors[vectorPosition].length);
                            }
                            errorPlaces[vectorPosition][position] = true;
                            output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                        }
                    } else {
                        int i;
                        int position = 0;
                        for (i = 0; i < errC && i < errorPlaces[vectorPosition].length / 2; i++) {
                            errorPlaces[vectorPosition][position] = true;
                            output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                            position += 2;
                        }
                        for (; i < errC; i++) {
                            position = random.nextInt(vectors[vectorPosition].length);
                            while (errorPlaces[vectorPosition][position]) {
                                position = random.nextInt(vectors[vectorPosition].length);
                            }
                            errorPlaces[vectorPosition][position] = true;
                            output[vectorPosition][position] = (byte) (random.nextInt() & 0xFF);
                        }
                    }
                }
            }
            break;
        }
        for (int i = 0; i < errorPlaces.length; i++)
            if (errorPlaces[i] == null) errorPlaces[i] = new boolean[255];
        pair = new Pair(output, errorPlaces);
        return pair;
    }
}