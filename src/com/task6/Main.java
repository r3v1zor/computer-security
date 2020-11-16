package com.task6;

import com.task5.ViginerUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final String delimiter = "~~~~~";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path: ");
        String folderPath = scanner.nextLine();

        Path path = Path.of(folderPath);

        System.out.println("Enter key:");
        final String key = scanner.nextLine();

        System.out.println("Enter archive filename (without format):");
        final String filename = scanner.nextLine();

        String encrypt = encrypt(path.toString(), key);

        final Path archiveFilepath = Path.of(String.format("%s/%s.archive", path.getParent(), filename));

        Files.write(Path.of(String.format("%s/%s.archive", path.getParent(), filename)), encrypt.getBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING);

        decrypt(archiveFilepath, key);
    }

    private static String encrypt(String dirPath, String key) throws IOException {
        List<String> result = new ArrayList<>();
        Map<Boolean, List<Path>> allPaths = getAllPaths(dirPath);

        result.add(encryptAllPaths(allPaths, key, dirPath.length()));

        for (Path path : allPaths.get(false)) {
            String encryptedFile = encryptFile(path, key);
            result.add(encryptedFile);
        }

        return String.join(delimiter, result);
    }

    private static String encryptAllPaths(Map<Boolean, List<Path>> paths, String key, int length) {
        return paths.values().stream()
                .map(col -> col.stream().map(path -> path.toString().substring(length)))
                .map(path -> ViginerUtils.encrypt(String.join("\n", path.collect(Collectors.toList())), key))
                .collect(Collectors.joining(delimiter));
    }

    private static String encryptFile(Path filepath, String key) throws IOException {
        if (Files.isDirectory(filepath)) {
            return null;
        }

        String text = Files.readString(filepath);
        return ViginerUtils.encrypt(text, key);
    }

    // 0 - files, 1 - directories
    private static Map<Boolean, List<Path>> getAllPaths(String directory) throws IOException {
        Path dirPath = Path.of(directory);

        return Files
                .walk(dirPath)
                .collect(Collectors.groupingBy(Files::isDirectory));
    }

    private static void decrypt(Path filepath, String key) throws IOException {
        String filename = filepath.getFileName().toString().split("\\.")[0];
        Path mainDir = filepath.getParent();

        String encryptedFiles = Files.readString(filepath);
        List<String> files = List.of(encryptedFiles.split(delimiter));

        List<String> dirs = List.of(ViginerUtils.decrypt(files.get(1), key).split("\n"));
        List<String> filePaths = List.of(ViginerUtils.decrypt(files.get(0), key).split("\n"));

        createAllDirectories(mainDir, filename, dirs);

        String dirPath = mainDir + "/" + filename;
        createAllFiles(dirPath, filePaths, files.subList(2, files.size()), key);
    }

    private static void createAllDirectories(Path mainDir, String fileName, List<String> paths) throws IOException {
        for (String path : paths) {
            Files.createDirectories(Path.of(mainDir + "/" + fileName + path));
        }
    }

    private static void createAllFiles(String dirPath, List<String> paths, List<String> encryptedFileData, String key) throws IOException {
        for (int i = 0; i < paths.size(); i++) {
            Path path = Path.of(dirPath + paths.get(i));
            String decryptedText = ViginerUtils.decrypt(encryptedFileData.get(i), key);

            Files.createFile(path);
            Files.write(path, decryptedText.getBytes());
        }
    }
}
