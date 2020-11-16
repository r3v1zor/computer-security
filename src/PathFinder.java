import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathFinder {
    private final static Path BASE_PATH = Path.of("/home/r3v1zor/Projects/IdeaProjects/computer-security/src/new");
    private final static Path BASE_FILEPATH = Path.of("/home/r3v1zor/Projects/IdeaProjects/computer-security/src/new/test.txt");

    public static void main(String[] args) {
        byte[] pattern = getFileSignature(BASE_FILEPATH, 2, 1);
        final List<Path> allFilePaths = findAllFilePaths(BASE_PATH);
        List<Path> result = new ArrayList<>();


        allFilePaths.forEach(filepath -> {
            if (isFileContainsGivenSignature(filepath, pattern)) {
                result.add(filepath);
            }
        });

        result.forEach(System.out::println);


    }

    private static List<Path> findAllFilePaths(Path basePath) {
        List<Path> paths = new ArrayList<>();

        try {
            Files.walk(basePath, Integer.MAX_VALUE).forEach(path -> {
                if (!Files.isDirectory(path)) {
                    paths.add(path);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paths;
    }

    private static byte[] getFileSignature(Path path, int amountOfBytes, int offset) {
        try {
            final byte[] allBytes = Files.readAllBytes(path);
            if (allBytes.length < amountOfBytes + offset) {
                throw new RuntimeException("Amount of bytes with given offset is bigger then file");
            }

            return Arrays.copyOfRange(allBytes, offset, offset + amountOfBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean isFileContainsGivenSignature(Path path, byte[] signature) {
        try {
            final byte[] allBytes = Files.readAllBytes(path);

            for (int i = 0; i < allBytes.length - signature.length + 1; i++) {
                byte[] byteRange = Arrays.copyOfRange(allBytes, i, i + signature.length);
                if (Arrays.equals(byteRange, signature)) {
                    return true;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
