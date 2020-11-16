package com.task4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Map<Character, Character> rusToEngMap;
    private static Map<Character, Character> engToRusMap;

    private static final int[] mask = {1, 2, 4, 8, 16, 32, 64, 128};

    static {
        rusToEngMap = new HashMap<>() {{
            put('е', 'e');
            put('у', 'y');
            put('о', 'o');
            put('р', 'p');
            put('а', 'a');
            put('х', 'x');
            put('с', 'c');
            put('Е', 'E');
            put('О', 'O');
            put('Р', 'P');
            put('А', 'A');
            put('Х', 'X');
            put('С', 'C');
            put('В', 'B');
            put('М', 'M');
            put('К', 'K');
            put('Т', 'T');
        }};

        engToRusMap = rusToEngMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public static void main(String[] args) throws IOException {
        final String text = Files.readString(Path.of("/home/r3v1zor/Projects/IdeaProjects/computer-security/src/com/task4/test.txt"));

        final String encode = encode(text, "Hello world");
        final List<Byte> decode = decode(encode);

        byte[] byteString = new byte[decode.size()];
        for (int i = 0; i < byteString.length; i++) {
            byteString[i] = decode.get(i);
        }

        System.out.println(new String(byteString, StandardCharsets.UTF_8));


    }

    private static String encode(String text, String phrase) {
        StringBuilder builder = new StringBuilder();
        final byte[] phraseBytes = phrase.getBytes();

        for (int i = 0, count = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);

            if (i < phraseBytes.length * 8 && rusToEngMap.containsKey(symbol)) {
                byte curByte = phraseBytes[count / 8];
                int position = count % 8;

                count++;

                if (getBit(curByte, position)) {
                    builder.append(rusToEngMap.get(symbol));
                    continue;
                }
            }

            builder.append(symbol);
        }

        return builder.toString();
    }

    private static List<Byte> decode(String text) {
        List<Byte> bytes = new ArrayList<>();
        int number = 0;

        for (int i = 0, count = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);

            if (engToRusMap.containsKey(symbol)) {
                number += mask[count];
                count++;
            } else if (rusToEngMap.containsKey(symbol)) {
                count++;
            }

            if (count % 8 == 0) {
                bytes.add((byte) number);
                number = 0;
                count = 0;
            }
        }

        return bytes;
    }

    private static boolean getBit(byte b, int position) {
        return (b & mask[position]) != 0;
    }
}
