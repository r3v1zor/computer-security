package com.task5;

import java.util.*;

public class ViginerUtils {
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИКЛМНОПРСТУФХЦ" +
            "ЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789<>*.?!,;:'+|=&~-_%()[]{}\"/\\ \n\r\t";
    public static Map<String, Integer> symbolToPosition = new HashMap<>();
    public static Map<String, List<String>> viginerMatrix;

    static {
        for (int i = 0; i < alphabet.length(); i++) {
            symbolToPosition.put(String.valueOf(alphabet.charAt(i)), i);
        }

        viginerMatrix = getMatrix();
    }

    private static Map<String, List<String>> getMatrix() {
        String[] symbols = alphabet.split("");
        int amountOfSymbols = symbols.length;

        Map<String, List<String>> result = new HashMap<>();

        for (int i = 0; i < amountOfSymbols; i++) {
            List<String> vigenerRow = new ArrayList<>(List.of(Arrays.copyOfRange(symbols, i, symbols.length)));
            List<String> offsetRow = List.of(Arrays.copyOfRange(symbols, 0, i));

            result.put(symbols[i], vigenerRow);
            result.get(symbols[i]).addAll(offsetRow);
        }

        return result;
    }

    public static String getEncryptedSymbol(String key, String value) {
        List<String> row = viginerMatrix.get(key);
        try {
            return row.get(symbolToPosition.get(value));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDecryptedSymbol(String key, String value) {
        List<String> row = viginerMatrix.get(key);
        int index = row.indexOf(value);
        return String.valueOf(alphabet.charAt(index));
    }

    public static String encrypt(String phrase, String key) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < phrase.length(); i++) {
            int keyIndex = i % key.length();
            String keySymbol = key.substring(keyIndex, keyIndex + 1);

            String phraseSymbol = phrase.substring(i, i + 1);

            String encryptedSymbol = getEncryptedSymbol(keySymbol, phraseSymbol);
            builder.append(encryptedSymbol);
        }

        return builder.toString();
    }

    public static String decrypt(String encryptedText, String key) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < encryptedText.length(); i++) {
            String phraseSymbol = encryptedText.substring(i, i + 1);

            int index = i % key.length();
            String keySymbol = key.substring(index, index + 1);

            String decryptedSymbol = getDecryptedSymbol(keySymbol, phraseSymbol);
            builder.append(decryptedSymbol);
        }

        return builder.toString();
    }
}
