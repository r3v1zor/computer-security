package com.task5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter phrase to encrypt");
        final String phraseToEncrypt = scanner.nextLine();

        System.out.println("Enter key:");
        final String key = scanner.nextLine();

        String encrypt = ViginerUtils.encrypt(phraseToEncrypt, key);
        String decrypt = ViginerUtils.decrypt(encrypt, key);

        System.out.println("Encrypted:\n" + encrypt);
        System.out.println("Decrypted:\n" + decrypt);
    }
}
