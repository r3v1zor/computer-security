package com.task2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static final int[] mask = {1, 2, 4, 8, 16, 32, 64, 128};

    public static void main(String[] args) {
        try {
            List<String> text = Files.readAllLines(Path.of("/home/r3v1zor/Projects/IdeaProjects/computer-security/src/new/test.txt"));
            final List<String> encrypt = encrypt("hello", text);
            final byte[] decrypt = decrypt(encrypt);
            System.out.println(new String(decrypt, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static boolean isBit(byte b, int position) {
        return (b & mask[position]) != 0;
    }

    private static List<String> encrypt(String phrase, List<String> text) {
        final byte[] phraseBytes = phrase.getBytes();
        List<String> result = new ArrayList<>();

        for (int i = 0; i < text.size(); i++) {
            int position = i % 8;
            int byteNumber = i / 8;

            String str = i < phraseBytes.length * 8 && isBit(phraseBytes[byteNumber], position) ? text.get(i) + " " : text.get(i);
            result.add(str);
        }

        return result;
    }

    private static byte[] decrypt(List<String> text) {
        int number = 0;
        byte[] result = new byte[text.size() / 8];
        for (int i = 0; i < text.size() ; i++) {
            String line = text.get(i);

            if (line.endsWith(" ")) {
                number += mask[i % 8];
            }

            if (i % 8 == 7) {
                result[i / 8] = (byte) number;
                number = 0;
            }
        }

        return result;
    }
}
