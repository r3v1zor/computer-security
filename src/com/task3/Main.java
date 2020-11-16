package com.task3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int[] mask = {1, 2, 4, 8, 16, 32, 64, 128};

    public static void main(String[] args) {
        try {
            String phrase = "hello";
            String text = Files.readString(Path.of("/home/r3v1zor/Projects/IdeaProjects/computer-security/src/new/test.txt"));

            final String encryptedText = encrypt(text, phrase);
            final byte[] decrypt = decrypt(encryptedText);

            System.out.println(new String(decrypt, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean getBit(byte b, int position) {
        return (b & mask[position]) != 0;
    }

    private static String encrypt(String text, String phrase) {
        final String[] words = text.split(" ");
        byte[] phraseBytes = phrase.getBytes();
        List<String> result = new ArrayList<>();

        int bitsCount = phraseBytes.length * 8;

        for (int i = 0; i < words.length; i++) {
            int position = i % 8;
            int byteNumber = i / 8;

            boolean condition = i < bitsCount && getBit(phraseBytes[byteNumber], position);
            String str = words[i] + (condition ? "  " : " ");
            result.add(str);
        }

        return String.join("", result);
    }

    private static byte[] decrypt(String text) {
        String[] splitStrings = text.split(" ");
        int result = 0;
        int count = 0;
        byte[] decodedBytes = new byte[splitStrings.length];
        for (int i = 0, n = 0; i < splitStrings.length - 1; i++) {
            if (splitStrings[i].isEmpty()) {
                int index = (i - (++count % 8)) % 8;
                result += mask[index];
            }

            if ((i - count) % 8 == 7) {
                decodedBytes[n++] = (byte) result;
                result = 0;
            }
        }

        return decodedBytes;
    }
}

